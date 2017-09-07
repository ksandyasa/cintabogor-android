package com.aprido.cintabogor.logic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.aprido.cintabogor.callback.MainCallback;
import com.aprido.cintabogor.callback.MapRouteLogicCallback;
import com.aprido.cintabogor.service.NetworkConnection;
import com.aprido.cintabogor.utility.CintaBogorUtils;
import com.aprido.cintabogor.utility.Config;
import com.aprido.cintabogor.utility.PreferenceUtility;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apridosandyasa on 5/2/17.
 */

public class MapRouteLogic {

    private final String TAG = MapRouteLogic.class.getSimpleName();
    private Context context;
    private Messenger messenger;
    private MapRouteLogicCallback callback;
    private Message message;
    private Bundle bundle;
    private Handler handler;
    private String failureResponse = "";
    private String[] stringResponse = {""};
    private List<LatLng> latLngList = new ArrayList<>();

    public MapRouteLogic(Context context, MapRouteLogicCallback listener) {
        this.context = context;
        this.callback = listener;
    }

    private void doNetworkService(String url) {
        Intent networkIntent = new Intent(this.context, NetworkConnection.class);
        this.messenger = new Messenger(this.handler);
        networkIntent.putExtra("messenger", this.messenger);
        networkIntent.putExtra("url", url);
        networkIntent.putExtra("from", "MapRouteLogic");
        this.context.startService(networkIntent);
    }

    public void setupMapRouteViews(LatLng placeLatLng, LatLng userLatLng) {
        if (CintaBogorUtils.ConnectionUtility.isNetworkConnected(this.context)) {
            obtainMapRouteItems(placeLatLng, userLatLng);
        }else{
            this.callback.failedSetupMapRouteViews();
        }
    }

    private void obtainMapRouteItems(LatLng placeLatLng, LatLng userLatLng) {
        this.handler = new Handler(this.context.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                parseMapRouteItemsResponse(msg);
            }

        };
        doNetworkService(Config.API_MAP_ROUTE + "?origin="+ placeLatLng.latitude + "," + placeLatLng.longitude + "&destination=" + userLatLng.latitude + "," + userLatLng.longitude + "&sensor=false");
    }

    private void parseMapRouteItemsResponse(Message message) {
        this.message = message;
        this.bundle = this.message.getData();
        this.failureResponse = this.bundle.getString("network_failure");
        if (this.failureResponse.equals("yes")) {
            this.callback.failedSetupMapRouteViews();
        }else{
            this.stringResponse[0] = this.bundle.getString("network_response");
            try {
                this.latLngList = CintaBogorUtils.ParseJSONUtility.getRouteListFromJSON(this.stringResponse[0]);
            } catch (JSONException e) {
                Log.d(TAG, "Exception map route " + e.getMessage());
            }
            this.callback.finishedSetupMapRouteViews(this.latLngList);
        }
    }

}
