package debug;

import cn.qingchengfit.card.di.BindStaffCardActivity;
import cn.qingchengfit.checkout.CheckViewModule;
import cn.qingchengfit.checkout.di.BindCheckoutCounterActivity;
import cn.qingchengfit.login.LoginDepModel;
import cn.qingchengfit.login.di.BindLoginActivity;
import cn.qingchengfit.student.StudentViewModel;
import cn.qingchengfit.student.di.BindStudentActivity;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Component(modules = {
    AppModel.class, AndroidSupportInjectionModule.class, AndroidInjectionModule.class,
    BindCheckoutCounterActivity.class, CheckViewModule.class,

    LoginDepModel.class, BindLoginActivity.class, BindStaffCardActivity.class,
    BindStudentActivity.class, StudentViewModel.class
}) public interface AppComponent extends AndroidInjector<MyApp> {
}
