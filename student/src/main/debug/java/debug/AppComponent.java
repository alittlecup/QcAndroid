package debug;

import cn.qingchengfit.student.di.BindStudentActivity;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(modules = {
    AppModel.class,BindStudentActivity.class,AndroidInjectionModule.class,AndroidSupportInjectionModule.class
}) public interface AppComponent extends AndroidInjector<MyApp> {


}
