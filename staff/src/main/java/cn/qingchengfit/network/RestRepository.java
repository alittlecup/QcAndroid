package cn.qingchengfit.network;

import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.NetWorkDialogEvent;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcRequestToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import timber.log.Timber;

/**
 * Created by yangming on 16/11/15.
 */

public abstract class RestRepository {

  public final Retrofit getApiAdapter;
  public final Retrofit postApiAdapter;
  private final Get_Token_Api token_api;

  public RestRepository() {
    HttpLoggingInterceptor interceptor =
        new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
          @Override public void log(String message) {
            Timber.d(message);
          }
        });
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
    OkHttpClient client = new OkHttpClient.Builder().addNetworkInterceptor(new Interceptor() {
      @Override public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        if (!request.method().equalsIgnoreCase("GET")) {
          RxBus.getBus().post(new NetWorkDialogEvent(NetWorkDialogEvent.EVENT_POST));
          String token = "";
          try {
            token = token_api.qcGetToken().execute().body().data.getToken();
          } catch (Exception e) {

          }
          request = request.newBuilder()
              .addHeader("X-CSRFToken", token)
              .addHeader("Cookie", "csrftoken=" + token + ";sessionid=" + sessionId())
              .addHeader("User-Agent", " FitnessTrainerAssistant/"
                  + appVersionName()
                  + " Android  OEM:"
                  + oem()
                  + "  QingchengApp/Staff")
              .build();
          Response response = chain.proceed(request);
          if (response != null){
            RxBus.getBus().post(new NetWorkDialogEvent(NetWorkDialogEvent.EVENT_HIDE_DIALOG));
          }
          return response;
        } else {

          request = request.newBuilder()
              .addHeader("Cookie", "sessionid=" + sessionId())
              .addHeader("User-Agent", " FitnessTrainerAssistant/"
                  + appVersionName()
                  + " Android  OEM:"
                  + oem()
                  + "  QingchengApp/Staff")
              .build();
        }
        return chain.proceed(request);
      }
    }).addNetworkInterceptor(interceptor).readTimeout(3, TimeUnit.MINUTES).build();

    Gson customGsonInstance = new GsonBuilder().enableComplexMapKeySerialization().create();

    getApiAdapter = new Retrofit.Builder().baseUrl(serverUrl())
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(customGsonInstance))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();

    postApiAdapter = new Retrofit.Builder().baseUrl(serverUrl())
        .addConverterFactory(GsonConverterFactory.create(customGsonInstance))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .client(client)
        .build();

    token_api = getApiAdapter.create(Get_Token_Api.class);
  }

  public abstract String serverUrl();

  public abstract String sessionId();

  public abstract String appVersionName();

  public abstract String oem();

  public <T> T getApi(Class<T> clazz) {
    return getApiAdapter.create(clazz);
  }

  public <T> T postApi(Class<T> clazz) {
    return postApiAdapter.create(clazz);
  }

  public interface Get_Token_Api {
    //获取token
    @GET("/api/csrftoken/") Call<QcDataResponse<QcRequestToken>> qcGetToken();
  }
}
