package cn.qingchengfit.network;

import android.content.Context;
import android.text.TextUtils;
import cn.qingchengfit.Constants;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.NetWorkDialogEvent;
import cn.qingchengfit.model.ComponentModuleManager;
import cn.qingchengfit.network.response.QcResponToken;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.widgets.BuildConfig;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.squareup.phrase.Phrase;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
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
  private Retrofit retrofitRxJava1;
  private Retrofit retrofitRxJava2;
  private String host = "";

  public QcRestRepository(final Context context, final String host, final String appOemTag) {
    this.host = host;
    if (BuildConfig.DEBUG) {
      String debug_ip = PreferenceUtils.getPrefString(context, "debug_ip", Constants.Server);
      if (!host.equals(debug_ip)) {
        this.host = debug_ip;
      }
    }
    HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(message -> LogUtil.d(message));
    interceptor.setLevel(
        BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);

    client = new OkHttpClient.Builder().addNetworkInterceptor(new Interceptor() {
      @Override public Response intercept(Chain chain) throws IOException {
        String token = "";
        Request request = chain.request();
        if (!request.method().equalsIgnoreCase("GET")) {
          RxBus.getBus().post(new NetWorkDialogEvent(NetWorkDialogEvent.EVENT_POST));
          try {
            token = QcRestRepository.this.createRxJava1Api(GetCsrfToken.class)
                .qcGetToken()
                .execute()
                .body().data.token;
          } catch (Exception e) {

          }

          request = request.newBuilder()
              .addHeader("X-CSRFToken", token)
              .addHeader("Cookie", "csrftoken=" + token + ";" + getSessionCookie(context))
              .addHeader("User-Agent", " FitnessTrainerAssistant/"
                  + AppUtils.getAppVer(context)
                  + " Android  OEM:"
                  + appOemTag
                  + "  QingchengApp/"
                  + AppUtils.getCurAppName(context))
              .build();
          Response response = chain.proceed(request);
          if (response != null) {
            RxBus.getBus().post(new NetWorkDialogEvent(NetWorkDialogEvent.EVENT_HIDE_DIALOG));
          }
          return response;
        } else {
          request = request.newBuilder()
              .addHeader("Cookie", QcRestRepository.getSessionCookie(context))
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
    }).addNetworkInterceptor(interceptor).readTimeout(3, TimeUnit.MINUTES).build();

    Gson customGsonInstance = new GsonBuilder().enableComplexMapKeySerialization().create();

    retrofitRxJava1 = new Retrofit.Builder().baseUrl(this.host)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(customGsonInstance))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();

    retrofitRxJava2 = new Retrofit.Builder().baseUrl(this.host)
        .addConverterFactory(GsonConverterFactory.create(customGsonInstance))
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .client(client)
        .build();
    ComponentModuleManager.register(Retrofit.class, retrofitRxJava1);
  }

  public void changeHost(String host) {
    Gson customGsonInstance = new GsonBuilder().enableComplexMapKeySerialization().create();
    this.host = host;

    retrofitRxJava1 = new Retrofit.Builder().baseUrl(host)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(customGsonInstance))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();

    retrofitRxJava2 = new Retrofit.Builder().baseUrl(host)
        .addConverterFactory(GsonConverterFactory.create(customGsonInstance))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .client(client)
        .build();
  }

  public static String getSessionName(Context context) {
    return PreferenceUtils.getPrefString(context, "session_key", "");
  }

  public static String getSessionDomain(Context context) {
    return PreferenceUtils.getPrefString(context, "session_domain", "");
  }

  public static void setSessionDomain(Context context, String domain) {
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

  public static String getSessionCookie(Context context) {
    return Phrase.from("sessionid={v};{k}={v};Domain={domain}")
        .put("v", getSession(context))
        .put("k", getSessionName(context))
        .put("domain", getSessionName(context))
        .format()
        .toString();
  }

  public static void setSession(Context context, String key, String value) {
    PreferenceUtils.setPrefString(context, "session_key", key);
    PreferenceUtils.setPrefString(context, "session_id", value);
  }

  public static void clearSession(Context context) {
    PreferenceUtils.setPrefString(context, "session_key", "");
    PreferenceUtils.setPrefString(context, "session_id", "");
    PreferenceUtils.setPrefString(context, "qingcheng.session", "");
  }

  public String getHost() {
    return host;
  }

  public <T> T createRxJava1Api(final Class<T> service) {
    return retrofitRxJava1.create(service);
  }

  public <T> T createRxJava2Api(final Class<T> service) {
    return retrofitRxJava2.create(service);
  }

  public interface GetCsrfToken {
    @GET("/api/csrftoken/") Call<QcResponToken> qcGetToken();
  }
}
