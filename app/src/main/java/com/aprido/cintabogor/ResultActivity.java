package com.aprido.cintabogor;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.aprido.cintabogor.adapter.ResultListAdapter;
import com.aprido.cintabogor.callback.ResultCallback;
import com.aprido.cintabogor.callback.ResultListAdapterCallback;
import com.aprido.cintabogor.logic.ResultLogic;

/**
 * Created by apridosandyasa on 10/14/16.
 */

public class ResultActivity extends AppCompatActivity implements ResultCallback, ResultListAdapterCallback {

    private Toolbar toolbar_result;
    private RecyclerView rv_result;
    private LinearLayoutManager rv_result_llm;
    private ResultListAdapter rv_result_adapter;
    private ResultLogic resultLogic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        handleIntent(getIntent());

        initView();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        handleIntent(intent);
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
            ResultActivity.this.finish();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        this.toolbar_result = (Toolbar) findViewById(R.id.toolbar_result);
        setSupportActionBar(this.toolbar_result);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        this.rv_result = (RecyclerView) findViewById(R.id.rv_result);
        this.rv_result_llm = new LinearLayoutManager(ResultActivity.this, LinearLayoutManager.VERTICAL, false);
        this.rv_result.setHasFixedSize(true);
        this.rv_result.setLayoutManager(this.rv_result_llm);

        this.resultLogic = new ResultLogic(ResultActivity.this, this);
        this.resultLogic.setupResultViews();
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //use the query to search
        }
    }

    @Override
    public void finishedSetupResultViews() {
        this.rv_result_adapter = new ResultListAdapter(ResultActivity.this, this);
        this.rv_result.setAdapter(this.rv_result_adapter);
    }

    @Override
    public void ShowDetailViews() {
        Intent detailIntent = new Intent(ResultActivity.this, DetailActivity.class);
        startActivity(detailIntent);
    }
}
