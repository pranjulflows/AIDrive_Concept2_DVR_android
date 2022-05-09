package com.aidrive.aidriveconcept;

import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.aidrive.aidriveconcept.databinding.ActivityMainBinding;
import com.aidrive.aidriveconcept.network.Utils;
import com.aidrive.aidriveconcept.ui.cam.CamFragment;
import com.aidrive.aidriveconcept.ui.home.HomeFragment;
import com.azure.android.maps.control.source.DataSource;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.freedesktop.gstreamer.GStreamer;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback  {
    private native String nativeGetGStreamerInfo();
    private native void nativeInit();     // Initialize native code, build pipeline, etc
    private native void nativeFinalize(); // Destroy pipeline and shutdown native code
    private native void nativePlay();     // Set pipeline to PLAYING
    private native void nativePause();    // Set pipeline to PAUSED
    private static native boolean nativeClassInit(); // Initialize native class: cache Method IDs for callbacks
    private native void nativeSurfaceCam0Init(Object surface);
    private native void nativeSurfaceCam1Init(Object surface);
    private native void nativeSurfaceCam0Finalize();
    private native void nativeSurfaceCam1Finalize();
    private long native_custom_data;      // Native code will use this to keep private data
    private boolean is_playing_desired;   // Whether the user asked to go to PLAYING

    private Utils utils;
    private String computer_ip;
    SurfaceView sv0;
    SurfaceView sv1;
    SurfaceHolder sh0;
    SurfaceHolder sh1;

    static {
        System.loadLibrary("gstreamer_android");
        System.loadLibrary("flyover");
        nativeClassInit();
    }

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        utils = new Utils();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(R.id.navigation_home, R.id.navigation_cam,R.id.navigation_chat,
                /*R.id.navigation_alarm,*/ R.id.navigation_task, R.id.navigation_diary).build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);

        // Remove title bars from fragments
        getSupportActionBar().hide();

        // INIT GST
        try {
            GStreamer.init(this);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        if (savedInstanceState != null) {
            is_playing_desired = savedInstanceState.getBoolean("playing");
            Log.i ("GStreamer", "Activity created. Saved state is playing:" + is_playing_desired);
        } else {
            is_playing_desired = false;
            Log.i ("GStreamer", "Activity created. There is no saved state, playing: false");
        }

        // INIT Native C++
        is_playing_desired = true;
        nativeInit();

        System.out.println("GST version: "+nativeGetGStreamerInfo());
        System.out.println("IP: "+computer_ip);
    }

    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        Log.d("GStreamer", "Surface changed to format " + format + " width " + width + " height " + height);

        if (holder == sh0)
            nativeSurfaceCam0Init (holder.getSurface());
        if (holder == sh1)
            nativeSurfaceCam1Init (holder.getSurface());
    }

    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("GStreamer", "Surface created: " + holder.getSurface());
    }

    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("GStreamer", "Surface destroyed");

        if (holder == sh0)
            nativeSurfaceCam0Finalize ();
        if (holder == sh1)
            nativeSurfaceCam1Finalize ();
    }

    public void onFragmentCreated(String data) {
        Log.i("MainActivity", "sendDataToActivity: Created " + data);
        if (data.equals("camFragment")) {
            CamFragment cf = (CamFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_cam);
            sv0 = this.findViewById(R.id.surface_video0);
            sv1 = this.findViewById(R.id.surface_video1);
            if (sv0 != null) {
                sh0 = sv0.getHolder();
                sh0.addCallback(this);
            }
            if (sv1 != null) {
                sh1 = sv1.getHolder();
                sh1.addCallback(this);
            }
        }else if (data.equals("mapFragment")) {
            HomeFragment cf = (HomeFragment)getSupportFragmentManager().findFragmentById(R.id.navigation_home);
            sv0 = this.findViewById(R.id.surfaceView1);
            sv1 = this.findViewById(R.id.surfaceView2);
            if (sv0 != null) {
                sh0 = sv0.getHolder();
                sh0.addCallback(this);
            }
            if (sv1 != null) {
                sh1 = sv1.getHolder();
                sh1.addCallback(this);
            }
        }
    }

    public void updateMapLocation()
    {
        //Create a data source and add it to the map.
        DataSource source = new DataSource();
        //Import the geojson data and add it to the data source.
        source.importDataFromUrl("asset://SamplePoiDataSet.json");
        //Add data source to the map.
    }

    public void onFragmentDestroyed(String data) {
        Log.i("MainActivity", "sendDataToActivity: Destroy" + data);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        nativeFinalize();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d ("GStreamer", "Saving state, playing:" + is_playing_desired);
        outState.putBoolean("playing", is_playing_desired);
    }

    // Called from native code. This sets the content of the TextView from the UI thread.
    private void setMessage(final String message) {
//        runOnUiThread (new Runnable() {
//            public void run() {
//                // UI Thread code here
//            }
//        });
    }

    // Called from native code. Native code calls this once it has created its pipeline and
    // the main loop is running, so it is ready to accept commands.
    private void onGStreamerInitialized () {
        Log.i ("GStreamer", "Gst initialized. Restoring state, playing:" + is_playing_desired);
        // Restore previous playing state
        is_playing_desired = true;
        if (is_playing_desired) {
            nativePlay();
        } else {
            nativePause();
        }

        // Re-enable buttons, now that GStreamer is initialized
//        final Activity activity = this;
//        runOnUiThread(new Runnable() {
//            public void run() {
//                activity.findViewById(R.id.button_play).setEnabled(true);
//                activity.findViewById(R.id.button_stop).setEnabled(true);
//            }
//        });
    }
}