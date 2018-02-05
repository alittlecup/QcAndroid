package cn.qingchengfit.recruit.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.network.GetApi;
import cn.qingchengfit.recruit.network.response.OnePermissionWrap;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/7/14.
 */

public class ResumePermissionPresenter extends BasePresenter {

  @Inject QcRestRepository restRepository;
  private MVPView view;

  @Inject public ResumePermissionPresenter() {
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public void queryChangeStatePermission(String gymId, String key){

    RxRegiste(restRepository.createGetApi(GetApi.class)
        .queryOnepermission(gymId, key).onBackpressureBuffer().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<OnePermissionWrap>>() {
          @Override public void call(QcDataResponse<OnePermissionWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              if (qcResponse.data.has_permission) {
                view.onCheckSuccess();
              } else {
                view.showAlert(R.string.alert_permission_forbid);
              }
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));

  }

  public interface MVPView extends CView {
    void onCheckSuccess();
  }

}
