package cn.qingcheng.gym.responsitory.network;

import cn.qingcheng.gym.bean.BrandsResponse;
import cn.qingcheng.gym.bean.GymTypeData;
import cn.qingcheng.gym.bean.ShopsResponse;
import cn.qingchengfit.network.response.QcDataResponse;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
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


}
