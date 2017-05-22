package com.qingchengfit.fitcoach.fragment;

import android.content.Context;

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
 * Created by Paper on 15/9/8 2015.
 */
public class BaseSettingFragment extends BaseFragment {

    public FragmentCallBack fragmentCallBack;

    @Override public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentCallBack) {
            fragmentCallBack = (FragmentCallBack) context;
        } else {
            throw new ExceptionInInitializerError();
        }
    }

    @Override public void onDetach() {
        super.onDetach();
        fragmentCallBack = null;
    }
}
