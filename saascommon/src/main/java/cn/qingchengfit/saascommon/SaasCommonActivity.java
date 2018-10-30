package cn.qingchengfit.saascommon;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.widget.FrameLayout;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CrashUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.views.activity.BaseActivity;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import javax.inject.Inject;

public class SaasCommonActivity extends BaseActivity implements HasSupportFragmentInjector {
  @Inject DispatchingAndroidInjector<Fragment> dispatchingFragmentInjector;
  public  FrameLayout webFragLayout;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_base_frag);
    webFragLayout = findViewById(R.id.web_frag_layout);
    webFragLayout.setOnTouchListener((v, event) -> true);
    onNewIntent(getIntent());
  }

  @Override public AndroidInjector<Fragment> supportFragmentInjector() {
    return dispatchingFragmentInjector;
  }

  protected void routeTo(String model, String path, Bundle bd) {
    String uri = model + path;
    try {
      uri = AppUtils.getCurAppSchema(this) + "://" + model + path;
      Intent to = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
      to.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      if (bd != null) {
        to.putExtras(bd);
      }
      startActivity(to);
      finish();
    } catch (Exception e) {
      LogUtil.e("找不到模块去处理" + uri);
      CrashUtils.sendCrash(e);
    }
  }
  @Override public int getFragId() {
    return R.id.web_frag_layout;
  }
}
