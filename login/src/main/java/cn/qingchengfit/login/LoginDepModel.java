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




  @Provides ILoginModel provideIGymModel(GymWrapper gymWrapper,LoginStatus loginStatus,QcRestRepository qcrestRepository){
    return new DefaultLoginModel(gymWrapper, loginStatus, qcrestRepository);
  }


  @Provides IWXAPI provideWx(Application application) {
    return  WXAPIFactory.createWXAPI(application, "23423423432");
  }


}
