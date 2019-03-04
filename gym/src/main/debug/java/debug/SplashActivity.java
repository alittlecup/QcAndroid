package debug;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.gym.BuildConfig;
import cn.qingchengfit.gym.R;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.saascommon.SaasCommonActivity;
import cn.qingchengfit.utils.CrashUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.views.activity.BaseActivity;
import javax.inject.Inject;

public class SplashActivity extends SaasCommonActivity {
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  TextView textView;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.splash_main_activity);
    EditText editText = findViewById(R.id.path_edit);
    textView = findViewById(R.id.textview);
    findViewById(R.id.open).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        String s = editText.getText().toString();
        if (TextUtils.isEmpty(s)) {
          routeTo(SplashActivity.this, "gym", "/my/gyms", null);
        } else {
          routeTo(SplashActivity.this, "gym", s, null);
        }
      }
    });

    findViewById(R.id.login).setOnClickListener(view -> {
      QcRouteUtil.setRouteOptions(new RouteOptions("login").setContext(this).setActionName("open"))
          .call();
    });
  }

  @Override protected void onResume() {
    super.onResume();
    textView.setText(QcRestRepository.getSession(this));
    if (!TextUtils.isEmpty(textView.getText())) {
      setLoginInfo();
    }
  }

  private void setLoginInfo() {
    Staff staff = new Staff();
    staff.setId("7505");
    loginStatus.setSession(QcRestRepository.getSession(this));
    loginStatus.setLoginUser(staff);
    loginStatus.setUserId("54405");

    CoachService coachService = new CoachService();
    coachService.setModel("staff_gym");
    coachService.setId("10548");
    gymWrapper.setCoachService(coachService);
  }

  private void routeTo(Context context, String model, String path, Bundle bd) {
    String uri = model + path;
    try {
      uri = BuildConfig.PROJECT_NAME + "://" + model + path;
      Intent to = new Intent(context.getPackageName(), Uri.parse(uri));
      if (context instanceof BaseActivity && ((BaseActivity) context).getModuleName()
          .equalsIgnoreCase(model)) {
        to.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
      } else {
        to.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      }
      if (bd != null) {
        to.putExtras(bd);
      }
      context.startActivity(to);
    } catch (Exception e) {
      LogUtil.e("找不到模块去处理" + uri);
      CrashUtils.sendCrash(e);
    }
  }
}


