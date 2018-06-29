package debug;

import android.app.Activity;
import android.app.Application;
import android.util.Log;
import cn.qingchengfit.checkout.BuildConfig;
import cn.qingchengfit.saasbase.gymconfig.GymConfigAcitivty;
import com.sensorsdata.analytics.android.sdk.SensorsDataAPI;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import org.json.JSONException;
import org.json.JSONObject;

public class MyApp extends Application implements HasActivityInjector {
  @Inject DispatchingAndroidInjector<Activity> dispatchingActivityInjector;


  // 数据接收的 URL
  final String SA_SERVER_URL =
      "http://qingchengfit.cloud.sensorsdata.cn:8006/sa?token=2f79f21494c6f970";
  // 配置分发的 URL
  final String SA_CONFIGURE_URL =
      "http://qingchengfit.cloud.sensorsdata.cn:8006/config?project=default";

  @Override public void onCreate() {
    super.onCreate();
    DaggerAppComponent.create().inject(this);

    //初始化神策
    SensorsDataAPI.sharedInstance(this,                               // 传入 Context
        SA_SERVER_URL,                      // 数据接收的 URL
        SA_CONFIGURE_URL,                   // 配置分发的 URL
        BuildConfig.DEBUG ? SensorsDataAPI.DebugMode.DEBUG_ONLY
            : SensorsDataAPI.DebugMode.DEBUG_OFF);

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


  @Override public AndroidInjector<Activity> activityInjector() {
    return dispatchingActivityInjector;
  }
}
