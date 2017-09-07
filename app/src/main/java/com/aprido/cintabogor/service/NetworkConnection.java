package com.aprido.cintabogor.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.aprido.cintabogor.logic.ContentListLogic;
import com.aprido.cintabogor.utility.CintaBogorUtils;
import com.aprido.cintabogor.utility.PreferenceUtility;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by apridosandyasa on 6/19/16.
 */
public class NetworkConnection extends IntentService {
    private final String TAG = NetworkConnection.class.getSimpleName();
    private String[] responseString = {""};
    private StringBuilder builder;
    private Messenger messenger;
    private Message message;
    private String url;
    private String from = "";

    public NetworkConnection() {
        super("");
    }

    public void doObtainDataFromServer(String url) {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .build();

        Request request = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/json")
                .addHeader("Content-Encoding", "gzip")
                .build();
        Log.d(TAG, url);

        Call call = okHttpClient.newCall(request);
        call.enqueue(new NetworkConnectionCallback());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        this.messenger = (Messenger) intent.getParcelableExtra("messenger");
        this.url = intent.getStringExtra("url");
        this.from = intent.getStringExtra("from");
        this.message = Message.obtain();
        doObtainDataFromServer(this.url);
    }

    private class NetworkConnectionCallback implements Callback {

        @Override
        public void onFailure(Call call, IOException e) {
            onFailureInMainThread();
        }

        @Override
        public void onResponse(Call call, final Response response) throws IOException {
            try {
                if (response.isSuccessful())
                    onResponseInMainThread(response);
                else
                    onFailureInMainThread();
            } catch (IOException e) {
                Log.d(TAG, "Exception " + e.getMessage());
            }
        }
    }

    public void onFailureInMainThread() {
        Bundle bundle = new Bundle();
        bundle.putString("network_response", this.responseString[0]);
        bundle.putString("network_failure", "yes");
        this.message.setData(bundle);
        try {
            this.messenger.send(this.message);
        } catch (RemoteException e) {
            Log.d(TAG, "Exception" + e.getMessage());
        }
    }

    public void onResponseInMainThread(Response response) throws IOException {
        this.responseString[0] = response.body().string();
        CintaBogorUtils.JSON_DATA = this.responseString[0];
        this.message.setData(null);
        Log.d(TAG, "responseString[0] " + this.responseString[0]);
        Log.d(TAG, "message.what" + this.message.what);
        Bundle bundle = new Bundle();
        Log.d(TAG, "from " + this.from);
        if (!this.from.equals("ContentListLogic"))
            bundle.putString("network_response", this.responseString[0]);
        else
            bundle.putString("network_response", this.responseString[0]);
        bundle.putString("network_failure", "no");
        this.message.setData(bundle);
        try {
            this.messenger.send(this.message);
        } catch (RemoteException e) {
            Log.d(TAG, "Exception" + e.getMessage());
        }
    }

}
