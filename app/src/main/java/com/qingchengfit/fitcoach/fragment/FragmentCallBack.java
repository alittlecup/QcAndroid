package com.qingchengfit.fitcoach.fragment;

import android.support.annotation.DrawableRes;
import android.support.annotation.MenuRes;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/8/12 2015.
 */
public interface FragmentCallBack {
    public void onFragmentChange(Fragment fragment);

    public void onToolbarMenu(@MenuRes int menu, @DrawableRes int naviicon, String title);

    public void onToolbarClickListener(Toolbar.OnMenuItemClickListener listener);

    public void hindToolbar();

    public void showToolbar();

    public void ShowLoading();

    public void hideLoading();

}
