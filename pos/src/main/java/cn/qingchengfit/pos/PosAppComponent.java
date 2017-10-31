package cn.qingchengfit.pos;

import android.app.Activity;
import cn.qingchengfit.pos.di.AppModel;
import cn.qingchengfit.pos.di.BindCashierDeskActivity;
import cn.qingchengfit.pos.di.BindExchangeActivity;
import cn.qingchengfit.pos.di.BindLoginActivity;
import cn.qingchengfit.pos.di.BindMainActivity;
import cn.qingchengfit.pos.di.BindPosBillActivity;
import cn.qingchengfit.pos.di.BindPosCardActivity;
import cn.qingchengfit.pos.di.BindPosStaffActivity;
import cn.qingchengfit.pos.di.BindPosStudentActivity;
import cn.qingchengfit.pos.di.BindSettingActivity;
import cn.qingchengfit.saasbase.di.BindBillActivity;
import cn.qingchengfit.saasbase.di.BindSaasCommonActivity;
import cn.qingchengfit.saasbase.qrcode.views.QRActivity;
import dagger.Binds;
import dagger.Component;
import dagger.Module;
import dagger.Subcomponent;
import dagger.android.ActivityKey;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.multibindings.IntoMap;

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

@Component(modules = { AppModel.class,
    AndroidInjectionModule.class, AndroidSupportInjectionModule.class, BindMainActivity.class,
  BindPosStudentActivity.class,
  BindPosCardActivity.class,  BindPosBillActivity.class, BindLoginActivity.class,
  BindExchangeActivity.class, BindSettingActivity.class, BindSaasCommonActivity.class,
  BindPosStaffActivity.class, PosAppComponent.SplashModule.class, BindCashierDeskActivity.class,
  cn.qingchengfit.pos.PosAppComponent.QRModule.class, cn.qingchengfit.pos.PosAppComponent.SplashModule.class

})
public interface PosAppComponent {
  void inject(PosApp app);

  @Subcomponent() public interface SplashSubcomponent extends AndroidInjector<SplashActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<SplashActivity> {
    }
  }

  @Module(subcomponents = SplashSubcomponent.class) abstract class SplashModule {
    @Binds @IntoMap @ActivityKey(SplashActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        SplashSubcomponent.Builder builder);
  }

  @Subcomponent() public interface QRSubcomponent extends AndroidInjector<QRActivity> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<QRActivity> {
    }
  }

  @Module(subcomponents = QRSubcomponent.class) abstract class QRModule {
    @Binds @IntoMap @ActivityKey(QRActivity.class)
    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(
        QRSubcomponent.Builder builder);
  }
}
