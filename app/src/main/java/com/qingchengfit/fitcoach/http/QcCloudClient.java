package com.qingchengfit.fitcoach.http;

import com.paper.paperbaselibrary.utils.LogUtil;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.paper.paperbaselibrary.utils.RevenUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.BuildConfig;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.http.bean.CheckCode;
import com.qingchengfit.fitcoach.http.bean.CheckPhoneBean;
import com.qingchengfit.fitcoach.http.bean.GetCodeBean;
import com.qingchengfit.fitcoach.http.bean.GetSysSessionBean;
import com.qingchengfit.fitcoach.http.bean.LoginBean;
import com.qingchengfit.fitcoach.http.bean.QcMyhomeResponse;
import com.qingchengfit.fitcoach.http.bean.QcResponCheckPhone;
import com.qingchengfit.fitcoach.http.bean.QcResponCoachSys;
import com.qingchengfit.fitcoach.http.bean.QcResponCode;
import com.qingchengfit.fitcoach.http.bean.QcResponDrawer;
import com.qingchengfit.fitcoach.http.bean.QcResponLogin;
import com.qingchengfit.fitcoach.http.bean.QcResponSystem;
import com.qingchengfit.fitcoach.http.bean.QcResponToken;
import com.qingchengfit.fitcoach.http.bean.QcResponUserInfo;
import com.qingchengfit.fitcoach.http.bean.QcResponse;
import com.qingchengfit.fitcoach.http.bean.RegisteBean;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;


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

    public static int SUCCESS = 200;
    public static QcCloudClient client;
    public PostApi postApi;
    public GetApi getApi;
    public DownLoadApi downLoadApi;
    private OkHttpClient okHttpClient;


    public QcCloudClient() {
        okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(10, TimeUnit.SECONDS);
        int cacheSize = 10 * 1024 * 1024; // 10 MiB
        File fileCache = new File(Configs.ExternalCache);
        try {
            Cache cache = new Cache(fileCache, cacheSize);
            okHttpClient.setCache(cache);

        } catch (IOException e) {
            RevenUtils.sendException("http Cache error!", "http", e);
        }
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(Configs.Server)
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .setClient(new OkClient(okHttpClient))
                .setRequestInterceptor(request ->
                        {
                            QcResponToken responToken = null;
                            try {
                                responToken = getApi.qcGetToken();
                            } catch (Exception e) {
//                                if (e.getKind() == RetrofitError.Kind.NETWORK) {
//                                    LogUtil.e("network error!!");
//                                }
//                                LogUtil.e(e.getMessage());
                            }
                            if (responToken != null) {
                                request.addHeader("X-CSRFToken", responToken.data.token);
                                request.addHeader("Cookie", "csrftoken=" + responToken.data.token + ";sessionid=" +
                                        PreferenceUtils.getPrefString(App.AppContex, "session_id", ""));
                                request.addHeader("Cache-Control", "max-age=0");
                            }
                        }
                )
                .setErrorHandler(cause -> {
                    LogUtil.e(cause.getCause().getMessage());
                    return null;
                })
                .build();
        RestAdapter restAdapter2 = new RestAdapter.Builder()
                .setEndpoint(Configs.Server)
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Cookie", "sessionid=" +
                                PreferenceUtils.getPrefString(App.AppContex, "session_id", ""));
                    }
                })
                .setClient(new OkClient(okHttpClient))
                .setErrorHandler(cause -> {
                            LogUtil.e(cause.getCause().getMessage());
                            return null;

                        }

                )
//                .setRequestInterceptor(request -> request.addHeader("Cookie","csrftoken="+ FileUtils.readCache("token")))
                .build();
//        RestAdapter restAdapter3 = new RestAdapter.Builder()
//                .setEndpoint("")
//                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
//                .setRequestInterceptor(request -> request.addHeader("Cookie","csrftoken="+ FileUtils.readCache("token")))
//                .build();

        postApi = restAdapter.create(PostApi.class);
        getApi = restAdapter2.create(GetApi.class);
//        downLoadApi = restAdapter3.create(DownLoadApi.class);
    }

    public static QcCloudClient getApi() {
        if (client == null) {
            return new QcCloudClient();
        } else return client;

    }

    public RestAdapter.Builder getRestAdapter() {
        return new RestAdapter.Builder()
                .setClient(new OkClient(okHttpClient))
                .setEndpoint(Configs.Server)
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .setRequestInterceptor(request ->
                        {
                            QcResponToken responToken = null;
                            try {
                                responToken = getApi.qcGetToken();
                            } catch (Exception e) {
                                //TODO handle error
                            }
                            if (responToken != null) {
//                                request.addHeader("X-CSRFToken", responToken.data.token);
//                                request.addHeader("Cache-Control", "max-age=0");
                            }
                        }
                );
    }


    public interface GetApi {
        //获取token
        @GET("/api/csrftoken/")
        QcResponToken qcGetToken();

        @POST("/api/users/{id}")
        rx.Observable<QcResponUserInfo> qcGetUserInfo(@Path("id") String id);

        @GET("/api/android/coaches/{id}/welcome/")
        rx.Observable<QcResponDrawer> getDrawerInfo(@Path("id") String id);


        @GET("/api/coaches/{id}/systems/")
        rx.Observable<QcResponCoachSys> qcGetSystem(@Path("id") String id, @Header("Cookie") String session_id);

        @GET("/api/coaches/{id}/detail/")
        rx.Observable<QcMyhomeResponse> qcGetDetail(@Path("id") String id);

        @GET("/api/messages/")
        rx.Observable<QcResponse> qcGetMessages();

        @GET("/message/{id}/")
        rx.Observable<QcResponse> qcGetMsgDetails(@Path("id") String id);

    }


    public interface PostApi {


        //登录
        @POST("/api/coaches/login/")
        rx.Observable<QcResponLogin> qcLogin(@Body LoginBean loginBean);

        //注册
        @POST("/api/coaches/register/")
        rx.Observable<QcResponLogin> qcRegister(
                @Body RegisteBean params);

        //获取电话验证码
        @POST("/api/send/verify/")
        rx.Observable<QcResponse> qcGetCode(@Body GetCodeBean account);

        @POST("/api/check/verify/")
        rx.Observable<QcResponCode> qcCheckCode(@Body CheckCode checkCode);

        @POST("/api/users/phone/check/")
        rx.Observable<QcResponCheckPhone> qcCheckPhone(@Body CheckPhoneBean phone);

        @PUT("/api/users/{id}/")
        rx.Observable<QcResponse> qcModifyInfo(@Path("id") String id);


    }

    public interface DownLoadApi {
        @GET("/")
        Response qcDownload();
    }

    public interface MutiSystemApi {
        @POST("/api/cloud/authenticate/")
        rx.Observable<QcResponSystem> qcGetSession(@Body GetSysSessionBean phone);
    }

}
