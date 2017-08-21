package cn.qingchengfit.saasbase.repository;

import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.network.body.CreatBrandBody;
import cn.qingchengfit.saasbase.network.body.FeedBackBody;
import cn.qingchengfit.saasbase.network.body.FixPhoneBody;
import cn.qingchengfit.saasbase.network.body.GetCodeBody;
import cn.qingchengfit.saasbase.network.body.LoginBody;
import cn.qingchengfit.saasbase.network.body.ModifyPwBody;
import cn.qingchengfit.saasbase.network.body.RegisteBody;
import cn.qingchengfit.saasbase.network.body.SystemInitBody;
import cn.qingchengfit.saasbase.network.response.CreatBrand;
import cn.qingchengfit.saasbase.network.response.QcResponseData;
import cn.qingchengfit.saasbase.network.response.QcResponseSystenInit;
import cn.qingchengfit.saasbase.qrcode.model.ScanBody;
import cn.qingchengfit.saasbase.staff.model.Login;
import cn.qingchengfit.saasbase.staff.model.body.ManagerBody;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by fb on 2017/8/17.
 */

public interface PostApi {

  @POST("/api/staffs/login/") Observable<QcResponseData<Login>> qcLogin(@Body LoginBody loginBody);

  //获取电话验证码
  @POST("/api/send/verify/") rx.Observable<QcResponse> qcGetCode(@Body GetCodeBody account);

  //注册
  @POST("/api/staffs/register/") rx.Observable<QcResponseData<Login>> qcRegister(@Body
      RegisteBody params);

  //修改密码
  @POST("/api/staffs/{id}/change/password/") rx.Observable<QcResponse> qcMoidfyPw(@Path("id") String id, @Body
      ModifyPwBody modifyPwBean);

  //修改电话号码
  @POST("/api/staffs/{id}/change/phone/") rx.Observable<QcResponse> qcModifyPhoneNum(@Path("id") String id,
      @Body FixPhoneBody fixPhoneBody);

  //发送意见
  @POST("/api/feedback/") rx.Observable<QcResponse> qcFeedBack(@Body FeedBackBody bean);

  @POST("/api/brands/") rx.Observable<QcResponseData<CreatBrand>> qcCreatBrand(@Body
      CreatBrandBody body);

  @POST("/api/systems/initial/") rx.Observable<QcResponseSystenInit> qcSystemInit(@Body
      SystemInitBody body);

  @PUT("/api/staffs/{id}/coaches/{cid}/") rx.Observable<QcResponse> qcFixCoach(@Path("id") String id, @Path("cid") String cid,
      @Query("id") String gymid, @Query("model") String model, @Body Staff coach);

  @DELETE("/api/staffs/{id}/coaches/{cid}/") rx.Observable<QcResponse> qcDelCoach(@Path("id") String id, @Path("cid") String cid,
      @Query("id") String gymid, @Query("model") String model);

  @POST("/api/staffs/{id}/users/") rx.Observable<QcResponse> qcCreateManager(@Path("id") String id, @Query("id") String gymid,
      @Query("model") String model, @Body ManagerBody body);

  @PUT("/api/staffs/{id}/users/{mid}/") rx.Observable<QcResponse> qcUpdateManager(@Path("id") String id, @Path("mid") String cid,
      @Query("id") String gymid, @Query("model") String model, @Body ManagerBody body);

  @DELETE("/api/staffs/{id}/managers/{mid}/") rx.Observable<QcResponse> qcDelManager(@Path("id") String id, @Path("mid") String cid,
      @Query("id") String gymid, @Query("model") String model);

  /**
   * 扫描二维码
   */
  @PUT("/api/scans/{uuid}/") rx.Observable<QcResponse> qcScans(@Path("uuid") String uuid, @Body
      ScanBody body);
}
