package cn.qingchengfit.shop.repository.remote;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.shop.repository.response.CategoryListResponse;
import cn.qingchengfit.shop.repository.response.GoodListResponse;
import cn.qingchengfit.shop.repository.response.ProductListResponse;
import cn.qingchengfit.shop.repository.response.RecordListResponse;
import cn.qingchengfit.shop.vo.Category;
import cn.qingchengfit.shop.vo.CategoryWrapper;
import cn.qingchengfit.shop.vo.Product;
import cn.qingchengfit.shop.vo.ProductWrapper;
import io.reactivex.Flowable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by huangbaole on 2017/12/19.
 */

public interface ShopRemoteRepository {
  Flowable<QcDataResponse<ProductListResponse>> qcLoadProducts(String staff_id,
      HashMap<String, Object> params);

  Flowable<QcDataResponse<CategoryListResponse>> qcLoadCategories(String staff_id,
      HashMap<String, Object> map);

  Flowable<QcDataResponse<CategoryWrapper>> qcPostCategory(String staff_id, Category category,HashMap<String, Object> params);

  Flowable<QcDataResponse> qcDeleteCategory(String staff_id, String category_id,HashMap<String, Object> params);

  Flowable<QcDataResponse> qcPutCategory(String staff_id, String category_id, Category category,HashMap<String, Object> params);

  Flowable<QcDataResponse<RecordListResponse>> qcLoadInventoryRecords(String staff_id,
      HashMap<String, Object> params);

  Flowable<QcDataResponse> qcUpdateInventoryRecord(String staff_id, HashMap<String, Object> params);

  Flowable<QcDataResponse<GoodListResponse>> qcLoadGoodInfo(String staff_id,
      HashMap<String, Object> params);

  Flowable<QcDataResponse<ProductListResponse>> qcLoadAllProductInfo(String staff_id,
      HashMap<String, Object> params);

  Flowable<QcDataResponse> qcPostProduct(String staff_id, HashMap<String, Object> params,
      Product json);

  Flowable<QcDataResponse> qcDeleteProduct(String staff_id, HashMap<String, Object> params,
      String product_id);

  Flowable<QcDataResponse> qcPutProduct(String staff_id, HashMap<String, Object> params,
      Product json);
  Flowable<QcDataResponse> qcPutProductStatus(String staff_id,String product_id,Map<String,Object> status, HashMap<String, Object> params);
  Flowable<QcDataResponse<ProductWrapper>> qcLoadProductInfo(String staff_id,
      HashMap<String, Object> params, String product_id);
}
