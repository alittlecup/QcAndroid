package debug;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.login.LoginActivity;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.saascommon.SaasCommonActivity;
import cn.qingchengfit.student.BuildConfig;
import cn.qingchengfit.student.R;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CrashUtils;
import cn.qingchengfit.utils.LogUtil;
import javax.inject.Inject;

public class SplashActivity extends SaasCommonActivity {
  TextView textView;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;



  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    Button button = findViewById(R.id.open);
    Button login = findViewById(R.id.login);
    textView = findViewById(R.id.content);
    button.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        routeTo("student", "/student/home", null);
      }
    });
    login.setOnClickListener(view -> {
      QcRouteUtil.setRouteOptions(new RouteOptions("login").setContext(this).setActionName("open"))
          .call();
      MyApp.isLogin=true;
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
    } catch (Exception e) {
      LogUtil.e("找不到模块去处理" + uri);
      CrashUtils.sendCrash(e);
    }
  }

  @Override protected void onResume() {
    super.onResume();
    textView.setText(QcRestRepository.getSession(this));
    if(MyApp.isLogin){
      setLoginInfo();
    }
  }
  private void setLoginInfo(){
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
}
