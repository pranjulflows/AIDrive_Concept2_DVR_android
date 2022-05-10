package com.aidrive.aidriveconcept.ui.settings;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aidrive.aidriveconcept.R;
import com.aidrive.aidriveconcept.databinding.FragmentDiaryBinding;
import com.aidrive.aidriveconcept.databinding.SettingsFragmentBinding;
import com.aidrive.aidriveconcept.ui.diary.DiaryViewModel;

public class SettingsFragment extends Fragment {

    private SettingsViewModel mViewModel;

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    private SettingsFragmentBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        SettingsViewModel camViewModel =
                new ViewModelProvider(this).get(SettingsViewModel.class);

        binding = SettingsFragmentBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textDiary;
        camViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}