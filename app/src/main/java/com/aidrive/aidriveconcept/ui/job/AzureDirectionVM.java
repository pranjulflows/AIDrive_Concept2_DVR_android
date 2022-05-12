package com.aidrive.aidriveconcept.ui.job;

import android.app.Activity;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aidrive.aidriveconcept.app.AiDriveApp;
import com.aidrive.aidriveconcept.repository.DirectionRoutesRepository;
import com.aidrive.aidriveconcept.repository.JobRepository;
import com.google.gson.Gson;
import com.softradix.core.Constants;
import com.softradix.core.ViewUtils;
import com.softradix.network.model.JobResponse;
import com.softradix.network.model.RoutePointsResponse;
import com.softradix.network.request.LoginRequest;

import java.util.HashMap;

public class AzureDirectionVM extends ViewModel {


    public AzureDirectionVM() {
        this.repository = new DirectionRoutesRepository();

    }

    private final DirectionRoutesRepository repository;
    private MutableLiveData<RoutePointsResponse> mutableLiveData = new MutableLiveData<>();

    public LiveData<RoutePointsResponse> getRoutes(Activity activity, String pickupLocation, String deliveryLocation) {

        if (ViewUtils.InternetCheck(activity)) {

            mutableLiveData = repository.getRoutes(pickupLocation, deliveryLocation);

        } else {
            Toast.makeText(activity, "You're not connected to the internet. Please connect and retry.", Toast.LENGTH_SHORT).show();
        }
        return mutableLiveData;
    }
}