package cn.qingchengfit.staffkit.views.course.msg;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.ShopConfigBody;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.model.responese.SignInConfig;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.CView;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MsgNotiPresenter extends BasePresenter {
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private MVPView view;
    private RestRepository restRepository;

    @Inject public MsgNotiPresenter(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    void editShopConfig(ShopConfigBody body) {
        RxRegiste(restRepository.getPost_api()
            .qcShopConfigs(loginStatus.staff_id(), gymWrapper.getParams(), body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onEditOk();
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

    void queryShopConfig(String key) {
        RxRegiste(restRepository.getGet_api()
            .qcGetShopConfig(loginStatus.staff_id(), key, gymWrapper.getParams())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<cn.qingchengfit.network.response.QcResponseData<SignInConfig.Data>>() {
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

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    public interface MVPView extends CView {
        void onShopConfigs(List<SignInConfig.Config> configs);

        void onEditOk();
    }
}
