package cn.qingchengfit.shop.repository.remote;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.shop.repository.response.CategoryListResponse;
import cn.qingchengfit.shop.repository.response.GoodListResponse;
import cn.qingchengfit.shop.repository.response.ProductListResponse;
import cn.qingchengfit.shop.repository.response.RecordListResponse;
import cn.qingchengfit.shop.vo.Category;
import cn.qingchengfit.shop.vo.Product;
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
      @Path("staff_id") String staff_id, @Body Category category,@QueryMap HashMap<String, Object> params);

  /**
   * 删除分类
   */
  @DELETE("/api/staffs/{staff_id}/mall/category/{category_id}/")
  Flowable<QcDataResponse> qcDeleteCategory(@Path("staff_id") String staff_id,
      @Path("category_id") int category_id,@QueryMap HashMap<String, Object> params);

  /**
   * 修改分类
   */
  @PUT("/api/staffs/{staff_id}/mall/category/{category_id}/")
  Flowable<QcDataResponse> qcPutCategory(@Path("staff_id") String staff_id,
      @Path("category_id") int category_id, @Body Category category,@QueryMap HashMap<String, Object> params);

  /**
   * 获取库存记录
   */
  @GET("/api/staffs/{staff_id}/mall/goods/inventory/records/")
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
  @GET("/api/staffs/{staff_id}/mall/product-goods/")
  Flowable<QcDataResponse<ProductListResponse>> qcLoadAllProductInfo(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 添加商品(商品上下架由商品本身的status字段表示，没有单独接口)
   * {@link Product#status}
   */
  @POST("api/staffs/{staff_id}/mall/products/") Flowable<QcDataResponse> qcPostProduct(
      @Path("staff_id") String staff_id,@QueryMap HashMap<String, Object> params, @Body Product json);

  /**
   * 删除商品
   */
  @DELETE("api/staffs/{staff_id}/mall/products/{product_id}/")
  Flowable<QcDataResponse> qcDeleteProduct(@Path("staff_id") String staff_id,
      @Path("product_id") String product_id,@QueryMap HashMap<String, Object> params);

  /**
   * 修改商品
   */
  @PUT("api/staffs/{staff_id}/mall/products/") Flowable<QcDataResponse> qcPutProduct(
      @Path("staff_id") String staff_id,@QueryMap HashMap<String, Object> params, @Body Product json);

  /**
   * 获取商品信息
   */
  @GET("api/staffs/{staff_id}/mall/products/{product_id}/")
  Flowable<QcDataResponse<Product>> qcLoadProductInfo(@Path("staff_id") String staff_id,
      @Path("product_id") String product_id,@QueryMap HashMap<String, Object> params);
}


