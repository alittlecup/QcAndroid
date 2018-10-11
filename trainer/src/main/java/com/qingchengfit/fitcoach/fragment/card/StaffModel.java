package com.qingchengfit.fitcoach.fragment.card;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.staff.beans.body.InvitationBody;
import cn.qingchengfit.saasbase.staff.beans.response.InvitationListWrap;
import cn.qingchengfit.saasbase.staff.beans.response.InvitationWrap;
import cn.qingchengfit.saasbase.staff.beans.response.SalerDataWrap;
import cn.qingchengfit.saasbase.staff.beans.response.StaffShipsListWrap;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.saasbase.staff.model.PostionListWrap;
import cn.qingchengfit.saasbase.staff.model.body.ManagerBody;
import cn.qingchengfit.saasbase.staff.network.response.SalerListWrap;
import cn.qingchengfit.saasbase.staff.network.response.UserWrap;
import com.google.gson.JsonObject;
import java.util.HashMap;
import javax.inject.Inject;
import retrofit2.http.PUT;
import rx.Observable;

public class StaffModel implements IStaffModel {
  @Inject public  StaffModel(){

  }
  @Override public Observable<QcDataResponse<UserWrap>> getCurUser() {
    return null;
  }

  @Override public Observable<QcDataResponse<SalerListWrap>> getSalers() {
    return null;
  }

  @Override public Observable<QcDataResponse<StaffShipsListWrap>> getStaffList() {
    return null;
  }

  @Override public Observable<QcDataResponse<StaffShipsListWrap>> getLeaveStaffList() {
    return null;
  }

  @Override public Observable<QcDataResponse<InvitationListWrap>> getInvitedStaffList() {
    return null;
  }

  @Override public Observable<QcDataResponse<InvitationWrap>> inviteStaff(InvitationBody body) {
    return null;
  }

  @Override public Observable<QcDataResponse> cancelInvite(String inviteId) {
    return null;
  }

  @Override public Observable<QcDataResponse> cancelTrainerInvite(String inviteId) {
    return null;
  }

  @Override public Observable<QcDataResponse> addStaff(ManagerBody body) {
    return null;
  }

  @Override public Observable<QcDataResponse> delStaff(String id) {
    return null;
  }

  @Override public Observable<QcDataResponse> resumeStaff(String id, String position_id) {
    return null;
  }

  @Override public Observable<QcDataResponse> editStaff(String id, ManagerBody body) {
    return null;
  }

  @Override public Observable<QcDataResponse> editTrainer(String id, ManagerBody body) {
    return null;
  }

  @Override public Observable<QcDataResponse<PostionListWrap>> getPositions() {
    return null;
  }

  @Override public Observable<QcDataResponse<SalerDataWrap>> getSalerDatas(String staffid,
      HashMap<String, Object> params) {
    return null;
  }

  @Override public Observable<QcDataResponse<StaffShipsListWrap>> getTrainers() {
    return null;
  }

  @Override public Observable<QcDataResponse<StaffShipsListWrap>> getTrainersWithParams(
      HashMap<String, Object> params) {
    return null;
  }

  @Override public Observable<QcDataResponse<StaffShipsListWrap>> getLeaveTrainers() {
    return null;
  }

  @Override public Observable<QcDataResponse<InvitationListWrap>> getInvitedTrainers() {
    return null;
  }

  @Override public Observable<QcDataResponse<InvitationWrap>> inviteTrainer(InvitationBody body) {
    return null;
  }

  @Override public Observable<QcDataResponse> delTrainer(String id) {
    return null;
  }

  @Override public Observable<QcDataResponse> resumeTrainer(String id) {
    return null;
  }

  @Override
  public Observable<QcDataResponse> inviteBySms(String uuid, String area_code, String phone,
      boolean isCoach) {
    return null;
  }

  @Override public Observable<QcDataResponse<JsonObject>> isSelfSu() {
    return null;
  }
}
