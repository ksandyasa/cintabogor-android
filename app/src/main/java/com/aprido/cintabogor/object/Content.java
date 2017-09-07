package com.aprido.cintabogor.object;

import java.io.Serializable;

/**
 * Created by apridosandyasa on 3/20/17.
 */

public class Content implements Serializable {

    private int id;
    private int idTipe;
    private String name;
    private String address_1;
    private String address_2;
    private String phone;
    private String email;
    private String website;
    private String latitude;
    private String longitude;
    private String picture_1;
    private String picture_2;
    private String picture_3;
    private String picture_4;
    private String description;

    public Content() {

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIdTipe(int idTipe) {
        this.idTipe = idTipe;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress_1(String address_1) {
        this.address_1 = address_1;
    }

    public void setAddress_2(String address_2) {
        this.address_2 = address_2;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public void setPicture_1(String picture_1) {
        this.picture_1 = picture_1;
    }

    public void setPicture_2(String picture_2) {
        this.picture_2 = picture_2;
    }

    public void setPicture_3(String picture_3) {
        this.picture_3 = picture_3;
    }

    public void setPicture_4(String picture_4) {
        this.picture_4 = picture_4;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public int getIdTipe() {
        return idTipe;
    }

    public String getName() {
        return name;
    }

    public String getAddress_1() {
        return address_1;
    }

    public String getAddress_2() {
        return address_2;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getWebsite() {
        return website;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getPicture_1() {
        return picture_1;
    }

    public String getPicture_2() {
        return picture_2;
    }

    public String getPicture_3() {
        return picture_3;
    }

    public String getPicture_4() {
        return picture_4;
    }

    public String getDescription() {
        return description;
    }

}
