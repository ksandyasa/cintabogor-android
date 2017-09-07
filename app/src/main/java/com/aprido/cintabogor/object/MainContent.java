package com.aprido.cintabogor.object;

import com.aprido.cintabogor.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apridosandyasa on 10/7/16.
 */

public class MainContent {

    private int icon;
    private int id;
    private String title;
    private int status;

    public MainContent() {
        initializeMainContentList();
    }

    public MainContent(int icon, String title, int id, int status) {
        this.icon = icon;
        this.title = title;
        this.id = id;
        this.status = status;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(int status) {
        this.status = status;
    }


    public int getIcon() {
        return this.icon;
    }

    public String getTitle() {
        return this.title;
    }

    public int getId() {
        return this.id;
    }

    public int getStatus() {
        return this.status;
    }

    private void initializeMainContentList() {
//        this.mainContentList = new ArrayList<>();
//        this.mainContentList.add(new MainContent(R.drawable.ic_kuliner, "Kuliner"));
//        this.mainContentList.add(new MainContent(R.drawable.ic_wisata, "Wisata"));
//        this.mainContentList.add(new MainContent(R.drawable.ic_hotel, "Hotel"));
//        this.mainContentList.add(new MainContent(R.drawable.ic_budaya, "Seni Budaya"));
//        this.mainContentList.add(new MainContent(R.drawable.ic_kerajinan, "Kerajinan"));
//        this.mainContentList.add(new MainContent(R.drawable.ic_belanja, "Belanja"));
//        this.mainContentList.add(new MainContent(R.drawable.ic_service, "Fasilitas Umum"));
//        this.mainContentList.add(new MainContent(R.drawable.ic_event, "Agenda"));
//        this.mainContentList.add(new MainContent(R.drawable.ic_pengaduan, "Layanan Pengaduan"));
    }
}
