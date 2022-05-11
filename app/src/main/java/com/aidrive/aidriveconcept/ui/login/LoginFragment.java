package com.aidrive.aidriveconcept.ui.login;

import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aidrive.aidriveconcept.MainActivity;
import com.aidrive.aidriveconcept.R;
import com.aidrive.aidriveconcept.app.AiDriveApp;
import com.aidrive.aidriveconcept.databinding.LoginFragmentBinding;
import com.google.gson.Gson;
import com.softradix.core.AppPreferences;
import com.softradix.core.Constants;
import com.softradix.core.ViewUtils;
import com.softradix.network.request.LoginRequest;

import java.util.HashMap;

public class LoginFragment extends Fragment {

    private LoginViewModel mViewModel;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    private LoginFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = LoginFragmentBinding.inflate(inflater, container, false);
        mViewModel = new ViewModelProvider(this).get(LoginViewModel.class);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.optionsBtn.setOnClickListener(view1 -> Navigation.findNavController(view1).navigate(R.id.optionsFragment));
//        binding.loginBtn.setOnClickListener(view1 -> startActivity(new Intent(requireActivity(), MainActivity.class)));

        binding.userNameEt.setText("test");
        binding.passwordEt.setText("test123");
        onClickEvents();
    }

    private void onClickEvents() {
        binding.loginBtn.setOnClickListener(view12 -> {
            if (binding.userNameEt.getText().toString().equals("")) {
                ViewUtils.showToast(requireActivity(), "Please enter your username.");
            } else if (binding.passwordEt.getText().toString().equals("")) {
                ViewUtils.showToast(requireActivity(), "Please enter your password.");
            } else if (AiDriveApp.getAppPreferences().getString(Constants.SERIAL_NUMBER).equals("")) {
                ViewUtils.showToast(requireActivity(), "Please enter serial number in options and try again.");
            } else {
                HashMap<String, String> request = new HashMap<>();
                request.put("id", "0");
                request.put("username", binding.userNameEt.getText().toString());
                request.put("password", binding.passwordEt.getText().toString());
                login(request);
            }
        });
    }

    void login(HashMap<String, String> request) {
        mViewModel.login(request, AiDriveApp.getAppPreferences().getString(Constants.SERIAL_NUMBER),
                requireActivity()).observe(getViewLifecycleOwner(), loginResponse -> {
            Log.e("TAG", "login: " + new Gson().toJson(loginResponse));
            ViewUtils.showToast(requireActivity(), "Login Successfully!");
            startActivity(new Intent(requireActivity(), MainActivity.class));
            requireActivity().finish();
        });
    }
}