package cn.qingchengfit.pos.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import cn.qingchengfit.pos.R;
import cn.qingchengfit.saasbase.routers.RouterCenter;
import cn.qingchengfit.views.activity.BaseActivity;
import com.anbillon.flabellum.annotations.Trunk;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import javax.inject.Inject;

/**
 * Created by fb on 2017/10/10.
 */

@Trunk(fragments = {LoginFragment.class})
public class LoginActivity extends BaseActivity implements HasSupportFragmentInjector{

  @Inject DispatchingAndroidInjector<Fragment> dispatchingFragmentInjector;
  @Inject RouterCenter routerCenter;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    getSupportFragmentManager()
        .beginTransaction()
        .setCustomAnimations(R.anim.slide_hold,R.anim.slide_hold)
        .replace(R.id.frag_login,new LoginFragment())
        .commit();
  }

  @Override protected Fragment getRouterFragment(Intent intent) {
    return routerCenter.getFragment(intent.getData(), intent.getBundleExtra("b"));
  }

  @Override public String getModuleName() {
    return "login";
  }

  @Override public AndroidInjector<Fragment> supportFragmentInjector() {
    return dispatchingFragmentInjector;
  }

  @Override protected boolean preHandle(Intent intent) {
    //return super.preHandle(intent);
    return false;
  }
}
