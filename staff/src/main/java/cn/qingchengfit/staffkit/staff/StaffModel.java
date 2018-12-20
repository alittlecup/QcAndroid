package cn.qingchengfit.staffkit.staff;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.ComponentModuleManager;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.staff.beans.body.InvitationBody;
import cn.qingchengfit.saasbase.staff.beans.body.StatusBody;
import cn.qingchengfit.saasbase.staff.beans.response.InvitationListWrap;
import cn.qingchengfit.saasbase.staff.beans.response.InvitationWrap;
import cn.qingchengfit.saasbase.staff.beans.response.SalerDataWrap;
import cn.qingchengfit.saasbase.staff.beans.response.StaffShipsListWrap;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.saasbase.staff.model.PostionListWrap;
import cn.qingchengfit.saasbase.staff.model.body.ManagerBody;
import cn.qingchengfit.saasbase.staff.model.body.SendSmsBody;
import cn.qingchengfit.saasbase.staff.network.response.SalerListWrap;
import cn.qingchengfit.saasbase.staff.network.response.UserWrap;
import cn.qingchengfit.saasbase.turnovers.TurFilterResponse;
import cn.qingchengfit.saasbase.turnovers.TurOrderListResponse;
import cn.qingchengfit.staffkit.constant.StaffAllApi;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;
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
  StaffAllApi staffAllApi;

  public StaffModel(QcRestRepository repository, GymWrapper gymWrapper, LoginStatus loginStatus) {

    this.gymWrapper = gymWrapper;
    this.loginStatus = loginStatus;
    staffApi = repository.createRxJava1Api(StaffApi.class);
    staffAllApi = repository.createRxJava1Api(StaffAllApi.class);
    ComponentModuleManager.register(IStaffModel.class, this);
  }

  @Override public Observable<QcDataResponse<UserWrap>> getCurUser() {
    return null;
  }

  @Override public Observable<QcDataResponse<SalerListWrap>> getSalers() {
    return staffApi.qcGetSalers(loginStatus.staff_id(), gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse<StaffShipsListWrap>> getStaffList() {
    return staffApi.qcGetStaffs(loginStatus.staff_id(), gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse<StaffShipsListWrap>> getLeaveStaffList() {
    return staffApi.qcGetLeaveingtStaffs(loginStatus.staff_id(), gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse<InvitationListWrap>> getInvitedStaffList() {
    return staffApi.qcGetStaffInvitations(loginStatus.staff_id(), gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse<InvitationWrap>> inviteStaff(InvitationBody body) {
    return staffApi.qcCreateStaffInvitations(loginStatus.staff_id(), body, gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse> cancelInvite(String inviteId) {
    return staffApi.qcCancleIvitation(loginStatus.staff_id(), inviteId, new StatusBody(3),
        gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse> cancelTrainerInvite(String inviteId) {
    return staffApi.qcCancleTrainerIvitation(loginStatus.staff_id(), inviteId, new StatusBody(3),
        gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse> addStaff(ManagerBody body) {
    return null;
  }

  @Override public Observable<QcDataResponse> delStaff(String id) {
    return staffApi.qcEditManager(loginStatus.staff_id(), id,
        new ManagerBody.Builder().staff_enable(false).build(), gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse> resumeStaff(String id, String position_id) {
    return staffApi.qcEditManager(loginStatus.staff_id(), id,
        new ManagerBody.Builder().staff_enable(true).position_id(position_id).build(),
        gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse> editStaff(String id, ManagerBody body) {
    return staffApi.qcEditManager(loginStatus.staff_id(), id, body, gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse> editTrainer(String id, ManagerBody body) {
    return staffApi.qcDelCoach(loginStatus.staff_id(), id, body, gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse<PostionListWrap>> getPositions() {
    return staffApi.qcGetPostions(loginStatus.staff_id(), gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse<SalerDataWrap>> getSalerDatas(String staffid,
      HashMap<String, Object> params) {
    return null;
  }

  @Override public Observable<QcDataResponse<StaffShipsListWrap>> getTrainers() {
    return staffApi.qcGetGymCoaches(loginStatus.staff_id(), gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse<StaffShipsListWrap>> getTrainersWithParams(
      HashMap<String, Object> params) {
    params.putAll(gymWrapper.getParams());
    return staffApi.qcGetGymCoaches(loginStatus.staff_id(), params);
  }

  @Override public Observable<QcDataResponse<StaffShipsListWrap>> getLeaveTrainers() {
    return staffApi.qcGetLeaveCoaches(loginStatus.staff_id(), gymWrapper.getParams(), null);
  }

  @Override public Observable<QcDataResponse<InvitationListWrap>> getInvitedTrainers() {
    return staffApi.qcGetTrainerInvitations(loginStatus.staff_id(), gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse<InvitationWrap>> inviteTrainer(InvitationBody body) {
    return staffApi.qcCreateTrainerInvitations(loginStatus.staff_id(), body,
        gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse> delTrainer(String id) {
    return staffApi.qcDelCoach(loginStatus.staff_id(), id,
        new ManagerBody.Builder().coach_enable(false).build(), gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse> resumeTrainer(String id) {
    return staffApi.qcDelCoach(loginStatus.staff_id(), id,
        new ManagerBody.Builder().coach_enable(true).build(), gymWrapper.getParams());
  }

  @Override
  public Observable<QcDataResponse> inviteBySms(String uuid, String area_code, String phone,
      boolean isCoach) {
    if (isCoach) {
      return staffApi.qcInviteTrainerSms(loginStatus.staff_id(), uuid,
          new SendSmsBody.Builder().area_code(area_code).phone(phone).build(),
          gymWrapper.getParams());
    } else {
      return staffApi.qcInviteSms(loginStatus.staff_id(), uuid,
          new SendSmsBody.Builder().area_code(area_code).phone(phone).build(),
          gymWrapper.getParams());
    }
  }

  @Override public Observable<QcDataResponse<JsonObject>> isSelfSu() {
    return staffApi.qcIsSu(loginStatus.staff_id(), gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse<TurFilterResponse>> qcGetTurnoversFilterItems() {
    return staffAllApi.qcGetTurnoversFilterItems(loginStatus.staff_id(), gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse<TurOrderListResponse>> qcGetTurnoverOrderItems(Map<String,Object> params) {
    Map<String, Object> params1 = gymWrapper.getParams();
    params1.putAll(params);
    return staffAllApi.qcGetTurnoversOrderList(loginStatus.staff_id(),params1);
  }
}
