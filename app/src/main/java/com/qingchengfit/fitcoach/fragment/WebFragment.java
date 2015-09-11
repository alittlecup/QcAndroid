package com.qingchengfit.fitcoach.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.qingchengfit.fitcoach.App;

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
 * Created by Paper on 15/9/10 2015.
 */
public class WebFragment extends Fragment {
    public static final String BASE_URL = "base_url";

    public static WebFragment newInstance(String baseUrl) {
        WebFragment fragment;
        if (App.canXwalk) {
            fragment = new XWalkFragment();
        } else {
            fragment = new OriginWebFragment();
        }
        Bundle args = new Bundle();
        args.putString(BASE_URL, baseUrl);
        fragment.setArguments(args);
        return fragment;
    }

    public void removeCookie() {

    }

}
