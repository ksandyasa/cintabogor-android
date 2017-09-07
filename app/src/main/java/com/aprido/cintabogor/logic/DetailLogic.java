package com.aprido.cintabogor.logic;

import android.content.Context;

import com.aprido.cintabogor.callback.DetailCallback;

/**
 * Created by apridosandyasa on 10/7/16.
 */

public class DetailLogic {

    private Context context;
    private DetailCallback callback;

    public DetailLogic(Context context, DetailCallback listener) {
        this.context = context;
        this.callback = listener;
    }

    public void setupDetailViews() {
        this.callback.finishedSetupBottomSheetViews();
    }

}
