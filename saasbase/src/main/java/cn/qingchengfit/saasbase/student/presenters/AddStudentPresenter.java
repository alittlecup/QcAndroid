package cn.qingchengfit.saasbase.student.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.repository.SaasModel;
import cn.qingchengfit.saasbase.student.network.body.EditStudentBody;
import cn.qingchengfit.saasbase.student.utils.StudentBusinessUtils;
import cn.qingchengfit.utils.CmStringUtils;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class AddStudentPresenter extends BasePresenter {
  @Inject GymWrapper gymWrapper;
  @Inject SaasModel saasModel;
  EditStudentBody body = new EditStudentBody.Builder().build();
  private MVPView view;

  @Inject public AddStudentPresenter() {
  }


  public void setName(String name){
    body.username = name;
  }

  public void setGender(int gender){
    body.gender = gender;
  }

  public void setPhone(String phone){
    body.phone = phone;
  }

  public void setBirthDay(String birthDay){
    body.date_of_birth = birthDay;
  }

  public void setAddress(String address){
    body.address = address;
  }

  public void setRemards(String remarks){
    body.remarks = remarks;
  }

  public void setCoachIds(List<Staff> coaches){
    body.coach_ids = StudentBusinessUtils.getIdStrFromStaffs(coaches);
  }

  public void setSallersIds(List<Staff> sallersIds){
    body.seller_ids = StudentBusinessUtils.getIdStrFromStaffs(sallersIds);
  }

  public void setComeFrom(String comefrom){
    body.origin = comefrom;
  }

  public void setRecomends(String id){
    body.recommend_by_id = id;
  }


  /**
   * 新增学员
   */
  public void addStudent() {
    if (!checkBody())
      return;
    view.showLoading();
    RxRegiste(saasModel.qcCreateStudent(body)
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse>() {
          @Override public void call(QcDataResponse qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.hideLoading();
              view.onSaveOK();
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public boolean checkBody(){
    if (CmStringUtils.isEmpty(body.username)){
      view.showAlert("请填写会员姓名");
      return false;
    }

    if (CmStringUtils.isEmpty(body.phone)){
      view.showAlert("请填写用户手机号");
      return false;
    }

    return true;

  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public interface MVPView extends CView {
    void onSaveOK();
  }
}
