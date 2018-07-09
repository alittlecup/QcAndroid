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
import cn.qingchengfit.login.LoginActivity;
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
      startActivity(new Intent(SplashActivity.this,LoginActivity.class));
      }
    });
  }

  @Override protected void onResume() {
    super.onResume();

  }
}
