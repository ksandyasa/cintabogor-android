package com.aprido.cintabogor.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.aprido.cintabogor.DetailActivity;
import com.aprido.cintabogor.R;
import com.aprido.cintabogor.object.Banner;
import com.aprido.cintabogor.object.Content;
import com.aprido.cintabogor.utility.Config;
import com.aprido.cintabogor.widget.CustomTextView;
import com.squareup.picasso.Picasso;

/**
 * Created by apridosandyasa on 10/7/16.
 */

public class SlideFragment extends Fragment {

    private Context context;
    private View view;
    private RelativeLayout rl_container_slide;
    private ImageView iv_pict_slide;
    private CustomTextView tv_title_slide, tv_detail_slide;
    private Banner banner;

    public SlideFragment() {

    }

    @SuppressLint("ValidFragment")
    public SlideFragment(Context context, Banner banner) {
        this.context = context;
        this.banner = banner;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.view = inflater.inflate(R.layout.content_slide, container, false);

        return this.view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.rl_container_slide = (RelativeLayout) view.findViewById(R.id.rl_container_slide);
        this.iv_pict_slide = (ImageView) view.findViewById(R.id.iv_pict_slide);
        this.tv_title_slide = (CustomTextView) view.findViewById(R.id.tv_title_slide);
        this.tv_detail_slide = (CustomTextView) view.findViewById(R.id.tv_detail_slide);
        this.tv_detail_slide.setVisibility(View.GONE);

        this.tv_title_slide.setText(this.banner.getName());

        Picasso.with(this.context)
                .load((this.banner.getPicture().contains("http")) ? this.banner.getPicture() : Config.URL_IMAGE_BANNER + this.banner.getPicture())
                .placeholder(R.drawable.placeholder)
                .into(this.iv_pict_slide);

    }

}
