package cn.qingchengfit.shop.repository.remote;

import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.shop.repository.response.CategoryListResponse;
import cn.qingchengfit.shop.repository.response.ProductListResponse;
import cn.qingchengfit.shop.vo.Category;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.reactivex.Flowable;
import java.util.HashMap;
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

  @Override public Flowable<QcDataResponse> qcPostCategory(String staff_id, Category category) {
    return shopApi.qcPostCategory(staff_id, category);
  }

  @Override public Flowable<QcDataResponse> qcDeleteCategory(String staff_id, int category_id) {
    return shopApi.qcDeleteCategory(staff_id, category_id);
  }

  @Override public Flowable<QcDataResponse> qcPutCategory(String staff_id, int category_id,
      Category category) {
    return shopApi.qcPutCategory(staff_id, category_id, category);
  }
}
