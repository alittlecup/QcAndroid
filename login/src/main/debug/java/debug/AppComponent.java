package debug;




import cn.qingchengfit.login.LoginDepModel;
import cn.qingchengfit.login.di.BindLoginActivity;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(modules = {
    AppModel.class,
  LoginDepModel.class, BindLoginActivity.class,
    AndroidInjectionModule.class,
    AndroidSupportInjectionModule.class
}) public interface AppComponent extends AndroidInjector<MyApp> {

}
