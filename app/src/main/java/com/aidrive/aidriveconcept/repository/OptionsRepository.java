package com.aidrive.aidriveconcept.repository;

import static com.aidrive.aidriveconcept.ui.LauncherActivity.flag;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.aidrive.aidriveconcept.app.AiDriveApp;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.gson.Gson;
import com.softradix.core.Constants;
import com.softradix.core.ViewUtils;
import com.softradix.network.ApiService;
import com.softradix.network.RetrofitClient;
import com.softradix.network.model.LoginResponse;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OptionsRepository {
    private static final String TAG = OptionsRepository.class.getName();
    ApiService service = RetrofitClient.apiClient(AiDriveApp.getAppPreferences()).create(ApiService.class);

    public MutableLiveData<LoginResponse> login(HashMap<String, String> request, String serialNo, Activity activity) {
        final MutableLiveData<LoginResponse> mutableLiveData = new MutableLiveData<>();
        service.login(request, serialNo).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                if (response.code() == 500) {
                    ViewUtils.showToast(AiDriveApp.context(), "Something went wrong please check your setting in options");
                }
                if (response.isSuccessful() && response.body() != null) {
                    AiDriveApp.getAppPreferences().putString(Constants.USER_DATA, new Gson().toJson(response.body()));

                    try {
                        if (response.body().string().equals("no auth")) {
                            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(activity)/*.Builder(activity, android.R.style.Theme_Material_Dialog_Alert)*/;
                            builder.setTitle("Alert!");
                            builder.setCancelable(false);
                            if (flag == 3) {
                                builder.setMessage("Please contact your administrator to reset your login details");
                            } else {
                                builder.setMessage("Your user name and password is incorrect please try again");
                                flag++;
                            }
                            builder.setPositiveButton("OK", (dialogInterface, i) -> dialogInterface.dismiss());
                            builder.show();
                        } else {
                            flag = 0;
                            LoginResponse loginResponse = new Gson().fromJson(response.body().string(), LoginResponse.class);
                            mutableLiveData.setValue(loginResponse);

                        }
                        Log.e(TAG, "onResponse: " + response.body().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                Log.e(TAG, " onFailure" + call);
                Log.e(TAG, " onFailure" + t);

            }
        });
        return mutableLiveData;
    }
}
