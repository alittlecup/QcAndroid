package cn.qingchengfit.saasbase.repository;

import cn.qingchengfit.model.responese.GymExtra;
import cn.qingchengfit.model.responese.StaffResponse;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.network.response.QcResponseData;
import cn.qingchengfit.saasbase.staff.model.StaffShipResponse;
import cn.qingchengfit.saasbase.staff.model.Staffs;
import java.util.HashMap;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by fb on 2017/8/17.
 */

public interface GetApi {


  //获取某个健身房的教练列表
  @GET("/api/staffs/{id}/coaches/") rx.Observable<QcResponseData<Staffs>> qcGetGymCoaches(@Path("id") String id,
      @Query("id") String gymid, @Query("model") String model, @Query("q") String keyword);

  //获取工作人员列表
  @GET("/api/staffs/{id}/managers/?show_all=1") rx.Observable<QcResponseData<StaffShipResponse>> qcGetStaffs(@Path("id") String staff_id,
      @Query("id") String gym_id, @Query("model") String gym_model, @Query("brand_id") String brand_id, @Query("q") String keywork);


  //工作人员详情
  @GET("/api/staffs/{id}/") rx.Observable<QcDataResponse<StaffResponse>> qcGetSelfInfo(@Path("id") String id);


  @GET("/api/v2/staffs/{staff_id}/gym-extra/")
  rx.Observable<QcDataResponse<GymExtra>> qcGetGymExtra(@Path("staff_id") String staff_id,
      @QueryMap HashMap<String, Object> params);
}
