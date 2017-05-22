package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.text.TextUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import com.google.gson.Gson;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.bean.Brand;
import com.qingchengfit.fitcoach.bean.CoachInitBean;
import com.qingchengfit.fitcoach.fragment.guide.GuideFragment;

public class GuideActivity extends BaseAcitivity {

    GuideFragment mFragment;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        mFragment = new GuideFragment();
        mFragment.setAddBrannd(getIntent().getBooleanExtra("add", false));
        mFragment.setBrand((Brand) getIntent().getParcelableExtra("brand"));
        getSupportFragmentManager().beginTransaction().replace(R.id.frag, mFragment).commit();
    }

    @Override public void onBackPressed() {
        if (!mFragment.getChildFragmentManager().popBackStackImmediate()) {
            super.onBackPressed();
        }
    }

    @Override protected void onDestroy() {
        String initStr = PreferenceUtils.getPrefString(this, "initSystem", "");

        if (!TextUtils.isEmpty(initStr)) {
            try {
                CoachInitBean initBean = new Gson().fromJson(initStr, CoachInitBean.class);
                initBean.brand_id = null;
                PreferenceUtils.setPrefString(this, "initSystem", new Gson().toJson(initBean));
            } catch (Exception e) {

            }
        }

        super.onDestroy();
    }
}
