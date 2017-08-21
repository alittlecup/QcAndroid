package cn.qingchengfit.saasbase.repository;

import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.response.QcResponToken;
import cn.qingchengfit.saasbase.network.response.GymList;
import cn.qingchengfit.saasbase.network.response.QcResponseData;
import cn.qingchengfit.saasbase.staff.model.QcResponsePostions;
import cn.qingchengfit.saasbase.staff.model.StaffShipResponse;
import cn.qingchengfit.saasbase.staff.model.Staffs;
import cn.qingchengfit.saasbase.staff.model.body.StaffWrapper;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by fb on 2017/8/17.
 */

public interface GetApi {
  //获取token
  @GET("/api/csrftoken/") Call<QcResponToken> qcGetToken();

  //获取健身房相关服务
  @GET("/api/staffs/{id}/services/") rx.Observable<QcResponseData<GymList>> qcGetCoachService(@Path("id") String id,
      @Query("brand_id") String brand_id);

  //获取某个健身房的教练列表
  @GET("/api/staffs/{id}/coaches/") rx.Observable<QcResponseData<Staffs>> qcGetGymCoaches(@Path("id") String id,
      @Query("id") String gymid, @Query("model") String model, @Query("q") String keyword);

  //获取工作人员列表
  @GET("/api/staffs/{id}/managers/?show_all=1") rx.Observable<QcResponseData<StaffShipResponse>> qcGetStaffs(@Path("id") String staff_id,
      @Query("id") String gym_id, @Query("model") String gym_model, @Query("brand_id") String brand_id, @Query("q") String keywork);

  //获取工作人员职位列表
  @GET("/api/staffs/{id}/positions/") rx.Observable<QcResponsePostions> qcGetPostions(@Path("id") String staff_id,
      @Query("id") String gym_id, @Query("model") String gym_model);

  //工作人员详情
  @GET("/api/staffs/{id}/") rx.Observable<QcResponseData<StaffWrapper>> qcGetSelfInfo(@Path("id") String id);
}
