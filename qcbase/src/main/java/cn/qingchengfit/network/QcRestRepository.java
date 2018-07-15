package cn.qingchengfit.network;

import android.content.Context;
import android.text.TextUtils;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventUnitNetError;
import cn.qingchengfit.events.NetWorkDialogEvent;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponToken;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.GZipper;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.PreferenceUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.phrase.Phrase;
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

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/5/22.
 */

public class QcRestRepository {

  private final OkHttpClient client;
  private Retrofit getApiAdapter;
  private Retrofit postApiAdapter;
  private String host = "";
  private Gson gson = new Gson();

  public QcRestRepository(final Context context, final String host, final String appOemTag) {
    this.host = host;
    HttpLoggingInterceptor interceptor =
        new HttpLoggingInterceptor(message -> LogUtil.d(message));
    interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

    final OkHttpClient tokenClient = new OkHttpClient();
    client = new OkHttpClient.Builder().addNetworkInterceptor(new Interceptor() {
      @Override public Response intercept(Chain chain) throws IOException {
        String token = "";
        Request request = chain.request();
        if (!request.method().equalsIgnoreCase("GET")) {
          RxBus.getBus().post(new NetWorkDialogEvent(NetWorkDialogEvent.EVENT_POST));
          try {
            token = QcRestRepository.this.createGetApi(GetCsrfToken.class)
                .qcGetToken()
                .execute()
                .body().data.token;
          } catch (Exception e) {

          }

          request = request.newBuilder()
              .addHeader("X-CSRFToken", token)
            .addHeader("Cookie", "csrftoken=" + token + ";"+getSessionCookie(context))
            .addHeader("User-Agent", " FitnessTrainerAssistant/"
                  + AppUtils.getAppVer(context)
                  + " Android  OEM:"
                  + appOemTag
                  + "  QingchengApp/"
                  + AppUtils.getCurAppName(context))
              .build();
          Response response = chain.proceed(request);
          if (response != null){
            RxBus.getBus().post(new NetWorkDialogEvent(NetWorkDialogEvent.EVENT_HIDE_DIALOG));
          }
          return response;
        } else {
          request = request.newBuilder()
            .addHeader("Cookie",  QcRestRepository.getSessionCookie(context))
              .addHeader("User-Agent", " FitnessTrainerAssistant/"
                  + AppUtils.getAppVer(context)
                  + " Android  OEM:"
                  + appOemTag
                  + "  QingchengApp/"
                  + AppUtils.getCurAppName(context))
              .build();
        }
        return chain.proceed(request);
      }
    })
      //.addNetworkInterceptor(errorInterceptor)
      .addNetworkInterceptor(interceptor).readTimeout(3, TimeUnit.MINUTES).build();

    Gson customGsonInstance = new GsonBuilder().enableComplexMapKeySerialization().create();

    getApiAdapter = new Retrofit.Builder().baseUrl(host)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(customGsonInstance))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();

    postApiAdapter = new Retrofit.Builder().baseUrl(host)
        .addConverterFactory(GsonConverterFactory.create(customGsonInstance))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .client(client)
        .build();
  }

  private Interceptor errorInterceptor = new Interceptor() {
    @Override public Response intercept(Chain chain) throws IOException {
      Request request = chain.request();
      Response response = null;
      try {
        response = chain.proceed(request);
      } catch (Exception e) {
        LogUtil.e("<-- HTTP FAILED: " + e);
      }
      try {
        if (response != null && request.headers().get("Content-Encoding") != null && response.headers().get("Content-Encoding").equalsIgnoreCase("Gzip")){
          String responebody = GZipper.doUnZipToString(response.body().bytes());
          QcDataResponse qcr = gson.fromJson(responebody,QcDataResponse.class);
          if (qcr.status != 200)
            RxBus.getBus().post(new EventUnitNetError(qcr.error_code,qcr.getMsg(),qcr.status));
        }
      }catch (Exception e){

      }

      return response;
    }
  };

  public OkHttpClient getClient(){
    return client;
  }

  public void changeHost(String host){
    Gson customGsonInstance = new GsonBuilder().enableComplexMapKeySerialization().create();
    this.host = host;

    getApiAdapter = new Retrofit.Builder().baseUrl(host)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(customGsonInstance))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();

    postApiAdapter = new Retrofit.Builder().baseUrl(host)
        .addConverterFactory(GsonConverterFactory.create(customGsonInstance))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .client(client)
        .build();
  }


  public static String getSessionName(Context context){
    return PreferenceUtils.getPrefString(context, "session_key", "");
  }

  public static String getSessionDomain(Context context){
    return PreferenceUtils.getPrefString(context, "session_domain", "");
  }
  public static void setSessionDomain(Context context,String domain){
    PreferenceUtils.setPrefString(context, "session_domain", domain);
  }

  public static String getSession(Context context) {
    String session1 = PreferenceUtils.getPrefString(context, "session_id", "");
    String session2 = PreferenceUtils.getPrefString(context, "qingcheng.session", "");
    if (TextUtils.isEmpty(session1) && TextUtils.isEmpty(session2)) {
      return "";
    } else {
      if (!TextUtils.isEmpty(session1)) {
        return session1;
      } else if (!TextUtils.isEmpty(session2)) {
        return session2;
      } else {
        return "";
      }
    }
  }

  public static String getSessionCookie(Context context){
    return Phrase.from("sessionid={v};{k}={v};Domain={domain}")
      .put("v",getSession(context))
      .put("k",getSessionName(context))
      .put("domain",getSessionName(context))
      .format()
      .toString()
      ;
  }


  public static void setSession(Context context,String key,String value){
    PreferenceUtils.setPrefString(context,"session_key",key);
    PreferenceUtils.setPrefString(context,"session_id",value);
  }

  public static void clearSession(Context context){
    PreferenceUtils.setPrefString(context, "session_key", "");
    PreferenceUtils.setPrefString(context, "session_id", "");
    PreferenceUtils.setPrefString(context, "qingcheng.session", "");
  }

  public String getHost() {
    return host;
  }

  public <T> T createGetApi(final Class<T> service) {
    return getApiAdapter.create(service);
  }

  public <T> T createPostApi(final Class<T> service) {
    return postApiAdapter.create(service);
  }
  
  public interface GetCsrfToken {
    @GET("/api/csrftoken/") Call<QcResponToken> qcGetToken();
  }
}
