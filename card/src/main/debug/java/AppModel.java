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
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentViewModel;
import cn.qingchengfit.student.respository.IStudentModel;
import cn.qingchengfit.student.respository.StudentModel;
import cn.qingchengfit.student.respository.StudentRepository;
import cn.qingchengfit.student.respository.StudentRepositoryImpl;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
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


  @Binds abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);


  @Provides static LoginStatus provideLogin() {
    LoginStatus build = new LoginStatus.Builder().build();
    Staff staff=new Staff();
    staff.setId("7505");
    build.setLoginUser(staff);
    build.setSession("zmypzucc4rd115a2ny8xjwziom0zkgus");
    build.setUserId("54405");
    return build;
  }



  @Provides static Application providesApplication() {
    return MyApp.INSTANCE;
  }

   @Provides static GymWrapper provideGym() {
    GymWrapper gymWrapper = new GymWrapper.Builder().build();
    CoachService coachService = new CoachService();
    coachService.setId("10548");
    coachService.setModel("staff_gym");
    gymWrapper.setCoachService(coachService);
    return gymWrapper;
  }

  @Provides static QcRestRepository provideQcRestRepository(Application application) {
    return new QcRestRepository(application, Configs.Server, "staff-qingcheng");
  }
}
