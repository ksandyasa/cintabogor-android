package com.aprido.cintabogor.fragment;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.aprido.cintabogor.R;
import com.aprido.cintabogor.adapter.ContentListAdapter;
import com.aprido.cintabogor.callback.ContentListCallback;
import com.aprido.cintabogor.callback.ContentListFragmentCallback;
import com.aprido.cintabogor.logic.ContentListLogic;
import com.aprido.cintabogor.object.Content;
import com.aprido.cintabogor.service.NetworkConnection;
import com.aprido.cintabogor.utility.CintaBogorUtils;
import com.aprido.cintabogor.utility.Config;
import com.nhaarman.listviewanimations.appearance.simple.ScaleInAnimationAdapter;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apridosandyasa on 10/7/16.
 */

public class ContentList extends Fragment implements ContentListCallback {

    private Context context;
    private View view;
    private GridView gv_content_list;
    private ContentListAdapter gv_content_list_adapter;
    private ScaleInAnimationAdapter scaleInAnimationAdapter;
    private ContentListFragmentCallback callback;
    private int mCategoryId;
    private int mId;
    private ContentListLogic contentListLogic;
    private Bundle data;
    private List<Content> contentList = new ArrayList<>();
    private BroadcastReceiver contentListReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String key = "position" + String.valueOf(data.getInt("position") - 1);
            if (intent.hasExtra(key)) {
                if (intent.getStringExtra(key).equals("finishLoad")) {
                    contentListLogic.setupContentListViews(mId, mCategoryId);
                }
            }
        }
    };

    public ContentList() {

    }

    @SuppressLint("ValidFragment")
    public ContentList(Context context, ContentListFragmentCallback listener, int id, int categoryId) {
        this.context = context;
        this.callback = listener;
        this.mId = id;
        this.mCategoryId = categoryId;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.data = getArguments();

        this.view = inflater.inflate(R.layout.content_item_list, container, false);

        return this.view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.gv_content_list = (GridView) view.findViewById(R.id.gv_content_list);

        this.gv_content_list_adapter = new ContentListAdapter(this.context, this.contentList);
        this.scaleInAnimationAdapter = new ScaleInAnimationAdapter(this.gv_content_list_adapter);
        this.scaleInAnimationAdapter.setAbsListView(this.gv_content_list);
        this.gv_content_list.setAdapter(this.scaleInAnimationAdapter);
        this.gv_content_list.setOnItemClickListener(new ActionItemClick());

        this.contentListLogic = new ContentListLogic(this.context, this);

        if (this.data.getInt("position") == 0) {
            this.contentListLogic.setupContentListViews(this.mId, this.mCategoryId);
        }

        this.context.registerReceiver(this.contentListReceiver, new IntentFilter("com.aprido.cintabogor"));
    }

    @Override
    public void onDetach() {
        if (this.contentListReceiver != null) {
            this.context.unregisterReceiver(this.contentListReceiver);
        }
        super.onDetach();
    }

    @Override
    public void failedSetupContentListViews() {

    }

    @Override
    public void finishedSetupContentListViews(List<Content> contentList) {
        this.contentList = contentList;
        Log.d("TAG", "contentList size " + this.contentList.size());

        this.gv_content_list_adapter = new ContentListAdapter(this.context, this.contentList);
        this.scaleInAnimationAdapter = new ScaleInAnimationAdapter(this.gv_content_list_adapter);
        this.scaleInAnimationAdapter.setAbsListView(this.gv_content_list);
        this.gv_content_list.setAdapter(this.scaleInAnimationAdapter);
        this.gv_content_list.setOnItemClickListener(new ActionItemClick());

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        intent.setAction("com.aprido.cintabogor");
        intent.putExtra("position"+this.data.getInt("position"), "finishLoad");
        this.context.sendBroadcast(intent);
    }

    private class ActionItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            callback.selectedContentListItem(contentList.get(position));
        }
    }
}
