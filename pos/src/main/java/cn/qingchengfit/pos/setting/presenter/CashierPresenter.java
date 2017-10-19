package cn.qingchengfit.pos.setting.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.pos.cashier.model.Cashier;
import cn.qingchengfit.pos.cashier.model.CashierWrapper;
import cn.qingchengfit.pos.net.PosApi;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/10/18.
 */

public class CashierPresenter extends BasePresenter {

  private MVPView view;
  @Inject QcRestRepository qcRestRepository;
  @Inject GymWrapper gymWrapper;

  @Inject public CashierPresenter() {
  }

  @Override public void attachView(PView v) {
    this.view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    if (view != null) {
      view = null;
    }
  }

  public void qcGetCashier() {
    RxRegiste(qcRestRepository.createGetApi(PosApi.class)
        .qcGetCashier()
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<CashierWrapper>>() {
          @Override public void call(QcDataResponse<CashierWrapper> cashierWrapperQcDataResponse) {
            if (cashierWrapperQcDataResponse.getStatus() == ResponseConstant.SUCCESS) {
              if (view != null) {
                view.onGetCashier(cashierWrapperQcDataResponse.data.cashiers);
              } else {
                view.onShowError(cashierWrapperQcDataResponse.getMsg());
              }
            }
          }
        }, new NetWorkThrowable()));
  }

  public void onAddCashier(String username, String phone, int gender) {
    HashMap<String, Object> params = new HashMap<>();
    params.put("username", username);
    params.put("phone", phone);
    params.put("gender", gender);
    params.put("gym_id", gymWrapper.id());
    RxRegiste(qcRestRepository.createPostApi(PosApi.class)
        .qcAddCashier(params)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse>() {
          @Override public void call(QcDataResponse qcDataResponse) {
            if (ResponseConstant.checkSuccess(qcDataResponse)) {
              view.onAddSuccess();
            } else {
              view.onShowError(qcDataResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public void onDelete(String cashierId, String gym_id) {
    HashMap<String, Object> params = new HashMap<>();
    params.put("gym_id", gymWrapper.id());
    RxRegiste(qcRestRepository.createPostApi(PosApi.class)
        .qcDeleteCashier(cashierId, params)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse>() {
          @Override public void call(QcDataResponse qcDataResponse) {
            if (ResponseConstant.checkSuccess(qcDataResponse)) {
              view.onDeleteSuccess();
            } else {
              view.onShowError(qcDataResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public void onModifyCashier(String cashierId, String username, String phone, int gender) {
    HashMap<String, Object> params = new HashMap<>();
    params.put("username", username);
    params.put("phone", phone);
    params.put("gender", gender);
    RxRegiste(qcRestRepository.createPostApi(PosApi.class)
        .qcModifyCashier(cashierId, params)
        .onBackpressureBuffer()
        .subscribe(new Action1<QcDataResponse>() {
          @Override public void call(QcDataResponse qcDataResponse) {
            if (ResponseConstant.checkSuccess(qcDataResponse)) {
              view.onModifySuccess();
            } else {
              view.onShowError(qcDataResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public interface MVPView extends CView {
    void onAddSuccess();

    void onDeleteSuccess();

    void onModifySuccess();

    void onGetCashier(List<Cashier> cashierList);
  }
}
