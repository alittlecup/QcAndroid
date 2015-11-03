package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;
import com.qingchengfit.fitcoach.BaseAcitivity;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.FragmentCallBack;
import com.qingchengfit.fitcoach.fragment.SettingFragment;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SettingActivity extends BaseAcitivity implements FragmentCallBack, WebActivityInterface {

    public static String TAG = SettingActivity.class.getName();
    FragmentManager fragmentManager;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    private MaterialDialog loadingDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        fragmentManager = getSupportFragmentManager();
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        onFragmentChange(SettingFragment.newInstance(), false);
    }


    @Override
    public void onFragmentChange(Fragment fragment) {
        fragmentManager.beginTransaction()
                .replace(R.id.settting_fraglayout, fragment)
                .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_left_out, R.anim.slide_right_out, R.anim.slide_left_in)
                .addToBackStack(null)
                .commit();
    }

    public void onFragmentChange(Fragment fragment, boolean addtoStack) {
        fragmentManager.beginTransaction()
                .replace(R.id.settting_fraglayout, fragment)
                .commit();
    }

    @Override
    public void onToolbarMenu(@MenuRes int menu, int icon, String title) {
        toolbar.setTitle(title);
        toolbar.getMenu().clear();
        if (menu != 0)
            toolbar.inflateMenu(menu);
        else toolbar.inflateMenu(R.menu.menu_blank);
        if (icon != 0)
            toolbar.setNavigationIcon(icon);
    }

    @Override
    public void onToolbarClickListener(Toolbar.OnMenuItemClickListener listener) {
        toolbar.setOnMenuItemClickListener(listener);
    }

    @Override
    public void hindToolbar() {
        toolbar.setVisibility(View.GONE);
    }

    @Override
    public void showToolbar() {
        toolbar.setVisibility(View.VISIBLE);
    }

    @Override
    public void ShowLoading() {
        if (loadingDialog == null)
            loadingDialog = new MaterialDialog.Builder(this)
                    .content("请稍后")
                    .progress(true, 0)
                    .cancelable(false)
                    .build();
        loadingDialog.show();
    }

    @Override
    public void onBackPressed() {
        toolbar.getMenu().clear();
        super.onBackPressed();
    }

    @Override
    public void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing())
            loadingDialog.dismiss();
    }

    @Override
    public void onfinish() {

    }


//    @Override
//    public void onBackPressed() {
//        if (fragmentManager.getBackStackEntryCount() == 1) {
//            super.onBackPressed();
//        } else fragmentManager.popBackStack();
//    }
}
