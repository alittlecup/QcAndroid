package debug;

import cn.qingcheng.gym.GymModule;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(modules = {
    AppModel.class,
    AndroidInjectionModule.class,
    AndroidSupportInjectionModule.class, GymModule.class
}) public interface AppComponent extends AndroidInjector<debug.MyApp> {

}
