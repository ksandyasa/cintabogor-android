package com.aprido.cintabogor.object;

/**
 * Created by apridosandyasa on 6/29/17.
 */

public class Banner {

    private String name;
    private String picture;
    private String link;

    public Banner() {

    }

    public Banner(String name, String picture, String link) {
        this.name = name;
        this.picture = picture;
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
