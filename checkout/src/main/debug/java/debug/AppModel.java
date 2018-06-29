package debug;

import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saasbase.routers.SaasbaseRouterCenter;
import cn.qingchengfit.saasbase.routers.billImpl;
import cn.qingchengfit.saasbase.routers.commonImpl;
import cn.qingchengfit.saasbase.routers.courseImpl;
import cn.qingchengfit.saasbase.routers.exportImpl;
import cn.qingchengfit.saasbase.routers.gymImpl;
import cn.qingchengfit.saasbase.routers.staffImpl;
import cn.qingchengfit.saasbase.routers.userImpl;
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
@Module public class AppModel {

   @Provides SaasbaseRouterCenter provideRc() {
    return new SaasbaseRouterCenter().registe(new exportImpl())
        .registe(new gymImpl())
        .registe(new staffImpl())
        .registe(new commonImpl())
        .registe(new courseImpl())
        .registe(new userImpl())
        .registe(new billImpl());
  }
   @Provides LoginStatus provideLogins() {
    return new LoginStatus.Builder().build();
  }
}
