package cn.qingchengfit.saasbase.staff.presenter;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.StaffPosition;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saascommon.events.EventSaasFresh;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.saasbase.staff.model.PostionListWrap;
import cn.qingchengfit.saasbase.staff.model.StaffShip;
import cn.qingchengfit.saasbase.staff.model.body.ManagerBody;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.utils.ListUtils;
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
  private ManagerBody body = new ManagerBody();
  private MVPView view;
  private List<StaffPosition> positions;
  private StaffShip staffShip;

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

  public void setStaff(StaffShip staff) {
    this.staffShip = staff;
    view.onStaff(staff);
  }

  public ManagerBody getBody() {
    return body;
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  public void setGender(int g) {
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

  public void setAvatar(String avatar){
    body.setAvatar(avatar);
  }

  public void editStaff() {
    body.setUsername(view.getName());
    body.setPhone(view.getPhone());
    body.setArea_code(view.getAreaCode());
    int ret = body.checkDataInPos();
    if (ret > 0) {
      view.showAlert(ret);
    } else {
      RxRegiste(staffModel.editStaff(staffShip.id, body)
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new NetSubscribe<QcDataResponse>() {
          @Override public void onNext(QcDataResponse qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              RxBus.getBus().post(new EventSaasFresh.StaffList());
              view.onShowError("修改成功！");
              view.popBack();
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }));
    }
  }

  public void resumeStaff() {
    ManagerBody resumeBody = new ManagerBody();
    resumeBody.staff_enable = true;
    RxRegiste(staffModel.editStaff(staffShip.id, resumeBody)
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse>() {
        @Override public void onNext(QcDataResponse qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            RxBus.getBus().post(new EventSaasFresh.StaffList());
            view.onShowError("复职成功！");
            view.popBack();
          } else {
            view.onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  public void addStaff() {
    body.setUsername(view.getName());
    body.setPhone(view.getPhone());
    body.setArea_code("+86");
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
    RxRegiste(staffModel.delStaff(staffShip.id)
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse>() {
        @Override public void onNext(QcDataResponse qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            view.onShowError("离职成功！");
            view.popBack();
          } else {
            view.onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  public void queryPositions() {
    RxRegiste(staffModel.getPositions()
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<PostionListWrap>>() {
        @Override public void onNext(QcDataResponse<PostionListWrap> qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            positions = qcResponse.data.positions;
          } else {
            view.onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  public void choosePosition() {
    if (staffShip != null && staffShip.is_coach && !staffShip.is_staff) {
      //只是教练的话 不能修改
      view.showAlert("教练身份无法被修改");
    } else {
      if (positions != null && view != null) {
        view.showSelectSheet(null, ListUtils.ListObj2Str(positions),
          new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
              if (i < positions.size()) {
                body.setPosition_id(positions.get(i).id);
                StaffDetailPresenter.this.view.onPosition(positions.get(i).getName());
              }
            }
          });
      }
    }
  }

  public interface MVPView extends CView {
    void onFixSuccess();

    void onAddSuccess();

    void onDelSuccess();

    void onStaff(StaffShip staff);

    void onFailed(String s);

    void onPosition(String positon);

    String getName();

    String getPhone();
    String getAreaCode();
  }
}
