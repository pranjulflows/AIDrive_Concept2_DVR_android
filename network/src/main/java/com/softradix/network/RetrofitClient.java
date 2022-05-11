package com.softradix.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.softradix.core.AppPreferences;
import com.softradix.core.Constants;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    public static String BaseAddress = "";

    public static Retrofit retrofit = null;

    public static Retrofit apiClient(AppPreferences appPreferences) {
        BaseAddress = appPreferences.getString(Constants.BASE_URL).equals("") ? Constants.DEFAULT_BASE_URL : appPreferences.getString(Constants.BASE_URL);
        Log.e("TAG", "apiClient: "+BaseAddress );
        if (retrofit == null) {
            final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();

            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(50, TimeUnit.SECONDS)
                    .writeTimeout(50, TimeUnit.SECONDS)
                    .addNetworkInterceptor(interceptor.setLevel(HttpLoggingInterceptor.Level.BODY))
                    .addInterceptor(chain -> {
                        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                        Request original = chain.request();
                        Request request = original.newBuilder()
//                                    .header("Authorization", token_type + " " + access_token)
                                .method(original.method(), original.body())
                                .build();

                        Response response = chain.proceed(request);
                        Log.d("MyApp", "Code : " + response.code());

                        // Magic is here ( Handle the error as your way )
//                         Response response = chain.proceed(chain.request());

                        return response;
                    })
                    .build();
            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();
            retrofit = new Retrofit.Builder()
                    .baseUrl(BaseAddress)

                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient)

                    .build();

        }
        return retrofit;
    }
}

