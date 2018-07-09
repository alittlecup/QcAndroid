package debug;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import cn.qingchengfit.login.BuildConfig;
import cn.qingchengfit.saascommon.SaasCommonActivity;

import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CrashUtils;
import cn.qingchengfit.utils.LogUtil;

public class SplashActivity extends SaasCommonActivity {
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FrameLayout frameLayout = new FrameLayout(this);
    Button button = new Button(this);
    button.setText("open");
    FrameLayout.LayoutParams layoutParams =
        new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
    layoutParams.gravity = Gravity.CENTER;
    button.setLayoutParams(layoutParams);
    frameLayout.addView(button);
    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.MATCH_PARENT);
    setContentView(frameLayout, params);

    button.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        routeTo("student", "/student/home", null);
      }
    });
  }

  protected void routeTo(String model, String path, Bundle bd) {
    String uri = model + path;
    try {
      uri = BuildConfig.PROJECT_NAME + "://" + model + path;
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
}
