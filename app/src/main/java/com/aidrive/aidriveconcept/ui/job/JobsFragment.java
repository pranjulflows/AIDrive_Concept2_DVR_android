package com.aidrive.aidriveconcept.ui.job;

import static com.aidrive.aidriveconcept.utils.Utils.gpsModel;
import static com.azure.android.maps.control.options.AnimationOptions.animationDuration;
import static com.azure.android.maps.control.options.AnimationOptions.animationType;
import static com.azure.android.maps.control.options.CameraOptions.center;
import static com.azure.android.maps.control.options.CameraOptions.zoom;
import static com.azure.android.maps.control.options.LineLayerOptions.strokeColor;
import static com.azure.android.maps.control.options.LineLayerOptions.strokeWidth;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.azure.android.maps.control.layer.LineLayer;
import com.azure.android.maps.control.options.AnimationType;
import com.azure.android.maps.control.source.DataSource;
import com.google.gson.Gson;
import com.mapbox.geojson.LineString;
import com.mapbox.geojson.Point;
import com.softradix.callbacks.OnJobJobListener;
import com.softradix.network.model.JobResponse;
import com.softradix.network.model.PointsItem;

import java.util.ArrayList;
import java.util.List;

public class JobsFragment extends Fragment implements JobTaskAdapter.OnJobJobListener {

    private FragmentTaskBinding binding;

    static {
        AzureMaps.setSubscriptionKey("joUzYHjb-kg-kko8UqO-l8o9Ayj7rIYsWnooiC1cbKk");
    }

    private JobsViewModel mViewModel;
    private AzureDirectionVM azureDirectionVM;
    MapControl mapControl;
    JobTaskAdapter adapter;
    List<JobResponse> jobsList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        mViewModel =
                new ViewModelProvider(this).get(JobsViewModel.class);
        azureDirectionVM =
                new ViewModelProvider(this).get(AzureDirectionVM.class);

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
//        mapControl.onReady(map -> {
//            //Create a data source and add it to the map.
//            DataSource source = new DataSource();
//            map.sources.add(source);
////            //Create a point and add it to the data source.
//////            source.add(Point.fromLngLat(gpsModel.getLongitude(), gpsModel.getLatitude()));
//////            source.add(Point.fromLngLat(30.7398368,76.660358));
//            List<Point> points = Arrays.asList(
//                    Point.fromLngLat(gpsModel.getLongitude(), gpsModel.getLatitude()),
//                    Point.fromLngLat(76.660358, 30.7398368));
//            Log.e("TAG", "attachObservers: " + points.size());
////Create a LineString geometry and add it to the data source.
//            source.add(LineString.fromLngLats(points));
//
////Create a line layer and add it to the map.
//            LineLayer layer = new LineLayer(source,
//                    strokeColor("blue"),
//                    strokeWidth(3f)
//            );
//            map.layers.add(layer);
//            map.setCamera(center(Point.fromLngLat(gpsModel.getLongitude(), gpsModel.getLatitude())), zoom(12.5),
//                    animationType(AnimationType.FLY), animationDuration(3000));
//
//            map.layers.add(layer);
//            //Create a symbol layer to render icons and/or text at points on the map.
//            SymbolLayer layer2 = new SymbolLayer(source);
//            //Add the layer to the map.
//            map.layers.add(layer2);
//            map.setCamera(defaults());
////            map.setCamera(center(Point.fromLngLat(gpsModel.getLongitude(), gpsModel.getLatitude())), zoom(14));
//
////            map.setCamera(centerOffset(new Offset(0, -75)));
//        });


//        assert binding.recyclerView != null;
        RecyclerView rv = binding.recyclerView;
//        if (rv != null) {
        rv.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.HORIZONTAL));
        rv.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new JobTaskAdapter(jobsList, this);
        rv.setAdapter(adapter);
//        }


        new Handler().postDelayed(this::attachObservers, 1000L);

    }

    private void attachObservers() {

        mViewModel.getJob(requireActivity()).observe(getViewLifecycleOwner(), jobResponse -> {

            jobsList.clear();
            jobsList.addAll(jobResponse);
            adapter.notifyDataSetChanged();

            Log.e("TAG", "onViewCreated: " + new Gson().toJson(jobResponse));
            setData(jobResponse);
        });

        azureDirectionVM.getRoutes(requireActivity(), gpsModel.getLatitude() + "," + gpsModel.getLongitude(), "30.7398368,76.660358").observe(getViewLifecycleOwner(), routePointsResponse -> {


//                source = new DataSource();
//                map.sources.add(source);
            //Create a point and add it to the data source.
//            source.add(Point.fromLngLat(gpsModel.getLongitude(), gpsModel.getLatitude()));
//            source.add(Point.fromLngLat(30.7398368,76.660358));
//Create a list of points.
            mapControl.onReady(azureMap -> {
                DataSource source = new DataSource();
                azureMap.sources.add(source);

                List<Point> points = new ArrayList<>();
                for (int i = 0; i < routePointsResponse.getRoutes().get(0).getLegs().get(0).getPoints().size(); i++) {
                    PointsItem pointsItem = routePointsResponse.getRoutes().get(0).getLegs().get(0).getPoints().get(0);
                    points.add(Point.fromLngLat(pointsItem.getLongitude(), pointsItem.getLatitude()));
                }
//                            List<Point> points = Arrays.asList(
//                    Point.fromLngLat(gpsModel.getLongitude(), gpsModel.getLatitude()),
//                    Point.fromLngLat(76.660358, 30.7398368));
                Log.e("TAG", "attachObservers: " + points.size());
//Create a LineString geometry and add it to the data source.
                source.add(LineString.fromLngLats(points));

//Create a line layer and add it to the map.
                LineLayer layer = new LineLayer(source,
                        strokeColor("blue"),
                        strokeWidth(3f)
                );
                azureMap.layers.add(layer);
                azureMap.setCamera(center(Point.fromLngLat(gpsModel.getLongitude(), gpsModel.getLatitude())), zoom(12.5),
                        animationType(AnimationType.FLY), animationDuration(3000));


            });


//                map.setCamera(bounds(BoundingBox.fromPoints(Point.fromLngLat(gpsModel.getLongitude() , gpsModel.getLatitude()),Point.fromLngLat(76.660358,30.7398368))));

        });
    }

    private void setData(List<JobResponse> jobResponse) {

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
    public void onStart() {
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

    @Override
    public void onJobSelected(JobResponse data) {
        binding.jobDescView.jobDescLayout.setVisibility(View.VISIBLE);

        binding.jobDescView.jobNameTxt.setText(data.getName());
        binding.jobDescView.jobDescTxt.setText(data.getDescription().equals("") ? "N/A" : data.getDescription());
        binding.jobDescView.jobStatus.setText(getString(R.string.job_status, data.getStatus()));
        binding.jobDescView.jobStartDate.setText(getString(R.string.start_date, data.getStartdate()));
        binding.jobDescView.jobEndDate.setText(getString(R.string.end_date, data.getEndTime()));
        binding.jobDescView.jobPriority.setText(getString(R.string.job_priority, data.getPriority()));
        binding.jobDescView.details.setText( data.getLoadDetails());
        binding.jobDescView.measurement.setText(getString(R.string.measurement_unit_type, data.getLoadMeasurement()));
        binding.jobDescView.units.setText(getString(R.string.qty_units_qty, data.getLoadUnit()));
        binding.jobDescView.pickupLocationTxt.setText(data.getDeliveryAddressNumber().concat(", ").concat(data.getPickupAddressStreet()).concat(", ").concat(data.getPickupAddressCity()).concat(", ")
                .concat(data.getPickupAddressState()).concat(",\n").concat(data.getPickupAddressCountry()).concat(", ").concat(data.getPickupAddressPostcode()));
        binding.jobDescView.deliveryLocationTxt.setText(data.getDeliveryAddressNumber().concat(", ").concat(data.getDeliveryAddressStreet()).concat(", ").concat(data.getDeliveryAddressCity()).concat(", ")
                .concat(data.getDeliveryAddressState()).concat(",\n").concat(data.getDeliveryAddressCountry()).concat(", ").concat(data.getDeliveryAddressPostcode()));
    }
}