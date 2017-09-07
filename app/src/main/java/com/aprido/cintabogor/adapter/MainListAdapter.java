package com.aprido.cintabogor.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.aprido.cintabogor.R;
import com.aprido.cintabogor.callback.MainListAdapterCallback;
import com.aprido.cintabogor.object.MainContent;
import com.aprido.cintabogor.widget.CustomTextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by apridosandyasa on 10/6/16.
 */

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.MainListViewHolder> {

    private Context context;
    private List<MainContent> mainContentList;
    private MainListAdapterCallback callback;
    private boolean isClickable = false;

    public MainListAdapter(Context context, List<MainContent> objects, MainListAdapterCallback listener) {
        this.context = context;
        this.mainContentList = objects;
        this.callback = listener;
    }

    @Override
    public MainListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_main, parent, false);
        MainListViewHolder holder = new MainListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MainListViewHolder holder, int position) {
        holder.civ_pict_item_main.setImageResource(this.mainContentList.get(position).getIcon());
        holder.tv_title_item_main.setText(this.mainContentList.get(position).getTitle());
        if (this.isClickable == true)
            holder.ll_container_item_main.setOnClickListener(new ActionClick(position));
        else
            holder.ll_container_item_main.setOnClickListener(null);
    }

    @Override
    public int getItemCount() {
        return this.mainContentList.size();
    }

    public void setItemClickable(boolean isClickable) {
        this.isClickable = isClickable;
        notifyDataSetChanged();
    }

    private class ActionClick implements View.OnClickListener {

        private int mPosition;

        public ActionClick(int position) {
            this.mPosition = position;
        }

        @Override
        public void onClick(View v) {
            callback.selectedMainContentItem(mPosition);
        }
    }

    public static class MainListViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout ll_container_item_main;
        private CircleImageView civ_pict_item_main;
        private CustomTextView tv_title_item_main;

        MainListViewHolder(View view) {
            super(view);

            this.ll_container_item_main = (LinearLayout) view.findViewById(R.id.ll_container_item_main);
            this.civ_pict_item_main = (CircleImageView) view.findViewById(R.id.civ_pict_item_main);
            this.tv_title_item_main = (CustomTextView) view.findViewById(R.id.tv_title_item_main);
        }
    }
}
