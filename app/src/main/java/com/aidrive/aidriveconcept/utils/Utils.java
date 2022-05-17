package com.aidrive.aidriveconcept.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.webkit.URLUtil;

import androidx.fragment.app.FragmentActivity;

import com.aidrive.aidriveconcept.model.GPSModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.net.ssl.HttpsURLConnection;

public class Utils {
   public static GPSModel gpsModel = new GPSModel(30.7132029, 76.6932075);

   public interface SimpleCallback {
      void notify(String result);
   }
   public static void importData(String urlOrFileName, Context context, SimpleCallback callback, SimpleCallback error){
      if(urlOrFileName != null && callback != null) {
         ExecutorService executor = Executors.newSingleThreadExecutor();
         Handler handler = new Handler(Looper.getMainLooper());

         executor.execute(() -> {
            String data = null;

            try {

//               if(URLUtil.isNetworkUrl(urlOrFileName)){
                  data = importFromWeb(urlOrFileName);
//               } else {
//                  //Assume file is in assets folder.
//                  data = importFromAssets(context, urlOrFileName);
//               }

               final String result = data;

               handler.post(() -> {
                  //Ensure the resulting data string is not null or empty.
                  if (result != null && !result.isEmpty()) {
                     callback.notify(result);
                  } else {
                     error.notify("No data imported.");
                  }
               });
            } catch(Exception e) {
               if(error != null){
                  error.notify(e.getMessage());
               }
            }
         });
      }
   }

   /**
    * Imports data from an assets file as a string.
    * @param context The context of the app.
    * @param fileName The asset file name.
    * @return
    * @throws IOException
    */
   private static String importFromAssets(Context context, String fileName) throws IOException {
      InputStream stream = null;

      try {
         stream = context.getAssets().open(fileName);

         if(stream != null) {
            return readStreamAsString(stream);
         }
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         // Close Stream and disconnect HTTPS connection.
         if (stream != null) {
            stream.close();
         }
      }

      return null;
   }


   /**
    * Imports data from the web as a string.
    * @param url URL to the data.
    * @return
    * @throws IOException
    */
   private static String importFromWeb(String url) throws IOException {
      InputStream stream = null;
      HttpsURLConnection connection = null;
      String result = null;

      try {
         connection = (HttpsURLConnection) new URL(url).openConnection();

         //For this use case, set HTTP method to GET.
         connection.setRequestMethod("GET");

         //Open communications link (network traffic occurs here).
         connection.connect();

         int responseCode = connection.getResponseCode();
         if (responseCode != HttpsURLConnection.HTTP_OK) {
            throw new IOException("HTTP error code: " + responseCode);
         }

         //Retrieve the response body as an InputStream.
         stream = connection.getInputStream();

         if (stream != null) {
            return readStreamAsString(stream);
         }
      } catch (Exception e) {
         e.printStackTrace();
      } finally {
         // Close Stream and disconnect HTTPS connection.
         if (stream != null) {
            stream.close();
         }
         if (connection != null) {
            connection.disconnect();
         }
      }

      return result;
   }

   /**
    * Reads an input stream as a string.
    * @param stream Stream to convert.
    * @return
    * @throws IOException
    */
   private static String readStreamAsString(InputStream stream) throws IOException {
      //Convert the contents of an InputStream to a String.
      BufferedReader in = new BufferedReader(new InputStreamReader(stream, "UTF-8"));

      String inputLine;
      StringBuffer response = new StringBuffer();

      while ((inputLine = in.readLine()) != null) {
         response.append(inputLine);
      }

      in.close();

      return response.toString();
   }
}
