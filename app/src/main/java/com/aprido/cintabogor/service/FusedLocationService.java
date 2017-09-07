package com.aprido.cintabogor.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by apridosandyasa on 2/8/16.
 */
public class FusedLocationService extends Service implements LocationListener {
    private static final String TAG = FusedLocationService.class.getSimpleName();

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    protected LocationManager mLocationGPSManager;
    protected LocationManager mLocationNetworkManager;

    private Location mLastLocation;
    private Location mNetworkLocation;

    // Google client to interact with Google API
    //private GoogleApiClient mGoogleApiClient;

    boolean isGPSEnabled = false;

    // flag for network status
    boolean isNetworkEnabled = false;

    // boolean flag to toggle periodic location updates
    private boolean mRequestingLocationUpdates = false;

    //private LocationRequest mLocationRequest;

    // Location updates intervals in sec
    private static int UPDATE_INTERVAL = 5000; // 10 sec
    private static int FATEST_INTERVAL = 2500; // 5 sec
    private static int DISPLACEMENT = 10; // 10 meters

    private Messenger messageHandler;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public IBinder onUnBind(Intent arg0) {
        // TO DO Auto-generated method
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        createLocationRequest();
//        Log.i(TAG, "checkPlayServices " + checkPlayServices());
//        if (checkPlayServices()) {
//            buildGoogleApiClient();
//
//            createLocationRequest();
//        }
    }

//    private void createLocationRequest() {
//        mLocationRequest = new LocationRequest();
//        mLocationRequest.setInterval(UPDATE_INTERVAL);
//        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
//        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
//        mLocationRequest.setSmallestDisplacement(0); // 10 meters
//    }

//    private void buildGoogleApiClient() {
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(LocationServices.API).build();
//
//        if (mGoogleApiClient != null) {
//            mGoogleApiClient.connect();
//        }
//    }

//    private boolean checkPlayServices() {
//        int resultCode = GooglePlayServicesUtil
//                .isGooglePlayServicesAvailable(this);
//        if (resultCode != ConnectionResult.SUCCESS) {
//            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
//                GooglePlayServicesUtil.getErrorDialog(resultCode, (AppCompatActivity) getApplicationContext(),
//                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
//            } else {
//                Log.i(TAG, "This device is not supported.");
//            }
//            return false;
//        }
//        return true;
//    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle bundleExtras = intent.getExtras();
        messageHandler = (Messenger) bundleExtras.get("MESSENGER");
        if (mLastLocation != null)
            sendMessageToActivity(101, mLastLocation);

        return START_STICKY;
    }

//    private void togglePeriodicLocationUpdates() {
//        if (!mRequestingLocationUpdates) {
//            mRequestingLocationUpdates = true;
//
//            // Starting the location updates
//            startLocationUpdates();
//
//            Log.d(TAG, "Periodic location updates started!");
//
//        } else {
//            mRequestingLocationUpdates = false;
//
//            // Stopping the location updates
//            stopLocationUpdates();
//
//            Log.d(TAG, "Periodic location updates stopped!");
//        }
//    }

//    private void stopLocationUpdates() {
//        LocationServices.FusedLocationApi.removeLocationUpdates(
//                mGoogleApiClient, this);
//    }

//    private void startLocationUpdates() {
//        LocationServices.FusedLocationApi.requestLocationUpdates(
//                mGoogleApiClient, mLocationRequest, this);
//    }

//    private void displayLocation() {
//        mLastLocation = LocationServices.FusedLocationApi
//                .getLastLocation(mGoogleApiClient);
//
//        if (mLastLocation != null) {
//            double latitude = mLastLocation.getLatitude();
//            double longitude = mLastLocation.getLongitude();
//
//            Log.i(TAG, "Latitude " + latitude + ", Longitude " + longitude);
//            sendMessageToActivity(101, mLastLocation);
//
//        } else {
//            obtainLocationUpdates();
//
//            Log.i(TAG, "Couldn't get the location. Make sure location is enabled on the device)");
//
//        }
//    }

    @TargetApi(Build.VERSION_CODES.M)
    private void createLocationRequest() {
        try {
            this.mLocationNetworkManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            this.mLocationGPSManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);

            isGPSEnabled = this.mLocationGPSManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            Log.i(FusedLocationService.class.getSimpleName(),
                    "GPS " + isGPSEnabled);

            isNetworkEnabled = this.mLocationNetworkManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            Log.i(FusedLocationService.class.getSimpleName(),
                    "Network " + isNetworkEnabled);

            if (!isGPSEnabled && !isNetworkEnabled) {
                Toast.makeText(getApplicationContext(),
                        "GPS & Network belum diaktifkan!",
                        Toast.LENGTH_SHORT).show();
            } else {
                if (isNetworkEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        ((AppCompatActivity)getApplicationContext()).requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
                    }
                    this.mLocationNetworkManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            0,
                            0,
                            this);
                    Log.i(FusedLocationService.class.getSimpleName(),
                            "request Location from Network");

                    displayLocationFromNetwork();
                } else{
                    Log.i(FusedLocationService.class.getSimpleName(),
                            "Network is disabled");
                }

                if (isGPSEnabled) {
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        ((AppCompatActivity)getApplicationContext()).requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
                    }
                    this.mLocationGPSManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            0,
                            0,
                            this);
                    Log.i(FusedLocationService.class.getSimpleName(),
                            "request Location from GPS");

                    displayLocationFromGPS();
                } else{
                    Log.i(FusedLocationService.class.getSimpleName(),
                            "GPS is disabled");
                }

            }
        } catch (Exception e) {
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void displayLocationFromNetwork() {
        if (isNetworkEnabled) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ((AppCompatActivity)getApplicationContext()).requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 0);
            }
            this.mNetworkLocation = this.mLocationNetworkManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Log.i(FusedLocationService.class.getSimpleName(),
                    "Network Latitude " + this.mNetworkLocation.getLatitude() + ", " +
                    "Network Longitude" + this.mNetworkLocation.getLongitude());


            sendMessageToActivity(101, this.mNetworkLocation);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void displayLocationFromGPS() {
        if (isGPSEnabled) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                ((AppCompatActivity)getApplicationContext()).requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
            this.mLastLocation = this.mLocationGPSManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Log.i(FusedLocationService.class.getSimpleName(),
                    "GPS Latitude " + this.mLastLocation.getLatitude() + ", " +
                    "GPS Longitude " + this.mLastLocation.getLongitude());

            sendMessageToActivity(101, this.mLastLocation);
        }
    }

    private void sendMessageToActivity(int state, Location location) {
        Message message = Message.obtain();
        switch (state) {
            case 101:
                Bundle bundleData = new Bundle();
                bundleData.putDouble("LATITUDE", location.getLatitude());
                bundleData.putDouble("LONGITUDE", location.getLongitude());
                message.arg1 = 101;
                message.setData(bundleData);
                try {
                    messageHandler.send(message);
                } catch (RemoteException e) {
                    Log.i(TAG, "Error send message " + e.getMessage());
                }

                break;
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onDestroy() {
        super.onDestroy();
//        if (mGoogleApiClient != null) {
//            mGoogleApiClient.disconnect();
//        }

        if (this.mLocationNetworkManager != null) {
            this.mLocationNetworkManager.removeUpdates(this);
        }

        if (this.mLocationGPSManager != null) {
            this.mLocationGPSManager.removeUpdates(this);
        }

    }

//    @Override
//    public void onConnected(Bundle bundle) {
//        displayLocation();
//
//        togglePeriodicLocationUpdates();
//        //displayLocation();
//    }

//    @Override
//    public void onConnectionSuspended(int i) {
//        mGoogleApiClient.connect();
//    }

//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
//                + connectionResult.getErrorCode());
//    }

    @Override
    public void onLocationChanged(Location location) {
        // Assign the new location
        mLastLocation = location;
        mNetworkLocation = location;

        Log.i(TAG, "Location changed!");

        sendMessageToActivity(101, mLastLocation);
        sendMessageToActivity(101, mNetworkLocation);

        // Displaying the new location on UI
        //displayLocation();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.i(FusedLocationService.class.getSimpleName(),
                "Provider " + provider + " are changed, Status " + status);
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.i(FusedLocationService.class.getSimpleName(),
                "Provider " + provider + " are enabled");
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.i(FusedLocationService.class.getSimpleName(),
                "Provider " + provider + " are disabled");
    }
}
