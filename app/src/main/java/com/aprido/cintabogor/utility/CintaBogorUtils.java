package com.aprido.cintabogor.utility;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.aprido.cintabogor.R;
import com.aprido.cintabogor.object.Banner;
import com.aprido.cintabogor.object.CategoryContent;
import com.aprido.cintabogor.object.Content;
import com.aprido.cintabogor.object.MainContent;
import com.aprido.cintabogor.object.SlideMenu;
import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.PolyUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by apridosandyasa on 3/20/17.
 */

public class CintaBogorUtils {

    public static String JSON_DATA = "";

    public static class ConnectionUtility {

        public static boolean isNetworkConnected(Context context) {
            ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                return true;
            }
            return false;
        }

    }

    public static class LocationUtility {

        public static float distanceBetween(LatLng latLng1, LatLng latLng2) {

            Location loc1 = new Location(LocationManager.GPS_PROVIDER);
            Location loc2 = new Location(LocationManager.GPS_PROVIDER);

            loc1.setLatitude(latLng1.latitude);
            loc1.setLongitude(latLng1.longitude);

            loc2.setLatitude(latLng2.latitude);
            loc2.setLongitude(latLng2.longitude);

            return loc1.distanceTo(loc2);
        }

    }

    public static class ParseJSONUtility {

        public static List<MainContent> getMainContentListItemsFromJSON(String json) throws JSONException {
            List<MainContent> mainContentList = new ArrayList<>();

            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                if (jsonArray.getJSONObject(i).getString("name").equals("kuliner"))
                    mainContentList.add(new MainContent(R.drawable.kuliner, "Kuliner", 1, 1));
                else if (jsonArray.getJSONObject(i).getString("name").equals("wisata"))
                    mainContentList.add(new MainContent(R.drawable.wisata, "Wisata", 2, 1));
                else if (jsonArray.getJSONObject(i).getString("name").equals("hotel"))
                    mainContentList.add(new MainContent(R.drawable.hotel, "Hotel", 3, 1));
                else if (jsonArray.getJSONObject(i).getString("name").equals("kegiatan"))
                    mainContentList.add(new MainContent(R.drawable.kegiatan, "Kegiatan", 4, 1));
                else if (jsonArray.getJSONObject(i).getString("name").equals("belanja"))
                    mainContentList.add(new MainContent(R.drawable.belanja, "Belanja", 5, 1));
                else if (jsonArray.getJSONObject(i).getString("name").equals("umum"))
                    mainContentList.add(new MainContent(R.drawable.umum, "Umum", 6, 1));
                else if (jsonArray.getJSONObject(i).getString("name").equals("budaya"))
                    mainContentList.add(new MainContent(R.drawable.budaya, "Budaya", 7, 1));
                else if (jsonArray.getJSONObject(i).getString("name").equals("properti"))
                    mainContentList.add(new MainContent(R.drawable.properti, "Properti", 8, 1));
            }

            return mainContentList;
        }

        public static List<CategoryContent> getCategoryContentListItemsFromJSON(String json) throws JSONException {
            List<CategoryContent> mainContentList = new ArrayList<>();

            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    mainContentList.add(new CategoryContent(R.drawable.cintabogor,
                            jsonArray.getJSONObject(i).getString("name"),
                            Integer.valueOf(jsonArray.getJSONObject(i).getInt("id")),
                            Integer.valueOf(jsonArray.getJSONObject(i).getInt("status"))));
                }
            }

            return mainContentList;
        }

        public static List<Content> getListContentItemsFromJSON(String json) throws JSONException {
            List<Content> contentListList = new ArrayList<>();

            JSONArray jsonArray = new JSONArray(json);
            for (int i = 0; i < jsonArray.length(); i++) {
                Content content = new Content();
                content.setId(jsonArray.getJSONObject(i).getInt("id"));
                content.setIdTipe(jsonArray.getJSONObject(i).getInt("idTipe"));
                content.setName(jsonArray.getJSONObject(i).getString("name"));
                content.setAddress_1(jsonArray.getJSONObject(i).getString("address_1"));
                content.setAddress_2(jsonArray.getJSONObject(i).getString("address_2"));
                content.setPhone(jsonArray.getJSONObject(i).getString("phone"));
                content.setEmail(jsonArray.getJSONObject(i).getString("email"));
                content.setWebsite(jsonArray.getJSONObject(i).getString("website"));
                content.setLatitude(jsonArray.getJSONObject(i).getString("latitute"));
                content.setLongitude(jsonArray.getJSONObject(i).getString("longitute"));
                content.setPicture_1(jsonArray.getJSONObject(i).getString("picture_1"));
                content.setPicture_2(jsonArray.getJSONObject(i).getString("picture_2"));
                content.setPicture_3(jsonArray.getJSONObject(i).getString("picture_3"));
                content.setPicture_4(jsonArray.getJSONObject(i).getString("picture_4"));
                content.setDescription(jsonArray.getJSONObject(i).getString("description"));
                contentListList.add(content);
            }

            return contentListList;
        }

        public static List<Banner> getListBannerItemsFromJSON(String json) throws JSONException {
            List<Banner> bannerList = new ArrayList<>();

            JSONArray jsonArray = new JSONArray(json);
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    Banner banner = new Banner();
                    banner.setName(jsonArray.getJSONObject(i).getString("name"));
                    banner.setPicture(jsonArray.getJSONObject(i).getString("picture"));
                    banner.setLink(jsonArray.getJSONObject(i).getString("link"));
                    bannerList.add(banner);
                }
            }

            return bannerList;
        }

        public static List<SlideMenu> getListNavigationItemsFromJSON(String json) throws JSONException {
            List<SlideMenu> slideMenuList = new ArrayList<>();

            slideMenuList.add(new SlideMenu("Favoritku"));
            slideMenuList.add(new SlideMenu("Jurnal Perjalanan"));
            slideMenuList.add(new SlideMenu("Pengaturan"));
            slideMenuList.add(new SlideMenu("Tentang Kami"));
            slideMenuList.add(new SlideMenu("Hubungi Kami"));

            return slideMenuList;
        }

        public static List<LatLng> getRouteListFromJSON(String json) throws JSONException {
            List<LatLng> latLngList = new ArrayList<>();

            JSONObject jsonObject = new JSONObject(json);
            String overviewPolyline = jsonObject.getJSONArray("routes").getJSONObject(0).getJSONObject("overview_polyline").getString("points");
            latLngList = PolyUtil.decode(overviewPolyline);

            return latLngList;
        }

    }

    public static class PermissionUtility {

        public static final int  REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
        public static final int REQUEST_CODE_ASK_PERMISSION = 123;

        private static int currentApiVersion = Build.VERSION.SDK_INT;

        public static boolean checkPermission(Context context) {
            if (currentApiVersion >= Build.VERSION_CODES.M) {

                List<String> permissionsNeeded = new ArrayList<String>();

                final List<String> permissionsList = new ArrayList<String>();
                if (!addPermission(permissionsList, Manifest.permission.ACCESS_COARSE_LOCATION, context))
                    permissionsNeeded.add("Location");
                if (!addPermission(permissionsList, Manifest.permission.ACCESS_FINE_LOCATION, context))
                    permissionsNeeded.add("Location");

                if (permissionsList.size() > 0) {
                    if (permissionsNeeded.size() > 0) {
                        ActivityCompat.requestPermissions((Activity)context
                                ,permissionsList.toArray(new String[permissionsList.size()]),
                                REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                    }
                }
                return true;
            } else {
                return true;
            }
        }

        @TargetApi(Build.VERSION_CODES.M)
        private static boolean addPermission(List<String> permissionList, String permission, Context context) {

            if (ContextCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);

                if (!ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, permission))
                    ;
                return false;
            }

            return true;

        }

    }

    public static String getDistanceFormatInString(float distance) {
        String formatString = "#,###,###,###.##";
        DecimalFormatSymbols otherSymbols;

        otherSymbols = new DecimalFormatSymbols(Locale.GERMAN);
        otherSymbols.setDecimalSeparator(',');
        otherSymbols.setGroupingSeparator('.');

        return new DecimalFormat(formatString, otherSymbols).format(distance);
    }

}
