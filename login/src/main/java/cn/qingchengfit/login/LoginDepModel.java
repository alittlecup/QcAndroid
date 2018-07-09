package cn.qingchengfit.login;

import android.app.Application;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.router.BaseRouter;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import dagger.Module;
import dagger.Provides;

@Module public class LoginDepModel {
  LoginStatus loginStatus =new LoginStatus.Builder()
    .build();

  DefaultLoginModel defaultLoginModel;
  GymWrapper gymWrapper = new GymWrapper();
  public LoginDepModel(Application app,BaseRouter router,
    QcRestRepository qcrestRepository){
    api = WXAPIFactory.createWXAPI(app, "23423423432");
    defaultLoginModel = new DefaultLoginModel(gymWrapper, loginStatus, qcrestRepository);
  }

  @Provides LoginStatus provideLoginstats(){
    return loginStatus;
  }
  @Provides GymWrapper provideGym(){
    return gymWrapper;
  }

  @Provides ILoginModel provideIGymModel(){
    return defaultLoginModel;
  }

  private IWXAPI api;

  @Provides IWXAPI provideWx() {
    return api;
  }


}
