package com.aprido.cintabogor.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.aprido.cintabogor.R;
import com.aprido.cintabogor.object.Content;
import com.aprido.cintabogor.utility.CintaBogorUtils;
import com.aprido.cintabogor.utility.Config;
import com.aprido.cintabogor.utility.PreferenceUtility;
import com.aprido.cintabogor.widget.CustomTextView;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apridosandyasa on 10/7/16.
 */

public class ContentListAdapter extends BaseAdapter {

    private Context context;
    private ContentListViewHolder holder;
    private List<Content> contentList = new ArrayList<>();

    public ContentListAdapter(Context context, List<Content> objects) {
        this.context = context;
        this.contentList = objects;
    }

    @Override
    public int getCount() {
        return this.contentList.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView ==  null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_content_list, parent, false);
            this.holder = new ContentListViewHolder();
            convertView.setTag(this.holder);
        }else {
            this.holder = (ContentListViewHolder) convertView.getTag();
        }

        this.holder.rl_container_item_content_list = (RelativeLayout) convertView.findViewById(R.id.rl_container_item_content_list);
        this.holder.iv_pict_item_content_list = (ImageView) convertView.findViewById(R.id.iv_pict_item_content_list);
        this.holder.tv_title_item_content_list = (CustomTextView) convertView.findViewById(R.id.tv_title_item_content_list);
        this.holder.tv_dist_item_content_list = (CustomTextView) convertView.findViewById(R.id.tv_dist_item_content_list);

        this.holder.tv_title_item_content_list.setText(this.contentList.get(position).getName());

        Log.d("TAG", "url " + this.contentList.get(position).getPicture_1());

        Picasso.with(this.context)
                .load((this.contentList.get(position).getPicture_1().contains("http")) ? this.contentList.get(position).getPicture_1() : Config.URL_IMAGE + this.contentList.get(position).getPicture_1())
                .placeholder(R.drawable.placeholder)
                .into(holder.iv_pict_item_content_list);

        LatLng userLatLng = new LatLng(Double.valueOf(PreferenceUtility.getInstance().loadDataString(this.context, PreferenceUtility.USER_LATITUDE)),
                Double.valueOf(PreferenceUtility.getInstance().loadDataString(this.context, PreferenceUtility.USER_LONGITUDE)));
        LatLng placeLatLng = new LatLng(Double.valueOf(this.contentList.get(position).getLatitude()),
                Double.valueOf(this.contentList.get(position).getLongitude()));
        this.holder.tv_dist_item_content_list.setText("" + CintaBogorUtils.getDistanceFormatInString(CintaBogorUtils.LocationUtility.distanceBetween(userLatLng, placeLatLng)) + " km");

        return convertView;
    }

    static class ContentListViewHolder {
        private RelativeLayout rl_container_item_content_list;
        private ImageView iv_pict_item_content_list;
        private CustomTextView tv_title_item_content_list, tv_dist_item_content_list;
    }
}
