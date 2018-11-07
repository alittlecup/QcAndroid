package cn.qingchengfit.staffkit.views.wardrobe.edit;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.EditWardrobeBody;
import cn.qingchengfit.model.responese.Locker;
import cn.qingchengfit.model.responese.LockerWrapper;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class WardrobeEditPresenter extends BasePresenter {
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  private MVPView view;
  private StaffRespository restRepository;

  @Inject public WardrobeEditPresenter(StaffRespository restRepository) {
    this.restRepository = restRepository;
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  public void completeWardrobe(String staffid, Long lockerid, EditWardrobeBody body) {
    RxRegiste(restRepository.getStaffAllApi()
        .qcEditLocker(staffid, lockerid + "", gymWrapper.getParams(), body)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<LockerWrapper>>() {
          @Override public void call(QcDataResponse<LockerWrapper> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onSaveOk(qcResponse.data.locker);
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

  public void delWardrobe(String staffid, Long lockerid) {
    RxRegiste(restRepository.getStaffAllApi()
        .qcDelLocker(staffid, lockerid + "", gymWrapper.getParams())
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onDelOk();
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

  public interface MVPView extends CView {
    void onSaveOk(Locker locker);

    void onDelOk();
  }
}
