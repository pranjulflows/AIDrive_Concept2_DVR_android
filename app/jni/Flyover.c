#include <string.h>
#include <stdint.h>
#include <jni.h>
#include <android/log.h>
#include <android/native_window.h>
#include <android/native_window_jni.h>
#include <gst/gst.h>
#include <gst/video/video.h>
#include <pthread.h>

GST_DEBUG_CATEGORY_STATIC (debug_category);
#define GST_CAT_DEFAULT debug_category

/*
 * These macros provide a way to store the native pointer to CustomData, which might be 32 or 64 bits, into
 * a jlong, which is always 64 bits, without warnings.
 */
#if GLIB_SIZEOF_VOID_P == 8
# define GET_CUSTOM_DATA(env, thiz, fieldID) (CustomData *)(*env)->GetLongField (env, thiz, fieldID)
# define SET_CUSTOM_DATA(env, thiz, fieldID, data) (*env)->SetLongField (env, thiz, fieldID, (jlong)data)
#else
# define GET_CUSTOM_DATA(env, thiz, fieldID) (CustomData *)(jint)(*env)->GetLongField (env, thiz, fieldID)
# define SET_CUSTOM_DATA(env, thiz, fieldID, data) (*env)->SetLongField (env, thiz, fieldID, (jlong)(jint)data)
#endif

/* Structure to contain all our information, so we can pass it to callbacks */
typedef struct _CustomData
{
    jobject app;                  /* Application instance, used to call its methods. A global reference is kept. */
    GstElement *pipeline0;         /* The running pipeline */
    GstElement *pipeline1;         /* The running pipeline */
    GMainContext *context;        /* GLib context used to run the main loop */
    GMainLoop *main_loop;         /* GLib main loop */
    gboolean cam0_initialized;         /* To avoid informing the UI multiple times about the initialization */
    gboolean cam1_initialized;         /* To avoid informing the UI multiple times about the initialization */
    GstElement *video_sink0;       /* The video sink element which receives XOverlay commands */
    GstElement *video_sink1;       /* The video sink element which receives XOverlay commands */
    ANativeWindow *native_window0; /* The Android native window where video will be rendered */
    ANativeWindow *native_window1; /* The Android native window where video will be rendered */
} CustomData;

/* These global variables cache values which are not changing during execution */
static pthread_t gst_app_thread;
static pthread_key_t current_jni_env;
static JavaVM *java_vm;
static jfieldID custom_data_field_id;
static jmethodID set_message_method_id;
static jmethodID on_gstreamer_initialized_method_id;

/*
 * Private methods
 */

/* Register this thread with the VM */
static JNIEnv *attach_current_thread (void)
{
  JNIEnv *env;
  JavaVMAttachArgs args;

  GST_DEBUG ("Attaching thread %p", g_thread_self ());
  args.version = JNI_VERSION_1_4;
  args.name = NULL;
  args.group = NULL;

  if ((*java_vm)->AttachCurrentThread (java_vm, &env, &args) < 0) {
    GST_ERROR ("Failed to attach current thread");
    return NULL;
  }

  return env;
}

/* Unregister this thread from the VM */
static void detach_current_thread (void *env)
{
  GST_DEBUG ("Detaching thread %p", g_thread_self ());
  (*java_vm)->DetachCurrentThread (java_vm);
}

/* Retrieve the JNI environment for this thread */
static JNIEnv *get_jni_env (void)
{
  JNIEnv *env;

  if ((env = pthread_getspecific (current_jni_env)) == NULL) {
    env = attach_current_thread ();
    pthread_setspecific (current_jni_env, env);
  }

  return env;
}

/* Change the content of the UI's TextView */
static void set_ui_message (const gchar * message, CustomData * data)
{
  JNIEnv *env = get_jni_env ();
  GST_DEBUG ("Setting message to: %s", message);
  jstring jmessage = (*env)->NewStringUTF (env, message);
  (*env)->CallVoidMethod (env, data->app, set_message_method_id, jmessage);
  if ((*env)->ExceptionCheck (env)) {
    GST_ERROR ("Failed to call Java method");
    (*env)->ExceptionClear (env);
  }
  (*env)->DeleteLocalRef (env, jmessage);
}

/* Retrieve errors from the bus and show them on the UI */
static void error_cb0 (GstBus * bus, GstMessage * msg, CustomData * data)
{
  GError *err;
  gchar *debug_info;
  gchar *message_string;

  gst_message_parse_error (msg, &err, &debug_info);
  message_string = g_strdup_printf ("Error received from Cam0 element %s: %s", GST_OBJECT_NAME (msg->src), err->message);
  g_clear_error (&err);
  g_free (debug_info);
  set_ui_message (message_string, data);
  g_free (message_string);
  gst_element_set_state (data->pipeline0, GST_STATE_NULL);
}

static void error_cb1 (GstBus * bus, GstMessage * msg, CustomData * data)
{
  GError *err;
  gchar *debug_info;
  gchar *message_string;

  gst_message_parse_error (msg, &err, &debug_info);
  message_string = g_strdup_printf ("Error received from Cam1 element %s: %s", GST_OBJECT_NAME (msg->src), err->message);
  g_clear_error (&err);
  g_free (debug_info);
  set_ui_message (message_string, data);
  g_free (message_string);
  gst_element_set_state (data->pipeline1, GST_STATE_NULL);
}


/* Notify UI about pipeline state changes */
static void state_changed_cb0 (GstBus * bus, GstMessage * msg, CustomData * data)
{
  GstState old_state, new_state, pending_state;
  gst_message_parse_state_changed (msg, &old_state, &new_state, &pending_state);
  /* Only pay attention to messages coming from the pipeline, not its children */
  if (GST_MESSAGE_SRC (msg) == GST_OBJECT (data->pipeline0)) {
    gchar *message = g_strdup_printf ("State changed pipeline0 to %s", gst_element_state_get_name (new_state));
    set_ui_message (message, data);
    g_free (message);
  }
}

static void state_changed_cb1 (GstBus * bus, GstMessage * msg, CustomData * data)
{
  GstState old_state, new_state, pending_state;
  gst_message_parse_state_changed (msg, &old_state, &new_state, &pending_state);
  /* Only pay attention to messages coming from the pipeline, not its children */
  if (GST_MESSAGE_SRC (msg) == GST_OBJECT (data->pipeline1)) {
    gchar *message = g_strdup_printf ("State changed pipeline1 to %s", gst_element_state_get_name (new_state));
    set_ui_message (message, data);
    g_free (message);
  }
}


/* Check if all conditions are met to report GStreamer as initialized.
 * These conditions will change depending on the application */
static void check_initialization_complete_cam0 (CustomData * data)
{
  JNIEnv *env = get_jni_env ();
  if (!data->cam0_initialized && data->native_window0 && data->main_loop) {
    GST_DEBUG("Initialization complete Cam0, notifying application. native_window:%p main_loop:%p", data->native_window0, data->main_loop);

    /* The main loop is running and we received a native window, inform the sink about it */
    gst_video_overlay_set_window_handle (GST_VIDEO_OVERLAY (data->video_sink0), (guintptr) data->native_window0);

    (*env)->CallVoidMethod (env, data->app, on_gstreamer_initialized_method_id);
    if ((*env)->ExceptionCheck (env)) {
      GST_ERROR ("Failed to call Java method");
      (*env)->ExceptionClear (env);
    }
    data->cam0_initialized = TRUE;
  }
}

static void check_initialization_complete_cam1 (CustomData * data)
{
  JNIEnv *env = get_jni_env ();
  if (!data->cam1_initialized && data->native_window1 && data->main_loop) {
    GST_DEBUG("Initialization complete Cam1, notifying application. native_window:%p main_loop:%p", data->native_window1, data->main_loop);

    /* The main loop is running and we received a native window, inform the sink about it */
    gst_video_overlay_set_window_handle (GST_VIDEO_OVERLAY (data->video_sink1), (guintptr) data->native_window1);

    (*env)->CallVoidMethod (env, data->app, on_gstreamer_initialized_method_id);
    if ((*env)->ExceptionCheck (env)) {
      GST_ERROR ("Failed to call Java method");
      (*env)->ExceptionClear (env);
    }
    data->cam1_initialized = TRUE;
  }
}

/* Main method for the native code. This is executed on its own thread. */
static void * app_function (void *userdata)
{
  JavaVMAttachArgs args;
  GstBus *bus0;
  GstBus *bus1;
  CustomData *data = (CustomData *) userdata;
  GSource *bus_source0;
  GSource *bus_source1;
  GError *error0 = NULL;
  GError *error1 = NULL;

  GST_DEBUG ("Creating pipeline in CustomData at %p", data);

  /* Create our own GLib Main Context and make it the default one */
  data->context = g_main_context_new ();
  g_main_context_push_thread_default (data->context);

  /* Build pipeline */
  //data->pipeline0 = gst_parse_launch ("tcpclientsrc host=\"192.168.213.1\" port=7001 ! application/x-rtp-stream,encoding-name=JPEG ! rtpstreamdepay ! rtpjpegdepay ! jpegdec ! autovideosink", &error0);
  //data->pipeline1 = gst_parse_launch ("tcpclientsrc host=\"192.168.213.1\" port=7002 ! application/x-rtp-stream,encoding-name=JPEG ! rtpstreamdepay ! rtpjpegdepay ! jpegdec ! autovideosink", &error1);

  data->pipeline0 = gst_parse_launch ("videotestsrc is-live=true ! video/x-raw,format=UYVY,width=400,height=225,framerate=24/1 ! videorate ! video/x-raw,framerate=24/1 ! autovideosink", &error0);
  data->pipeline1 = gst_parse_launch ("videotestsrc is-live=true ! video/x-raw,format=UYVY,width=400,height=225,framerate=24/1 ! videorate ! video/x-raw,framerate=24/1 ! autovideosink", &error1);


  if (error0) {
    gchar *message = g_strdup_printf ("Unable to build pipeline0: %s", error0->message);
    g_clear_error (&error0);
    set_ui_message (message, data);
    g_free (message);
    return NULL;
  }

  if (error1) {
    gchar *message = g_strdup_printf ("Unable to build pipeline1: %s", error1->message);
    g_clear_error (&error1);
    set_ui_message (message, data);
    g_free (message);
    return NULL;
  }

  /* Set the pipeline to READY, so it can already accept a window handle, if we have one */
  gst_element_set_state (data->pipeline0, GST_STATE_READY);
  gst_element_set_state (data->pipeline1, GST_STATE_READY);

  data->video_sink0 = gst_bin_get_by_interface (GST_BIN (data->pipeline0), GST_TYPE_VIDEO_OVERLAY);
  data->video_sink1 = gst_bin_get_by_interface (GST_BIN (data->pipeline1), GST_TYPE_VIDEO_OVERLAY);

  if (!data->video_sink0) {
    GST_ERROR ("Could not retrieve video sink 0");
    return NULL;
  }

  if (!data->video_sink1) {
    GST_ERROR ("Could not retrieve video sink 1");
    return NULL;
  }


  /* Instruct the bus0 to emit signals for each received message, and connect to the interesting signals */
  bus0 = gst_element_get_bus (data->pipeline0);
  bus1 = gst_element_get_bus (data->pipeline1);
  bus_source0 = gst_bus_create_watch (bus0);
  bus_source1 = gst_bus_create_watch (bus1);
  g_source_set_callback (bus_source0, (GSourceFunc) gst_bus_async_signal_func,NULL, NULL);
  g_source_set_callback (bus_source1, (GSourceFunc) gst_bus_async_signal_func,NULL, NULL);
  g_source_attach (bus_source0, data->context);
  g_source_attach (bus_source1, data->context);
  g_source_unref (bus_source0);
  g_source_unref (bus_source1);
  g_signal_connect (G_OBJECT (bus0), "message::error", (GCallback) error_cb0, data);
  g_signal_connect (G_OBJECT (bus0), "message::state-changed",(GCallback) state_changed_cb0, data);
  gst_object_unref (bus0);
  g_signal_connect (G_OBJECT (bus1), "message::error", (GCallback) error_cb1, data);
  g_signal_connect (G_OBJECT (bus1), "message::state-changed",(GCallback) state_changed_cb1, data);
  gst_object_unref (bus1);


  /* Create a GLib Main Loop and set it to run */
  GST_DEBUG ("Entering main loop... (CustomData:%p)", data);
  data->main_loop = g_main_loop_new (data->context, FALSE);
  check_initialization_complete_cam0(data);
  check_initialization_complete_cam1(data);
  g_main_loop_run (data->main_loop);
  GST_DEBUG ("Exited main loop");
  g_main_loop_unref (data->main_loop);
  data->main_loop = NULL;

  /* Main loop ended - Free resources */
  g_main_context_pop_thread_default (data->context);
  g_main_context_unref (data->context);

  gst_element_set_state (data->pipeline0, GST_STATE_NULL);
  gst_element_set_state (data->pipeline1, GST_STATE_NULL);
  gst_object_unref (data->video_sink0);
  gst_object_unref (data->video_sink1);
  gst_object_unref (data->pipeline0);
  gst_object_unref (data->pipeline1);

  return NULL;
}

/*
 * Java Bindings
 */
static jstring gst_native_get_gstreamer_info (JNIEnv * env, jobject thiz)
{
  char *version_utf8 = gst_version_string ();
  jstring *version_jstring = (*env)->NewStringUTF (env, version_utf8);
  g_free (version_utf8);
  return version_jstring;
}

/* Instruct the native code to create its internal data structure, pipeline and thread */
static void gst_native_init (JNIEnv * env, jobject thiz)
{
  CustomData *data = g_new0 (CustomData, 1);
  SET_CUSTOM_DATA (env, thiz, custom_data_field_id, data);
  GST_DEBUG_CATEGORY_INIT (debug_category, "flyover", 0, "AIDrive Flyover");
  gst_debug_set_threshold_for_name ("tutorial-3", GST_LEVEL_DEBUG);
  GST_DEBUG ("Created CustomData at %p", data);
  data->app = (*env)->NewGlobalRef (env, thiz);
  GST_DEBUG ("Created GlobalRef for app object at %p", data->app);
  pthread_create (&gst_app_thread, NULL, &app_function, data);
}

/* Quit the main loop, remove the native thread and free resources */
static void gst_native_finalize (JNIEnv * env, jobject thiz)
{
  CustomData *data = GET_CUSTOM_DATA (env, thiz, custom_data_field_id);
  if (!data)
    return;
  GST_DEBUG ("Quitting main loop...");
  g_main_loop_quit (data->main_loop);
  GST_DEBUG ("Waiting for thread to finish...");
  pthread_join (gst_app_thread, NULL);
  GST_DEBUG ("Deleting GlobalRef for app object at %p", data->app);
  (*env)->DeleteGlobalRef (env, data->app);
  GST_DEBUG ("Freeing CustomData at %p", data);
  g_free (data);
  SET_CUSTOM_DATA (env, thiz, custom_data_field_id, NULL);
  GST_DEBUG ("Done finalizing");
}

/* Set pipeline to PLAYING state */
static void gst_native_play (JNIEnv * env, jobject thiz)
{
  CustomData *data = GET_CUSTOM_DATA (env, thiz, custom_data_field_id);
  if (!data)
    return;
  GST_DEBUG ("Setting state to PLAYING");
  gst_element_set_state (data->pipeline0, GST_STATE_PLAYING);
  gst_element_set_state (data->pipeline1, GST_STATE_PLAYING);
}

/* Set pipeline to PAUSED state */
static void gst_native_pause (JNIEnv * env, jobject thiz)
{
  CustomData *data = GET_CUSTOM_DATA (env, thiz, custom_data_field_id);
  if (!data)
    return;
  GST_DEBUG ("Setting state to PAUSED");
  gst_element_set_state (data->pipeline0, GST_STATE_PAUSED);
  gst_element_set_state (data->pipeline1, GST_STATE_PAUSED);
}

/* Static class initializer: retrieve method and field IDs */
static jboolean gst_native_class_init (JNIEnv * env, jclass klass)
{
  custom_data_field_id = (*env)->GetFieldID (env, klass, "native_custom_data", "J");
  set_message_method_id = (*env)->GetMethodID (env, klass, "setMessage", "(Ljava/lang/String;)V");
  on_gstreamer_initialized_method_id = (*env)->GetMethodID (env, klass, "onGStreamerInitialized", "()V");

  if (!custom_data_field_id || !set_message_method_id
      || !on_gstreamer_initialized_method_id) {
    /* We emit this message through the Android log instead of the GStreamer log because the later
     * has not been initialized yet.
     */
    __android_log_print (ANDROID_LOG_ERROR, "tutorial-3","The calling class does not implement all necessary interface methods");
    return JNI_FALSE;
  }
  return JNI_TRUE;
}

static void gst_native_surface_cam0_init (JNIEnv * env, jobject thiz, jobject surface)
{
  CustomData *data = GET_CUSTOM_DATA (env, thiz, custom_data_field_id);
  if (!data)
    return;
  ANativeWindow *new_native_window = ANativeWindow_fromSurface (env, surface);
  GST_DEBUG ("Received surface Cam0 %p (native window %p)", surface, new_native_window);

  if (data->native_window0) {
    ANativeWindow_release (data->native_window0);
    if (data->native_window0 == new_native_window) {
      GST_DEBUG ("New native window Cam0 is the same as the previous one %p", data->native_window0);
      if (data->video_sink0) {
        gst_video_overlay_expose (GST_VIDEO_OVERLAY (data->video_sink0));
      }
      return;
    } else {
      GST_DEBUG ("Released previous Cam0 native window %p", data->native_window0);
      data->cam0_initialized = FALSE;
    }
  }
  data->native_window0 = new_native_window;

  check_initialization_complete_cam0(data);
}

static void gst_native_surface_cam1_init (JNIEnv * env, jobject thiz, jobject surface)
{
  CustomData *data = GET_CUSTOM_DATA (env, thiz, custom_data_field_id);
  if (!data)
    return;
  ANativeWindow *new_native_window = ANativeWindow_fromSurface (env, surface);
  GST_DEBUG ("Received surface Cam1 %p (native window %p)", surface, new_native_window);

  if (data->native_window1) {
    ANativeWindow_release (data->native_window1);
    if (data->native_window1 == new_native_window) {
      GST_DEBUG ("New native window Cam1 is the same as the previous one %p", data->native_window1);
      if (data->video_sink1) {
        gst_video_overlay_expose (GST_VIDEO_OVERLAY (data->video_sink1));
      }
      return;
    } else {
      GST_DEBUG ("Released previous Cam1 native window %p", data->native_window1);
      data->cam1_initialized = FALSE;
    }
  }
  data->native_window1 = new_native_window;

  check_initialization_complete_cam1(data);
}

static void gst_native_cam0_surface_finalize (JNIEnv * env, jobject thiz)
{
  CustomData *data = GET_CUSTOM_DATA (env, thiz, custom_data_field_id);
  if (!data)
    return;
  GST_DEBUG ("Releasing Native Window Cam0 %p", data->native_window0);

  if (data->video_sink0) {
    gst_video_overlay_set_window_handle (GST_VIDEO_OVERLAY (data->video_sink0), (guintptr) NULL);
    gst_element_set_state (data->pipeline0, GST_STATE_READY);
  }

  ANativeWindow_release (data->native_window0);
  data->native_window0 = NULL;
  data->cam0_initialized = FALSE;
}

static void gst_native_cam1_surface_finalize (JNIEnv * env, jobject thiz)
{
  CustomData *data = GET_CUSTOM_DATA (env, thiz, custom_data_field_id);
  if (!data)
    return;
  GST_DEBUG ("Releasing Native Window Cam1 %p", data->native_window1);

  if (data->video_sink1) {
    gst_video_overlay_set_window_handle (GST_VIDEO_OVERLAY (data->video_sink1), (guintptr) NULL);
    gst_element_set_state (data->pipeline1, GST_STATE_READY);
  }

  ANativeWindow_release (data->native_window1);
  data->native_window1 = NULL;
  data->cam1_initialized = FALSE;
}


static JNINativeMethod native_methods[] = {
  {"nativeGetGStreamerInfo", "()Ljava/lang/String;", (void *) gst_native_get_gstreamer_info },
  {"nativeInit", "()V", (void *) gst_native_init},
  {"nativeFinalize", "()V", (void *) gst_native_finalize},
  {"nativePlay", "()V", (void *) gst_native_play},
  {"nativePause", "()V", (void *) gst_native_pause},
  {"nativeSurfaceCam0Init", "(Ljava/lang/Object;)V", (void *) gst_native_surface_cam0_init},
  {"nativeSurfaceCam1Init", "(Ljava/lang/Object;)V", (void *) gst_native_surface_cam1_init},
  {"nativeSurfaceCam0Finalize", "()V", (void *) gst_native_cam0_surface_finalize},
  {"nativeSurfaceCam1Finalize", "()V", (void *) gst_native_cam1_surface_finalize},
  {"nativeClassInit", "()Z", (void *) gst_native_class_init}
};

jint JNI_OnLoad (JavaVM * vm, void *reserved)
{
  JNIEnv *env = NULL;
  java_vm = vm;

  if ((*vm)->GetEnv (vm, (void **) &env, JNI_VERSION_1_4) != JNI_OK) {
    __android_log_print (ANDROID_LOG_ERROR, "flyover","Could not retrieve JNIEnv");
    return 0;
  }
  jclass klass = (*env)->FindClass (env, "com/aidrive/aidriveconcept/MainActivity");
  (*env)->RegisterNatives (env, klass, native_methods, G_N_ELEMENTS (native_methods));

  pthread_key_create (&current_jni_env, detach_current_thread);

  return JNI_VERSION_1_4;
}
