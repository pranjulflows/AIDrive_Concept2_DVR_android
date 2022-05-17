package com.aidrive.aidriveconcept.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.softradix.network.ApiService;
import com.softradix.network.BuildConfig;
import com.softradix.network.RetrofitClient;
import com.softradix.network.model.RoutePointsResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DirectionRoutesRepository {
    ApiService service = RetrofitClient.apiClient(BuildConfig.AZURE_DIRECTION_URL).create(ApiService.class);
    private static final String TAG = DirectionRoutesRepository.class.getName();


    public MutableLiveData<RoutePointsResponse> getRoutes(String pickupLocation, String deliveryLocation) {
        final MutableLiveData<RoutePointsResponse> mutableLiveData = new MutableLiveData<>();
        service.getRoutes(BuildConfig.AZURE_KEY, pickupLocation.concat(":").concat(deliveryLocation),"car","2").enqueue(new Callback<RoutePointsResponse>() {
            @Override
            public void onResponse(@NonNull Call<RoutePointsResponse> call, @NonNull Response<RoutePointsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    mutableLiveData.setValue(response.body());
                }
                }

            @Override
            public void onFailure(@NonNull Call<RoutePointsResponse> call, @NonNull Throwable t) {
                Log.e(TAG, " onFailure" + call);
                Log.e(TAG, " onFailure" + t);
            }
        });
        return mutableLiveData;
    }

}
