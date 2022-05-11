package com.aidrive.aidriveconcept.ui.options;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.aidrive.aidriveconcept.app.AiDriveApp;
import com.softradix.core.Constants;

public class OptionsViewModel extends ViewModel {

    private final MutableLiveData<String> baseurl = new MutableLiveData<>();
    private final MutableLiveData<String> serialNumber = new MutableLiveData<>();


    public LiveData<String> getBaseUrl() {
        if (AiDriveApp.getAppPreferences().getString(Constants.BASE_URL).equals("")) {
            baseurl.setValue(Constants.DEFAULT_BASE_URL);
        } else {
            baseurl.setValue(AiDriveApp.getAppPreferences().getString(Constants.BASE_URL));
        }
        return baseurl;

    }


    public LiveData<String> getSerialNumberUrl() {
        if (!AiDriveApp.getAppPreferences().getString(Constants.SERIAL_NUMBER).equals("")) {
            serialNumber.setValue(AiDriveApp.getAppPreferences().getString(Constants.SERIAL_NUMBER));
        }
        return serialNumber;
    }
}