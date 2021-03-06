package com.qingchengfit.fitcoach.fragment.card;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.staff.beans.body.InvitationBody;
import cn.qingchengfit.saasbase.staff.beans.response.InvitationListWrap;
import cn.qingchengfit.saasbase.staff.beans.response.InvitationWrap;
import cn.qingchengfit.saasbase.staff.beans.response.SalerDataWrap;
import cn.qingchengfit.saasbase.staff.beans.response.StaffShipsListWrap;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.saasbase.staff.model.PostionListWrap;
import cn.qingchengfit.saasbase.staff.model.body.BatchPayResponse;
import cn.qingchengfit.saasbase.staff.model.body.ManagerBody;
import cn.qingchengfit.saasbase.staff.network.response.SalerListWrap;
import cn.qingchengfit.saasbase.staff.network.response.UserWrap;
import cn.qingchengfit.saasbase.turnovers.TurFilterResponse;
import cn.qingchengfit.saasbase.turnovers.TurOrderListDataWrapper;
import cn.qingchengfit.saasbase.turnovers.TurOrderListResponse;
import cn.qingchengfit.saasbase.turnovers.TurOrderSellerHistoryWrapper;
import cn.qingchengfit.saasbase.turnovers.TurnoversChartStatDataResponse;
import com.google.gson.JsonObject;
import com.qingchengfit.fitcoach.http.TrainerAllApi;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import rx.Observable;

public class StaffModel implements IStaffModel {
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject QcRestRepository restRepository;
  @Inject public  StaffModel(){

  }
  @Override public Observable<QcDataResponse<UserWrap>> getCurUser() {
    return null;
  }

  @Override public Observable<QcDataResponse<SalerListWrap>> getSalers() {

    return restRepository.createRxJava1Api(TrainerAllApi.class).qcGetSalers(loginStatus.staff_id(),gymWrapper.getParams());
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

  @Override public Observable<QcDataResponse<TurFilterResponse>> qcGetTurnoversFilterItems() {
    return null;
  }

  @Override public Observable<QcDataResponse<TurOrderListResponse>> qcGetTurnoverOrderItems(
      Map<String, Object> params) {
    return null;
  }

  @Override
  public Observable<QcDataResponse<TurnoversChartStatDataResponse>> qcGetTurnoverChartStat(
      Map<String, Object> params) {
    return null;
  }

  @Override public Observable<QcDataResponse<TurOrderListDataWrapper>> qcGetTurnoverOrderDetail(
      String turnover_id) {
    return null;
  }

  @Override public Observable<QcDataResponse<TurOrderListDataWrapper>> qcPutTurnoverOrderDetail(
      String turnover_id, String seller_id) {
    return null;
  }

  @Override public Observable<QcDataResponse<TurOrderSellerHistoryWrapper>> qcGetOrderHistorty(
      String turnover_id) {
    return null;
  }

  @Override public Observable<QcDataResponse<BatchPayResponse>> qcGetBatchPayMethod() {
    return restRepository.createRxJava1Api(TrainerAllApi.class).qcGetbatchPay(loginStatus.staff_id(),gymWrapper.getParams());
  }
}
