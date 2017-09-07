package com.aprido.cintabogor.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aprido.cintabogor.R;
import com.aprido.cintabogor.callback.MapRouteFragmentCallback;
import com.aprido.cintabogor.callback.MapRouteLogicCallback;
import com.aprido.cintabogor.logic.MapRouteLogic;
import com.aprido.cintabogor.widget.CustomButton;
import com.aprido.cintabogor.widget.CustomTextView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apridosandyasa on 5/2/17.
 */

public class MapRouteFragment extends DialogFragment implements OnMapReadyCallback, MapRouteLogicCallback {

    private Context context;
    private View view;
    private CustomTextView tv_title_map_route;
    private CustomButton btn_close_map_route;
    private SupportMapFragment supportMapFragment;
    private GoogleMap googleMap;
    private MapRouteFragmentCallback callback;
    private LatLng placeLatLng, userLatLng;
    private MapRouteLogic mapRouteLogic;
    private List<LatLng> coordinateList = new ArrayList<>();
    private List<Marker> markerList = new ArrayList<>();
    private String mTitle;

    public MapRouteFragment() {

    }

    @SuppressLint("ValidFragment")
    public MapRouteFragment(Context context, MapRouteFragmentCallback listener, LatLng placeLatLng, LatLng userLatLng, String title) {
        this.context = context;
        this.callback = listener;
        this.placeLatLng = placeLatLng;
        this.userLatLng = userLatLng;
        this.mTitle = title;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.view = inflater.inflate(R.layout.content_map_route, container, false);

        return this.view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.tv_title_map_route = (CustomTextView) view.findViewById(R.id.tv_title_map_route);
        this.btn_close_map_route = (CustomButton) view.findViewById(R.id.btn_close_map_route);

        this.tv_title_map_route.setText(this.mTitle);

        this.btn_close_map_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callback.onDismissMapRouteFragmnet();
            }
        });

        placeLatLng = new LatLng((!(String.valueOf(placeLatLng.latitude)).contains("-") ? Double.valueOf("-" + String.valueOf(placeLatLng.latitude)) : placeLatLng.latitude),
                placeLatLng.longitude);

        initGoogleMaps();
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = ViewGroup.LayoutParams.MATCH_PARENT;
        params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((android.view.WindowManager.LayoutParams) params);

        super.onResume();
    }

    @Override
    public void onDestroyView() {
        if (supportMapFragment != null)
            ((AppCompatActivity)this.context).getSupportFragmentManager().beginTransaction().remove(supportMapFragment).commit();
        super.onDestroyView();
    }

    private void initGoogleMaps() {
        this.supportMapFragment = (SupportMapFragment) ((AppCompatActivity)this.context).getSupportFragmentManager().findFragmentById(R.id.googleMaps);
        this.supportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        this.googleMap.getUiSettings().setCompassEnabled(true);
        this.googleMap.getUiSettings().setZoomControlsEnabled(true);
        this.googleMap.getUiSettings().setRotateGesturesEnabled(false);

        this.mapRouteLogic = new MapRouteLogic(this.context, this);
        this.mapRouteLogic.setupMapRouteViews(placeLatLng, userLatLng);

//        for (int i = 0; i < this.coordinateList.size(); i++) {
//            this.markerList.add(this.googleMap.addMarker(
//                    new MarkerOptions().position(this.coordinateList.get(i))));
//        }
    }

    @Override
    public void failedSetupMapRouteViews() {

    }

    @Override
    public void finishedSetupMapRouteViews(List<LatLng> latLngList) {
        coordinateList = latLngList;
        Log.d("TAG", "coordinateList size " + this.coordinateList.size());
        if (coordinateList.size() > 0) {
            PolylineOptions options = new PolylineOptions().width(10).color(this.context.getResources().getColor(R.color.colorPrimary)).geodesic(true);
            options.add(placeLatLng);
            for (int z = 0; z < coordinateList.size(); z++) {
                options.add(coordinateList.get(z));
            }
            options.add(userLatLng);
            this.googleMap.addPolyline(options);

            int median = (coordinateList.size() / 2) + 2;

            this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(coordinateList.get(median), 10));

            this.googleMap.addMarker(new MarkerOptions()
                    .position(userLatLng));
            this.googleMap.addMarker(new MarkerOptions()
                    .position(placeLatLng));
        }else{
            this.googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this.placeLatLng, 13));
            Toast.makeText(this.context, "Tidak tersedia route untuk " + mTitle, Toast.LENGTH_SHORT).show();
        }
    }
}
