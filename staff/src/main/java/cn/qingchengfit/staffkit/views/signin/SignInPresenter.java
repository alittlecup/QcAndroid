package cn.qingchengfit.staffkit.views.signin;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.SignInConfig;
import cn.qingchengfit.model.responese.SignInUrl;
import cn.qingchengfit.network.HttpUtil;
import cn.qingchengfit.network.ResultSubscribe;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.ShopConfigs;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by yangming on 16/9/18.
 */
public class SignInPresenter extends BasePresenter {

    public SignInView view;
    @Inject StaffRespository restRepository;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;

    @Inject public SignInPresenter() {

    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        this.view = (SignInView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void getSignInConfig() {

        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("keys", "user_checkin_with_locker");

        Observable observable = restRepository.getStaffAllApi().qcGetShopConfig(App.staffId, ShopConfigs.USER_CHECKIN_WITH_LOCKER, params);
        HttpUtil.getInstance().toSubscribe(observable, new ResultSubscribe<SignInConfig.Data>() {
            @Override protected void _onNext(SignInConfig.Data signInConfig) {
                if (view != null)
                    view.onGetSignInConfig(signInConfig.configs);
            }

            @Override protected void _onError(String message) {
                Timber.e(message);
                if (view != null)
                    view.onFail();
            }
        });
    }

    public void getSigninUrl() {
        HashMap<String, Object> params = gymWrapper.getParams();
        RxRegiste(restRepository.getStaffAllApi()
            .qcGetCheckinUrl(App.staffId, params)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<SignInUrl>() {
                @Override public void call(SignInUrl signInUrl) {
                    if (view != null) view.onGetSignInUrl(signInUrl);
                }
            }, new NetWorkThrowable()));
    }

    public interface SignInView extends PView {

        void onGetSignInConfig(List<SignInConfig.Config> signInConfig);

        void onGetSignInUrl(SignInUrl signInUrl);

        void onFail();
    }
}