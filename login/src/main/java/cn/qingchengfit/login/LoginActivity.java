package cn.qingchengfit.login;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;


import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.login.views.LoginFragment;
import cn.qingchengfit.login.views.RegistInitFragment;
import cn.qingchengfit.login.views.RegisteFragment;
import cn.qingchengfit.login.views.RegisteInitFirstFragment;
import cn.qingchengfit.login.views.RegisteInitSecoundFragment;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.activity.BaseActivity;
import cn.qingchengfit.widgets.LoadingDialog;
import com.anbillon.flabellum.annotations.Trunk;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import javax.inject.Inject;
@Trunk(fragments = {
  LoginFragment.class,RegisteFragment.class, RegistInitFragment.class, RegisteInitFirstFragment.class,
  RegisteInitSecoundFragment.class
})
public class LoginActivity extends BaseActivity implements HasSupportFragmentInjector {
  @Inject DispatchingAndroidInjector<Fragment> dispatchingFragmentInjector;

	ViewPager viewpager;
	TabLayout loginTabview;
  LoadingDialog dialog;
  @Inject LoginStatus loginStatus;

  @Override public AndroidInjector<Fragment> supportFragmentInjector() {
    return dispatchingFragmentInjector;
  }
  @Override protected void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    viewpager = findViewById(R.id.viewpager);
    loginTabview = findViewById(R.id.login_tabview);

    if (!CompatUtils.less21() ) {
      loginTabview.setPadding(0,
          MeasureUtils.getStatusBarHeight(getBaseContext()), 0, 0);
    }
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
        return "登录";
      } else {
        return "注册";
      }
    }

    @Override public int getCount() {
      return 2;
    }
  }
}
