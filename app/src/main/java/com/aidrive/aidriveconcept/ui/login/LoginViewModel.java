package com.aidrive.aidriveconcept.ui.login;

import android.app.Activity;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aidrive.aidriveconcept.repository.OptionsRepository;
import com.softradix.core.ViewUtils;
import com.softradix.network.model.LoginResponse;

import java.util.HashMap;

public class LoginViewModel extends ViewModel {

    public LoginViewModel() {
        this.repository = new OptionsRepository();

    }

    private final OptionsRepository repository;
    private MutableLiveData<LoginResponse> mutableLiveData = new MutableLiveData<>();

    public LiveData<LoginResponse> login(HashMap<String, String> loginRequest, String serialNo, Activity activity) {

        if (ViewUtils.InternetCheck(activity)) {
            mutableLiveData = repository.login(loginRequest, serialNo,activity);

        } else {
            Toast.makeText(activity, "You're not connected to the internet. Please connect and retry.", Toast.LENGTH_SHORT).show();
        }
        return mutableLiveData;
    }
}