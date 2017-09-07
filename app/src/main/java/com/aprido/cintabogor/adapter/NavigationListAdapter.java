package com.aprido.cintabogor.adapter;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.aprido.cintabogor.R;
import com.aprido.cintabogor.callback.NavigationListAdapterCallback;
import com.aprido.cintabogor.object.SlideMenu;
import com.aprido.cintabogor.widget.CustomTextView;

import java.util.List;

/**
 * Created by apridosandyasa on 10/7/16.
 */

public class NavigationListAdapter extends RecyclerView.Adapter<NavigationListAdapter.NavigationListViewHolder> {

    private Context context;
    private List<SlideMenu> slideMenuList;
    private NavigationListAdapterCallback callback;

    public NavigationListAdapter(Context context, List<SlideMenu> objects, NavigationListAdapterCallback listener) {
        this.context = context;
        this.slideMenuList = objects;
        this.callback = listener;
    }

    @Override
    public NavigationListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_navigationview, parent, false);
        NavigationListViewHolder holder = new NavigationListViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(NavigationListViewHolder holder, int position) {
        Drawable mDrawable = (position == 0) ? this.context.getResources().getDrawable(R.drawable.ic_favourite) : (position == 1) ? this.context.getResources().getDrawable(R.drawable.ic_jurnal) : (position == 2) ? this.context.getResources().getDrawable(R.drawable.ic_pengaturan) : (position == 3) ? this.context.getResources().getDrawable(R.drawable.ic_about_us) : this.context.getResources().getDrawable(R.drawable.ic_contact_us);
        mDrawable.setColorFilter(new
                PorterDuffColorFilter(this.context.getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_ATOP));

        holder.tv_title_item_navigationview.setText(this.slideMenuList.get(position).getTitle());
        holder.iv_pict_item_navigationview.setImageDrawable(mDrawable);

        holder.ll_container_item_navigationview.setOnClickListener(new ActionClick(position));
    }

    @Override
    public int getItemCount() {
        return this.slideMenuList.size();
    }

    private class ActionClick implements View.OnClickListener {

        private int mPosition;

        public ActionClick(int position) {
            this.mPosition = position;
        }

        @Override
        public void onClick(View v) {
            callback.selectedNavigationItem(this.mPosition);
        }
    }

    public static class NavigationListViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout ll_container_item_navigationview;
        private ImageView iv_pict_item_navigationview;
        private CustomTextView tv_title_item_navigationview;

        NavigationListViewHolder(View view) {
            super(view);
            this.ll_container_item_navigationview = (LinearLayout) view.findViewById(R.id.ll_container_item_navigationview);
            this.iv_pict_item_navigationview = (ImageView) view.findViewById(R.id.iv_pict_item_navigationview);
            this.tv_title_item_navigationview = (CustomTextView) view.findViewById(R.id.tv_title_item_navigationview);
        }
    }
}
