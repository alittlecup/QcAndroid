package cn.qingchengfit.staffkit.train.presenter;

import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.network.response.QcResponseData;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.NetWorkThrowable;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.train.model.SignUpDetailResponse;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/3/22.
 */

public class SignUpDetailPresenter extends BasePresenter {

    @Inject RestRepository restRepository;
    private SignUpView signUpView;

    @Inject public SignUpDetailPresenter() {
    }

    @Override public void attachView(PView v) {
        signUpView = (SignUpView) v;
    }

    @Override public void unattachView() {
        super.unattachView();
        signUpView = null;
    }

    public void querySignDetail(String orderId) {
        RxRegiste(restRepository.getGet_api()
            .qcGetSignDetail(orderId)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<SignUpDetailResponse>>() {
                @Override public void call(cn.qingchengfit.network.response.QcResponseData<SignUpDetailResponse> signDetail) {
                    if (ResponseConstant.checkSuccess(signDetail)) {
                        if (signUpView != null) {
                            signUpView.onGetSignUpDataSuccess(signDetail.data);
                        }
                    } else {
                        signUpView.onFailed(signDetail.getMsg());
                    }
                }
            }, new NetWorkThrowable()));
    }
}
