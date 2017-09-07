package com.aprido.cintabogor.logic;

import android.content.Context;

import com.aprido.cintabogor.callback.ResultCallback;

/**
 * Created by apridosandyasa on 10/14/16.
 */

public class ResultLogic {

    private Context context;
    private ResultCallback callback;

    public ResultLogic(Context context, ResultCallback listener) {
        this.context = context;
        this.callback = listener;
    }

    public void setupResultViews() {
        callback.finishedSetupResultViews();
    }
}
