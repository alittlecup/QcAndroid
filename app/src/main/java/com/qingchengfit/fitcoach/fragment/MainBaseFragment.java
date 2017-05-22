package com.qingchengfit.fitcoach.fragment;

import android.content.Context;
import android.support.v4.app.Fragment;
import com.qingchengfit.fitcoach.activity.OpenDrawerInterface;

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
 * Created by Paper on 15/10/11 2015.
 */
public class MainBaseFragment extends Fragment {

    protected OpenDrawerInterface openDrawerInterface;

    @Override public void onAttach(Context context) {
        openDrawerInterface = (OpenDrawerInterface) context;
        super.onAttach(context);
    }

    @Override public void onDetach() {
        openDrawerInterface = null;
        super.onDetach();
    }
}
