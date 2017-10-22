package cn.qingchengfit.pos.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import cn.qingchengfit.pos.R;
import cn.qingchengfit.views.activity.BaseActivity;
import com.anbillon.flabellum.annotations.Trunk;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import javax.inject.Inject;
@Trunk(fragments = {
  PosMainFragment.class
})
public class MainActivity extends BaseActivity implements HasSupportFragmentInjector {
  @Inject DispatchingAndroidInjector<Fragment> dispatchingFragmentInjector;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    getSupportFragmentManager()
        .beginTransaction()
        .setCustomAnimations(R.anim.slide_hold,R.anim.slide_hold)
        .replace(R.id.frag_main,new PosMainFragment())
        .commit();
  }
  @Override public AndroidInjector<Fragment> supportFragmentInjector() {
    return dispatchingFragmentInjector;
  }
}
