package com.aidrive.aidriveconcept.ui.job;

import static com.azure.android.maps.control.options.AnimationOptions.animationDuration;
import static com.azure.android.maps.control.options.AnimationOptions.animationType;
import static com.azure.android.maps.control.options.CameraBoundsOptions.bounds;
import static com.azure.android.maps.control.options.CameraBoundsOptions.padding;
import static com.azure.android.maps.control.options.CameraOptions.zoom;
import static com.azure.android.maps.control.options.Expression.any;
import static com.azure.android.maps.control.options.Expression.eq;
import static com.azure.android.maps.control.options.Expression.geometryType;
import static com.azure.android.maps.control.options.LineLayerOptions.filter;
import static com.azure.android.maps.control.options.LineLayerOptions.lineCap;
import static com.azure.android.maps.control.options.LineLayerOptions.strokeColor;
import static com.azure.android.maps.control.options.LineLayerOptions.strokeOpacity;
import static com.azure.android.maps.control.options.LineLayerOptions.strokeWidth;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.text.HtmlCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aidrive.aidriveconcept.R;
import com.aidrive.aidriveconcept.adapter.JobTaskAdapter;
import com.aidrive.aidriveconcept.databinding.FragmentTaskBinding;
import com.azure.android.maps.control.AzureMaps;
import com.azure.android.maps.control.Layer;
import com.azure.android.maps.control.MapControl;
import com.azure.android.maps.control.MapMath;
import com.azure.android.maps.control.layer.LineLayer;
import com.azure.android.maps.control.layer.SymbolLayer;
import com.azure.android.maps.control.options.AnimationType;
import com.azure.android.maps.control.options.LineCap;
import com.azure.android.maps.control.source.DataSource;
import com.google.gson.Gson;
import com.mapbox.geojson.BoundingBox;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.FeatureCollection;
import com.mapbox.geojson.MultiLineString;
import com.mapbox.geojson.Point;
import com.softradix.network.model.JobResponse;
import com.softradix.network.model.PointsItem;
import com.softradix.network.model.RoutesItem;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    boolean isRefreshing = false;

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

        RecyclerView rv = binding.recyclerView;
        rv.addItemDecoration(new DividerItemDecoration(requireActivity(), DividerItemDecoration.HORIZONTAL));
        rv.setLayoutManager(new LinearLayoutManager(requireActivity(), LinearLayoutManager.VERTICAL, false));
        adapter = new JobTaskAdapter(jobsList, this);
        rv.setAdapter(adapter);

        new Handler().postDelayed(this::attachObservers, 1000L);

        onClickEvents();
    }

    private void onClickEvents() {
//        binding.swipeRefresh.setOnRefreshListener(() -> {
//            binding.swipeRefresh.setRefreshing(false);
//            attachObservers();
//        });
    }

    private void attachObservers() {
        if (isAdded())
            mViewModel.getJob(requireActivity()).observe(getViewLifecycleOwner(), jobResponse -> {

                jobsList.clear();
                jobsList.addAll(jobResponse);
                adapter.notifyDataSetChanged();
//                binding.swipeRefresh.setRefreshing(false);

                Log.e("TAG", "onViewCreated: " + new Gson().toJson(jobResponse));
//            setDirection();
            });

//        azureDirectionVM.getRoutes(requireActivity(),
//
//                gpsModel.getLatitude() + "," + gpsModel.getLongitude(),
//                "29.953022,78.024249").observe(getViewLifecycleOwner(), routePointsResponse -> {
//            List<LineLayer> layers = new ArrayList<>();
//
//            mapControl.getMapAsync(azureMap -> {
//
//                DataSource source = new DataSource();
//                azureMap.sources.add(source);
//
//                layers.add(new LineLayer(
//                        source,
//                        strokeColor("#e82e3e"),
//                        strokeWidth(4f),
//                        filter(any(eq(geometryType(), "LineString"), eq(geometryType(), "MultiLineString"))),
//                        lineCap(LineCap.ROUND)
//                ));
//                layers.add(new LineLayer(
//                        source,
//                        strokeColor("#e3592b"),
//                        strokeWidth(4f),
//                        strokeOpacity(0.8F),
//                        filter(any(eq(geometryType(), "LineString"), eq(geometryType(), "MultiLineString"))),
//                        lineCap(LineCap.ROUND)
//                ));
//                layers.add(new LineLayer(
//                        source,
//                        strokeColor("#3066db"),
//                        strokeWidth(4f),
//                        strokeOpacity(0.3F),
//                        filter(any(eq(geometryType(), "LineString"), eq(geometryType(), "MultiLineString"))),
//                        lineCap(LineCap.ROUND)
//                ));
//                List<RoutesItem> routes = new ArrayList<>(routePointsResponse.getRoutes());
//                List<List<Point>> pointsList = new ArrayList<>();
//                for (int i = 0; i < routes.size(); i++) {
//                    List<Point> points = new ArrayList<>();
//                    for (int index = 0; index < routes.get(i).getLegs().get(0).getPoints().size(); index++) {
//                        PointsItem pointsItem = routes.get(i).getLegs().get(0).getPoints().get(index);
//                        points.add(Point.fromLngLat(pointsItem.getLongitude(), pointsItem.getLatitude()));
//                    }
//                    pointsList.add(points);
//                }
//
//                Feature feature = Feature.fromGeometry(Point.fromLngLat(gpsModel.getLongitude(), gpsModel.getLatitude()));
//                Feature feature2 = Feature.fromGeometry(Point.fromLngLat(78.024249, 29.953022));
//                FeatureCollection featureCollection = FeatureCollection.fromFeatures(Arrays.asList(feature, feature2));
//                source.add(featureCollection);
//                source.add(MultiLineString.fromLngLats(pointsList));
//                BoundingBox bbox = MapMath.fromData(featureCollection);
//
//                //Update the maps camera so it is focused on the data.
//
//                azureMap.setCamera(
//                        bounds(bbox),
//                        padding(50), zoom(12.5),
//                        animationType(AnimationType.FLY), animationDuration(2000));
//                //Add the layer to the map.
//                SymbolLayer marker = new SymbolLayer(source);
//
//                azureMap.layers.add(marker);
//                azureMap.layers.add(new Layer[]{layers.get(0), layers.get(1), layers.get(2)});
//
////                azureMap.layers.add(start);
//
//            });
//
////Create a list of points.
//
////            mapControl.getMapAsync(azureMap -> {
////                DataSource source = new DataSource();
////                azureMap.sources.add(source);
////                Log.e("TAG", "attachObservers: " + getLocationFromAddress(requireActivity(), "SM heights,Phase 8b, Mohali, Punjab, India, 160055"));
////                List<Point> points = new ArrayList<>();
////                for (int i = 0; i < routePointsResponse.getRoutes().get(0).getLegs().get(0).getPoints().size(); i++) {
////                    PointsItem pointsItem = routePointsResponse.getRoutes().get(0).getLegs().get(0).getPoints().get(i);
////                    points.add(Point.fromLngLat(pointsItem.getLongitude(), pointsItem.getLatitude()));
////                }
////                Feature feature = Feature.fromGeometry(Point.fromLngLat(gpsModel.getLongitude(), gpsModel.getLatitude()));
////                Feature feature2 = Feature.fromGeometry(Point.fromLngLat(78.024249, 29.953022));
////                FeatureCollection featureCollection = FeatureCollection.fromFeatures(Arrays.asList(feature, feature2));
////                //Optionally, update the maps camera to focus in on the data.
////                source.add(featureCollection);
////                source.add(LineString.fromLngLats(points));
////                source.add(Point.fromLngLat(gpsModel.getLongitude(),gpsModel.getLatitude()));
////                //Create a symbol layer to render icons and/or text at points on the map.
////                SymbolLayer start = new SymbolLayer(source);
////                //Add the layer to the map.
////                azureMap.layers.add(start);
//////                azureMap.layers.add(start);
////                //Calculate the bounding box of all the data in the Feature Collection.
////                BoundingBox bbox = MapMath.fromData(featureCollection);
////                //Update the maps camera so it is focused on the data.
////                azureMap.setCamera(
////                        bounds(bbox),
////                        padding(50), zoom(12.5),
////                        animationType(AnimationType.FLY), animationDuration(2000));
////                azureMap.layers.add(
////                        new LineLayer(
////                                source,
////                                strokeColor("Green"),
////                                strokeWidth(4f)
////                        )
////                );
////
////
////            });
//
////                map.setCamera(bounds(BoundingBox.fromPoints(Point.fromLngLat(gpsModel.getLongitude() , gpsModel.getLatitude()),Point.fromLngLat(76.660358,30.7398368))));
//
//        });
    }

    private void setDirection(Point pickup, Point delivery) {

        azureDirectionVM.getRoutes(requireActivity(),
                pickup.latitude() + "," + pickup.longitude(),
                delivery.latitude() + "," + delivery.longitude()).observe(getViewLifecycleOwner(), routePointsResponse -> {

//Create a list of points.

            mapControl.getMapAsync(azureMap -> {

                DataSource source = new DataSource();
                azureMap.sources.add(source);
                List<LineLayer> layers = new ArrayList<>();
                layers.add(new LineLayer(
                        source,
                        strokeColor("#e82e3e"),
                        strokeWidth(4f),
                        filter(any(eq(geometryType(), "LineString"), eq(geometryType(), "MultiLineString"))),
                        lineCap(LineCap.ROUND)
                ));
                layers.add(new LineLayer(
                        source,
                        strokeColor("#e3592b"),
                        strokeWidth(4f),
                        strokeOpacity(0.8F),
                        filter(any(eq(geometryType(), "LineString"), eq(geometryType(), "MultiLineString"))),
                        lineCap(LineCap.ROUND)
                ));
                layers.add(new LineLayer(
                        source,
                        strokeColor("#3066db"),
                        strokeWidth(4f),
                        strokeOpacity(0.3F),
                        filter(any(eq(geometryType(), "LineString"), eq(geometryType(), "MultiLineString"))),
                        lineCap(LineCap.ROUND)
                ));
                List<RoutesItem> routes = new ArrayList<>(routePointsResponse.getRoutes());
                List<List<Point>> pointsList = new ArrayList<>();
                for (int i = 0; i < routes.size(); i++) {
                    List<Point> points = new ArrayList<>();
                    for (int index = 0; index < routes.get(i).getLegs().get(0).getPoints().size(); index++) {
                        PointsItem pointsItem = routes.get(i).getLegs().get(0).getPoints().get(index);
                        points.add(Point.fromLngLat(pointsItem.getLongitude(), pointsItem.getLatitude()));
                    }
                    pointsList.add(points);
                }

                Feature feature = Feature.fromGeometry(Point.fromLngLat(pickup.longitude(), pickup.latitude()));
                Feature feature2 = Feature.fromGeometry(Point.fromLngLat(delivery.longitude(), delivery.latitude()));
                FeatureCollection featureCollection = FeatureCollection.fromFeatures(Arrays.asList(feature, feature2));
                source.add(featureCollection);
                source.add(MultiLineString.fromLngLats(pointsList));
                BoundingBox bbox = MapMath.fromData(featureCollection);

                //Update the maps camera so it is focused on the data.

                azureMap.setCamera(
                        bounds(bbox),
                        padding(50), zoom(12.5),
                        animationType(AnimationType.FLY), animationDuration(2000));
                //Add the layer to the map.
                SymbolLayer marker = new SymbolLayer(source);

                azureMap.layers.add(marker);
                azureMap.layers.add(new Layer[]{layers.get(0), layers.get(1), layers.get(2)}, "labels");

//                azureMap.layers.add(start);

            });

        });
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

    public Point getLocationFromAddress(Context context, String strAddress) {

        Geocoder coder = new Geocoder(context);
        List<Address> address;
        Point p1 = null;

        try {
            // May throw an IOException
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }

            Address location = address.get(0);
            p1 = Point.fromLngLat(location.getLongitude(), location.getLatitude());
//            p1 = new LatLng(, );

        } catch (IOException ex) {

            ex.printStackTrace();
        }

        return p1;
    }

    @Override
    public void onJobSelected(JobResponse data) {
        binding.jobDescView.jobDescLayout.setVisibility(View.VISIBLE);
        binding.jobDescView.jobDescriptionView.setVisibility(View.VISIBLE);

        binding.jobDescView.jobNameTxt.setText(data.getName());


        binding.jobDescView.jobDescTxt.setText(data.getDescription().equals("") ? "N/A" : HtmlCompat.fromHtml(data.getDescription(), HtmlCompat.FROM_HTML_MODE_LEGACY));
        binding.jobDescView.jobStatus.setText(getString(R.string.job_status, data.getStatus()));
        binding.jobDescView.jobStartDate.setText(getString(R.string.start_date, data.getStartdate()));
        binding.jobDescView.jobEndDate.setText(getString(R.string.end_date, data.getEndTime()));
        binding.jobDescView.jobPriority.setText(getString(R.string.job_priority, data.getPriority()));
        binding.jobDescView.details.setText(data.getLoadDetails());
        binding.jobDescView.measurement.setText(getString(R.string.measurement_unit_type, data.getLoadMeasurement()));
        binding.jobDescView.units.setText(getString(R.string.qty_units_qty, data.getLoadUnit()));
        binding.jobDescView.pickupLocationTxt.setText(data.getPickupAddressNumber().concat(", ").concat(data.getPickupAddressStreet()).concat(", ").concat(data.getPickupAddressCity()).concat(", ")
                .concat(data.getPickupAddressState()).concat(", ").concat(data.getPickupAddressCountry()).concat(", ").concat(data.getPickupAddressPostcode()));
        binding.jobDescView.deliveryLocationTxt.setText(data.getDeliveryAddressNumber().concat(", ").concat(data.getDeliveryAddressStreet()).concat(", ").concat(data.getDeliveryAddressCity()).concat(", ")
                .concat(data.getDeliveryAddressState()).concat(", ").concat(data.getDeliveryAddressCountry()).concat(", ").concat(data.getDeliveryAddressPostcode()));

        Point pickup = getLocationFromAddress(requireActivity(), binding.jobDescView.pickupLocationTxt.getText().toString());
        Point delivery = getLocationFromAddress(requireActivity(), binding.jobDescView.deliveryLocationTxt.getText().toString());

        setDirection(pickup, delivery);
    }
}