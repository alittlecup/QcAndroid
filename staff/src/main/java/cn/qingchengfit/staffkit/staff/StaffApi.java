package cn.qingchengfit.staffkit.staff;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.staff.beans.body.InvitationBody;
import cn.qingchengfit.saasbase.staff.beans.body.StatusBody;
import cn.qingchengfit.saasbase.staff.beans.response.InvitationListWrap;
import cn.qingchengfit.saasbase.staff.beans.response.InvitationWrap;
import cn.qingchengfit.saasbase.staff.beans.response.StaffShipsListWrap;
import cn.qingchengfit.saasbase.staff.model.PostionListWrap;
import cn.qingchengfit.saasbase.staff.model.body.ManagerBody;
import cn.qingchengfit.saasbase.staff.model.body.SendSmsBody;
import cn.qingchengfit.saasbase.staff.network.response.SalerListWrap;
import com.google.gson.JsonObject;
import java.util.HashMap;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

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

public interface StaffApi {

  //获取某个健身房的教练列表
  @GET("/api/staffs/{id}/coaches/") rx.Observable<QcDataResponse<StaffShipsListWrap>> qcGetGymCoaches(@Path("id") String id,
    @QueryMap HashMap<String, Object> params);

  //获取某个健身房的离职教练列表
  @GET("/api/staffs/{id}/coaches/?show_all=1&coach_enable=0") rx.Observable<QcDataResponse<StaffShipsListWrap>> qcGetLeaveCoaches(@Path("id") String id,
    @QueryMap HashMap<String, Object> params, @Query("q") String keyword);

  //设置教练离职
  @PUT("/api/staffs/{id}/coaches/{cid}/") rx.Observable<QcDataResponse> qcDelCoach(@Path("id") String id, @Path("cid") String cid,
    @Body ManagerBody body,
    @QueryMap HashMap<String, Object> params);

  //获取某个健身房的(无视权限)教练列表
  @GET("/api/staffs/{id}/method/coaches/") rx.Observable<QcDataResponse<SalerListWrap>> qcGetGymCoachesPermission(@Path("id") String id,
    @QueryMap HashMap<String, Object> params);

  //获取销售 卖卡  包含销售和教练
  @GET("/api/staffs/{staff_id}/sellers/") rx.Observable<QcDataResponse<SalerListWrap>> qcGetSalers(@Path("staff_id") String staff_id,
      @QueryMap HashMap<String, Object> params);


  //获取工作人员列表
  @GET("/api/v2/staffs/{id}/staffs/?show_all=1") rx.Observable<QcDataResponse<StaffShipsListWrap>> qcGetStaffs(@Path("id") String staff_id,
    @QueryMap HashMap<String,Object> params);

  //获取离职工作人员列表
  @GET("/api/v2/staffs/{id}/staffs/?show_all=1&staff_enable=0") rx.Observable<QcDataResponse<StaffShipsListWrap>> qcGetLeaveingtStaffs(@Path("id") String staff_id,
    @QueryMap HashMap<String,Object> params);

  //获取工作人员职位列表
  @GET("/api/staffs/{id}/positions/") rx.Observable<QcDataResponse<PostionListWrap>> qcGetPostions(@Path("id") String staff_id,
    @QueryMap HashMap<String,Object> params);
  //离职工作人员
  @PUT("/api/v2/staffs/{id}/staffs/{mid}/") rx.Observable<QcDataResponse> qcEditManager(@Path("id") String id, @Path("mid") String cid,
    @Body ManagerBody body,
    @QueryMap HashMap<String,Object> params);

  //判断当前用户是否超级管理员
  @GET("/api/staffs/{staff_id}/superuser/") rx.Observable<QcDataResponse<JsonObject>> qcIsSu(@Path("staff_id") String id,@QueryMap HashMap<String,Object> params);

  /**
   * 邀请列表
   */
  @GET("/api/v2/staffs/{id}/staff/invitations/?show_all=1") rx.Observable<QcDataResponse<InvitationListWrap>> qcGetStaffInvitations(@Path("id") String staff_id,
    @QueryMap HashMap<String,Object> params);

 /**
   * 新增邀请
   */
  @POST("/api/v2/staffs/{id}/staff/invitations/") rx.Observable<QcDataResponse<InvitationWrap>> qcCreateStaffInvitations(@Path("id") String staff_id,
    @Body InvitationBody body, @QueryMap HashMap<String,Object> params);
  /**
   * cancle invitation
   */
  @PUT("/api/v2/staffs/{staff_id}/staff/invitations/{uuid}/") rx.Observable<QcDataResponse> qcCancleIvitation(
    @Path("staff_id") String staff_id,@Path("uuid") String uuid, @Body StatusBody status,@QueryMap HashMap<String,Object> params
  );

 /**
   *  invitation send msg
   */
  @POST("/api/v2/staffs/{staff_id}/staff/invitations/{uuid}/sms/") rx.Observable<QcDataResponse> qcInviteSms(
    @Path("staff_id") String staff_id,@Path("uuid") String uuid, @Body SendSmsBody body, @QueryMap HashMap<String,Object> params
  );


 /**
   * 邀请列表
   */
  @GET("/api/v2/staffs/{id}/coach/invitations/?show_all=1") rx.Observable<QcDataResponse<InvitationListWrap>> qcGetTrainerInvitations(@Path("id") String staff_id,
    @QueryMap HashMap<String,Object> params);

 /**
   * 新增邀请
   */
  @POST("/api/v2/staffs/{id}/coach/invitations/?show_all=1") rx.Observable<QcDataResponse<InvitationWrap>> qcCreateTrainerInvitations(@Path("id") String staff_id,
    @Body InvitationBody body, @QueryMap HashMap<String,Object> params);

  /**
   * cancle invitation
   */
  @PUT("/api/v2/staffs/{staff_id}/coach/invitations/{uuid}/") rx.Observable<QcDataResponse> qcCancleTrainerIvitation(
    @Path("staff_id") String staff_id,@Path("uuid") String uuid, @Body StatusBody status,@QueryMap HashMap<String,Object> params
  );

 /**
   *  invitation send msg
   */
  @POST("/api/v2/staffs/{staff_id}/coach/invitations/{uuid}/sms/") rx.Observable<QcDataResponse> qcInviteTrainerSms(
    @Path("staff_id") String staff_id,@Path("uuid") String uuid, @Body String area_code,@Body String phone,@QueryMap HashMap<String,Object> params
  );



}
