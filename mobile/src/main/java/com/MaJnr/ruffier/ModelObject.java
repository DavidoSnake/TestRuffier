package com.MaJnr.ruffier;

import com.MaJnr.testruffier.R;

public enum ModelObject {

    PAGE1(1, R.layout.fragment_first_use_page_1),
    PAGE2(2, R.layout.fragment_first_use_page_2),
    PAGE3(3, R.layout.fragment_first_use_page_3);

    private int mTitleResId;
    private int mLayoutResId;

    ModelObject(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}