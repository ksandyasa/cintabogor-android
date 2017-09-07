package com.aprido.cintabogor;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.aprido.cintabogor.adapter.ContentPagerAdapter;
import com.aprido.cintabogor.callback.CategoryTabCallback;
import com.aprido.cintabogor.callback.ContentPagerAdapterCallback;
import com.aprido.cintabogor.logic.CategoryLogic;
import com.aprido.cintabogor.object.CategoryContent;
import com.aprido.cintabogor.object.Content;
import com.aprido.cintabogor.object.MainContent;
import com.aprido.cintabogor.utility.CintaBogorUtils;
import com.aprido.cintabogor.utility.PreferenceUtility;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apridosandyasa on 10/7/16.
 */

public class ContentListActivity extends BaseActivity implements ContentPagerAdapterCallback, CategoryTabCallback {

    private static final String TAG = ContentListActivity.class.getSimpleName();
    private Toolbar toolbar_list;
    private TabLayout tabs_list;
    private ViewPager vp_list;
    private ContentPagerAdapter vp_list_adapter;
    private int mPosition = 0;
    private int categoryId = 0;
    private String categoryTitle = "";
    private List<CategoryContent> mainContentList = new ArrayList<>();
    private List<List<Content>> listContentList = new ArrayList<List<Content>>();
    private int countPosition = 0;
    private boolean isLoading = false;
    private CategoryLogic categoryLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        this.mPosition = getIntent().getIntExtra("position", 0);
        this.categoryId = getIntent().getIntExtra("id", 0);
        this.categoryTitle = getIntent().getStringExtra("title");

        initView();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "requestCode " + requestCode);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            ContentListActivity.this.finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        this.toolbar_list = (Toolbar) findViewById(R.id.toolbar_list);
        setSupportActionBar(this.toolbar_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Kategori " + this.categoryTitle);
        getSupportActionBar().setSubtitle(getResources().getString(R.string.app_name));

        this.tabs_list = (TabLayout) findViewById(R.id.tabs_list);
        this.vp_list = (ViewPager) findViewById(R.id.vp_list);

        showLoadingDialog();
        this.categoryLogic = new CategoryLogic(ContentListActivity.this, this);
        this.categoryLogic.setupCategoryTabViews(this.categoryId);
    }

    @Override
    public void backToMainView(Content content) {
        Intent detailIntent = new Intent(ContentListActivity.this, DetailActivity.class);
        detailIntent.putExtra("from", ContentListActivity.class.getSimpleName());
        detailIntent.putExtra("content", content);
        startActivity(detailIntent);
    }

    @Override
    public void failedSetupCategoryTabViews() {

    }

    @Override
    public void finishedSetupCategoryTabViews(List<CategoryContent> categoryContentList) {
        this.mainContentList = categoryContentList;

        this.vp_list_adapter = new ContentPagerAdapter(getSupportFragmentManager(), ContentListActivity.this, this, this.mainContentList, this.categoryId);
        this.vp_list.setAdapter(this.vp_list_adapter);
        this.vp_list.setOffscreenPageLimit(this.mainContentList.size());
        this.tabs_list.setupWithViewPager(this.vp_list);
        dismissLoadingDialog();

        //this.categoryLogic.setupContentCategoryTabViews(this.categoryId, this.mainContentList.get(countPosition).getId());
    }

    @Override
    public void finishedSetupContentCategoryTabViews(List<Content> contentList, int categoryId) {
        countPosition++;
        this.listContentList.add(contentList);
//        if (countPosition < this.mainContentList.size()) {
//            this.categoryLogic.setupContentCategoryTabViews(this.categoryId, this.mainContentList.get(countPosition).getId());
//        }else{
//        }
    }
}
