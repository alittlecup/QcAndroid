package cn.qingchengfit.shop.repository.remote;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.shop.repository.response.CategoryListResponse;
import cn.qingchengfit.shop.repository.response.GoodListResponse;
import cn.qingchengfit.shop.repository.response.ProductListResponse;
import cn.qingchengfit.shop.repository.response.RecordListResponse;
import cn.qingchengfit.shop.vo.Category;
import io.reactivex.Flowable;
import java.util.HashMap;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.FieldMap;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

/**
 * Created by huangbaole on 2017/12/18.
 */

public interface ShopApi {
  /**
   * 获取商品列表
   */
  @GET("/api/staffs/{staff_id}/mall/products/")
  Flowable<QcDataResponse<ProductListResponse>> qcLoadProducts(@Path("staff_id") String staff_id,
      @QueryMap HashMap<String, Object> map);

  /**
   * 获取分类列表
   */
  @GET("/api/staffs/{staff_id}/mall/category/")
  Flowable<QcDataResponse<CategoryListResponse>> qcLoadCategories(@Path("staff_id") String staff_id,
      @QueryMap HashMap<String, Object> map);

  /**
   * 新增分类
   */
  @POST("/api/staffs/{staff_id}/mall/category/") Flowable<QcDataResponse> qcPostCategory(
      @Path("staff_id") String staff_id, @Body Category category);

  /**
   * 删除分类
   */
  @DELETE("/api/staffs/{staff_id}/mall/category/{category_id}/")
  Flowable<QcDataResponse> qcDeleteCategory(@Path("staff_id") String staff_id,
      @Path("category_id") int category_id);

  /**
   * 修改分类
   */
  @PUT("/api/staffs/{staff_id}/mall/category/{category_id}/")
  Flowable<QcDataResponse> qcPutCategory(@Path("staff_id") String staff_id,
      @Path("category_id") int category_id, @Body Category category);

  /**
   * 获取库存记录
   */
  @GET("/api/staffs/{staff_id}mall/goods/inventory/records/")
  Flowable<QcDataResponse<RecordListResponse>> qcLoadInventoryRecords(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 添加库存记录
   */
  @POST("/api/staffs/{staff_id}/mall/goods/inventory/records/")
  Flowable<QcDataResponse> qcUpdateInventoryRecord(@Path("staff_id") String staff_id,
      @FieldMap HashMap<String, Object> params);

  /**
   * 获取商品规格信息
   */
  @GET("/api/staffs/{staff_id}/mall/goods/")
  Flowable<QcDataResponse<GoodListResponse>> qcLoadGoodInfo(@Path("staff_id") String staff_id,
      @QueryMap HashMap<String, Object> params);

  /**
   * 获取全部商品及规格信息
   */
  @GET("/api/staffs/{staff_id}/mall/product-goods/?brand_id=1&shop_id=1")
  Flowable<QcDataResponse<ProductListResponse>> qcLoadAllProductInfo(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);
}

