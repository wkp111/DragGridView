package com.peake.draggridview;

/**
 * Created by wkp on 2017/5/3.
 */

public class ItemTitleDefault implements ItemTitle {
    private String mTitle;

    public ItemTitleDefault(String title) {
        mTitle = title;
    }

    public ItemTitleDefault() {
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    @Override
    public String getTitle() {
        return mTitle;
    }
}
