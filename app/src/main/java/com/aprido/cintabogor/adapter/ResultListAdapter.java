package com.aprido.cintabogor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aprido.cintabogor.R;
import com.aprido.cintabogor.callback.ResultListAdapterCallback;
import com.aprido.cintabogor.widget.CustomTextView;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by apridosandyasa on 10/14/16.
 */

public class ResultListAdapter extends RecyclerView.Adapter<ResultListAdapter.ResultListViewHolder> {

    private Context context;
    private ResultListAdapterCallback callback;

    public ResultListAdapter(Context context, ResultListAdapterCallback listener) {
        this.context = context;
        this.callback = listener;
    }

    @Override
    public ResultListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_nearby, parent, false);
        ResultListViewHolder holder = new ResultListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ResultListViewHolder holder, int position) {
        holder.ll_container_item_nearby.setOnClickListener(new ShowDetailViewsFromAdapter());
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    private class ShowDetailViewsFromAdapter implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            callback.ShowDetailViews();
        }
    }

    public static class ResultListViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout ll_container_item_nearby;
        private CircleImageView civ_pict_item_nearby;
        private CustomTextView tv_title_item_nearby, tv_dist_item_nearby,
                tv_detail_item_nearby;

        ResultListViewHolder(View view) {
            super(view);
            this.ll_container_item_nearby = (LinearLayout) view.findViewById(R.id.ll_container_item_nearby);
            this.civ_pict_item_nearby = (CircleImageView) view.findViewById(R.id.civ_pict_item_nearby);
            this.tv_title_item_nearby = (CustomTextView) view.findViewById(R.id.tv_title_item_nearby);
            this.tv_dist_item_nearby = (CustomTextView) view.findViewById(R.id.tv_dist_item_nearby);
            this.tv_detail_item_nearby = (CustomTextView) view.findViewById(R.id.tv_detail_item_nearby);
        }
    }

}
