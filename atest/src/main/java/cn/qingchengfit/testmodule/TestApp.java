package cn.qingchengfit.testmodule;

import android.app.Activity;
import android.app.Application;
import android.support.v4.app.Fragment;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.router.BaseRouter;
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
 * Created by Paper on 2017/5/31.
 */

public class TestApp extends Application implements HasActivityInjector, HasSupportFragmentInjector {
    @Inject DispatchingAndroidInjector<Activity> dispatchingActivityInjector;
    @Inject DispatchingAndroidInjector<Fragment> dispatchingFragmentInjector;

    @Override public void onCreate() {
        super.onCreate();
        ToastUtils.init(this);
        Staff staff = new Staff("冯博", "15629051755", "", 1);
        staff.setId("7409");
        PreferenceUtils.setPrefString(this, "session_id", "tpjo66ambckwr0klgp3o60ki3h1ld9tx");
        LogUtil.e("session:" + PreferenceUtils.getPrefString(this, "session_id", ""));
        AppComponent component = DaggerAppComponent.builder()
            .testModule(new TestModule.Builder().app(this)
                .gymWrapper(new GymWrapper.Builder().brand(new Brand("1")).build())
                .loginStatus(new LoginStatus.Builder().userId("7409").loginUser(staff).session("tpjo66ambckwr0klgp3o60ki3h1ld9tx").build())
                .router(new BaseRouter())
                .restRepository(new QcRestRepository(this, "http://cloudtest.qingchengfit.cn/", "staff-test"))
                .build())
            .build();
        component.inject(this);
    }

    @Override public DispatchingAndroidInjector<Activity> activityInjector() {
        return dispatchingActivityInjector;
    }

    @Override public DispatchingAndroidInjector<Fragment> supportFragmentInjector() {
        return dispatchingFragmentInjector;
    }
}
