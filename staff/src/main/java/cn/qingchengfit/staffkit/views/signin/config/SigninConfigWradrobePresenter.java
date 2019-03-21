package cn.qingchengfit.staffkit.views.signin.config;

import android.util.Pair;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.SignInConfig;
import cn.qingchengfit.network.ActivityLifeCycleEvent;
import cn.qingchengfit.network.HttpUtil;
import cn.qingchengfit.network.ResultSubscribe;
import cn.qingchengfit.gym.gymconfig.network.response.ShopConfigBody;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.gym.gymconfig.ShopConfigs;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.subjects.PublishSubject;
import timber.log.Timber;

public class SigninConfigWradrobePresenter extends BasePresenter {
    PublishSubject<ActivityLifeCycleEvent> lifecycleSubject = PublishSubject.create();
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject SerPermisAction serPermisAction;
    private MVPView view;
    private StaffRespository restRepository;

    @Inject public SigninConfigWradrobePresenter(StaffRespository restRepository) {
        this.restRepository = restRepository;
    }

    public void getSignInConfig() {

        HashMap<String, Object> params = gymWrapper.getParams();
        //        params.put("keys", "user_checkin_with_locker");

        Observable observable =
            restRepository.getStaffAllApi().qcGetShopConfig(App.staffId, ShopConfigs.USER_CHECKIN_WITH_LOCKER, params);

        RxRegiste(HttpUtil.getInstance().toSubscribe(observable, new ResultSubscribe<SignInConfig.Data>() {
            @Override protected void _onNext(SignInConfig.Data signInConfig) {
                view.onGetSignInConfig(signInConfig.configs);
            }

            @Override protected void _onError(String message) {
                view.onShowError(message);
            }
        }));
    }

    public void getSignOutConfig() {

        HashMap<String, Object> params = gymWrapper.getParams();
        Observable observable =
            restRepository.getStaffAllApi().qcGetShopConfig(App.staffId, ShopConfigs.CHECK_OUT_WITH_RETURN_LOCKER, params);
        RxRegiste(HttpUtil.getInstance().toSubscribe(observable, new ResultSubscribe<SignInConfig.Data>() {
            @Override protected void _onNext(SignInConfig.Data signInConfig) {
                view.onGetSignOutConfig(signInConfig.configs);
            }

            @Override protected void _onError(String message) {
                Timber.e(message);
                view.onShowError(message);
            }
        }));
    }

    boolean checkPermission() {
        return serPermisAction.check(gymWrapper.id(), gymWrapper.model(), PermissionServerUtils.CHECKIN_LOCKER_LINK_CAN_CHANGE_NEW);
    }

    //public void putCheckinWithLocker(String id, boolean value) {
    //
    //    ShopConfigBody.Config config = new ShopConfigBody.Config();
    //    config.setId(id);
    //    config.setValue(value ? "1" : "0");
    //    List<ShopConfigBody.Config> configs = new ArrayList<>();
    //    configs.add(config);
    //    ShopConfigBody body = new ShopConfigBody();
    //    body.setConfigs(configs);
    //    Observable observable = restRepository.getStaffAllApi().qcShopConfigs(App.staffId, gymWrapper.getParams(), body);
    //    RxRegiste(HttpUtil.getInstance().toSubscribe(observable, new ResultSubscribe() {
    //        @Override protected void _onNext(Object o) {
    //            view.onCheckInConfigComplete();
    //        }
    //
    //        @Override protected void _onError(String message) {
    //            Timber.e(message);
    //            view.onShowError(message);
    //        }
    //    }, ActivityLifeCycleEvent.PAUSE, lifecycleSubject));
    //}

    public void putCheckoutWithReturnLocker(List<Pair<String,Boolean>> fix) {
        if (fix == null)
            return;
        List<ShopConfigBody.Config> configs = new ArrayList<>();
        for (Pair<String, Boolean> stringBooleanPair : fix) {
            ShopConfigBody.Config config = new ShopConfigBody.Config();
            config.setId(stringBooleanPair.first);
            config.setValue(stringBooleanPair.second?"1":"0");
            configs.add(config);
        }
        ShopConfigBody body = new ShopConfigBody();
        body.setConfigs(configs);
        Observable observable = restRepository.getStaffAllApi().qcShopConfigs(App.staffId, gymWrapper.getParams(), body);
        RxRegiste(HttpUtil.getInstance().toSubscribe(observable, new ResultSubscribe() {
            @Override protected void _onNext(Object o) {
                view.onCheckOutConfigComplete();
            }

            @Override protected void _onError(String message) {
                Timber.e(message);
                view.onShowError(message);
            }
        }, ActivityLifeCycleEvent.PAUSE, lifecycleSubject));
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    public interface MVPView extends CView {
        void onGetSignInConfig(List<SignInConfig.Config> signInConfig);

        void onGetSignOutConfig(List<SignInConfig.Config> signInConfig);

        void onCheckInConfigComplete();

        void onCheckOutConfigComplete();
    }
}
