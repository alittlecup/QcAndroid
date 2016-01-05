package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.paper.paperbaselibrary.utils.AppUtils;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.AddBelongGymFragment;
import com.qingchengfit.fitcoach.fragment.AddSelfGymFragment;
import com.qingchengfit.fitcoach.fragment.AddSelfNotiFragment;
import com.qingchengfit.fitcoach.fragment.AddStudentManulkFragment;
import com.qingchengfit.fitcoach.fragment.GymDetailFragment;
import com.qingchengfit.fitcoach.fragment.GymDetailNativeFragment;
import com.qingchengfit.fitcoach.fragment.SaleGlanceFragment;
import com.qingchengfit.fitcoach.fragment.StatementGlanceFragment;

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
 * Created by Paper on 15/10/12 2015.
 */
public class FragActivity extends BaseAcitivity {

    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frag);
        int type = getIntent().getIntExtra("type", 0);


        switch (type) {
            case 0:
                fragment = new StatementGlanceFragment();
                break;
            case 1:
                fragment = new SaleGlanceFragment();
                break;
            case 2:
                fragment = new AddSelfNotiFragment();
                break;
            case 3:
                boolean isnew = getIntent().getBooleanExtra("isNew", false);
                fragment = AddSelfGymFragment.newInstance(isnew);

                break;
            case 4:
                fragment = new AddBelongGymFragment();
                break;
            case 5:
                int id = getIntent().getIntExtra("id", 1);
                String host = getIntent().getStringExtra("host");
                boolean isPrivate = getIntent().getBooleanExtra("isPrivate", false);
                fragment = GymDetailFragment.newInstance(id, host, isPrivate);
                break;
            case 6:
                long gymid = getIntent().getLongExtra("id", 1l);
                String isPrivategym = getIntent().getStringExtra("model");
                fragment = GymDetailNativeFragment.newInstance(gymid,isPrivategym);
                break;
            case 7:
                fragment = AddStudentManulkFragment.newInstance();
                break;
            default:
                break;
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.web_frag_layout, fragment)
                .commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        AppUtils.hideKeyboard(this);
        if (fragment instanceof GymDetailFragment && fragment.isVisible()) {
            if (((GymDetailFragment) fragment).canGoBack()) {
                ((GymDetailFragment) fragment).goBack();
            } else this.finish();
        } else if (getSupportFragmentManager().popBackStackImmediate()) {

        } else {

            this.finish();
        }
    }
}
