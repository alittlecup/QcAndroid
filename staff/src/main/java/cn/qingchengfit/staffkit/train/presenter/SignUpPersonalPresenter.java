package cn.qingchengfit.staffkit.train.presenter;

import android.text.TextUtils;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.train.model.SignRecord;
import java.util.HashMap;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/3/22.
 */

public class SignUpPersonalPresenter extends BasePresenter {

    @Inject RestRepository restRepository;
    private SignUpView signUpView;
    private int page, totalpage;

    @Inject public SignUpPersonalPresenter() {
    }

    @Override public void attachView(PView v) {
        signUpView = (SignUpView) v;
    }

    @Override public void unattachView() {
        super.unattachView();
        signUpView = null;
    }

    public void initPage() {
        page = 1;
        totalpage = 2;
    }

    public void queryData(String keyword, String gym_id, String competition_id) {

        if (page > totalpage) {
            signUpView.onFailed("没有更多数据");
            return;
        }

        HashMap<String, Object> params = new HashMap<>();
        params.put("gym_id", gym_id);
        params.put("competition_id", competition_id);
        RxRegiste(restRepository.getGet_api()
            .qcGetSignPersonal(params, keyword).onBackpressureBuffer().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<cn.qingchengfit.network.response.QcResponseData<SignRecord>>() {
                @Override public void call(cn.qingchengfit.network.response.QcResponseData<SignRecord> signRecord) {
                    totalpage = signRecord.data.pages;
                    if (ResponseConstant.checkSuccess(signRecord)) {
                        if (signUpView != null) {
                            signUpView.onGetSignUpDataSuccess(signRecord.data);
                        }
                    } else {
                        signUpView.onFailed(signRecord.getMsg());
                    }
                    page++;
                }
            }, new NetWorkThrowable()));
    }
}
