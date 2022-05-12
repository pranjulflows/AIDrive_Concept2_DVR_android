package com.aidrive.aidriveconcept.repository;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.aidrive.aidriveconcept.app.AiDriveApp;
import com.google.gson.Gson;
import com.softradix.core.Constants;
import com.softradix.core.ViewUtils;
import com.softradix.network.ApiService;
import com.softradix.network.RetrofitClient;
import com.softradix.network.model.JobResponse;
import com.softradix.network.model.LoginResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class JobRepository {
    private static final String TAG = JobRepository.class.getName();
    ApiService service = RetrofitClient.apiClient(AiDriveApp.getAppPreferences()).create(ApiService.class);

    public MutableLiveData<List<JobResponse>> getJob(HashMap<String, String> request, Activity activity) {
        final MutableLiveData<List<JobResponse>> mutableLiveData = new MutableLiveData<>();
        service.Jobs(request).enqueue(new Callback<List<JobResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<JobResponse>> call, @NonNull Response<List<JobResponse>> response) {
                if (response.code() == 500) {
                    ViewUtils.showToast(AiDriveApp.context(), "Something went wrong please check your setting in options");
                }
                if (response.isSuccessful() && response.body() != null) {
//                    AiDriveApp.getAppPreferences().putString(Constants.USER_DATA, new Gson().toJson(response.body()));

                    mutableLiveData.setValue(response.body());

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<JobResponse>> call, @NonNull Throwable t) {
                Log.e(TAG, " onFailure" + call);
                Log.e(TAG, " onFailure" + t);

            }
        });
        return mutableLiveData;
    }
}
