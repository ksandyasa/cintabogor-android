package com.aprido.cintabogor.object;

import com.aprido.cintabogor.R;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by apridosandyasa on 10/7/16.
 */

public class SlideMenu implements Serializable {

    private String title;

    public SlideMenu() {
    }

    public SlideMenu(String title) {
        this.title = title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return this.title;
    }

}
