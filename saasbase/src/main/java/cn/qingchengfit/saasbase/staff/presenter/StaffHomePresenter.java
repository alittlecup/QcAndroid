package cn.qingchengfit.saasbase.staff.presenter;

import android.os.Bundle;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.model.common.ICommonUser;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.staff.beans.Invitation;
import cn.qingchengfit.saasbase.staff.beans.response.InvitationListWrap;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.saasbase.staff.model.StaffShip;
import cn.qingchengfit.saasbase.staff.views.SuParams;
import cn.qingchengfit.subscribes.NetSubscribe;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class StaffHomePresenter extends BasePresenter<StaffHomePresenter.MVPView> {

  private List<StaffShip> staffShips = new ArrayList<>();
  private List<StaffShip> leavestaffShips = new ArrayList<>();
  private List<Invitation> invitations = new ArrayList<>();
  private StaffShip su;

  @Inject IStaffModel staffModel;

  @Inject public StaffHomePresenter() {

  }

  public void queryData() {
    RxRegiste(Observable.zip(staffModel.getStaffList(), staffModel.getLeaveStaffList(),
      (response, response2) -> {
        List<StaffShip> staffShips = new ArrayList<>();
        if (ResponseConstant.checkSuccess(response)) {
          if (response.data.staffships != null) {

            staffShips.addAll(response.data.staffships);
          }
        } else {
          throw new RuntimeException(response.getMsg());
        }
        if (ResponseConstant.checkSuccess(response2)) {
          if (response2.data.staffships != null) {
            staffShips.addAll(response2.data.staffships);
          }
        } else {
          throw new RuntimeException(response2.getMsg());
        }
        return staffShips;
      })
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<List<StaffShip>>() {
        @Override public void onNext(List<StaffShip> ships) {
          staffShips.clear();
          leavestaffShips.clear();
          if (ships != null) {
            for (StaffShip staffship : ships) {
              if (staffship.is_superuser) {
                su = staffship;
              } else if (staffship.staff_enable) {
                staffShips.add(staffship);
              } else {
                leavestaffShips.add(staffship);
              }
            }
          }
          mvpView.onStafflistDone();
          if (su != null) mvpView.onSu(su);
        }
      }));

    RxRegiste(staffModel.getInvitedStaffList()
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<InvitationListWrap>>() {
        @Override public void onNext(QcDataResponse<InvitationListWrap> qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            invitations.clear();
            List<Invitation> datas = qcResponse.data.invitations;
            if (datas != null) {
              invitations.addAll(datas);
            }
            mvpView.onInvitionslistDone();
          } else {
            mvpView.onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  public void isSu() {
    RxRegiste(staffModel.isSelfSu()
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<JsonObject>>() {
        @Override public void onNext(QcDataResponse<JsonObject> qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            if (qcResponse.data.getAsJsonObject("ship").get("is_superuser").getAsBoolean()){
              mvpView.routeTo("/su/", SuParams.builder().mStaff(getSu()).build());
            }else {
              mvpView.showAlert("超级管理员仅本人可以修改");
            }
          } else{
            mvpView.onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  public List<StaffShip> getStaffShips() {
    return staffShips;
  }

  public List<StaffShip> getLeaveStaffShips() {
    return leavestaffShips;
  }

  public List<Invitation> getInvitations() {
    return invitations;
  }

  public List<? extends ICommonUser> getAllCommonUser() {
    List<ICommonUser> list = new ArrayList<>();
    list.addAll(staffShips);
    list.addAll(leavestaffShips);
    list.addAll(invitations);
    return list;
  }

  public StaffShip getSu() {
    return su;
  }

  public interface MVPView extends CView {
    void onStafflistDone();

    void onInvitionslistDone();

    void onSu(StaffShip staff);
    void routeTo(String path,Bundle bd);
  }
}
