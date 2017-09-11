package cn.qingchengfit.network;

import android.content.Context;
import android.text.TextUtils;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.NetWorkDialogEvent;
import cn.qingchengfit.network.response.QcResponToken;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.PreferenceUtils;
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
  private final Retrofit getApiAdapter;
  private final Retrofit postApiAdapter;
  private String host = "";

  public QcRestRepository(final Context context, final String host, final String appOemTag) {
    this.host = host;
    HttpLoggingInterceptor interceptor =
        new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {

          @Override public void log(String message) {
            LogUtil.d(message);
          }
        });

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
              .addHeader("Cookie", "csrftoken=" + token + ";sessionid=" + getSession(context))
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
              .addHeader("Cookie", "sessionid=" + getSession(context))
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
        return null;
      }
    }
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
