package com.example.huangbaole.shop.repository.remote;

import cn.qingchengfit.network.QcRestRepository;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
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
        .addConverterFactory(GsonConverterFactory.create(customGsonInstance))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .build();
    shopApi = build.create(ShopApi.class);
  }
}
