package cn.qingcheng.gym.responsitory.network;

import cn.qingcheng.gym.bean.BrandPostBody;
import cn.qingcheng.gym.bean.BrandResponse;
import cn.qingcheng.gym.bean.BrandsResponse;
import cn.qingcheng.gym.bean.GymApplyOrderResponse;
import cn.qingcheng.gym.bean.GymApplyOrderResponses;
import cn.qingcheng.gym.bean.GymPosition;
import cn.qingcheng.gym.bean.GymPositions;
import cn.qingcheng.gym.bean.GymSearchResponse;
import cn.qingcheng.gym.bean.GymTypeData;
import cn.qingcheng.gym.bean.ShopsResponse;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.model.base.Space;
import cn.qingchengfit.network.response.QcDataResponse;
import java.util.Map;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface GymApi {
  @GET("/api/staffs/{id}/brands/") rx.Observable<QcDataResponse<BrandsResponse>> qcGetBrands(
      @Path("id") String id);

  @GET("/api/staffs/{id}/brands/{brand_id}/shops/")
  rx.Observable<QcDataResponse<ShopsResponse>> qcGetBrandAllShops(@Path("id") String id,
      @Path("brand_id") String brand_id);

  @GET("/api/v2/gym/types/") rx.Observable<QcDataResponse<GymTypeData>> qcGetGymType();

  @DELETE("/api/gym/{id}/") Observable<QcDataResponse> qcDelGym(@Path("id") String id);

  @POST("/api/brands/") rx.Observable<QcDataResponse<BrandResponse>> qcCreatBrand(
      @Body BrandPostBody body);

  @POST("/api/systems/initial/") rx.Observable<QcDataResponse<Shop>> qcSystemInit(@Body Shop body);

  @PUT("/api/common/user/gyms/{gym_id}/") rx.Observable<QcDataResponse> editGymIntro(
      @Path("gym_id") String gymId, @Body Shop body);

  @GET("/api/v2/enter/gyms/search/")
  rx.Observable<QcDataResponse<GymSearchResponse>> qcGetGymsByName(@Query("name") String name);

  @GET("/api/v2/gyms/{gymId}/staff/enter/positions/?show_all=1")
  rx.Observable<QcDataResponse<GymPositions>> qcGetGymPositions(@Path("gymId") String id);

  @POST("/api/v2/gym/enter/applications/")
  rx.Observable<QcDataResponse<GymApplyOrderResponse>> qcPostGymApply(
      @Body Map<String, Object> body);

  @GET("/api/v2/gym/enter/applications/")
  rx.Observable<QcDataResponse<GymApplyOrderResponses>> qcGetGymApplyOrder(
      @QueryMap Map<String, Object> body);

  @GET("api/v2/gyms/{gymID}/staff/enter/user/position/")
  rx.Observable<QcDataResponse<GymPosition>> qcGetGymUserPosition(@Path("gymID") String id,
      @Query("position_type") String type);

  @GET("api/v2/gyms/{gymId}/enter/application/{orderId}/")
  rx.Observable<QcDataResponse<GymApplyOrderResponse>> qcGetGymApplyOrderInfo(
      @Path("gymId") String gymId, @Path("orderId") String orderId);

  @PUT("api/v2/gyms/{gymId}/enter/application/{orderId}/")
  rx.Observable<QcDataResponse<GymApplyOrderResponse>> qcDealGymApplyOrder(
      @Path("gymId") String gymId, @Path("orderId") String orderId, @Body Map<String, Object> body);

  /**
   * 场地操作
   */
  //新建场地
  @POST("/api/staffs/{staff_id}/spaces/") rx.Observable<QcDataResponse> qcCreateSpace(@Path("staff_id") String staff_id,
      @Query("id") String gymid, @Query("model") String model, @Query("brand_id") String brand_id, @Body
      Space space);

}

