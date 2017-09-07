package com.aprido.cintabogor;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.aprido.cintabogor.callback.DetailCallback;
import com.aprido.cintabogor.callback.MapRouteFragmentCallback;
import com.aprido.cintabogor.fragment.MapRouteFragment;
import com.aprido.cintabogor.logic.DetailLogic;
import com.aprido.cintabogor.object.Content;
import com.aprido.cintabogor.utility.CintaBogorUtils;
import com.aprido.cintabogor.utility.Config;
import com.aprido.cintabogor.utility.PreferenceUtility;
import com.aprido.cintabogor.widget.CustomTextView;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.picasso.Picasso;

/**
 * Created by apridosandyasa on 10/7/16.
 */

public class DetailActivity extends AppCompatActivity implements DetailCallback {

    private Toolbar toolbar_detail;
    private LinearLayout bottomSheetDetail;
    private BottomSheetBehavior bottomSheetBehavior;
    private DetailLogic detailLogic;
    private ShareActionProvider mShareActionProvider;
    private MapRouteFragment mapRouteFragment;
    private LatLng placeLatLng, userLatLng;
    private Content content;
    private String from = "";
    private ImageView iv_pict_detail, iv_lokasi_detail;
    private CustomTextView tv_title_detail, tv_distance_detail,
                            tv_address_detail, tv_vulasan_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        this.from = getIntent().getStringExtra("from");

        content = (Content) getIntent().getSerializableExtra("content");

        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detail, menu);
        MenuItem item = menu.findItem(R.id.action_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);
//        mShareActionProvider.setShareHistoryFileName(
//                ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);
        mShareActionProvider.setOnShareTargetSelectedListener(new DetailShareTargetListener());

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Here's a message originally posted by");
        sendIntent.setType("text/plain");
        mShareActionProvider.setShareIntent(sendIntent);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {

            return true;
        }

        if (id == android.R.id.home) {
            DetailActivity.this.finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        this.toolbar_detail = (Toolbar) findViewById(R.id.toolbar_detail);
        setSupportActionBar(this.toolbar_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        this.iv_pict_detail = (ImageView) findViewById(R.id.iv_pict_detail);
        this.tv_title_detail = (CustomTextView) findViewById(R.id.tv_title_detail);
        this.iv_lokasi_detail = (ImageView) findViewById(R.id.iv_lokasi_detail);
        this.tv_distance_detail = (CustomTextView) findViewById(R.id.tv_distance_detail);
        this.tv_address_detail = (CustomTextView) findViewById(R.id.tv_address_detail);
        this.tv_vulasan_detail = (CustomTextView) findViewById(R.id.tv_vulasan_detail);
        this.bottomSheetDetail = (LinearLayout) findViewById(R.id.bottomSheetDetail);

        this.detailLogic = new DetailLogic(DetailActivity.this, this);
        this.detailLogic.setupDetailViews();
    }

    @Override
    public void finishedSetupBottomSheetViews() {
        this.userLatLng = new LatLng(Double.valueOf(PreferenceUtility.getInstance().loadDataString(DetailActivity.this, PreferenceUtility.USER_LATITUDE)),
                Double.valueOf(PreferenceUtility.getInstance().loadDataString(DetailActivity.this, PreferenceUtility.USER_LONGITUDE)));
        this.placeLatLng = new LatLng(Double.valueOf(this.content.getLatitude()),
                Double.valueOf(this.content.getLongitude()));

        this.tv_title_detail.setText(content.getName());
        Picasso.with(DetailActivity.this)
                .load((this.content.getPicture_1().contains("http")) ? this.content.getPicture_1() : Config.URL_IMAGE + this.content.getPicture_1())
                .placeholder(R.drawable.placeholder)
                .into(this.iv_pict_detail);

        this.tv_distance_detail.setText("" + CintaBogorUtils.getDistanceFormatInString(CintaBogorUtils.LocationUtility.distanceBetween(userLatLng, placeLatLng)) + " km");
        this.tv_address_detail.setText(content.getAddress_1());
        Log.d("TAG", "description " + content.getDescription());
        this.tv_vulasan_detail.setText((content.getDescription().equals("null")) ? "" : Html.fromHtml(content.getDescription()));

        this.bottomSheetBehavior = BottomSheetBehavior.from(this.bottomSheetDetail);
        this.bottomSheetBehavior.setPeekHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                24,
                getResources().getDisplayMetrics()));

        this.iv_lokasi_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapRouteFragment = new MapRouteFragment(DetailActivity.this, new MapRouteFragmentCallback() {
                    @Override
                    public void onDismissMapRouteFragmnet() {
                        mapRouteFragment.dismiss();
                        mapRouteFragment = null;
                    }
                }, placeLatLng, userLatLng, content.getName());
                mapRouteFragment.setCancelable(false);
                mapRouteFragment.show(getSupportFragmentManager(), "mapRouteFragment");
            }
        });
    }

    private class DetailShareTargetListener implements ShareActionProvider.OnShareTargetSelectedListener {

        @Override
        public boolean onShareTargetSelected(ShareActionProvider source, Intent intent) {
            Toast.makeText(DetailActivity.this, "" + intent.getComponent().toString(), Toast.LENGTH_LONG).show();

            return false;
        }
    }
}
