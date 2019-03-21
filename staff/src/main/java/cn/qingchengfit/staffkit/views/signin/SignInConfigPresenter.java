package cn.qingchengfit.staffkit.views.signin;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.SignInCostBody;
import cn.qingchengfit.model.responese.SignInCardCostBean;
import cn.qingchengfit.model.responese.SignInConfig;
import cn.qingchengfit.network.ActivityLifeCycleEvent;
import cn.qingchengfit.network.HttpUtil;
import cn.qingchengfit.network.ResultSubscribe;
import cn.qingchengfit.gym.gymconfig.network.response.ShopConfigBody;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.gym.gymconfig.ShopConfigs;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.subjects.PublishSubject;
import timber.log.Timber;

/**
 * Created by yangming on 16/8/29.
 */
public class SignInConfigPresenter extends BasePresenter {

    public SignInConfigView view;
    PublishSubject<ActivityLifeCycleEvent> lifecycleSubject = PublishSubject.create();
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private StaffRespository restRepository;

    @Inject public SignInConfigPresenter(StaffRespository restRepository) {
        this.restRepository = restRepository;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        this.view = (SignInConfigView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void confirm(List<SignInCostBody.CardCost> card_costs) {
        HashMap<String, Object> params = gymWrapper.getParams();
        SignInCostBody body = new SignInCostBody();
        body.setShop_id(gymWrapper.shop_id());
        body.setCard_costs(card_costs);
        Observable observable = restRepository.getStaffAllApi().qcPutSignInCostConfig(App.staffId, params, body);
        RxRegiste(HttpUtil.getInstance().toSubscribe(observable, new ResultSubscribe() {
            @Override protected void _onNext(Object o) {
                view.onCostConfigSuccess();
            }

            @Override protected void _onError(String message) {
                Timber.e(message);
                view.onFail();
            }
        }, ActivityLifeCycleEvent.PAUSE, lifecycleSubject));
    }

    public void putCheckinWithLocker(String id, boolean value) {

        ShopConfigBody.Config config = new ShopConfigBody.Config();
        config.setId(id);
        config.setValue(value ? "1" : "0");
        List<ShopConfigBody.Config> configs = new ArrayList<>();
        configs.add(config);
        ShopConfigBody body = new ShopConfigBody();
        body.setConfigs(configs);
        Observable observable = restRepository.getStaffAllApi().qcShopConfigs(App.staffId, gymWrapper.getParams(), body);
        RxRegiste(HttpUtil.getInstance().toSubscribe(observable, new ResultSubscribe() {
            @Override protected void _onNext(Object o) {
                view.onCheckInConfigComplete();
            }

            @Override protected void _onError(String message) {
                Timber.e(message);
                view.onFail();
            }
        }, ActivityLifeCycleEvent.PAUSE, lifecycleSubject));
        //        RxRegiste(restRepository.getStaffAllApi().qcShopConfigs(App.staffId, coachService.getId(), coachService.getModel(), body)
      //                .onBackpressureBuffer().subscribeOn(Schedulers.io())
        //                .observeOn(AndroidSchedulers.mainThread())
        //                .subscribe(new Action1<QcResponse>() {
        //                    @Override
        //                    public void call(QcResponse qcResponse) {
        //                        view.onCheckInConfigComplete();
        //                    }
        //                }, new Action1<Throwable>() {
        //                    @Override
        //                    public void call(Throwable throwable) {
        //                        Timber.e(throwable.getMessage());
        //                        view.onFail();
        //                    }
        //                })
        //        );

    }

    public void putCheckoutWithReturnLocker(String id, final boolean value) {
        ShopConfigBody.Config config = new ShopConfigBody.Config();
        config.setId(id);
        config.setValue(value ? "1" : "0");
        List<ShopConfigBody.Config> configs = new ArrayList<>();
        configs.add(config);
        ShopConfigBody body = new ShopConfigBody();
        body.setConfigs(configs);

        Observable observable = restRepository.getStaffAllApi().qcShopConfigs(App.staffId, gymWrapper.getParams(), body);
        RxRegiste(HttpUtil.getInstance().toSubscribe(observable, new ResultSubscribe() {
            @Override protected void _onNext(Object o) {
                view.onCheckOutConfigComplete();
            }

            @Override protected void _onError(String message) {
                Timber.e(message);
                view.onFail();
            }
        }, ActivityLifeCycleEvent.PAUSE, lifecycleSubject));

        //                RxRegiste(restRepository.getStaffAllApi().qcShopConfigs(App.staffId, coachService.getId(), coachService.getModel(), body)
      //                .onBackpressureBuffer().subscribeOn(Schedulers.io())
        //                .observeOn(AndroidSchedulers.mainThread())
        //                .subscribe(new Action1<QcResponse>() {
        //                    @Override
        //                    public void call(QcResponse qcResponse) {
        //                        view.onCheckOutConfigComplete();
        //                    }
        //                }, new Action1<Throwable>() {
        //                    @Override
        //                    public void call(Throwable throwable) {
        //                        Timber.e(throwable.getMessage());
        //                        view.onFail();
        //                    }
        //                })
        //        );
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
                Timber.e(message);
                view.onFail();
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
                view.onFail();
            }
        }));


    }

    public void getCardCostList() {
        HashMap<String, Object> params = gymWrapper.getParams();

        Observable observable = restRepository.getStaffAllApi().qcGetSignInCostConfig(App.staffId, params);
        RxRegiste(HttpUtil.getInstance().toSubscribe(observable, new ResultSubscribe<SignInCardCostBean.Data>() {
            @Override protected void _onNext(SignInCardCostBean.Data signInCardCostBean) {
                view.onGetCostList(signInCardCostBean.card_costs);
            }

            @Override protected void _onError(String message) {
                Timber.e(message);
                view.onFail();
            }
        }));

    }

    public interface SignInConfigView extends PView {

        void onGetSignInConfig(List<SignInConfig.Config> signInConfig);

        void onGetSignOutConfig(List<SignInConfig.Config> signInConfig);

        void onGetCostList(List<SignInCardCostBean.CardCost> signInConfigs);

        void onCheckInConfigComplete();

        void onCheckOutConfigComplete();

        void onCostConfigSuccess();

        void onFail();
    }
}
