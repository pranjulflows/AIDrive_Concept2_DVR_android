package com.aidrive.aidriveconcept.ui.job;

import static com.aidrive.aidriveconcept.utils.Utils.gpsModel;
import static com.azure.android.maps.control.options.CameraOptions.center;
import static com.azure.android.maps.control.options.CameraOptions.centerOffset;
import static com.azure.android.maps.control.options.CameraOptions.zoom;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aidrive.aidriveconcept.R;
import com.aidrive.aidriveconcept.adapter.JobTaskAdapter;
import com.aidrive.aidriveconcept.databinding.FragmentTaskBinding;
import com.azure.android.maps.control.AzureMaps;
import com.azure.android.maps.control.MapControl;
import com.azure.android.maps.control.data.Offset;
import com.azure.android.maps.control.layer.SymbolLayer;
import com.azure.android.maps.control.source.DataSource;
import com.mapbox.geojson.Point;

public class JobsFragment extends Fragment {

    private FragmentTaskBinding binding;
    static {
        AzureMaps.setSubscriptionKey("joUzYHjb-kg-kko8UqO-l8o9Ayj7rIYsWnooiC1cbKk");
    }

    MapControl mapControl;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        JobsViewModel camViewModel =
                new ViewModelProvider(this).get(JobsViewModel.class);

        binding = FragmentTaskBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        // INIT MAP
        //mapControl = findViewById(R.id.mapcontrol);
        mapControl = root.findViewById(R.id.mapcontrol);

        mapControl.onCreate(savedInstanceState);


        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Wait until the map resources are ready.
        mapControl.onReady(map -> {
            //Create a data source and add it to the map.
            DataSource source = new DataSource();
            map.sources.add(source);
            //Create a point and add it to the data source.
            source.add(Point.fromLngLat(gpsModel.getLongitude(),gpsModel.getLatitude()));
            //Create a symbol layer to render icons and/or text at points on the map.
            SymbolLayer layer = new SymbolLayer(source);
            //Add the layer to the map.
            map.layers.add(layer);
            map.setCamera(center(Point.fromLngLat(gpsModel.getLongitude(),gpsModel.getLatitude())), zoom(14));
            map.setCamera(centerOffset(new Offset(0,-75)));
        });

    RecyclerView rv = binding.listView.recyclerView;
    rv.addItemDecoration(new DividerItemDecoration(requireActivity(),DividerItemDecoration.HORIZONTAL));
    rv.setLayoutManager(new LinearLayoutManager(requireActivity(),LinearLayoutManager.VERTICAL,false));
    rv.setAdapter(new JobTaskAdapter());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        mapControl.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mapControl.onResume();
    }

    @Override
    public void onStart(){
        super.onStart();
        mapControl.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapControl.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
        mapControl.onStop();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapControl.onLowMemory();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapControl.onSaveInstanceState(outState);
    }
}