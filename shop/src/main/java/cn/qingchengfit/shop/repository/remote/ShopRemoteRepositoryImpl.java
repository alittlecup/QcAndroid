package cn.qingchengfit.shop.repository.remote;

import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.shop.repository.response.CategoryListResponse;
import cn.qingchengfit.shop.repository.response.GoodListResponse;
import cn.qingchengfit.shop.repository.response.ProductListResponse;
import cn.qingchengfit.shop.repository.response.RecordListResponse;
import cn.qingchengfit.shop.vo.Category;
import cn.qingchengfit.shop.vo.CategoryWrapper;
import cn.qingchengfit.shop.vo.Product;
import cn.qingchengfit.shop.vo.ProductWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.reactivex.Flowable;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import javax.inject.Singleton;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by huangbaole on 2017/12/19.
 */
@Singleton public class ShopRemoteRepositoryImpl implements ShopRemoteRepository {
  ShopApi shopApi;

  @Inject public ShopRemoteRepositoryImpl(QcRestRepository restRepository) {
    OkHttpClient client = restRepository.getClient();
    Gson customGsonInstance = new GsonBuilder().enableComplexMapKeySerialization().create();

    Retrofit build = new Retrofit.Builder().baseUrl(restRepository.getHost())
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(customGsonInstance))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build();
    shopApi = build.create(ShopApi.class);
  }

  @Override public Flowable<QcDataResponse<ProductListResponse>> qcLoadProducts(String staff_id,
      HashMap<String, Object> params) {
    return shopApi.qcLoadProducts(staff_id, params);
  }

  @Override public Flowable<QcDataResponse<CategoryListResponse>> qcLoadCategories(String staff_id,
      HashMap<String, Object> map) {
    return shopApi.qcLoadCategories(staff_id, map);
  }

  @Override public Flowable<QcDataResponse<CategoryWrapper>> qcPostCategory(String staff_id,
      Category category, HashMap<String, Object> params) {

    return shopApi.qcPostCategory(staff_id, category, params);
  }

  @Override public Flowable<QcDataResponse> qcDeleteCategory(String staff_id, String category_id,
      HashMap<String, Object> params) {
    return shopApi.qcDeleteCategory(staff_id, category_id, params);
  }

  @Override public Flowable<QcDataResponse> qcPutCategory(String staff_id, String category_id,
      Category category, HashMap<String, Object> params) {
    return shopApi.qcPutCategory(staff_id, category_id, category, params);
  }

  @Override
  public Flowable<QcDataResponse<RecordListResponse>> qcLoadInventoryRecords(String staff_id,
      HashMap<String, Object> params) {
    return shopApi.qcLoadInventoryRecords(staff_id, params);
  }

  @Override public Flowable<QcDataResponse> qcUpdateInventoryRecord(String staff_id,
      HashMap<String, Object> params) {
    return shopApi.qcUpdateInventoryRecord(staff_id, params);
  }

  @Override public Flowable<QcDataResponse<GoodListResponse>> qcLoadGoodInfo(String staff_id,
      HashMap<String, Object> params) {
    return shopApi.qcLoadGoodInfo(staff_id, params);
  }

  @Override
  public Flowable<QcDataResponse<ProductListResponse>> qcLoadAllProductInfo(String staff_id,
      HashMap<String, Object> params) {
    return shopApi.qcLoadAllProductInfo(staff_id, params);
  }

  @Override
  public Flowable<QcDataResponse> qcPostProduct(String staff_id, HashMap<String, Object> params,
      Product json) {
    return shopApi.qcPostProduct(staff_id, params, json);
  }

  @Override
  public Flowable<QcDataResponse> qcDeleteProduct(String staff_id, HashMap<String, Object> params,
      String product_id) {
    return shopApi.qcDeleteProduct(staff_id, product_id, params);
  }

  @Override
  public Flowable<QcDataResponse> qcPutProduct(String staff_id, HashMap<String, Object> params,
      Product json) {
    Gson gson = new Gson();
    Product product = gson.fromJson(gson.toJson(json), Product.class);
    product.setId(null);
    return shopApi.qcPutProduct(staff_id, json.getProductId(), params, product);
  }

  @Override public Flowable<QcDataResponse> qcPutProductStatus(String staff_id, String product_id,
      Map<String, Object> status, HashMap<String, Object> params) {
    return shopApi.qcPutProductStatus(staff_id, product_id, params, status);
  }

  @Override public Flowable<QcDataResponse<ProductWrapper>> qcLoadProductInfo(String staff_id,
      HashMap<String, Object> params, String product_id) {
    return shopApi.qcLoadProductInfo(staff_id, product_id, params);
  }
}
