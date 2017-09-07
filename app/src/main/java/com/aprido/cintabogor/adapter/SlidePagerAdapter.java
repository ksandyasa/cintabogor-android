package com.aprido.cintabogor.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.aprido.cintabogor.fragment.SlideFragment;
import com.aprido.cintabogor.object.Banner;
import com.aprido.cintabogor.object.Content;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apridosandyasa on 10/7/16.
 */

public class SlidePagerAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private List<Fragment> fragmentList;
    private List<Banner> contentList = new ArrayList<>();

    public SlidePagerAdapter(FragmentManager fm, Context context, List<Banner> objects) {
        super(fm);
        this.context = context;
        this.contentList = objects;
        initializeFragmentListData();
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return this.fragmentList.size();
    }

    private void initializeFragmentListData() {
        this.fragmentList = new ArrayList<>();
        for (int i = 0; i < this.contentList.size(); i++) {
            this.fragmentList.add(new SlideFragment(this.context, this.contentList.get(i)));
        }
    }
}
