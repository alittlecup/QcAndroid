package cn.qingchengfit.saasbase.staff.model;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.staff.beans.body.InvitationBody;
import cn.qingchengfit.saasbase.staff.beans.response.InvitationListWrap;
import cn.qingchengfit.saasbase.staff.beans.response.InvitationWrap;
import cn.qingchengfit.saasbase.staff.beans.response.SalerDataWrap;
import cn.qingchengfit.saasbase.staff.beans.response.StaffShipsListWrap;
import cn.qingchengfit.saasbase.staff.model.body.ManagerBody;
import cn.qingchengfit.saasbase.staff.network.response.SalerListWrap;
import cn.qingchengfit.saasbase.staff.network.response.UserWrap;
import cn.qingchengfit.saasbase.turnovers.TurFilterResponse;
import cn.qingchengfit.saasbase.turnovers.TurOrderListDataWrapper;
import cn.qingchengfit.saasbase.turnovers.TurOrderListResponse;
import cn.qingchengfit.saasbase.turnovers.TurOrderSellerHistoryWrapper;
import cn.qingchengfit.saasbase.turnovers.TurnoversChartStatDataResponse;
import com.google.gson.JsonObject;
import java.util.HashMap;
import java.util.Map;
import retrofit2.http.Body;

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
 * Created by Paper on 2017/10/10.
 */

public interface IStaffModel {
  rx.Observable<QcDataResponse<UserWrap>> getCurUser();
  //获取所有销售人员
  rx.Observable<QcDataResponse<SalerListWrap>> getSalers();

  /**
   * 获取工作人员详情
   */
  rx.Observable<QcDataResponse<StaffShipsListWrap>> getStaffList();
  rx.Observable<QcDataResponse<StaffShipsListWrap>> getLeaveStaffList();
  rx.Observable<QcDataResponse<InvitationListWrap>> getInvitedStaffList();
  rx.Observable<QcDataResponse<InvitationWrap>> inviteStaff(InvitationBody body);
  rx.Observable<QcDataResponse> cancelInvite(String inviteId);
  rx.Observable<QcDataResponse> cancelTrainerInvite(String inviteId);

  rx.Observable<QcDataResponse> addStaff(ManagerBody body);
  rx.Observable<QcDataResponse> delStaff(String id);
  rx.Observable<QcDataResponse> resumeStaff(String id,String position_id);
  rx.Observable<QcDataResponse> editStaff(String id,ManagerBody body);
  rx.Observable<QcDataResponse> editTrainer(String id,ManagerBody body);

  rx.Observable<QcDataResponse<PostionListWrap>> getPositions();

  rx.Observable<QcDataResponse<SalerDataWrap>> getSalerDatas(String staffid, HashMap<String, Object> params);

  rx.Observable<QcDataResponse<StaffShipsListWrap>> getTrainers();
  rx.Observable<QcDataResponse<StaffShipsListWrap>> getTrainersWithParams(HashMap<String, Object> params);
  rx.Observable<QcDataResponse<StaffShipsListWrap>> getLeaveTrainers();
  rx.Observable<QcDataResponse<InvitationListWrap>> getInvitedTrainers();
  rx.Observable<QcDataResponse<InvitationWrap>> inviteTrainer(InvitationBody body);
  rx.Observable<QcDataResponse> delTrainer(String id);
  rx.Observable<QcDataResponse> resumeTrainer(String id);

  rx.Observable<QcDataResponse> inviteBySms(String uuid, @Body String area_code,@Body String phone,boolean isCoach);
  rx.Observable<QcDataResponse<JsonObject>> isSelfSu();



  rx.Observable<QcDataResponse<TurFilterResponse>> qcGetTurnoversFilterItems();
  rx.Observable<QcDataResponse<TurOrderListResponse>> qcGetTurnoverOrderItems(
      Map<String,Object> params);
  rx.Observable<QcDataResponse<TurnoversChartStatDataResponse>> qcGetTurnoverChartStat(
      Map<String,Object> params);

  rx.Observable<QcDataResponse<TurOrderListDataWrapper>> qcGetTurnoverOrderDetail(
     String turnover_id);

  rx.Observable<QcDataResponse<TurOrderListDataWrapper>> qcPutTurnoverOrderDetail(
      String turnover_id,String seller_id);

  rx.Observable<QcDataResponse<TurOrderSellerHistoryWrapper>> qcGetOrderHistorty(
      String turnover_id);



}
