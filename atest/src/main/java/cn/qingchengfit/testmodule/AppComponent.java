package cn.qingchengfit.testmodule;

import android.app.Activity;
import cn.qingchengfit.recruit.di.BindRecruitModule;
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
 * Created by Paper on 2017/5/31.
 */
@Component(modules = {
    AndroidSupportInjectionModule.class, AndroidInjectionModule.class, TestModule.class, BindRecruitModule.class,
    AppComponent.MainModule.class,

    //AppComponent.RecruitModule.class,
}) public interface AppComponent extends AndroidInjector<TestApp> {
    void inject(TestApp app);
    //@Component.Builder
    //abstract class Builder extends AndroidInjector.Builder<TestApp>{
    //}

    @Subcomponent() public interface MainSubcomponent extends AndroidInjector<MainActivity> {
        @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<MainActivity> {
        }
    }

    @Module(subcomponents = MainSubcomponent.class) abstract class MainModule {
        @Binds @IntoMap @ActivityKey(MainActivity.class)
        abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(MainSubcomponent.Builder builder);
    }

    //@Subcomponent() public interface RecruitSubcomponent extends AndroidInjector<RecruitActivity> {
    //    @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<RecruitActivity> {
    //    }
    //}
    //@Module(subcomponents = RecruitSubcomponent.class) abstract class RecruitModule {
    //    @Binds @IntoMap @ActivityKey(RecruitActivity.class)
    //    abstract AndroidInjector.Factory<? extends Activity> bindYourFragmentInjectorFactory(RecruitSubcomponent.Builder builder);
    //}
}
