package com.aprido.cintabogor.logic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.util.Log;

import com.aprido.cintabogor.callback.CategoryTabCallback;
import com.aprido.cintabogor.callback.ContentListCallback;
import com.aprido.cintabogor.object.CategoryContent;
import com.aprido.cintabogor.object.Content;
import com.aprido.cintabogor.service.NetworkConnection;
import com.aprido.cintabogor.utility.CintaBogorUtils;
import com.aprido.cintabogor.utility.Config;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apridosandyasa on 4/30/17.
 */

public class CategoryLogic {

    private final String TAG = CategoryLogic.class.getSimpleName();
    private Context context;
    private Messenger messenger;
    private Message message;
    private Bundle bundle;
    private Handler handler;
    private String failureResponse = "";
    private String[] stringResponse = {""};
    private CategoryTabCallback callback;
    private List<CategoryContent> categoryContentList = new ArrayList<>();
    private List<Content> contentList = new ArrayList<>();

    public CategoryLogic(Context context, CategoryTabCallback listener) {
        this.context = context;
        this.callback = listener;
    }

    private void doNetworkService(String url) {
        Intent networkIntent = new Intent(this.context, NetworkConnection.class);
        this.messenger = new Messenger(this.handler);
        networkIntent.putExtra("messenger", this.messenger);
        networkIntent.putExtra("url", url);
        networkIntent.putExtra("from", "CategoryContent");
        this.context.startService(networkIntent);
    }

    public void setupCategoryTabViews(int id) {
        if (CintaBogorUtils.ConnectionUtility.isNetworkConnected(this.context)) {
            obtainCategoryContentListItems(id);
        }else{
            this.callback.failedSetupCategoryTabViews();
        }
    }

    public void setupContentCategoryTabViews(int id, int categoryId) {
        if (CintaBogorUtils.ConnectionUtility.isNetworkConnected(this.context)) {
            obtainContentListItems(id, categoryId);
        }else{
            this.callback.failedSetupCategoryTabViews();
        }

    }

    private void obtainCategoryContentListItems(int id) {
        this.handler = new Handler(this.context.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                parseCategoryContentListItemsResponse(msg);
            }

        };
        doNetworkService(Config.API_GET_CATEGORY_CONTENT_LIST + "?menu=" + id + "&type=category");
    }

    private void parseCategoryContentListItemsResponse(Message message) {
        this.message = message;
        this.bundle = this.message.getData();
        this.failureResponse = this.bundle.getString("network_failure");
        if (this.failureResponse.equals("yes")) {
            this.callback.failedSetupCategoryTabViews();
        }else{
            this.stringResponse[0] = this.bundle.getString("network_response");
            try {
                this.categoryContentList.clear();
                this.categoryContentList = CintaBogorUtils.ParseJSONUtility.getCategoryContentListItemsFromJSON(this.stringResponse[0]);
                this.callback.finishedSetupCategoryTabViews(this.categoryContentList);
            } catch (JSONException e) {
                Log.d(TAG, "Exception " + e.getMessage());
            }
        }
    }

    private void obtainContentListItems(int id, final int categoryId) {
        this.handler = new Handler(this.context.getMainLooper()) {

            @Override
            public void handleMessage(Message msg) {
                parseContentListItemsResponse(msg, categoryId);
            }

        };
        doNetworkService(Config.API_GET_CATEGORY_CONTENT_LIST + "?menu=" + id + "&category=" + categoryId + "&type=place");
    }

    private void parseContentListItemsResponse(Message message, int categoryId) {
        this.message = message;
        this.bundle = this.message.getData();
        this.failureResponse = this.bundle.getString("network_failure");
        if (this.failureResponse.equals("yes")) {
            this.callback.failedSetupCategoryTabViews();
        }else{
            this.stringResponse[0] = this.bundle.getString("network_response");;
            try {
                this.contentList = CintaBogorUtils.ParseJSONUtility.getListContentItemsFromJSON(this.stringResponse[0]);
                Log.d(TAG, this.stringResponse[0]);
            } catch (JSONException e) {
                Log.d(TAG, "Exception " + e.getMessage());
            }
            this.callback.finishedSetupContentCategoryTabViews(this.contentList, categoryId);
        }
    }

}
