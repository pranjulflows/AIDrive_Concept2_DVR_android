package com.aidrive.aidriveconcept.ui.job;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aidrive.aidriveconcept.app.AiDriveApp;
import com.aidrive.aidriveconcept.repository.JobRepository;
import com.google.gson.Gson;
import com.softradix.core.Constants;
import com.softradix.core.ViewUtils;
import com.softradix.network.model.JobResponse;
import com.softradix.network.model.LoginResponse;
import com.softradix.network.request.LoginRequest;

import java.util.HashMap;
import java.util.List;

public class JobsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public JobsViewModel() {
        this.repository = new JobRepository();
        mText = new MutableLiveData<>();
        mText.setValue("This is task fragment");
    }

    private final JobRepository repository;
    private MutableLiveData<List<JobResponse>> mutableLiveData = new MutableLiveData<>();

    public LiveData<List<JobResponse>> getJob(Activity activity) {

        if (ViewUtils.InternetCheck(activity)) {

            //region for nougat
            // Map<String, String> headerMap = Arrays.stream(AiDriveApp.getAppPreferences().getString(Constants.USER_LOGIN).split(","))
            //  .map(s -> s.split(":"))
            //  .collect(Collectors.toMap(s -> s[0], s -> s[1]));
            // endregion

            Log.e("TAG", "getJob: "+AiDriveApp.getAppPreferences().getString(Constants.USER_DATA) );

            HashMap<String, String> myMap = new HashMap<>();
            LoginRequest request = new Gson().fromJson(AiDriveApp.getAppPreferences().getString(Constants.USER_LOGIN), LoginRequest.class);
            LoginResponse loginResponse = new Gson().fromJson(AiDriveApp.getAppPreferences().getString(Constants.USER_DATA), LoginResponse.class);
            myMap.put("id",loginResponse.getId());
            myMap.put("username",request.getUsername());
            myMap.put("password", request.getPassword());
            mutableLiveData = repository.getJob(myMap, activity);

        } else {
            Toast.makeText(activity, "You're not connected to the internet. Please connect and retry.", Toast.LENGTH_SHORT).show();
        }
        return mutableLiveData;
    }

    public LiveData<String> getText() {
        return mText;
    }
}