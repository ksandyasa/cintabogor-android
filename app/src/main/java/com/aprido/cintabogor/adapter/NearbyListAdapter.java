package com.aprido.cintabogor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aprido.cintabogor.R;
import com.aprido.cintabogor.callback.NearbyListAdapterCallback;
import com.aprido.cintabogor.object.Content;
import com.aprido.cintabogor.utility.CintaBogorUtils;
import com.aprido.cintabogor.utility.Config;
import com.aprido.cintabogor.utility.PreferenceUtility;
import com.aprido.cintabogor.widget.CustomTextView;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by apridosandyasa on 10/7/16.
 */

public class NearbyListAdapter extends RecyclerView.Adapter<NearbyListAdapter.NearbyListViewHolder> {

    private Context context;
    private NearbyListAdapterCallback callback;
    private List<Content> contentList = new ArrayList<>();

    public NearbyListAdapter(Context context, NearbyListAdapterCallback listener, List<Content> objects) {
        this.context = context;
        this.callback = listener;
        this.contentList = objects;
    }

    @Override
    public NearbyListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_nearby, parent, false);
        NearbyListViewHolder holder = new NearbyListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NearbyListViewHolder holder, int position) {
        holder.tv_title_item_nearby.setText(this.contentList.get(position).getName());
        holder.tv_detail_item_nearby.setText(this.contentList.get(position).getAddress_1());

        Log.d("TAG", "url " + this.contentList.get(position).getPicture_1());

        Picasso.with(this.context)
                .load((this.contentList.get(position).getPicture_1().contains("http")) ? this.contentList.get(position).getPicture_1() : Config.URL_IMAGE + this.contentList.get(position).getPicture_1())
                .placeholder(R.drawable.placeholder)
                .into(holder.civ_pict_item_nearby);

        LatLng userLatLng = new LatLng(Double.valueOf(PreferenceUtility.getInstance().loadDataString(this.context, PreferenceUtility.USER_LATITUDE)),
                Double.valueOf(PreferenceUtility.getInstance().loadDataString(this.context, PreferenceUtility.USER_LONGITUDE)));
        LatLng placeLatLng = new LatLng(Double.valueOf(this.contentList.get(position).getLatitude()),
                Double.valueOf(this.contentList.get(position).getLongitude()));
        holder.tv_dist_item_nearby.setText("" + CintaBogorUtils.getDistanceFormatInString(CintaBogorUtils.LocationUtility.distanceBetween(userLatLng, placeLatLng)) + " km");

        holder.ll_container_item_nearby.setOnClickListener(new ActionClick(position));
    }

    @Override
    public int getItemCount() {
        return this.contentList.size();
    }

    private class ActionClick implements View.OnClickListener {

        private int mPosition;

        public ActionClick(int position) {
            this.mPosition = position;
        }

        @Override
        public void onClick(View v) {
            callback.selectedNearbyItem(this.mPosition);
        }
    }

    public static class NearbyListViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout ll_container_item_nearby;
        private CircleImageView civ_pict_item_nearby;
        private CustomTextView tv_title_item_nearby, tv_dist_item_nearby,
                                tv_detail_item_nearby;

        NearbyListViewHolder(View view) {
            super(view);
            this.ll_container_item_nearby = (LinearLayout) view.findViewById(R.id.ll_container_item_nearby);
            this.civ_pict_item_nearby = (CircleImageView) view.findViewById(R.id.civ_pict_item_nearby);
            this.tv_title_item_nearby = (CustomTextView) view.findViewById(R.id.tv_title_item_nearby);
            this.tv_dist_item_nearby = (CustomTextView) view.findViewById(R.id.tv_dist_item_nearby);
            this.tv_detail_item_nearby = (CustomTextView) view.findViewById(R.id.tv_detail_item_nearby);
        }
    }
}
