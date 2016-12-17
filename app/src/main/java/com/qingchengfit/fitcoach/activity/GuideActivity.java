package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.guide.GuideFragment;

public class GuideActivity extends BaseAcitivity {

    Fragment mFragment ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        mFragment = new GuideFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frag,mFragment)
                .commit();

    }

    @Override public void onBackPressed() {
        if (!mFragment.getChildFragmentManager().popBackStackImmediate()){
            super.onBackPressed();
        }
    }
}
