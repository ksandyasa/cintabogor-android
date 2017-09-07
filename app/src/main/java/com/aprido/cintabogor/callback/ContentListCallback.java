package com.aprido.cintabogor.callback;

import com.aprido.cintabogor.object.CategoryContent;
import com.aprido.cintabogor.object.Content;

import java.util.List;

/**
 * Created by apridosandyasa on 10/7/16.
 */

public interface ContentListCallback {
    void failedSetupContentListViews();
    void finishedSetupContentListViews(List<Content> contentList);
}
