package cn.qingchengfit.pos;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.v4.app.Fragment;
import cn.qingchengfit.Constants;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.pos.di.AppModel;
import cn.qingchengfit.pos.di.PosGymWrapper;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.support.HasSupportFragmentInjector;
import javax.inject.Inject;

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
  public static Context context;
  @Override public void onCreate() {
    super.onCreate();
    ToastUtils.init(this);
    initInjcet();
    context = this;
  }

  @Override protected void attachBaseContext(Context base) {
    super.attachBaseContext(base);
    MultiDex.install(this);
  }

  public void initInjcet() {
    LoginStatus.Builder lb = new LoginStatus.Builder();
    //String staffid = PreferenceUtils.getPrefString(this, Configs.PREFER_WORK_ID, "");
    //String staffname = PreferenceUtils.getPrefString(this, Configs.PREFER_WORK_NAME, "");
    //String session = PreferenceUtils.getPrefString(this, Configs.PREFER_SESSION, "");
    //String user_id = PreferenceUtils.getPrefString(this, Configs.PREFER_USER_ID, "");
    ToastUtils.init(this);
    Staff staff = new Staff("纸团", "15123358198", "", 1);
    staff.setId("3288");
    //PreferenceUtils.setPrefString(this, "session_id", "30kj9b0jyy5edj4mhxt0d8jj4cricxgr");
    CoachService coachService = new CoachService();
    coachService.setId("8670");
    coachService.setModel("staff_gym");
    LogUtil.e("session:" + PreferenceUtils.getPrefString(this, "session_id", ""));
    PosAppComponent component = DaggerPosAppComponent.builder()
        .appModel(new AppModel.Builder()
            .gymWrapper(new PosGymWrapper.Builder().brand(new Brand("1"))
                .coachService(coachService)
                .build())
            .loginStatus(new LoginStatus.Builder().userId("7060")
                .loginUser(staff).session("30kj9b0jyy5edj4mhxt0d8jj4cricxgr")
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
}
