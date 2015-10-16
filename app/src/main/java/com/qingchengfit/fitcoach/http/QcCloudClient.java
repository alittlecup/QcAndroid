package com.qingchengfit.fitcoach.http;

import com.paper.paperbaselibrary.utils.LogUtil;
import com.paper.paperbaselibrary.utils.PreferenceUtils;
import com.paper.paperbaselibrary.utils.RevenUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.BuildConfig;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.http.bean.AddCertificate;
import com.qingchengfit.fitcoach.http.bean.AddGymBean;
import com.qingchengfit.fitcoach.http.bean.AddWorkExperience;
import com.qingchengfit.fitcoach.http.bean.CheckCode;
import com.qingchengfit.fitcoach.http.bean.CheckPhoneBean;
import com.qingchengfit.fitcoach.http.bean.FeedBackBean;
import com.qingchengfit.fitcoach.http.bean.GetCodeBean;
import com.qingchengfit.fitcoach.http.bean.GetSysSessionBean;
import com.qingchengfit.fitcoach.http.bean.LoginBean;
import com.qingchengfit.fitcoach.http.bean.ModifyCoachInfo;
import com.qingchengfit.fitcoach.http.bean.ModifyDes;
import com.qingchengfit.fitcoach.http.bean.ModifyPhoneNum;
import com.qingchengfit.fitcoach.http.bean.ModifyPwBean;
import com.qingchengfit.fitcoach.http.bean.OrganizationBean;
import com.qingchengfit.fitcoach.http.bean.PostStudents;
import com.qingchengfit.fitcoach.http.bean.QcAddGymResponse;
import com.qingchengfit.fitcoach.http.bean.QcAllCoursePlanResponse;
import com.qingchengfit.fitcoach.http.bean.QcAllStudentResponse;
import com.qingchengfit.fitcoach.http.bean.QcCertificateDetailResponse;
import com.qingchengfit.fitcoach.http.bean.QcCertificatesReponse;
import com.qingchengfit.fitcoach.http.bean.QcCoachRespone;
import com.qingchengfit.fitcoach.http.bean.QcCoachSystemDetailResponse;
import com.qingchengfit.fitcoach.http.bean.QcCoachSystemResponse;
import com.qingchengfit.fitcoach.http.bean.QcCourseResponse;
import com.qingchengfit.fitcoach.http.bean.QcEvaluateResponse;
import com.qingchengfit.fitcoach.http.bean.QcExperienceResponse;
import com.qingchengfit.fitcoach.http.bean.QcMyhomeResponse;
import com.qingchengfit.fitcoach.http.bean.QcNotiDetailResponse;
import com.qingchengfit.fitcoach.http.bean.QcNotificationResponse;
import com.qingchengfit.fitcoach.http.bean.QcReportGlanceResponse;
import com.qingchengfit.fitcoach.http.bean.QcResponCheckPhone;
import com.qingchengfit.fitcoach.http.bean.QcResponCoachSys;
import com.qingchengfit.fitcoach.http.bean.QcResponCode;
import com.qingchengfit.fitcoach.http.bean.QcResponDrawer;
import com.qingchengfit.fitcoach.http.bean.QcResponLogin;
import com.qingchengfit.fitcoach.http.bean.QcResponSystem;
import com.qingchengfit.fitcoach.http.bean.QcResponToken;
import com.qingchengfit.fitcoach.http.bean.QcResponUserInfo;
import com.qingchengfit.fitcoach.http.bean.QcResponse;
import com.qingchengfit.fitcoach.http.bean.QcSaleDetailRespone;
import com.qingchengfit.fitcoach.http.bean.QcSaleGlanceResponse;
import com.qingchengfit.fitcoach.http.bean.QcSchedulesResponse;
import com.qingchengfit.fitcoach.http.bean.QcSearchOrganResponse;
import com.qingchengfit.fitcoach.http.bean.QcSerachGymRepsonse;
import com.qingchengfit.fitcoach.http.bean.QcStatementDetailRespone;
import com.qingchengfit.fitcoach.http.bean.QcStudentResponse;
import com.qingchengfit.fitcoach.http.bean.QcSystemCardsResponse;
import com.qingchengfit.fitcoach.http.bean.QcVersionResponse;
import com.qingchengfit.fitcoach.http.bean.RegisteBean;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.OkHttpClient;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.OkClient;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;
import retrofit.http.QueryMap;


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
                    if (cause.getKind() == RetrofitError.Kind.NETWORK) {

                    }
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
        rx.Observable<QcNotificationResponse> qcGetMessages();

        @GET("/api/messages/{id}/")
        rx.Observable<QcNotiDetailResponse> qcGetMsgDetails(@Path("id") int id);

        @GET("/api/coaches/{id}/")
        rx.Observable<QcCoachRespone> qcGetCoach(@Path("id") int id);

        @GET("/api/app/version/")
        rx.Observable<QcVersionResponse> qcGetVersion();

        @GET("/api/coaches/{id}/certificates/")
        rx.Observable<QcCertificatesReponse> qcGetCertificates(@Path("id") int id);

        @GET("/api/coaches/{id}/experiences/")
        rx.Observable<QcExperienceResponse> qcGetExperiences(@Path("id") int id);

        @GET("/api/certificates/{id}/")
        rx.Observable<QcCertificateDetailResponse> qcGetCertificateDetail(@Path("id") int id);

        @GET("/api/coaches/{id}/evaluate/")
        rx.Observable<QcEvaluateResponse> qcGetEvaluate(@Path("id") int id);

        @GET("/api/gym/")
        rx.Observable<QcSerachGymRepsonse> qcSearchGym(@QueryMap Map<String, String> params);

        @GET("/api/organizations/")
        rx.Observable<QcSearchOrganResponse> qcSearchOrganization(@QueryMap Map<String, String> params);

        //获取教练日程
        @GET("/api/coaches/{id}/schedules/")
        rx.Observable<QcSchedulesResponse> qcGetCoachSchedule(@Path("id") int id, @QueryMap Map<String, String> params);

        //获取教练预约概览
        @GET("/api/coaches/{id}/schedules/reports/glance/")
        rx.Observable<QcReportGlanceResponse> qcGetCoachReportGlance(@Path("id") int id);

        //获取教练预约概览
        @GET("/api/coaches/{id}/sale/reports/glance/")
        rx.Observable<QcSaleGlanceResponse> qcGetCoachSaleGlance(@Path("id") int id);

        //获取教练系统
        @GET("/api/coaches/{id}/systems/")
        rx.Observable<QcCoachSystemResponse> qcGetCoachSystem(@Path("id") int id);

        //获取教练系统
        @GET("/api/coaches/{id}/systems/detail/")
        rx.Observable<QcCoachSystemDetailResponse> qcGetCoachSystemDetail(@Path("id") int id);

        //获取教练报表强详情
        @GET("/api/coaches/{id}/systems/report/schedules")
        rx.Observable<QcStatementDetailRespone> qcGetStatementDatail(@Path("id") int id, @QueryMap Map<String, String> params);

        //获取教练销售详情
        @GET("/api/coaches/{id}/systems/report/sale")
        rx.Observable<QcSaleDetailRespone> qcGetSaleDatail(@Path("id") int id, @QueryMap Map<String, String> params);


        //获取教练课程
        @GET("/api/coaches/{id}/systems/courses/")
        rx.Observable<QcCourseResponse> qcGetSystemCourses(@Path("id") int id, @QueryMap Map<String, String> params);


        //获取教练某个系统下的学员
        @GET("/api/coaches/{id}/systems/users")
        rx.Observable<QcStudentResponse> qcGetSystemStudent(@Path("id") int id, @QueryMap Map<String, String> params);

        //获取教练所有学员
        @GET("/api/coaches/{id}/users")
        rx.Observable<QcAllStudentResponse> qcGetAllStudent(@Path("id") int id);

        //获取所有课程计划
        @GET("/api/coaches/{id}/plans")
        rx.Observable<QcAllCoursePlanResponse> qcGetAllPlans(@Path("id") int id);

        //获取所有健身房充值卡
        @GET("/api/coaches/{id}/systems/cardtpls/")
        rx.Observable<QcSystemCardsResponse> qcGetSystemCard(@Path("id") int id, @QueryMap Map<String, String> params);


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

        @PUT("/api/coaches/{id}/")
        rx.Observable<QcResponse> qcModifyCoach(@Path("id") int id, @Body ModifyCoachInfo coachInfo);

        @POST("/api/coaches/{id}/change/password/")
        rx.Observable<QcResponse> qcMoidfyPw(@Path("id") int id, @Body ModifyPwBean modifyPwBean);

        @POST("/api/feedback/")
        rx.Observable<QcEvaluateResponse> qcFeedBack(@Body FeedBackBean bean);

        @POST("/api/certificates/")
        rx.Observable<QcResponse> qcAddCertificate(@Body AddCertificate addExperience);

        @PUT("/api/certificates/{id}/")
        rx.Observable<QcResponse> qcEditCertificate(@Path("id") int id, @Body AddCertificate addExperience);

        @DELETE("/api/certificates/{id}/")
        rx.Observable<QcResponse> qcDelCertificate(@Path("id") int id);


        @POST("/api/experiences/")
        rx.Observable<QcResponse> qcAddExperience(@Body AddWorkExperience addWorkExperience);

        @PUT("/api/experiences/{id}")
        rx.Observable<QcResponse> qcEditExperience(@Path("id") int id, @Body AddWorkExperience addWorkExperience);

        @DELETE("/api/experiences/{id}")
        rx.Observable<QcResponse> qcDelExperience(@Path("id") int id);

        @POST("/api/gym/")
        rx.Observable<QcAddGymResponse> qcAddGym(@Body AddGymBean addGymBean);

        @POST("/api/organizations/")
        rx.Observable<QcResponse> qcAddOrganization(@Body OrganizationBean organizationBean);

        @POST("/api/coaches/{id}/change/phone/")
        rx.Observable<QcResponse> qcModifyPhoneNum(@Path("id") int id, @Body ModifyPhoneNum modifyPwBean);

        @POST("/api/coaches/{id}/change/description/")
        rx.Observable<QcResponse> qcModifyDes(@Path("id") int id, @Body ModifyDes modifyDes);

        //上传手机用户
        @POST("/api/coaches/{id}/systems/users/bulk/create/")
        rx.Observable<QcResponse> qcPostCreatStudents(@Path("id") int id, @Body PostStudents students);




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
