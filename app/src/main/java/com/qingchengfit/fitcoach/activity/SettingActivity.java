package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.FragmentCallBack;
import com.qingchengfit.fitcoach.fragment.SettingFragment;

public class SettingActivity extends BaseAcitivity implements FragmentCallBack {

    public static String TAG = SettingActivity.class.getName();
    FragmentManager fragmentManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        fragmentManager = getSupportFragmentManager();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.settting_fraglayout, SettingFragment.newInstance("", ""))
                .commit();
    }


    @Override
    public void onFragmentChange(Fragment fragment) {
        fragmentManager.beginTransaction()
                .add(R.id.settting_fraglayout, fragment)

                .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out)
                .commit();
    }

//    @Override
//    public void onBackPressed() {
//        if (fragmentManager.getBackStackEntryCount() == 1) {
//            super.onBackPressed();
//        } else fragmentManager.popBackStack();
//    }
}
