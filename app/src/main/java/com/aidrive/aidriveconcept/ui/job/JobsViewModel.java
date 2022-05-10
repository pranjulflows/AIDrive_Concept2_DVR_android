package com.aidrive.aidriveconcept.ui.job;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class JobsViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public JobsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is task fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}