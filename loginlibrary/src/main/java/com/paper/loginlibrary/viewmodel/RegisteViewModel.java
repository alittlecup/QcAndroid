package com.paper.loginlibrary.viewmodel;

import android.content.res.Resources;
import android.databinding.ObservableField;
import android.support.v4.app.Fragment;

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
 * Created by Paper on 15/8/4 2015.
 */
public class RegisteViewModel {
    private final Fragment mView;
    private final Resources mResources;
    public ObservableField<String> numberOfUsersLoggedIn = new ObservableField<>();
    public RegisteViewModel(Fragment view, Resources resources) {
        mView = view;
        mResources = resources; // You might want to abstract this for testability
    }

}
