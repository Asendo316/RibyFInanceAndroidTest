package com.cordiscorp.ribyandroidtest.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.cordiscorp.ribyandroidtest.R;
import com.cordiscorp.ribyandroidtest.connectivity.models.UserLocation;
import com.cordiscorp.ribyandroidtest.contract.FetchDistanceContract;
import com.cordiscorp.ribyandroidtest.presenter.FetchDistancePresenter;
import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindColor;
import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import mumayank.com.airlocationlibrary.AirLocation;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener,
        FetchDistanceContract.View , OnMapReadyCallback, RoutingListener {

    private AirLocation airLocation;
    @BindView(R.id.startStopBtn) ToggleButton startStopBtn;
    @BindView(R.id.loadingView) FrameLayout loadingView;
    @BindString(R.string.maps_key) String maps_key;
    private MarkerOptions startOption, stopOption;

    private int STATE = 0;
    private UserLocation startLocation = null, stopLocation = null;

    private FetchDistancePresenter fetchDistancePresenter;

    private SupportMapFragment mapFragment;

    private GoogleMap mMap;
    private MarkerOptions yourLocation;
    private Marker marker;

    protected @BindColor(R.color.colorBlack) int black;
    protected @BindColor(R.color.colorAccent) int jet;
    protected @BindColor(R.color.colorPrimary) int primary;
    protected @BindColor(R.color.primary_dark_material_light) int light;

    private List<Polyline> polylines;

    private static final int[] COLORS = new int[]{R.color.colorBlack, R.color.colorBlack,
            R.color.colorAccent, R.color.colorPrimary,
            R.color.primary_dark_material_light};

    protected LatLng beginLocation, endLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        fetchDistancePresenter = new FetchDistancePresenter(this);

        startStopBtn.setOnCheckedChangeListener(this);

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        polylines = new ArrayList<>();

        mapFragment.getMapAsync(this);

        fetchLocation(0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        airLocation.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        airLocation.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void fetchLocation(final int STATE) {
        airLocation = new AirLocation(this, true, true, new AirLocation.Callbacks() {
            @Override
            public void onSuccess(Location location) {
                displayUserLocation(location);
                switch (STATE) {
                    case 0:
                        stopLocation = new UserLocation(location.getLatitude(),
                                location.getLongitude());
                        beginLocation = new LatLng(location.getLatitude(),
                                location.getLongitude());


                        if (startLocation != null && stopLocation != null)
                            fetchDistancePresenter.getDistance(startLocation, stopLocation, maps_key);
                        break;
                    case 1:
                        endLocation = new LatLng(location.getLatitude(),
                                location.getLongitude());

                        startLocation = new UserLocation(location.getLatitude(),
                                location.getLongitude());
                        break;
                }
            }

            @Override
            public void onFailed(AirLocation.LocationFailedEnum locationFailedEnum) {
            }
        });
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
        if (isChecked) {
            STATE = 1;
            fetchLocation(STATE);
        } else {
            STATE = 0;
            fetchLocation(STATE);
        }
    }

    @Override
    public void showGetDistanceProgress() {
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideGetDistanceProgress() {
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void showGetDistanceResults(String distance, String duration, String start, String end) {
        drawRouteFrom(beginLocation, endLocation,false);
        displayErrorBottomSheet(1,"Success",distance,duration,start,end,"");
    }

    private void drawRouteFrom(LatLng startLocation, LatLng endLocation, boolean displayAltRoute) {
        Routing routing = new Routing.Builder()
                .travelMode(AbstractRouting.TravelMode.DRIVING)
                .withListener(this)
                .alternativeRoutes(displayAltRoute)
                .waypoints(startLocation, endLocation)
                .key(maps_key)
                .build();
        routing.execute();
    }


    @Override
    public void onFetchDIstanceFailed(Throwable throwable) {
        displayErrorBottomSheet(0,"Error","","","","",throwable.getMessage());
    }

    @Override
    public void showFetchDistanceError(String message) {
        displayErrorBottomSheet(0,"Error","","","","",message);
    }

    public void displayErrorBottomSheet(int VIEW, String header, String distance, String duration, String start, String end, String error) {
        BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(this);
        mBottomSheetDialog.setCancelable(true);
        View sheetView = this.getLayoutInflater().inflate(R.layout.snippet_dialog, null);
        Button cancelDialog;
        TextView titleTxt, distanceTxt, durationTxt, startAd, stopAd, erBody, erTitle;
        RelativeLayout successView,errorView;


        titleTxt = sheetView.findViewById(R.id.titleTxt);
        distanceTxt = sheetView.findViewById(R.id.distanceTxt);
        durationTxt = sheetView.findViewById(R.id.durationTxt);
        startAd = sheetView.findViewById(R.id.startAd);
        stopAd = sheetView.findViewById(R.id.stopAd);
        erTitle = sheetView.findViewById(R.id.erTitle);
        erBody = sheetView.findViewById(R.id.erBody);
        cancelDialog = sheetView.findViewById(R.id.cancelDialog);

        errorView = sheetView.findViewById(R.id.errorView);
        successView = sheetView.findViewById(R.id.successView);

        titleTxt.setText(header);
        erTitle.setText(header);
        distanceTxt.setText("Distance:\n"+distance);
        durationTxt.setText("Duration:\n"+duration);
        startAd.setText("Start Location:\n"+start);
        stopAd.setText("Stop Location:\n"+end);
        erBody.setText(error);

        switch (VIEW){
            case 0:
                errorView.setVisibility(View.VISIBLE);
                successView.setVisibility(View.GONE);
                break;
            case 1:
                errorView.setVisibility(View.GONE);
                successView.setVisibility(View.VISIBLE);
                break;
        }

        cancelDialog.setOnClickListener(view -> mBottomSheetDialog.dismiss());

        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();
    }

    @Override
    public void onRoutingFailure(RouteException e) {

    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        displayMapMarkers(route, shortestRouteIndex);
    }

    private void displayMapMarkers(ArrayList<Route> route, int shortestRouteIndex) {
        CameraUpdate center = CameraUpdateFactory.newLatLng(beginLocation);
        mMap.moveCamera(center);

        if (polylines.size() > 0) {
            for (Polyline poly : polylines) {
                poly.remove();
            }
        }

        polylines = new ArrayList<>();
        //add route(s) to the map.
        for (int i = 0; i < route.size(); i++) {

            //In case of more than 5 alternative routes
            int colorIndex = i % COLORS.length;

            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(COLORS[colorIndex]));
            polyOptions.width(10 + i * 3);
            polyOptions.addAll(route.get(i).getPoints());
            Polyline polyline = mMap.addPolyline(polyOptions);
            polylines.add(polyline);

        }

        // Start marker
        drawStartStopMarker(
                startOption,
                beginLocation,
                bitmapDescriptorFromVector(this, R.drawable.ic_start_location));

        // End marker
        drawStartStopMarker(
                stopOption,
                endLocation,
                bitmapDescriptorFromVector(this, R.drawable.ic_stop_location));
    }

    private void drawStartStopMarker(MarkerOptions options, LatLng latLng ,BitmapDescriptor icon) {
        options = new MarkerOptions();
        options.position(latLng);
        options.icon(icon);
        mMap.addMarker(options);
    }

    @Override
    public void onRoutingCancelled() {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        if (googleMap == null) return;
        mMap = googleMap;

        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    private void displayUserLocation(Location location) {
        mMap.clear();
        yourLocation = new MarkerOptions()
                .position(new
                        LatLng(location.getLatitude(),
                        location.getLongitude()))
                .title("Your Location");

        mMap.animateCamera(CameraUpdateFactory
                .newLatLngZoom(new LatLng(
                                location.getLatitude(),
                                location.getLongitude()),
                        13));

        mMap.addMarker(yourLocation);
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


}
