package cn.qingchengfit.checkout.repository;

import android.util.Log;
import cn.qingchengfit.checkout.bean.HomePageBean;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import io.reactivex.Flowable;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CheckoutModel implements ICheckoutModel {
  CheckoutApi checkoutApi;
  @Inject public CheckoutModel(QcRestRepository qcRestRepository){
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
  }
  @Override public Flowable<QcDataResponse<HomePageBean>> qcGetHomePageInfo(String staff_id,
      Map<String, Object> params) {
    QcDataResponse<HomePageBean> qcDataResponse=new QcDataResponse<>();
    qcDataResponse.setStatus(200);
    HomePageBean bean=new HomePageBean();
    bean.setCashier(30);
    bean.setCharge(10);
    bean.setNew_count(20);
    bean.setSum("2000.03");
    qcDataResponse.setData(bean);
    return Flowable.just(qcDataResponse).delay(2,TimeUnit.SECONDS);
    //return checkoutApi.qcGetCeckoutHomeInfo(staff_id,params);
  }
}
