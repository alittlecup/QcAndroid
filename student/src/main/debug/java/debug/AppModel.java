package debug;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.saascommon.mvvm.ViewModelFactory;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentActivity;
import cn.qingchengfit.student.StudentViewModel;
import cn.qingchengfit.student.respository.IStudentModel;
import cn.qingchengfit.student.respository.StudentModel;
import cn.qingchengfit.student.respository.StudentRepository;
import cn.qingchengfit.student.respository.StudentRepositoryImpl;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import java.util.List;
import javax.inject.Singleton;

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
@Module public abstract class AppModel {

  static LoginStatus loginStatus = new LoginStatus.Builder().build();
  static GymWrapper gymWrapper = new GymWrapper.Builder().build();

  @Binds abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

  @Provides static LoginStatus provideLogin() {

    return loginStatus;
  }

  @Singleton @ContributesAndroidInjector
  abstract SplashActivity contributeStudentActivityInjector();

  @Provides static Application providesApplication() {
    return MyApp.INSTANCE;
  }

  @Provides static GymWrapper provideGym() {
    return gymWrapper;
  }

  @Provides static QcRestRepository provideQcRestRepository(Application application) {
    return new QcRestRepository(application, "http://cloudtest01.qingchengfit.cn/", "staff-qingcheng");
  }

  @Provides static IPermissionModel providePermission() {
    return new IPermissionModel() {
      @Override public boolean check(String permission) {
        return true;
      }

      @Override public boolean checkAllGym(String permission) {
        return false;
      }

      @Override public boolean checkInBrand(String permission) {
        return false;
      }

      @Override public boolean check(String permission, List<String> shopids) {
        return false;
      }
    };
  }
}
