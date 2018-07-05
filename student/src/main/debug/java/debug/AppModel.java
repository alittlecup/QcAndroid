package debug;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saascommon.mvvm.ViewModelFactory;
import cn.qingchengfit.student.StudentViewModel;
import cn.qingchengfit.student.respository.StudentRepository;
import cn.qingchengfit.student.respository.StudentRepositoryImpl;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 15/11/19 2015.
 */
@Module(includes = { StudentViewModel.class }) public abstract class AppModel {

  @Binds abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

  @Provides static LoginStatus provideLogin() {
    return new LoginStatus.Builder().build();
  }

  @Provides static StudentRepository provideStudentRepository() {
    return new StudentRepositoryImpl();
  }
  @Provides static Application providesApplication(){
    return MyApp.INSTANCE;
  }

}
