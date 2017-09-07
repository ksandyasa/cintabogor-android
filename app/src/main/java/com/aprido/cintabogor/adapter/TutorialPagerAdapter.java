package com.aprido.cintabogor.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.aprido.cintabogor.fragment.ContentItemTutorial;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apridosandyasa on 10/14/16.
 */

public class TutorialPagerAdapter extends FragmentStatePagerAdapter {

    private Context context;
    private List<Fragment> fragmentList;

    public TutorialPagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
        initializeFragmentList();
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        this.fragmentList.get(position).setArguments(bundle);
        return this.fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return this.fragmentList.size();
    }

    private void initializeFragmentList() {
        this.fragmentList = new ArrayList<>();
        this.fragmentList.add(new ContentItemTutorial(this.context));
        this.fragmentList.add(new ContentItemTutorial(this.context));
        this.fragmentList.add(new ContentItemTutorial(this.context));
        this.fragmentList.add(new ContentItemTutorial(this.context));
    }
}
