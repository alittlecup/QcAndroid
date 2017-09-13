package cn.qingchengfit.staffkit.views.login;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.widgets.LoadingDialog;
import dagger.android.AndroidInjection;
import javax.inject.Inject;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.viewpager) ViewPager viewpager;
    @BindView(R.id.login_tabview) TabLayout loginTabview;
    LoadingDialog dialog;
    @Inject LoginStatus loginStatus;

    @Override protected void onCreate(Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        viewpager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(loginTabview));
        viewpager.setAdapter(new LoginFragAdapter(getSupportFragmentManager()));
        loginTabview.setupWithViewPager(viewpager);
        if (getIntent().getBooleanExtra("isRegiste", false)) {
            viewpager.setCurrentItem(1);
        }
        dialog = new LoadingDialog(this);
        if (loginStatus.isLogined()) {
            ToastUtils.show("已登录");
            finish();
        }
    }

    class LoginFragAdapter extends FragmentPagerAdapter {

        public LoginFragAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override public Fragment getItem(int position) {
            if (position == 0) {
                return new LoginFragment();
            } else {
                return new RegisteFragment();
            }
        }

        @Override public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return getString(R.string.login_btn_label);
            } else {
                return getString(R.string.registe_indicat_label);
            }
        }

        @Override public int getCount() {
            return 2;
        }
    }
}
