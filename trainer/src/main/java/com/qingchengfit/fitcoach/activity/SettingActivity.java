package com.qingchengfit.fitcoach.activity;

import android.os.Bundle;
import android.support.annotation.MenuRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;


import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.views.activity.BaseActivity;
import com.afollestad.materialdialogs.MaterialDialog;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.FragmentCallBack;
import com.qingchengfit.fitcoach.fragment.ModifyBrifeFragment;
import com.qingchengfit.fitcoach.fragment.RecordFragment;
import com.qingchengfit.fitcoach.fragment.main.SettingFragment;
import com.qingchengfit.fitcoach.fragment.personalpage.EditHomeFragment;
import com.qingchengfit.fitcoach.fragment.personalpage.EditResumeFragmentBuilder;

public class SettingActivity extends BaseActivity
    implements FragmentCallBack, WebActivityInterface {

    public static String TAG = SettingActivity.class.getName();
    FragmentManager fragmentManager;
	Toolbar toolbar;
	TextView toolbarTitle;
    private MaterialDialog loadingDialog;
    private int result = 0;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
      toolbar = (Toolbar) findViewById(R.id.toolbar);
      toolbarTitle = (TextView) findViewById(R.id.toolbar_title);

      if (android.os.Build.VERSION.SDK_INT >=21)
            getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.toolbar));
        toolbar.setNavigationIcon(R.drawable.ic_arrow_left);
        fragmentManager = getSupportFragmentManager();
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        int to = getIntent().getIntExtra("to", 0);
        switch (to) {
            case 1:
                //onFragmentChange(ModifyInfoFragment.newInstance("", ""), false);
                break;
            case 3:
                onFragmentChange(new RecordFragment(), false);
                break;
            case 4:
              //onFragmentChange(new WorkExepSettingFragment(), false);
                break;
            case 5:
                onFragmentChange(ModifyBrifeFragment.newInstance(getIntent().getStringExtra("desc")), false);
                break;
            case 6:
                onFragmentChange(new EditHomeFragment(), false);
                break;
            case 7:
                onFragmentChange(new EditResumeFragmentBuilder("").build(), false);
                break;

            default:
                onFragmentChange(SettingFragment.newInstance(), false);
                break;
        }
    }

    @Override protected boolean isFitSystemBar() {
        return false;
    }

    @Override public void onFragmentChange(Fragment fragment) {
        AppUtils.hideKeyboard(this);
        fragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.slide_right_in, R.anim.slide_hold, R.anim.slide_hold, R.anim.slide_right_out)
            .replace(R.id.settting_fraglayout, fragment)
            .addToBackStack(null)
            .commit();
    }

    public void onFragmentChange(Fragment fragment, boolean addtoStack) {
        fragmentManager.beginTransaction().replace(R.id.settting_fraglayout, fragment).commit();
    }

    @Override public void onToolbarMenu(@MenuRes int menu, int icon, String title) {
        toolbarTitle.setText(title);
        toolbar.getMenu().clear();
        if (menu != 0) {
            toolbar.inflateMenu(menu);
        } else {
            toolbar.inflateMenu(R.menu.menu_blank);
        }
        if (icon != 0) toolbar.setNavigationIcon(icon);
    }

    @Override public void onToolbarClickListener(Toolbar.OnMenuItemClickListener listener) {
        toolbar.setOnMenuItemClickListener(listener);
    }

    @Override public void hindToolbar() {
        toolbar.setVisibility(View.GONE);
    }

    @Override public void showToolbar() {
        toolbar.setVisibility(View.VISIBLE);
    }

    @Override public void ShowLoading(String content) {
        //if (loadingDialog == null) {
        //    loadingDialog = new MaterialDialog.Builder(this).content("请稍后").progress(true, 0).cancelable(false).build();
        //}
        //if (content != null) loadingDialog.setContent(content);
        //loadingDialog.show();
        super.showLoading();
    }

    @Override public void onBackPressed() {
        AppUtils.hideKeyboard(this);
        toolbar.getMenu().clear();
        super.onBackPressed();
    }

    @Override public void hideLoading() {
        //if (loadingDialog != null && loadingDialog.isShowing()) loadingDialog.dismiss();
        super.hideLoading();
    }

    @Override public void fixCount() {
        result++;
    }

    @Override public void onfinish() {

    }

    @Override public void finish() {
        setResult(result);
        super.finish();
    }
    //    @Override
    //    public void onBackPressed() {
    //        if (fragmentManager.getBackStackEntryCount() == 1) {
    //            super.onBackPressed();
    //        } else fragmentManager.popBackStack();
    //    }
}
