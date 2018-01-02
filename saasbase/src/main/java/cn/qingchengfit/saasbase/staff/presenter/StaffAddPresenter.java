package cn.qingchengfit.saasbase.staff.presenter;

import android.view.View;
import android.widget.AdapterView;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.model.base.StaffPosition;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.staff.beans.Invitation;
import cn.qingchengfit.saasbase.staff.beans.body.InvitationBody;
import cn.qingchengfit.saasbase.staff.beans.response.InvitationWrap;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.saasbase.staff.model.PostionListWrap;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.utils.ListUtils;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class StaffAddPresenter extends BasePresenter<StaffAddPresenter.MVPView> {

  @Inject IStaffModel staffModel;
  private InvitationBody body = new InvitationBody();
  private List<StaffPosition> positions;

  @Inject public StaffAddPresenter() {
  }

  public InvitationBody getBody() {
    return body;
  }

  public void setGender(int gender) {
    body.setGender(gender);
  }

  public void addStaff() {
    body.setUsername(mvpView.getName());
    body.setPhone(mvpView.getPhone());
    body.setArea_code(mvpView.getPhoneDisrct());
    RxRegiste(staffModel.inviteStaff(body)
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<InvitationWrap>>() {
        @Override public void onNext(QcDataResponse<InvitationWrap> qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            mvpView.onGetLink(qcResponse.getData().invitation);
          } else {
            mvpView.onShowError(qcResponse.getMsg());
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
            mvpView.onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  public void choosePosition() {
    //if (staff != null &&staff.is_coach && !staff.is_staff ){
    //  //只是教练的话 不能修改
    //  mvpView.showAlert("教练身份无法被修改");
    //}else {
    if (positions != null && mvpView != null) {
      mvpView.showSelectSheet(null, ListUtils.ListObj2Str(positions),
        new AdapterView.OnItemClickListener() {
          @Override public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            if (i < positions.size()) {
              body.setPosition_id(positions.get(i).id);
              mvpView.onPosition(positions.get(i).getName());
            }
          }
        });
    }
    //}
  }

  public interface MVPView extends CView {
    void onPosition(String position);

    String getName();

    String getPhone();

    String getPhoneDisrct();

    void onGetLink(Invitation url);
  }
}
