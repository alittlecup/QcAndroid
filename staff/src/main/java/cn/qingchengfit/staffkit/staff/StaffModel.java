package cn.qingchengfit.staffkit.staff;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.staff.beans.response.SalerDataWrap;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.saasbase.staff.model.PostionListWrap;
import cn.qingchengfit.saasbase.staff.model.body.ManagerBody;
import cn.qingchengfit.saasbase.staff.network.response.SalerListWrap;
import cn.qingchengfit.saasbase.staff.network.response.UserWrap;
import java.util.HashMap;
import rx.Observable;

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
 * Created by Paper on 2017/12/1.
 */

public class StaffModel implements IStaffModel {


  GymWrapper gymWrapper;
  LoginStatus loginStatus;
  StaffApi staffApi;

  public StaffModel(QcRestRepository repository, GymWrapper gymWrapper, LoginStatus loginStatus) {

    this.gymWrapper = gymWrapper;
    this.loginStatus = loginStatus;
    staffApi = repository.createGetApi(StaffApi.class);
  }

  @Override public Observable<QcDataResponse<UserWrap>> getCurUser() {
    return null;
  }

  @Override public Observable<QcDataResponse<SalerListWrap>> getSalers() {
    return staffApi.qcGetSalers(loginStatus.staff_id(), gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse<SalerListWrap>> getStaffList(String id) {
    return null;
  }

  @Override public Observable<QcDataResponse> addStaff(ManagerBody body) {
    return null;
  }

  @Override public Observable<QcDataResponse> delStaff(String id) {
    return null;
  }

  @Override public Observable<QcDataResponse> editStaff(String id, ManagerBody body) {
    return null;
  }

  @Override public Observable<QcDataResponse<PostionListWrap>> getPositions() {
    return null;
  }

  @Override public Observable<QcDataResponse<SalerDataWrap>> getSalerDatas(String staffid,
    HashMap<String, Object> params) {
    return null;
  }

  @Override public Observable<QcDataResponse<SalerListWrap>> getTrainers() {
    return staffApi.qcGetGymCoachesPermission(loginStatus.staff_id(),gymWrapper.getParams());
  }
}
