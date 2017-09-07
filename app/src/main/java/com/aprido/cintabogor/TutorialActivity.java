package com.aprido.cintabogor;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.aprido.cintabogor.adapter.TutorialPagerAdapter;
import com.ugurtekbas.fadingindicatorlibrary.FadingIndicator;

/**
 * Created by apridosandyasa on 10/14/16.
 */

public class TutorialActivity extends AppCompatActivity {

    private ViewPager vp_tutorial;
    private TutorialPagerAdapter vp_tutorial_adapter;
    private FadingIndicator fi_tutorial;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tutorial);

        initView();
    }

    @Override
    public void onBackPressed() {

    }

    private void initView() {
        this.vp_tutorial = (ViewPager) findViewById(R.id.vp_tutorial);
        this.fi_tutorial = (FadingIndicator) findViewById(R.id.fi_tutorial);

        this.vp_tutorial_adapter = new TutorialPagerAdapter(getSupportFragmentManager(), TutorialActivity.this);
        this.vp_tutorial.setAdapter(this.vp_tutorial_adapter);
        this.vp_tutorial.setOffscreenPageLimit(this.vp_tutorial_adapter.getCount());
        this.fi_tutorial.setViewPager(this.vp_tutorial);
    }
}
