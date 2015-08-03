package com.qingchengfit.fitcoach.http;

import com.paper.paperbaselibrary.utils.FileUtils;
import com.qingchengfit.fitcoach.BuildConfig;
import com.qingchengfit.fitcoach.http.bean.QcResponToken;
import com.qingchengfit.fitcoach.http.bean.QcResponse;
import com.qingchengfit.fitcoach.http.bean.RegisteBean;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
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


    public static QcCloudClient getApi() {
        if (client == null) {
            return new QcCloudClient();
        } else return client;

    }

    public interface QcCloudServer {

        //获取token
        @GET("/api/csrftoken/")
        rx.Observable<QcResponToken> qcGetToken();

        //登录
        @POST("/")
        rx.Observable<QcResponse> qcLogin(String account, String password);

        //注册
        @POST("/api/coaches/register/")
        rx.Observable<QcResponse> qcRegister(
                @Header("X-CSRFToken") String token,
                @Header("Cookie") String cookie,
                @Body RegisteBean params);

        @GET("/")
        public rx.Observable<QcResponse> getTest();
    }


    public QcCloudClient() {
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://192.168.31.143:8888")
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .setRequestInterceptor(new RequestInterceptor(){
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Cookie","csrftoken="+ FileUtils.readCache("token"));
                    }
                })
                .build();

        qcCloudServer = restAdapter.create(QcCloudServer.class);

    }


}
