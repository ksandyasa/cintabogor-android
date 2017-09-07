package com.aprido.cintabogor;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.aprido.cintabogor.adapter.MainListAdapter;
import com.aprido.cintabogor.adapter.NavigationListAdapter;
import com.aprido.cintabogor.adapter.NearbyListAdapter;
import com.aprido.cintabogor.adapter.SlidePagerAdapter;
import com.aprido.cintabogor.callback.MainCallback;
import com.aprido.cintabogor.callback.MainListAdapterCallback;
import com.aprido.cintabogor.callback.NavigationListAdapterCallback;
import com.aprido.cintabogor.callback.NearbyListAdapterCallback;
import com.aprido.cintabogor.fragment.AboutUsFragment;
import com.aprido.cintabogor.fragment.ContactUsFragment;
import com.aprido.cintabogor.logic.MainLogic;
import com.aprido.cintabogor.object.Banner;
import com.aprido.cintabogor.object.Content;
import com.aprido.cintabogor.object.MainContent;
import com.aprido.cintabogor.object.SlideMenu;
import com.aprido.cintabogor.service.FusedLocationService;
import com.aprido.cintabogor.utility.CintaBogorUtils;
import com.aprido.cintabogor.utility.Config;
import com.aprido.cintabogor.utility.PermissionCheck;
import com.aprido.cintabogor.utility.PreferenceUtility;
import com.aprido.cintabogor.widget.BackdropBottomSheetBehavior;
import com.aprido.cintabogor.widget.BottomSheetBehaviorGoogleMapsLike;
import com.aprido.cintabogor.widget.DepthPageTransformer;
import com.aprido.cintabogor.widget.FixedSpeedScroller;
import com.aprido.cintabogor.widget.MergedAppBarLayoutBehavior;
import com.aprido.cintabogor.widget.RecycleDividerItemDecoration;
import com.google.android.gms.maps.model.LatLng;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends BaseActivity implements MainCallback, NavigationListAdapterCallback,
        NearbyListAdapterCallback, MainListAdapterCallback {

    private static final String TAG = MainActivity.class.getSimpleName();
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private CoordinatorLayout coordinatorLayout;
    private NestedScrollView bottomSheet;
    private ViewPager vp_main;
    private SlidePagerAdapter vp_main_adapter;
    private RecyclerView rv_nearby_main, rv_content_main, rv_content_navigationview;
    private LinearLayoutManager rv_nearby_main_llm, rv_content_navigationview_llm;
    private GridLayoutManager rv_content_main_glm;
    private NearbyListAdapter rv_nearby_main_adapter;
    private NavigationListAdapter rv_content_navigationview_adapter;
    private MainListAdapter rv_content_main_adapter;
    private BottomSheetBehaviorGoogleMapsLike bottomSheetBehaviorGoogleMapsLike;
    private BackdropBottomSheetBehavior backdropBottomSheetBehavior;
    private LinearLayout backBottomSheet;
    private AppBarLayout mergedAppBarLayout;
    private Toolbar expanded_toolbar;
    private MergedAppBarLayoutBehavior mergedAppBarLayoutBehavior;
    private LinearLayout bottomBackground;
    private BottomSheetBehavior bottomSheetBehavior;
    private boolean isSearchOn = false;
    private String searchQuery = "";
    private SearchView searchView;
    private SearchManager searchManager;
    private MenuItem searchMenuItem;
    private AlertDialog alertDialog;
    private AlertDialog.Builder alertDialogBuilder;
    private MainLogic mainLogic;
    private int currentSlide = 0;
    private Menu mainMenu;
    private LatLng userLatLng;
    private int isRunning = 0;
    private List<SlideMenu> slideMenuList = new ArrayList<>();
    private List<MainContent> mainContentList = new ArrayList<>();
    private List<Content> nearbyContentList = new ArrayList<>();

    private Handler messageHandler = new MessengerHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intentStartService = new Intent(MainActivity.this, FusedLocationService.class);
                intentStartService.putExtra("MESSENGER", new Messenger(messageHandler));
                startService(intentStartService);
            }
        }, 1000);

    }

    @Override
    public void onBackPressed() {
        ShowCloseDialog();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.mainMenu = menu;
        this.searchMenuItem = menu.findItem(R.id.action_search);
        this.searchView = (SearchView) this.searchMenuItem.getActionView();
        this.searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        this.searchView.setSearchableInfo(this.searchManager.getSearchableInfo(MainActivity.this.getComponentName()));
        this.searchView.setOnQueryTextListener(new SearchQueryListener());
        this.searchView.setOnSearchClickListener(new SearchViewExpandListener());
        this.searchView.setOnCloseListener(new SearchViewCollapseListener());

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PermissionCheck.REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                Map<String, Integer> perms = new HashMap<String, Integer>();

                perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);

                for (int i = 0; i < permissions.length; i++)
                    perms.put(permissions[i], grantResults[i]);
                // Check for ACCESS_FINE_LOCATION
                if (perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // All Permissions Granted
                } else {
                    // Permission Denied
                    Toast.makeText(MainActivity.this, "Aplikasi menolak perizinan yang ingin dilakukan", Toast.LENGTH_SHORT)
                            .show();
                }
            }
            break;

            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Log.d("TAG", "result ok " + resultCode);
            Log.d("TAG", "request code " + requestCode);
            switch (requestCode) {
                case 0:
                    setupLocationServices();

                    break;
            }
        }
    }

    private void initView() {
        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        this.coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        this.rv_nearby_main = (RecyclerView) findViewById(R.id.rv_nearby_main);
        this.backBottomSheet = (LinearLayout) coordinatorLayout.findViewById(R.id.backBottomSheet);
        this.bottomSheet = (NestedScrollView) coordinatorLayout.findViewById(R.id.bottomSheet);
        this.rv_content_main = (RecyclerView) findViewById(R.id.rv_content_main);
        this.vp_main = (ViewPager) findViewById(R.id.vp_main);
        this.bottomBackground = (LinearLayout) findViewById(R.id.bottomBackground);
        this.mergedAppBarLayout = (AppBarLayout) findViewById(R.id.merged_appbarlayout);
        this.expanded_toolbar = (Toolbar) findViewById(R.id.expanded_toolbar);
        this.navigationView = (NavigationView) findViewById(R.id.navigationView);
        this.rv_content_navigationview = (RecyclerView) findViewById(R.id.rv_content_navigationview);

        this.mainLogic = new MainLogic(MainActivity.this, this);

        showLoadingDialog();
        mainLogic.setupMainViews();

        PermissionCheck permissionCheck = new PermissionCheck();
        permissionCheck.checkPermission(MainActivity.this);

        setupLocationServices();

    }

    private void setupLocationServices() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        Log.d("TAG", "" + locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER));
        Log.d("TAG", "" + locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER));

        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || !locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            ShowSettingsAlert();
        }else{
            Intent intentStartService = new Intent(MainActivity.this, FusedLocationService.class);
            intentStartService.putExtra("MESSENGER", new Messenger(messageHandler));
            startService(intentStartService);
        }
    }

    public void ShowSettingsAlert() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);

        // Setting Dialog Title
        alertDialog.setTitle("Peringatan");

        // Setting Dialog Message
        alertDialog.setMessage("GPS belum diaktifkan\nIngin mengaktifkan GPS?");

        // On Pressing Setting button
        alertDialog.setPositiveButton("Setelan",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivityForResult(intent, 0);
                        dialog.cancel();
                    }
                });

        // On pressing cancel button
        alertDialog.setNegativeButton("Tutup",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });

        alertDialog.show();
    }

    private void ShowCloseDialog() {
        this.alertDialogBuilder = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setMessage("Keluar dari Cinta Bogor?")
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        //drawerLayout.closeDrawers();
                        Intent intentStartService = new Intent(MainActivity.this, FusedLocationService.class);
                        stopService(intentStartService);
                        MainActivity.this.finish();
                    }
                })
                .setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                        dialog.dismiss();
                    }
                })
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK &&
                                event.getAction() == KeyEvent.ACTION_UP &&
                                !event.isCanceled()) {
                            dialog.dismiss();
                            return true;
                        }
                        return false;
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);
        this.alertDialog = this.alertDialogBuilder.create();
        this.alertDialog.show();
    }

    @Override
    public void finishedSetupToolbar() {
        final Drawable upArrow = getResources().getDrawable(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        upArrow.setColorFilter(getResources().getColor(R.color.colorWhite), PorterDuff.Mode.SRC_ATOP);

        this.expanded_toolbar.setNavigationIcon(upArrow);
        setSupportActionBar(this.expanded_toolbar);
        this.mergedAppBarLayoutBehavior = MergedAppBarLayoutBehavior.from(mergedAppBarLayout);
        this.mergedAppBarLayoutBehavior.setToolbarTitle(getResources().getString(R.string.app_name));
        this.mergedAppBarLayoutBehavior.setNavigationOnClickListener(new ActionSlideMenu());
    }

    @Override
    public void finishedSetupBottomSheet() {
        this.bottomSheetBehaviorGoogleMapsLike = BottomSheetBehaviorGoogleMapsLike.from(this.bottomSheet);
        this.bottomSheetBehaviorGoogleMapsLike.addBottomSheetCallback(new BottomSheetBehaviorGoogleMapsLikeCallback());

        this.backdropBottomSheetBehavior = BackdropBottomSheetBehavior.from(this.backBottomSheet);
        this.backdropBottomSheetBehavior.setPeekHeight((int) getResources().getDimension(R.dimen.bottom_sheet_peek_height));

        this.bottomSheetBehavior = BottomSheetBehavior.from(this.bottomBackground);
        this.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        this.bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBackgroundCallback());
    }

    @Override
    public void finishedSetupNearbyListViews(List<Content> nearbyList) {
        Log.d("TAG Main", "size nearby list " + nearbyList.size());
        for (int i = 0; i < nearbyList.size(); i++) {
            LatLng placeLatLng = new LatLng(Double.parseDouble(nearbyList.get(i).getLatitude()), Double.parseDouble(nearbyList.get(i).getLongitude()));
            LatLng userLatLng = new LatLng(Double.parseDouble(PreferenceUtility.getInstance().loadDataString(MainActivity.this, PreferenceUtility.USER_LATITUDE)), Double.parseDouble(PreferenceUtility.getInstance().loadDataString(MainActivity.this, PreferenceUtility.USER_LONGITUDE)));
            int distance = (int) (CintaBogorUtils.LocationUtility.distanceBetween(placeLatLng, userLatLng) / 1000);
            Log.d("TAG", "distance nearby " + distance);
            if (distance < 500) {
                this.nearbyContentList.add(nearbyList.get(i));
            }
        }

        this.rv_nearby_main_llm = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        this.rv_nearby_main.setHasFixedSize(true);
        this.rv_nearby_main.setLayoutManager(this.rv_nearby_main_llm);
        this.rv_nearby_main.addItemDecoration(new RecycleDividerItemDecoration(MainActivity.this));

        this.rv_nearby_main_adapter = new NearbyListAdapter(MainActivity.this, this, this.nearbyContentList);
        this.rv_nearby_main.setAdapter(this.rv_nearby_main_adapter);
    }

    @Override
    public void finishedSetupSlideViews(List<Banner> contentList) {
        Log.d("TAG Main", "size slide list " + contentList.size());
        try {
            this.vp_main_adapter = new SlidePagerAdapter(getSupportFragmentManager(), MainActivity.this, contentList);
            this.vp_main.setAdapter(this.vp_main_adapter);
            this.vp_main.setPageTransformer(true, new DepthPageTransformer());
            this.vp_main.setOffscreenPageLimit(this.vp_main_adapter.getCount());

            Field mScroller;
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(this.vp_main.getContext(), new AccelerateDecelerateInterpolator());
            // scroller.setFixedDuration(5000);
            mScroller.set(this.vp_main, scroller);

            final Handler handler = new Handler();
            final Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    if (currentSlide == vp_main_adapter.getCount() - 1) {
                        currentSlide = 0;
                    }
                    vp_main.setCurrentItem(currentSlide++, true);
                }
            };
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    handler.post(runnable);
                }
            }, 1000, 4000);

            mainLogic.setupNearbyViews(mainContentList.get(1).getId());
        } catch (NoSuchFieldException e) {
        } catch (IllegalArgumentException e) {
        } catch (IllegalAccessException e) {
        } catch (Exception e) {

        }
    }

    public void doSetupContentViews(List<MainContent> mainContentList) {
        this.mainContentList = mainContentList;

        this.rv_content_main_glm = new GridLayoutManager(MainActivity.this, 3);
        this.rv_content_main.setHasFixedSize(true);
        this.rv_content_main.setLayoutManager(this.rv_content_main_glm);

        this.rv_content_main_adapter = new MainListAdapter(MainActivity.this, this.mainContentList, this);
        this.rv_content_main.setAdapter(this.rv_content_main_adapter);
        this.rv_content_main_adapter.setItemClickable(true);
    }

    @Override
    public void finishedSetupNavigationViews(List<SlideMenu> slideMenuList, final List<MainContent> mainContentList) {
        Log.d("TAG Main", "size slideMenu list " + slideMenuList.size());
        this.slideMenuList = slideMenuList;
        doSetupContentViews(mainContentList);

        this.rv_content_navigationview_llm = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        this.rv_content_navigationview.setHasFixedSize(true);
        this.rv_content_navigationview.setLayoutManager(this.rv_content_navigationview_llm);
        this.rv_content_navigationview.addItemDecoration(new RecycleDividerItemDecoration(MainActivity.this));

        this.rv_content_navigationview_adapter = new NavigationListAdapter(MainActivity.this, this.slideMenuList, this);
        this.rv_content_navigationview.setAdapter(this.rv_content_navigationview_adapter);

        this.actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open_drawer, R.string.close_drawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }

        };

        this.drawerLayout.setDrawerListener(this.actionBarDrawerToggle);
        this.actionBarDrawerToggle.syncState();
        dismissLoadingDialog();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mainLogic.setupSlideViews(mainContentList.get(1).getId());
                bottomSheetBehaviorGoogleMapsLike.setState(BottomSheetBehaviorGoogleMapsLike.STATE_ANCHOR_POINT);
            }
        }, 500);
    }

    @Override
    public void failedSetupContentViews() {

    }

    @Override
    public void selectedNavigationItem(int position) {
        if (position == 3) {
            AboutUsFragment aboutUsFragment = new AboutUsFragment(MainActivity.this);
            aboutUsFragment.show(getSupportFragmentManager(), "aboutUsFragment");
        }else if (position == 4) {
            ContactUsFragment contactUsFragment = new ContactUsFragment(MainActivity.this);
            contactUsFragment.show(getSupportFragmentManager(), "contactUsFragment");
        }
        drawerLayout.closeDrawers();
    }

    @Override
    public void selectedNearbyItem(int position) {
        Intent detailIntent = new Intent(MainActivity.this, DetailActivity.class);
        detailIntent.putExtra("from", MainActivity.class.getSimpleName());
        detailIntent.putExtra("content", this.nearbyContentList.get(position));
        startActivity(detailIntent);
    }

    @Override
    public void selectedMainContentItem(final int position) {
        bottomSheetBehaviorGoogleMapsLike.setState(BottomSheetBehaviorGoogleMapsLike.STATE_EXPANDED);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent contentListIntent = new Intent(MainActivity.this, ContentListActivity.class);
                contentListIntent.putExtra("position", position);
                contentListIntent.putExtra("id", mainContentList.get(position).getId());
                contentListIntent.putExtra("title", mainContentList.get(position).getTitle());
                startActivity(contentListIntent);
            }
        }, 500);

    }

    private void setupTransparantBg(boolean isSearchOn) {
        if (isSearchOn == true) {
            this.bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }else{
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(this.searchView.getWindowToken(), 0);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }, 500);
        }

    }

    private class BottomSheetBehaviorGoogleMapsLikeCallback extends BottomSheetBehaviorGoogleMapsLike.BottomSheetCallback {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, @BottomSheetBehaviorGoogleMapsLike.State int newState) {
            switch (newState) {
                case BottomSheetBehaviorGoogleMapsLike.STATE_COLLAPSED:
                    ((RelativeLayout) findViewById(R.id.rl_header_main)).setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
                    Log.d("bottomsheet-", "STATE_COLLAPSED");
                    break;
                case BottomSheetBehaviorGoogleMapsLike.STATE_DRAGGING:
                    isSearchOn = false;
                    setupTransparantBg(isSearchOn);
                    searchView.onActionViewCollapsed();
                    ((RelativeLayout) findViewById(R.id.rl_header_main)).setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    Log.d("bottomsheet-", "STATE_DRAGGING");
                    break;
                case BottomSheetBehaviorGoogleMapsLike.STATE_EXPANDED:
                    mainMenu.findItem(R.id.action_search).setVisible(true);
                    Log.d("bottomsheet-", "STATE_EXPANDED");
                    break;
                case BottomSheetBehaviorGoogleMapsLike.STATE_ANCHOR_POINT:
                    mainMenu.findItem(R.id.action_search).setVisible(false);
                    Log.d("bottomsheet-", "STATE_ANCHOR_POINT");
                    break;
                case BottomSheetBehaviorGoogleMapsLike.STATE_HIDDEN:
                    Log.d("bottomsheet-", "STATE_HIDDEN");
                    break;
                default:
                    Log.d("bottomsheet-", "STATE_SETTLING");
                    break;
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    }

    private class BottomSheetBackgroundCallback extends BottomSheetBehavior.BottomSheetCallback {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
//            if (newState == BottomSheetBehavior.STATE_DRAGGING)
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    }

    private class ActionSlideMenu implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            drawerLayout.openDrawer(navigationView);
        }
    }

    private class SearchQueryListener implements SearchView.OnQueryTextListener {

        @Override
        public boolean onQueryTextSubmit(String query) {
            Log.d(TAG, "searchQuery2 " + query);
            //doSearchPersonalInfo(query);
            isSearchOn = false;
            searchView.onActionViewCollapsed();
            setupTransparantBg(isSearchOn);

            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            Log.d(TAG, "searchQuery2 " + newText);
            if (TextUtils.isEmpty(newText)) {
                searchQuery = "";
            }

            return false;
        }
    }

    private class SearchViewExpandListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Log.d(TAG, "searchView is expanded");
            isSearchOn = true;
            setupTransparantBg(isSearchOn);
        }
    }

    private class SearchViewCollapseListener implements SearchView.OnCloseListener {

        @Override
        public boolean onClose() {
            Log.d(TAG, "searchView is collapsed");
            isSearchOn = false;
            setupTransparantBg(isSearchOn);

            return false;
        }
    }

    private class MessengerHandler extends Handler {
        private Bundle bundleData;

        @Override
        public void handleMessage(Message message) {
            int state = message.arg1;
            switch (state) {
                case Config.LOCATION_LATLNG:
                    bundleData = message.getData();
                    Log.i(MainActivity.class.getSimpleName(),
                            "Latitude " + bundleData.get("LATITUDE") + ", Longitude " + bundleData.get("LONGITUDE"));
                    userLatLng = new LatLng((double) bundleData.get("LATITUDE"), (double) bundleData.get("LONGITUDE"));
                    PreferenceUtility.getInstance().saveData(MainActivity.this, PreferenceUtility.USER_LATITUDE, "" + bundleData.get("LATITUDE"));
                    PreferenceUtility.getInstance().saveData(MainActivity.this, PreferenceUtility.USER_LONGITUDE, "" + bundleData.get("LONGITUDE"));

                    break;
            }
        }
    }

}
