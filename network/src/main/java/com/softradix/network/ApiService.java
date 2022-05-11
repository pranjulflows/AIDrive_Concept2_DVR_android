package com.softradix.network;

import com.softradix.network.model.LoginResponse;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;

public interface ApiService {
    @GET("Auth/authDriver/{serialNumber}")
    Call<ResponseBody> login(@HeaderMap HashMap<String,String> loginRequest, @Path("serialNumber")String serial);
}
