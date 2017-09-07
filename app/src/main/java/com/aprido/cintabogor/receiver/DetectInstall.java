package com.aprido.cintabogor.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by apridosandyasa on 6/12/17.
 */

public class DetectInstall extends BroadcastReceiver {

    private String referrerId;

    @Override
    public void onReceive(Context context, Intent intent) {

        if ((null != intent)
                && (intent.getAction().equals("com.android.vending.INSTALL_REFERRER"))) {
            referrerId = intent.getStringExtra("referrer");
            Intent broadcastIntent = new Intent();
            broadcastIntent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
            broadcastIntent.setAction("com.aprido.cintabogor");
            broadcastIntent.putExtra("referrer", referrerId);
            context.sendBroadcast(broadcastIntent);
//            Log.d("TAG", "referrer " + referrerId);
//            Toast.makeText(context, "referrer " + referrerId, Toast.LENGTH_SHORT).show();
        }
    }
}