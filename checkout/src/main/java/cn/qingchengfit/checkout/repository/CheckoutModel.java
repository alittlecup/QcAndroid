package cn.qingchengfit.checkout.repository;

import android.util.Log;
import cn.qingchengfit.checkout.bean.OrderStatusBeanWrapper;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.checkout.bean.CashierBean;
import cn.qingchengfit.checkout.bean.HomePageBean;
import cn.qingchengfit.checkout.bean.ScanResultBean;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saascommon.network.ComponentModuleManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import io.reactivex.Flowable;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CheckoutModel implements ICheckoutModel {
  CheckoutApi checkoutApi;
  PayApi payApi;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;



  @Inject public CheckoutModel(QcRestRepository qcRestRepository) {
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
    checkoutApi = retrofit.create(CheckoutApi.class);
    payApi = retrofit.create(PayApi.class);
    ComponentModuleManager.register(ICheckoutModel.class,this);
  }

  @Override public Flowable<QcDataResponse<HomePageBean>> qcGetHomePageInfo() {
    return checkoutApi.qcGetCheckoutHomeInfo(loginStatus.staff_id(), gymWrapper.getParams());
  }

  @Override
  public Flowable<QcDataResponse<CashierBean>> qcPostCashierOrder(Map<String, Object> params) {
    HashMap<String, Object> params1 = gymWrapper.getParams();
    params1.putAll(params);
    return checkoutApi.qcPostCashierOrder(loginStatus.staff_id(), params1);
  }

  @Override
  public Flowable<QcDataResponse<ScanResultBean>> qcPostScanOrder(Map<String, Object> params) {
    HashMap<String, Object> params1 = gymWrapper.getParams();
    params1.putAll(params);
    return payApi.qcPostScanOrder(params1);
  }

  @Override
  public Flowable<QcDataResponse<OrderStatusBeanWrapper>> qcGetOrderStatus(String orderNum) {
    return payApi.qcGetOrderStatus(orderNum, gymWrapper.getParams());
  }
}
