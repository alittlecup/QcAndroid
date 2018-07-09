package debug;

import cn.qingchengfit.login.LoginDepModel;
import cn.qingchengfit.login.di.BindLoginActivity;
import cn.qingchengfit.student.StudentViewModel;
import cn.qingchengfit.student.di.BindStudentActivity;
import cn.qingchengfit.student.respository.StudentModel;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.AndroidSupportInjectionModule;
import javax.inject.Singleton;

@Component(modules = {
    AppModel.class,
    StudentViewModel.class,
    BindStudentActivity.class,
    AndroidInjectionModule.class,
    AndroidSupportInjectionModule.class,

    LoginDepModel.class, BindLoginActivity.class,
}) public interface AppComponent extends AndroidInjector<MyApp> {

}
