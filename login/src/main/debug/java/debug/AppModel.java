package debug;

import android.app.Application;
import android.arch.lifecycle.ViewModelProvider;
import android.content.Context;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.login.ILoginModel;
import cn.qingchengfit.login.bean.CheckCodeBody;
import cn.qingchengfit.login.bean.GetCodeBody;
import cn.qingchengfit.login.bean.Login;
import cn.qingchengfit.login.bean.LoginBody;
import cn.qingchengfit.login.bean.RegisteBody;
import cn.qingchengfit.login.views.CheckProtocolModel;
import cn.qingchengfit.login.views.LoginView;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.router.BaseRouter;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.saascommon.mvvm.ViewModelFactory;

import com.google.gson.JsonObject;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import java.util.HashMap;
import javax.inject.Singleton;
import rx.Observable;

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

  LoginStatus loginStatus =new LoginStatus.Builder()
      .build();
  GymWrapper gymWrapper = new GymWrapper();


  @Binds abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);




  @Provides static Application providesApplication() {
    return MyApp.INSTANCE;
  }



  @Provides static QcRestRepository provideQcRestRepository(Application application) {
    return new QcRestRepository(application, Configs.Server, "staff-qingcheng");
  }


  @Provides  static LoginStatus provideLoginstats(){
    return loginStatus;
  }
  @Provides static GymWrapper provideGym(){
    return gymWrapper;
  }

}

