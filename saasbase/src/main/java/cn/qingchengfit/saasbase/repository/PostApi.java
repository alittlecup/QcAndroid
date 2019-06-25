package cn.qingchengfit.saasbase.repository;


import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.coach.model.CoachResponse;
import cn.qingchengfit.saasbase.network.response.QcResponseData;
import cn.qingchengfit.saasbase.staff.model.body.ChangeSuBody;
import cn.qingchengfit.saasbase.staff.model.body.ManagerBody;
import cn.qingchengfit.saasbase.user.bean.CheckCodeBody;
import cn.qingchengfit.saasbase.user.bean.GetCodeBody;
import java.util.HashMap;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by fb on 2017/8/17.
 */

public interface PostApi {


  //获取电话验证码
  @POST("/api/send/verify/") rx.Observable<QcResponse> qcGetCode(@Body GetCodeBody account);



  @PUT("/api/staffs/{id}/coaches/{cid}/") rx.Observable<QcResponse> qcFixCoach(@Path("id") String id, @Path("cid") String cid,
      @Query("id") String gymid, @Query("model") String model, @Body Staff coach);

  @DELETE("/api/staffs/{id}/coaches/{cid}/") rx.Observable<QcResponse> qcDelCoach(@Path("id") String id, @Path("cid") String cid,
      @Query("id") String gymid, @Query("model") String model);

  //工作人员、教练列表
  @POST("/api/staffs/{id}/users/") rx.Observable<QcResponse> qcCreateManager(@Path("id") String id, @Query("id") String gymid,
      @Query("model") String model, @Body ManagerBody body);

  @PUT("/api/staffs/{id}/users/{mid}/") rx.Observable<QcResponse> qcUpdateManager(@Path("id") String id, @Path("mid") String cid,
      @Query("id") String gymid, @Query("model") String model, @Body ManagerBody body);

  @DELETE("/api/staffs/{id}/managers/{mid}/") rx.Observable<QcResponse> qcDelManager(@Path("id") String id, @Path("mid") String cid,
      @Query("id") String gymid, @Query("model") String model);

  //添加教练
  @POST("/api/staffs/{id}/coaches/") rx.Observable<QcResponseData<CoachResponse>> qcAddCoach(@Path("id") String id,
      @Query("id") String gymid, @Query("model") String model, @Body Staff coach);


  /**
   * 验证验证码
   */
  @POST("api/check/verify/") rx.Observable<QcResponse> qcCheckCode(@Body CheckCodeBody body);

  /**
   * 修改超级管理员
   * @param staffid
   * @param params
   * @param body
   * @return
   */
  @PUT("/api/staffs/{staff_id}/superuser/") rx.Observable<QcResponse> qcChangeSu(@Path("staff_id") String staffid,
      @QueryMap HashMap<String, Object> params, @Body ChangeSuBody body);

}
