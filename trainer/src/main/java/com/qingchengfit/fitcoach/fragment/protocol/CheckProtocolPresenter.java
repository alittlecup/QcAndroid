package com.qingchengfit.fitcoach.fragment.protocol;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import com.qingchengfit.fitcoach.http.QcCloudClient;
import java.util.HashMap;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/9/11.
 */

public class CheckProtocolPresenter extends BasePresenter {

  MVPView view;
  @Inject QcRestRepository qcRestRepository;

  @Inject public CheckProtocolPresenter() {
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    if (view != null) view = null;
  }

  public void getIsAgree(String phone) {
    HashMap<String, Object> params = new HashMap<>();
    params.put("phone", phone);
    RxRegiste(qcRestRepository.createRxJava1Api(QcCloudClient.GetApi.class)
        .qcCheckProtocol(params)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<CheckProtocolModel>>() {
          @Override
          public void call(QcDataResponse<CheckProtocolModel> checkProtocolModelQcDataResponse) {
            if (ResponseConstant.checkSuccess(checkProtocolModelQcDataResponse)) {
              if (view != null) {
                view.onCheck(checkProtocolModelQcDataResponse.data.has_read_agreement);
              }
            } else {
              view.showAlert(checkProtocolModelQcDataResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public interface MVPView {
    void onCheck(boolean isAgree);

    void showAlert(String s);
  }
}
