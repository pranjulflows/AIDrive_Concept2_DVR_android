package com.aidrive.aidriveconcept.ui.cam;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.aidrive.aidriveconcept.MainActivity;
import com.aidrive.aidriveconcept.databinding.FragmentCamBinding;

public class CamFragment extends Fragment {

    private MainActivity activity;
    private FragmentCamBinding binding;
    SurfaceView sv0;
    SurfaceView sv1;
    SurfaceView sv2;
    SurfaceView sv3;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        activity = (MainActivity) getActivity();
        CamViewModel camViewModel = new ViewModelProvider(this).get(CamViewModel.class);


        binding = FragmentCamBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        ((MainActivity) getActivity()).getSupportActionBar().hide();

        return root;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sv0 = binding.surfaceVideo0;
        sv1 = binding.surfaceVideo1;
        sv2 = binding.surfaceVideo2;
        sv3 = binding.surfaceVideo3;
        activity.onFragmentCreated("camFragment");
    }

    public SurfaceView getSurfaceView0()
    {
        return sv0;
    }
    public SurfaceView getSurfaceView1()
    {
        return sv1;
    }
    public SurfaceView getSurfaceView2()
    {
        return sv2;
    }
    public SurfaceView getSurfaceView3()
    {
        return sv3;
    }

    @Override
    public void onDestroyView() {
        activity.onFragmentDestroyed("camFragment");
        super.onDestroyView();
        binding = null;
    }
}