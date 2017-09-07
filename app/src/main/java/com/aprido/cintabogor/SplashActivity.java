package com.aprido.cintabogor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import com.aprido.cintabogor.utility.Config;
import com.aprido.cintabogor.utility.PreferenceUtility;

/**
 * Created by apridosandyasa on 10/14/16.
 */

public class SplashActivity extends AppCompatActivity {

    private ImageView iv_splash;
    private Animation anim;
    private BroadcastReceiver splashReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.hasExtra("referrer")) {
                //startAnimationSplash();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        setContentView(R.layout.activity_splash);

        this.iv_splash = (ImageView) findViewById(R.id.iv_splash);

        startAnimationSplash();
        registerReceiver(this.splashReceiver, new IntentFilter("com.aprido.cintabogor"));
    }

    @Override
    protected void onDestroy() {
        if (this.splashReceiver != null) {
            unregisterReceiver(this.splashReceiver);
        }
        super.onDestroy();
    }

    private void startAnimationSplash() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (PreferenceUtility.getInstance().loadDataString(SplashActivity.this, PreferenceUtility.USER_LATITUDE) == null ||
                        PreferenceUtility.getInstance().loadDataString(SplashActivity.this, PreferenceUtility.USER_LATITUDE).equals(""))
                    PreferenceUtility.getInstance().saveData(SplashActivity.this, PreferenceUtility.USER_LATITUDE, Config.DEFAULT_LATITUDE);
                if (PreferenceUtility.getInstance().loadDataString(SplashActivity.this, PreferenceUtility.USER_LONGITUDE) == null ||
                        PreferenceUtility.getInstance().loadDataString(SplashActivity.this, PreferenceUtility.USER_LONGITUDE).equals(""))
                    PreferenceUtility.getInstance().saveData(SplashActivity.this, PreferenceUtility.USER_LONGITUDE, Config.DEFAULT_LONGITUDE);

                Intent tutorialIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(tutorialIntent);
                SplashActivity.this.finish();
            }
        }, 2500);

//        Animation fadeIn = new AlphaAnimation(0, 1);
//        fadeIn.setInterpolator(new DecelerateInterpolator()); // add this
//        fadeIn.setDuration(1500);
//
//        Animation fadeOut = new AlphaAnimation(1, 0.5f);
//        fadeOut.setInterpolator(new AccelerateInterpolator()); // and this
//        fadeOut.setStartOffset(1000);
//        fadeOut.setDuration(1500);
//
//        AnimationSet animation = new AnimationSet(false); // change to false
//        animation.addAnimation(fadeIn);
//        animation.addAnimation(fadeOut);
//        animation.setRepeatCount(1);
//
//        this.iv_splash.startAnimation(animation);
//
//        animation.setAnimationListener(new SplashAnimation());
    }

    class SplashAnimation implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
            iv_splash.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationEnd(Animation animation) {
            iv_splash.setVisibility(View.INVISIBLE);
            Intent tutorialIntent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(tutorialIntent);
            SplashActivity.this.finish();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

}
