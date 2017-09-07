package com.aprido.cintabogor.utility;

/**
 * Created by apridosandyasa on 3/20/17.
 */

public interface Config {

    String BASE_URL = "http://cintabogor.com/ajax";
    String API_GET_SLIDE_MENU = BASE_URL + "/data.php?type=menu";
    String API_GET_BANNER = BASE_URL + "/data.php?type=banner";
    String API_GET_CONTENT_MAIN_MENU = BASE_URL + "/data.php?type=menu";
    String API_GET_CONTENT_NEARBY = BASE_URL + "/data.php?type=near";
    String API_GET_CATEGORY_CONTENT_LIST = BASE_URL + "/data.php";
    String API_GET_CONTENT_LIST = BASE_URL + "/wisata.php?type=place";
    String URL_IMAGE = "http://www.cintabogor.com/images/data/";
    String URL_IMAGE_BANNER = "http://www.cintabogor.com/images/banner/";
    String API_MAP_ROUTE = "https://maps.googleapis.com/maps/api/directions/json";
    int LOCATION_LATLNG = 101;
    int SETTING_GPS = 102;
    String DEFAULT_LATITUDE = "-6.227029";
    String DEFAULT_LONGITUDE = "106.852706";

}
