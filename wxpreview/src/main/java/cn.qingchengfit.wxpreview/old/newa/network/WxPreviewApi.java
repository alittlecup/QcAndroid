package cn.qingchengfit.wxpreview.old.newa.network;

import android.support.v4.util.ArrayMap;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.wxpreview.old.newa.bean.ShopWrapper;
import java.util.HashMap;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import rx.Observable;

public interface WxPreviewApi {

  @GET("/api/staffs/{id}/shops/detail") Observable<QcDataResponse<ShopWrapper>> qcGetShopDetail(
      @Path("id") String id, @QueryMap HashMap<String, Object> params);

  @GET("/api/coaches/{id}/shops/detail") Observable<QcDataResponse<ShopWrapper>> qcGetTrainerShopDetail(
      @Path("id") String id, @QueryMap HashMap<String, Object> params);


  @PUT("/api/staffs/{staff_id}/shops/detail/") Observable<QcDataResponse> qcEditShop(
      @Path("staff_id") String staffId, @Body ArrayMap<String, Object> body);

  @PUT("/api/coaches/{staff_id}/shops/detail/") Observable<QcDataResponse> qcTrainEditShop(
      @Path("staff_id") String staffId, @Body ArrayMap<String, Object> body);
}
