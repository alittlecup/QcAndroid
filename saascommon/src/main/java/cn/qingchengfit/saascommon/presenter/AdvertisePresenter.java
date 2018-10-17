package cn.qingchengfit.saascommon.presenter;

import javax.inject.Inject;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saascommon.model.Advertisement;
import cn.qingchengfit.saascommon.network.SaasCommonApi;
import cn.qingchengfit.saascommon.network.response.AdvertiseWrap;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Bob Du on 2018/10/17 13:38
 */
public class AdvertisePresenter extends BasePresenter {
    @Inject
    QcRestRepository qcRestRepository;

    private AdvertiseView view;

    @Override
    public void attachView(PView v) {
        view = (AdvertiseView) v;
    }

    @Override
    public void unattachView() {
        super.unattachView();
        view = null;
    }

    @Inject
    public AdvertisePresenter() {}

//    public void queryAdvertise() {
//        RxRegiste(qcRestRepository.createGetApi(SaasCommonApi.class)
//                .qcGetAdvertise("staff").onBackpressureBuffer().subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Action1<QcDataResponse<AdvertiseWrap>>() {
//                    @Override public void call(QcDataResponse<AdvertiseWrap> qcResponse) {
//                        if (ResponseConstant.checkSuccess(qcResponse)) {
//                            view.onLoadAdvertise(qcResponse.data.advertisement);
//                        } else {
//                            view.onShowError(qcResponse.getMsg());
//                        }
//                    }
//                }, new NetWorkThrowable()));
//    }

    public interface AdvertiseView extends CView {
        void onLoadAdvertise(Advertisement advertisement);
    }

}

