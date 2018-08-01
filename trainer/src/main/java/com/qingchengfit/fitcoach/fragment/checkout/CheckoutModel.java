package com.qingchengfit.fitcoach.fragment.checkout;

import android.util.Log;
import cn.qingchengfit.checkout.bean.HomePageBean;
import cn.qingchengfit.checkout.bean.OrderStatusBean;
import cn.qingchengfit.checkout.bean.OrderStatusBeanWrapper;
import cn.qingchengfit.checkout.bean.ScanResultBean;
import cn.qingchengfit.checkout.repository.CheckoutApi;
import cn.qingchengfit.checkout.repository.ICheckoutModel;
import cn.qingchengfit.checkout.repository.PayApi;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saascommon.bean.CashierBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import io.reactivex.Flowable;
import java.util.Map;
import javax.inject.Inject;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CheckoutModel implements ICheckoutModel {
  CheckoutTrainerApi checkoutApi;
  PayApi payApi;
  @Inject CheckoutModel(QcRestRepository qcRestRepository){
    OkHttpClient client = qcRestRepository.getClient();
    OkHttpClient http = client.newBuilder().addInterceptor(new HttpLoggingInterceptor(message -> {
      Log.d("HTTP", message);
    }).setLevel(HttpLoggingInterceptor.Level.BODY)).build();
    Gson customGsonInstance = (new GsonBuilder()).enableComplexMapKeySerialization().create();

    Retrofit retrofit = new Retrofit.Builder().client(http)
        .addConverterFactory(GsonConverterFactory.create(customGsonInstance))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .baseUrl(qcRestRepository.getHost())
        .build();
    checkoutApi = retrofit.create(CheckoutTrainerApi.class);
    payApi = retrofit.create(PayApi.class);
  }

  @Override public Flowable<QcDataResponse<HomePageBean>> qcGetHomePageInfo(String staff_id,
      Map<String, Object> params) {
    return checkoutApi.qcGetCheckoutHomeInfo(staff_id, params);
  }

  @Override public Flowable<QcDataResponse<CashierBean>> qcPostCashierOrder(String staff_id,
      Map<String, Object> params) {
    return checkoutApi.qcPostCashierOrder(staff_id, params);
  }

  @Override
  public Flowable<QcDataResponse<ScanResultBean>> qcPostScanOrder(Map<String, Object> params) {
    return payApi.qcPostScanOrder(params);
  }

  @Override public Flowable<QcDataResponse<OrderStatusBeanWrapper>> qcGetOrderStatus(String orderNum,Map<String,Object> params) {
    return payApi.qcGetOrderStatus(orderNum,params);
  }
}
