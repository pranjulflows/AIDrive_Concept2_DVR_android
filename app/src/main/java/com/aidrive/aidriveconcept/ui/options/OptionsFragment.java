package com.aidrive.aidriveconcept.ui.options;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aidrive.aidriveconcept.app.AiDriveApp;
import com.aidrive.aidriveconcept.databinding.OptionsFragmentBinding;
import com.google.gson.Gson;
import com.softradix.core.AppPreferences;
import com.softradix.core.Constants;
import com.softradix.core.ViewUtils;
import com.softradix.network.model.LoginResponse;
import com.softradix.network.request.LoginRequest;

public class OptionsFragment extends Fragment {

    private OptionsViewModel mViewModel;

    public static OptionsFragment newInstance() {
        return new OptionsFragment();
    }

    private OptionsFragmentBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(OptionsViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = OptionsFragmentBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    private void attachObservers() {
        mViewModel.getBaseUrl().observe(getViewLifecycleOwner(), binding.cloudApiEt::setText);
        mViewModel.getSerialNumberUrl().observe(getViewLifecycleOwner(), binding.serialNumberEt::setText);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        attachObservers();

        binding.backBtn.setOnClickListener(view1 -> Navigation.findNavController(view).popBackStack());
        binding.saveBtn.setOnClickListener(view12 -> {
            /*if (binding.deviceApiPointEt.getText().toString().equals("")) {
                ViewUtils.showToast(requireActivity(), "Please enter Device Api Point.");
            } else*/
            if (binding.cloudApiEt.getText().toString().equals("")) {
                ViewUtils.showToast(requireActivity(), "Please enter cloud Api Point.");
            }
            else if (binding.serialNumberEt.getText().toString().equals("")) {
                ViewUtils.showToast(requireActivity(), "Please enter serial number.");
            } else {
                if(!binding.cloudApiEt.getText().toString().equals(Constants.DEFAULT_BASE_URL)){
                    AiDriveApp.getAppPreferences().putString(Constants.BASE_URL, binding.cloudApiEt.getText().toString());
                }
                AiDriveApp.getAppPreferences().putString(Constants.SERIAL_NUMBER, binding.serialNumberEt.getText().toString());
                AiDriveApp.getAppPreferences().putString(Constants.DEVICE_API_POINT, binding.deviceApiPointEt.getText().toString());
                ViewUtils.showToast(requireActivity(),"Api End Points are updated!");
                Navigation.findNavController(binding.getRoot()).popBackStack();
            }
        });
    }


}