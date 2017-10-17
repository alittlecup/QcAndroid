package cn.qingchengfit.saasbase.bill;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.routers.Ibill;
import cn.qingchengfit.saasbase.routers.RouterCenter;
import cn.qingchengfit.views.activity.BaseActivity;
import com.anbillon.flabellum.annotations.Trunk;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import javax.inject.Inject;

/**
 * Created by fb on 2017/10/13.
 */

@Trunk(fragments = { FragmentBillHome.class, }) public class BillActivity extends BaseActivity
    implements HasSupportFragmentInjector {

  @Inject DispatchingAndroidInjector<Fragment> dispatchingFragmentInjector;
  @Inject RouterCenter routerCenter;
  @Inject Ibill iBill;
  @BindView(R2.id.web_frag_layout) FrameLayout webFragLayout;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_base_frag);
    ButterKnife.bind(this);
    webFragLayout.setOnTouchListener(new View.OnTouchListener() {
      @Override public boolean onTouch(View v, MotionEvent event) {
        return true;
      }
    });
    routerCenter.registe(iBill);
    onNewIntent(getIntent());
  }

  @Override protected Fragment getRouterFragment(Intent intent) {
    return routerCenter.getFragment(intent.getData(), intent.getBundleExtra("b"));
  }

  @Override public String getModuleName() {
    return "bill";
  }

  @Override public AndroidInjector<Fragment> supportFragmentInjector() {
    return dispatchingFragmentInjector;
  }

  @Override protected boolean preHandle(Intent intent) {
    //return super.preHandle(intent);
    return false;
  }
}
