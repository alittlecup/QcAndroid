package cn.qingchengfit.inject.moudle;

import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.model.db.QCDbManager;
import cn.qingchengfit.staffkit.repository.SerPermissionImpl;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.rest.RestRepositoryV2;
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
    private App app;
    private SerPermissionImpl serPermission;
    private LoginStatus loginStatus;
    private GymWrapper gymWrapper;
    private RestRepository restRepository;

    public AppModel() {
    }

    public AppModel(App app, LoginStatus loginStatus, GymWrapper gymWrapper, RestRepository restRepository) {
        this.app = app;
        this.loginStatus = loginStatus;
        this.gymWrapper = gymWrapper;
        this.restRepository = restRepository;
    }

    @Provides App provideApplicationContext() {
        return app;
    }

    @Provides RestRepository provoideRepository() {
        return restRepository;
    }

    @Provides RestRepositoryV2 provoideRepositoryT() {
        return new RestRepositoryV2();
    }

    @Provides QCDbManager provoideQcDbManager() {
        return new QCDbManager(app);
    }

    @Provides SerPermissionImpl provoidePermission(SerPermissionImpl serPermission) {
        return serPermission;
    }

    @Provides LoginStatus providerLoginStatus() {
        return loginStatus;
    }

    @Provides GymWrapper provideGym() {
        return gymWrapper;
    }
}
