package com.aprido.cintabogor.callback;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by apridosandyasa on 5/2/17.
 */

public interface MapRouteLogicCallback {
    void failedSetupMapRouteViews();
    void finishedSetupMapRouteViews(List<LatLng> latLngList);
}
