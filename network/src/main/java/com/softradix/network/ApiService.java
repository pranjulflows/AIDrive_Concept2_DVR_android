package com.softradix.network;

import com.softradix.network.model.JobResponse;
import com.softradix.network.model.LoginResponse;
import com.softradix.network.model.RoutePointsResponse;

import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    @GET("Auth/authDriver/{serialNumber}")
    Call<LoginResponse> login(@HeaderMap HashMap<String,String> loginRequest, @Path("serialNumber")String serial);

    @GET("Jobs")
    Call<List<JobResponse>> Jobs(@HeaderMap HashMap<String,String> loginRequest);

    //  https://atlas.microsoft.com/route/directions/json?
    //  subscription-key=joUzYHjb-kg-kko8UqO-l8o9Ayj7rIYsWnooiC1cbKk&api-version=1.0&
    //  query=47.6422356,-122.1389797:47.6641142,-122.3011268
    //  &travelMode=car
    //  &traffic=true
    //  &computeTravelTimeFor=all


    @GET("directions/json")
    Call<RoutePointsResponse> getRoutes(@Query("subscription-key")String apiKey, @Query("query")String query,
                                        @Query("travelMode")String travelMode, @Query("maxAlternatives")String maxAlternatives);
}
