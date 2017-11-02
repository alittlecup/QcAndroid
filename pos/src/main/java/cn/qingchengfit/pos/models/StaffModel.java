package cn.qingchengfit.pos.models;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.pos.cashier.model.CashierWrap;
import cn.qingchengfit.pos.net.StaffApi;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.saasbase.staff.model.PostionListWrap;
import cn.qingchengfit.saasbase.staff.model.body.ManagerBody;
import cn.qingchengfit.saasbase.staff.network.response.SalerListWrap;
import cn.qingchengfit.saasbase.staff.network.response.UserWrap;
import rx.Observable;
import rx.functions.Func1;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/9/25.
 */

public class StaffModel implements IStaffModel {

  QcRestRepository repository;
  GymWrapper gymWrapper;
  LoginStatus loginStatus;
  StaffApi staffApi;

  public StaffModel(QcRestRepository repository, GymWrapper gymWrapper, LoginStatus loginStatus) {
    this.repository = repository;
    this.gymWrapper = gymWrapper;
    this.loginStatus = loginStatus;
    staffApi = repository.createGetApi(StaffApi.class);
  }

  @Override public Observable<QcDataResponse<UserWrap>> getCurUser() {
    return staffApi.getCurrentUser(gymWrapper.getParams()).flatMap(new Func1<QcDataResponse<CashierWrap>, Observable<QcDataResponse<UserWrap>>>() {
      @Override public Observable<QcDataResponse<UserWrap>> call(
        QcDataResponse<CashierWrap> rsp) {
        QcDataResponse<UserWrap> response = new QcDataResponse<>();
        response.setMsg(rsp.msg);
        response.setStatus(rsp.status);
        UserWrap userWrap = new UserWrap();
        userWrap.staff_id = rsp.data.cashier.id;
        userWrap.user = rsp.data.cashier.user;
        response.setData(userWrap);
        return Observable.just(response);
      }


    });
  }

  @Override public Observable<QcDataResponse<SalerListWrap>> getSalers() {
    return staffApi.qcGetSalers(gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse<SalerListWrap>> getStaffList(String id) {
    return staffApi.getStaffs(loginStatus.staff_id(),gymWrapper.getParams(),"");
  }

  @Override public Observable<QcDataResponse> addStaff(ManagerBody body) {
    return staffApi.addManager(gymWrapper.getParams(),body);
  }

  @Override public Observable<QcDataResponse> delStaff(String id) {
    return staffApi.delManager(id,gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse> editStaff(String id, ManagerBody body) {
    return staffApi.updateManager(id,gymWrapper.getParams(),body);
  }

  @Override public Observable<QcDataResponse<PostionListWrap>> getPositions() {
    return staffApi.getPostions(gymWrapper.getGymId());
  }
}
