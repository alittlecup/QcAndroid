package cn.qingchengfit.pos;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import cn.qingchengfit.Constants;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.pos.di.AppModel;
import cn.qingchengfit.pos.di.PosGymWrapper;
import cn.qingchengfit.utils.CrashUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
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
 * Created by Paper on 2017/9/25.
 */

public class PosApp extends Application implements HasActivityInjector, HasSupportFragmentInjector {
  @Inject DispatchingAndroidInjector<Activity> dispatchingActivityInjector;
  @Inject DispatchingAndroidInjector<Fragment> dispatchingFragmentInjector;

  // 数据接收的 URL
  final String SA_SERVER_URL = "http://qingchengfit.cloud.sensorsdata.cn:8006/sa?token=2f79f21494c6f970";
  // 配置分发的 URL
  final String SA_CONFIGURE_URL = "http://qingchengfit.cloud.sensorsdata.cn:8006/config?project=default";
  final SensorsDataAPI.DebugMode SA_DEBUG_MODE = SensorsDataAPI.DebugMode.DEBUG_OFF;

  public static Context context;
  @Override public void onCreate() {
    super.onCreate();
    ToastUtils.init(this);
    initInjcet();
    initSensor();
    context = this;
  }


  @Override protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }

  public void initInjcet() {
    LoginStatus.Builder lb = new LoginStatus.Builder();
    ToastUtils.init(this);
    LogUtil.e("session:" + PreferenceUtils.getPrefString(this, "session_id", ""));
    PosAppComponent component = DaggerPosAppComponent.builder()
        .appModel(new AppModel.Builder()
            .gymWrapper(new PosGymWrapper.Builder()
                .build())
            .loginStatus(new LoginStatus.Builder()
                .build())
            .qcrestRepository(new QcRestRepository(this, Constants.ServerDebug,"pos"))
            .build())
        .build();
    component.inject(this);
  }

  @Override public DispatchingAndroidInjector<Activity> activityInjector() {
    return dispatchingActivityInjector;
  }

  @Override public DispatchingAndroidInjector<android.support.v4.app.Fragment> supportFragmentInjector() {
    return dispatchingFragmentInjector;
  }

  void initSensor(){
    //初始化神策
    SensorsDataAPI.sharedInstance(this,                               // 传入 Context
      SA_SERVER_URL,                      // 数据接收的 URL
      SA_CONFIGURE_URL,                   // 配置分发的 URL
      BuildConfig.DEBUG ? SensorsDataAPI.DebugMode.DEBUG_ONLY : SensorsDataAPI.DebugMode.DEBUG_OFF);

    try {
      // 打开自动采集, 并指定追踪哪些 AutoTrack 事件
      List<SensorsDataAPI.AutoTrackEventType> eventTypeList = new ArrayList<>();
      // $AppStart
      eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_START);
      // $AppEnd
      eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_END);
      // $AppViewScreen
      eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_VIEW_SCREEN);
      // $AppClick
      eventTypeList.add(SensorsDataAPI.AutoTrackEventType.APP_CLICK);
      SensorsDataAPI.sharedInstance(this).enableAutoTrack(eventTypeList);
      SensorsDataAPI.sharedInstance().trackFragmentAppViewScreen();
      JSONObject properties = new JSONObject();
      properties.put("qc_app_name", "Staff");
      SensorsDataAPI.sharedInstance(this).registerSuperProperties(properties);
    } catch (JSONException e) {
      CrashUtils.sendCrash(e);
      e.printStackTrace();
    } catch (Exception e) {
      CrashUtils.sendCrash(e);
    }
  }
}
