package cn.qingchengfit.staffkit.views.gym.config;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.ShopConfigBody;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.model.responese.SignInConfig;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.network.response.QcResponseData;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.CView;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class GymConfigPresenter extends BasePresenter {
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private MVPView view;
    private RestRepository restRepository;

    @Inject public GymConfigPresenter(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    void queryShopConfig(String staffid, String key) {
        RxRegiste(restRepository.getGet_api()
            .qcGetShopConfig(staffid, key, gymWrapper.getParams())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<SignInConfig.Data>>() {
                @Override public void call(cn.qingchengfit.network.response.QcResponseData<SignInConfig.Data> qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onShopConfigs(qcResponse.data.configs);
                    } else {
                        view.onShowError(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onShowError(throwable.getMessage());
                }
            }));
    }

    void editShopConfig(String staffid, ShopConfigBody body) {
        RxRegiste(restRepository.getPost_api()
            .qcShopConfigs(staffid, gymWrapper.getParams(), body)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {

                    } else {
                        view.onShowError(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onShowError(throwable.getMessage());
                }
            }));
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    public interface MVPView extends CView {
        void onShopConfigs(List<SignInConfig.Config> configs);

        void onEditOk();
    }
}
