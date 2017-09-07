package com.aprido.cintabogor.logic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.aprido.cintabogor.callback.ContentListCallback;
import com.aprido.cintabogor.object.CategoryContent;
import com.aprido.cintabogor.object.Content;
import com.aprido.cintabogor.service.NetworkConnection;
import com.aprido.cintabogor.utility.CintaBogorUtils;
import com.aprido.cintabogor.utility.Config;
import com.aprido.cintabogor.utility.PreferenceUtility;

import org.json.JSONException;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apridosandyasa on 10/7/16.
 */

public class ContentListLogic {

    private final String TAG = ContentListLogic.class.getSimpleName();
    private Context context;
    private ContentListCallback callback;
    private Messenger messenger;
    private Message message;
    private Bundle bundle;
    private Handler handler;
    private String failureResponse = "";
    private String[] stringResponse = {""};
    private List<CategoryContent> categoryContentList = new ArrayList<>();
    private List<Content> contentList = new ArrayList<>();

    public ContentListLogic(Context context, ContentListCallback listener) {
        this.context = context;
        this.callback = listener;
    }

    public void setupContentListViews(int id, int categoryId) {
        if (CintaBogorUtils.ConnectionUtility.isNetworkConnected(this.context)) {
            obtainContentListItems(id, categoryId);
        }else{
            this.callback.failedSetupContentListViews();
        }
    }


    private void doNetworkService(String url) {
        Intent networkIntent = new Intent(this.context, NetworkConnection.class);
        this.messenger = new Messenger(this.handler);
        networkIntent.putExtra("messenger", this.messenger);
        networkIntent.putExtra("url", url);
        networkIntent.putExtra("from", "ContentListLogic");
        this.context.startService(networkIntent);
    }

    private void obtainContentListItems(int id, int categoryId) {
        this.handler = new Handler(this.context.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                parseContentListItemsResponse(msg);
            }

        };
        doNetworkService(Config.API_GET_CATEGORY_CONTENT_LIST + "?menu=" + id + "&category=" + categoryId + "&type=place");
    }

    private void parseContentListItemsResponse(Message message) {
        this.message = message;
        this.bundle = this.message.getData();
        this.failureResponse = this.bundle.getString("network_failure");
        if (this.failureResponse.equals("yes")) {
            this.callback.failedSetupContentListViews();
        }else{
            this.stringResponse[0] = this.bundle.getString("network_response");;
            try {
//                this.stringResponse[0] = new String(this.bundle.getByteArray("network_response_bytes"), "UTF-8");
                Log.d(TAG, this.stringResponse[0]);
                this.contentList = CintaBogorUtils.ParseJSONUtility.getListContentItemsFromJSON(this.stringResponse[0]);
            } catch (JSONException e) {
                Log.d(TAG, "Exception " + e.getMessage());
            }
            this.callback.finishedSetupContentListViews(this.contentList);
        }
    }

}
