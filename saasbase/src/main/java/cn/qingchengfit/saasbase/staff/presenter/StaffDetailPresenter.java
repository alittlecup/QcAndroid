package cn.qingchengfit.saasbase.staff.presenter;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.base.StaffPosition;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.staff.di.StaffSelectData;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.saasbase.staff.model.body.ManagerBody;
import cn.qingchengfit.subscribes.NetSubscribe;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/5/12 2016.
 */
public class StaffDetailPresenter extends BasePresenter {

  @Inject LoginStatus loginStatus;
  @Inject IStaffModel staffModel;
  @Inject StaffSelectData staffSelectData;
  private ManagerBody body = new ManagerBody();
  private MVPView view;

  @Inject public StaffDetailPresenter() {
  }

  @Override public void onStart() {
    super.onStart();
  }

  @Override public void onStop() {
    super.onStop();
  }

  @Override public void onPause() {
    super.onPause();
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
    if (staffSelectData.staff != null)
      view.onStaff(staffSelectData.staff);
  }

  public void setGender(int g){
    body.setGender(g);
  }

  @Override public void attachIncomingIntent(Intent intent) {
    super.attachIncomingIntent(intent);
  }

  @Override public void onCreate() {
    super.onCreate();
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public void editStaff() {
    int ret = body.checkDataInPos();
    if (ret > 0) {
      view.showAlert(ret);
    } else {
      RxRegiste(staffModel.editStaff(staffSelectData.staff.id ,body)
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new NetSubscribe<QcDataResponse>() {
          @Override public void onNext(QcDataResponse qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onShowError("修改成功！");
              view.popBack();
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }));
    }
  }

  public void addStaff() {
    int ret = body.checkDataInPos();
    if (ret > 0) {
      view.showAlert(ret);
    } else {
      RxRegiste(staffModel.addStaff(body)
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new NetSubscribe<QcDataResponse>() {
          @Override public void onNext(QcDataResponse qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onShowError("添加成功！");
              view.popBack();
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }));
    }
  }

  public void delStaff() {
    RxRegiste(staffModel.delStaff(staffSelectData.staff.id)
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse>() {
        @Override public void onNext(QcDataResponse qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            view.onShowError("添加成功！");
            view.popBack();
          } else {
            view.onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  public void queryPostions(String staffId) {

  }

  public interface MVPView extends CView {
    void onFixSuccess();

    void onAddSuccess();

    void onDelSuccess();
    void onStaff(Staff staff);
    void onFailed(String s);

    void onPositions(List<StaffPosition> positions);
  }
}
