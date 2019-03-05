package cn.qingcheng.gym.responsitory.network;

import cn.qingcheng.gym.bean.BrandPostBody;
import cn.qingcheng.gym.bean.BrandResponse;
import cn.qingcheng.gym.bean.BrandsResponse;
import cn.qingcheng.gym.bean.GymTypeData;
import cn.qingcheng.gym.bean.ShopsResponse;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.network.response.QcDataResponse;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
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

  @POST("/api/systems/initial/") rx.Observable<QcDataResponse<Shop>> qcSystemInit(
      @Body Shop body);
  @PUT("/api/common/user/gyms/{gym_id}/") rx.Observable<QcDataResponse> editGymIntro(
      @Path("gym_id") String gymId, @Body Shop body);

}

