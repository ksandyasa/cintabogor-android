package com.aprido.cintabogor.callback;

import com.aprido.cintabogor.object.CategoryContent;
import com.aprido.cintabogor.object.Content;

import java.util.List;

/**
 * Created by apridosandyasa on 4/30/17.
 */

public interface CategoryTabCallback {
    void failedSetupCategoryTabViews();
    void finishedSetupCategoryTabViews(List<CategoryContent> categoryContentList);
    void finishedSetupContentCategoryTabViews(List<Content> contentList, int categoryId);

}
