package cn.qingchengfit.testmodule;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import android.util.Log;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.saasbase.gymconfig.GymConfigAcitivty;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.weex.utils.WeexDelegate;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.HasSupportFragmentInjector;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/5/31.
 */

public class TestApp extends Application
    implements HasActivityInjector, HasSupportFragmentInjector {
  @Inject DispatchingAndroidInjector<Activity> dispatchingActivityInjector;
  @Inject DispatchingAndroidInjector<Fragment> dispatchingFragmentInjector;

  @Override protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }

  // 数据接收的 URL
  final String SA_SERVER_URL = "http://qingchengfit.cloud.sensorsdata.cn:8006/sa?token=2f79f21494c6f970";
  // 配置分发的 URL
  final String SA_CONFIGURE_URL = "http://qingchengfit.cloud.sensorsdata.cn:8006/config?project=default";

  @Override public void onCreate() {
    super.onCreate();
    ToastUtils.init(this);
    WeexDelegate.initWXSDKEngine(this);
    Staff staff = new Staff("黄宝乐", "18510247443", "", 1);
    staff.setId("7505");
    PreferenceUtils.setPrefString(this, "session_id", "msqr15z8v6co8yhvk8hrnxrsp9qjji8b");

    LogUtil.e("session:" + PreferenceUtils.getPrefString(this, "session_id", ""));
    AppComponent component = DaggerAppComponent.builder()
        .testModule(new TestModule.Builder().app(this)
            .gymWrapper(new GymWrapper.Builder().brand(new Brand("1")).build())
            .loginStatus(new LoginStatus.Builder().userId("7409")
                .loginUser(staff)
                .session("msqr15z8v6co8yhvk8hrnxrsp9qjji8b")
                .build())
            .router(new BaseRouter())
            .restRepository(
                new QcRestRepository(this, "http://cloudtest.qingchengfit.cn/", "staff-test"))
            .build())
        .build();
    component.inject(this);
    init();
  }

  private void init() {
    SensorsDataAPI.sharedInstance(this,                               // 传入 Context
        SA_SERVER_URL,                      // 数据接收的 URL
        SA_CONFIGURE_URL,                   // 配置分发的 URL
        BuildConfig.DEBUG ? SensorsDataAPI.DebugMode.DEBUG_ONLY : SensorsDataAPI.DebugMode.DEBUG_OFF);

    try {
      SensorsDataAPI.sharedInstance(this).enableAutoTrack();
      //初始化 SDK 之后，开启自动采集 Fragment 页面浏览事件
      SensorsDataAPI.sharedInstance().trackFragmentAppViewScreen();
      List<Class<?>> classList = new ArrayList<>();

      classList.add(GymConfigAcitivty.class);
      SensorsDataAPI.sharedInstance(this).ignoreAutoTrackActivities(classList);
      JSONObject properties = new JSONObject();
      properties.put("qc_app_name", "Staff");
      SensorsDataAPI.sharedInstance(this).registerSuperProperties(properties);
    } catch (JSONException e) {
      Log.e("hs_bug", e.getMessage());
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
      Log.e("hs_bug", e.getMessage());
    }
  }

  @Override public DispatchingAndroidInjector<Activity> activityInjector() {
    return dispatchingActivityInjector;
  }

  @Override public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
    return dispatchingFragmentInjector;
  }
}
