package com.aprido.cintabogor.logic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.aprido.cintabogor.callback.MainCallback;
import com.aprido.cintabogor.object.Banner;
import com.aprido.cintabogor.object.Content;
import com.aprido.cintabogor.object.MainContent;
import com.aprido.cintabogor.object.SlideMenu;
import com.aprido.cintabogor.service.NetworkConnection;
import com.aprido.cintabogor.utility.CintaBogorUtils;
import com.aprido.cintabogor.utility.Config;
import com.aprido.cintabogor.utility.PreferenceUtility;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apridosandyasa on 10/7/16.
 */

public class MainLogic {

    private final String TAG = MainLogic.class.getSimpleName();
    private Context context;
    private MainCallback callback;
    private Messenger messenger;
    private Message message;
    private Bundle bundle;
    private Handler handler;
    private String failureResponse = "";
    private String[] stringResponse = {""};
    private List<SlideMenu> slideMenuList = new ArrayList<>();
    private List<MainContent> mainContentList = new ArrayList<>();
    private List<Banner> contentList = new ArrayList<>();
    private List<Content> nearbyList = new ArrayList<>();

    public MainLogic(Context context, MainCallback listener) {
        this.context = context;
        this.callback = listener;
    }

    public void setupMainViews() {
        finishedSetupMainViews();
        setupContentViews();
        Log.d("TAG", "setupMainViews");
    }

    public void setupContentViews() {
        if (CintaBogorUtils.ConnectionUtility.isNetworkConnected(this.context)) {
            obtainContentViewsItems();
        }else{
            this.callback.failedSetupContentViews();
        }
    }

    public void setupSlideViews(int id) {
        if (CintaBogorUtils.ConnectionUtility.isNetworkConnected(this.context)) {
            obtainSlideItems(id);
        }else{
            this.callback.failedSetupContentViews();
        }
    }

    public void setupNearbyViews(int id) {
        if (CintaBogorUtils.ConnectionUtility.isNetworkConnected(this.context)) {
            obtainNearbyItems(id);
        }else{
            this.callback.failedSetupContentViews();
        }
    }

    private void finishedSetupMainViews() {
        this.callback.finishedSetupToolbar();
        this.callback.finishedSetupBottomSheet();
    }

    private void doNetworkService(String url) {
        Intent networkIntent = new Intent(this.context, NetworkConnection.class);
        this.messenger = new Messenger(this.handler);
        networkIntent.putExtra("messenger", this.messenger);
        networkIntent.putExtra("url", url);
        networkIntent.putExtra("from", "MainContent");
        this.context.startService(networkIntent);
    }

    private void obtainContentViewsItems() {
        this.handler = new Handler(this.context.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                parseContentViewsItemsResponse(msg);
            }

        };
        doNetworkService(Config.API_GET_CONTENT_MAIN_MENU);
    }

    private void parseContentViewsItemsResponse(Message message) {
        this.message = message;
        this.bundle = this.message.getData();
        this.failureResponse = this.bundle.getString("network_failure");
        if (this.failureResponse.equals("yes")) {
            this.callback.failedSetupContentViews();
        }else{
            this.stringResponse[0] = this.bundle.getString("network_response");
            PreferenceUtility.getInstance().saveData(context, PreferenceUtility.CONTENT_VIEWS, this.stringResponse[0]);
            try {
                this.mainContentList = CintaBogorUtils.ParseJSONUtility.getMainContentListItemsFromJSON(this.stringResponse[0]);
//                this.callback.finishedSetupContentViews(this.mainContentList);
            } catch (JSONException e) {
                Log.d(TAG, "Exception " + e.getMessage());
            }
            obtainNavigationItems();
        }
    }

    private void obtainSlideItems(int id) {
        this.handler = new Handler(this.context.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                parseSlideItemsResponse(msg);
            }

        };
        doNetworkService(Config.API_GET_BANNER);
    }

    private void parseSlideItemsResponse(Message message) {
        this.message = message;
        this.bundle = this.message.getData();
        this.failureResponse = this.bundle.getString("network_failure");
        if (this.failureResponse.equals("yes")) {
            this.callback.failedSetupContentViews();
        }else{
            this.stringResponse[0] = this.bundle.getString("network_response");
            try {
                this.contentList = CintaBogorUtils.ParseJSONUtility.getListBannerItemsFromJSON(this.stringResponse[0]);
                Log.d("TAG", "size slide list " + this.contentList.size());
                this.callback.finishedSetupSlideViews(this.contentList);
            } catch (JSONException e) {
                Log.d(TAG, "Exception " + e.getMessage());
            }
        }
    }

    private void obtainNearbyItems(int id) {
        this.handler = new Handler(this.context.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                parseNearbyItemsResponse(msg);
            }

        };
        doNetworkService(Config.API_GET_CONTENT_NEARBY);
    }

    private void parseNearbyItemsResponse(Message message) {
        this.message = message;
        this.bundle = this.message.getData();
        this.failureResponse = this.bundle.getString("network_failure");
        if (this.failureResponse.equals("yes")) {
            this.callback.failedSetupContentViews();
        }else{
            this.stringResponse[0] = this.bundle.getString("network_response");
            try {
                this.nearbyList = CintaBogorUtils.ParseJSONUtility.getListContentItemsFromJSON(this.stringResponse[0]);
                Log.d("TAG", "size nearby list " + this.nearbyList.size());
                this.callback.finishedSetupNearbyListViews(this.nearbyList);
            } catch (JSONException e) {
                Log.d(TAG, "Exception " + e.getMessage());
            }
        }
    }

    private void obtainNavigationItems() {
        this.handler = new Handler(this.context.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                parseNavigationItemsResponse(msg);
            }

        };
        doNetworkService(Config.API_GET_SLIDE_MENU);
    }

    private void parseNavigationItemsResponse(Message message) {
        this.message = message;
        this.bundle = this.message.getData();
        this.failureResponse = this.bundle.getString("network_failure");
        if (this.failureResponse.equals("yes")) {
            this.callback.failedSetupContentViews();
        }else{
            this.stringResponse[0] = this.bundle.getString("network_response");
            try {
                this.slideMenuList = CintaBogorUtils.ParseJSONUtility.getListNavigationItemsFromJSON(this.stringResponse[0]);
                this.callback.finishedSetupNavigationViews(this.slideMenuList, this.mainContentList);
            } catch (JSONException e) {
                Log.d(TAG, "Exception " + e.getMessage());
            }
        }
    }

}
