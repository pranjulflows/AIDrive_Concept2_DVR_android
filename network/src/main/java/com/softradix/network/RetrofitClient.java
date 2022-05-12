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
    //  https://atlas.microsoft.com/route/directions/json?
    //  subscription-key=joUzYHjb-kg-kko8UqO-l8o9Ayj7rIYsWnooiC1cbKk&api-version=1.0&
    //  query=47.6422356,-122.1389797:47.6641142,-122.3011268
    //  &travelMode=car
    //  &traffic=true
    //  &computeTravelTimeFor=all


    public static OkHttpClient okHttpClient() {
        final HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        return new OkHttpClient.Builder()
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
    }

    public static Retrofit apiClient(AppPreferences appPreferences) {
        BaseAddress = appPreferences.getString(Constants.BASE_URL).equals("") ? Constants.DEFAULT_BASE_URL : appPreferences.getString(Constants.BASE_URL);
        Log.e("TAG", "apiClient: 1" + BaseAddress);
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        if (retrofit == null)
            retrofit = new Retrofit.Builder()
                    .baseUrl(BaseAddress)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient())

                    .build();
        return retrofit;
    }

    public static Retrofit apiClient(String BaseAddress) {
        Log.e("TAG", "apiClient: 2" + BaseAddress);

        return new Retrofit.Builder()
                .baseUrl(BaseAddress)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient())
                .build();
    }
}

