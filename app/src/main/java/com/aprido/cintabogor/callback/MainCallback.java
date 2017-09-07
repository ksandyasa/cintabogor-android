package com.aprido.cintabogor.callback;

import com.aprido.cintabogor.object.Banner;
import com.aprido.cintabogor.object.Content;
import com.aprido.cintabogor.object.MainContent;
import com.aprido.cintabogor.object.SlideMenu;

import java.util.List;

/**
 * Created by apridosandyasa on 10/7/16.
 */

public interface MainCallback {
    void finishedSetupToolbar();
    void finishedSetupBottomSheet();
    void finishedSetupNearbyListViews(List<Content> nearbyList);
    void finishedSetupSlideViews(List<Banner> contentList);
    void finishedSetupNavigationViews(List<SlideMenu> slideMenuList, List<MainContent> mainContentList);
    void failedSetupContentViews();
}
