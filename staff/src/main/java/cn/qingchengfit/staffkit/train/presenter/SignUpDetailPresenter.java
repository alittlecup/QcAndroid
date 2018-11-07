package cn.qingchengfit.staffkit.train.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.train.model.SignUpDetailResponse;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/3/22.
 */

public class SignUpDetailPresenter extends BasePresenter {

    @Inject StaffRespository restRepository;
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
        RxRegiste(restRepository.getStaffAllApi()
            .qcGetSignDetail(orderId).onBackpressureBuffer().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<SignUpDetailResponse>>() {
                @Override public void call(QcDataResponse<SignUpDetailResponse> signDetail) {
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
