package debug;

import cn.qingchengfit.student.di.BindStudentActivity;
import dagger.Component;
import dagger.android.AndroidInjector;


@Component(modules = {
    AppModel.class,BindStudentActivity.class
}) public interface AppComponent extends AndroidInjector<MyApp> {
}
