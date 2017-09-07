package com.aprido.cintabogor.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.aprido.cintabogor.callback.ContentListFragmentCallback;
import com.aprido.cintabogor.callback.ContentPagerAdapterCallback;
import com.aprido.cintabogor.fragment.ContentList;
import com.aprido.cintabogor.logic.ContentListLogic;
import com.aprido.cintabogor.object.CategoryContent;
import com.aprido.cintabogor.object.Content;
import com.aprido.cintabogor.object.MainContent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apridosandyasa on 10/7/16.
 */

public class ContentPagerAdapter extends FragmentStatePagerAdapter implements ContentListFragmentCallback {

    private Context context;
    private List<Fragment> fragmentList;
    private ContentPagerAdapterCallback callback;
    private List<CategoryContent> mainContentList = new ArrayList<>();
    private int mCategoryId;

    public ContentPagerAdapter(FragmentManager fm, Context context, ContentPagerAdapterCallback listener, List<CategoryContent> objects, int categoryId) {
        super(fm);
        this.context = context;
        this.callback = listener;
        this.mainContentList = objects;
        this.mCategoryId = categoryId;
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

    @Override
    public CharSequence getPageTitle(int position) {
        return this.mainContentList.get(position).getTitle();
    }

    private void initializeFragmentList() {
        this.fragmentList = new ArrayList<>();
        for (int i=0; i < this.mainContentList.size(); i++) {
            this.fragmentList.add(new ContentList(this.context, this, this.mCategoryId, this.mainContentList.get(i).getId()));
        }
    }

    @Override
    public void selectedContentListItem(Content content) {
        callback.backToMainView(content);
    }
}
