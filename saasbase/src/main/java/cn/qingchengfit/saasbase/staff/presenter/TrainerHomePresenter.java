package cn.qingchengfit.saasbase.staff.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.common.ICommonUser;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.staff.beans.Invitation;
import cn.qingchengfit.saasbase.staff.beans.response.InvitationListWrap;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.saasbase.staff.model.StaffShip;
import cn.qingchengfit.subscribes.NetSubscribe;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TrainerHomePresenter extends BasePresenter<TrainerHomePresenter.MVPView> {

  private List<StaffShip> staffShips = new ArrayList<>();
  private List<StaffShip> leavestaffShips = new ArrayList<>();
  private List<Invitation> invitations = new ArrayList<>();

  @Inject IStaffModel staffModel;

  @Inject public TrainerHomePresenter() {

  }

  public void queryData() {
    RxRegiste(Observable.zip(staffModel.getTrainers(), staffModel.getLeaveTrainers(), (t1, t2) -> {
      List<Staff> ret = new ArrayList<>();
      if (ResponseConstant.checkSuccess(t1)) {
        if (t1.data.staffships != null){
          staffShips.clear();
          staffShips.addAll(t1.data.staffships);
        }
      } else {
        throw new RuntimeException(t1.getMsg());
      }
      if (ResponseConstant.checkSuccess(t2)) {
        leavestaffShips.clear();
        leavestaffShips.addAll(t2.data.staffships);
      } else {
        throw new RuntimeException(t2.getMsg());
      }
      return ret;
    })
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<List<Staff>>() {
        @Override public void onNext(List<Staff> coaches) {
          mvpView.onStafflistDone();
        }
      }));


    RxRegiste(staffModel.getInvitedTrainers()
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

  public interface MVPView extends CView {
    void onStafflistDone();

    void onInvitionslistDone();
  }
}
