package com.qingchengfit.fitcoach.http;

import com.qingchengfit.fitcoach.BuildConfig;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.http.bean.LoginBean;
import com.qingchengfit.fitcoach.http.bean.QcResponLogin;
import com.qingchengfit.fitcoach.http.bean.QcResponToken;
import com.qingchengfit.fitcoach.http.bean.QcResponse;
import com.qingchengfit.fitcoach.http.bean.RegisteBean;
import com.squareup.okhttp.OkHttpClient;

import java.util.concurrent.TimeUnit;

import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;


/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/7/30 2015.
 */
public class QcCloudClient {



    public QcCloudServer qcCloudServer;
    public static QcCloudClient client;
    public QcGetToken qcGetToken;

    public static QcCloudClient getApi() {
        if (client == null) {
            return new QcCloudClient();
        } else return client;

    }

    public interface QcGetToken{
        //获取token
        @GET("/api/csrftoken/")
        QcResponToken qcGetToken();
    }

    public interface QcCloudServer {


        //登录
        @POST("/api/coaches/login/")
        rx.Observable<QcResponLogin> qcLogin(@Body LoginBean loginBean);

        //注册
        @POST("/api/coaches/register/")
        rx.Observable<QcResponse> qcRegister(
                @Header("X-CSRFToken") String token,
                @Body RegisteBean params);

        //获取电话验证码
        @POST("/api/")
        rx.Observable<QcResponse> qcGetCode(

        );

        @GET("/")
        public rx.Observable<QcResponse> getTest();

    }


    public QcCloudClient() {
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Configs.Server)
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .setClient(new OkClient(okHttpClient))
                .setRequestInterceptor(request ->
                        {
                            QcResponToken responToken = qcGetToken.qcGetToken();

                            request.addHeader("X-CSRFToken", responToken.data.token);
                            request.addHeader("Cookie", "csrftoken=" + responToken.data.token);
//                            request.addHeader("X-CSRFToken", FileUtils.readCache("token"));
//                            request.addHeader("Cookie", "csrftoken=" + FileUtils.readCache("token"));
                        }
                )
                .build();
        RestAdapter restAdapter2 = new RestAdapter.Builder()
                .setEndpoint(Configs.Server)
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
//                .setRequestInterceptor(request -> request.addHeader("Cookie","csrftoken="+ FileUtils.readCache("token")))
                .build();

        qcCloudServer = restAdapter.create(QcCloudServer.class);
        qcGetToken = restAdapter2.create(QcGetToken.class);
    }


}
