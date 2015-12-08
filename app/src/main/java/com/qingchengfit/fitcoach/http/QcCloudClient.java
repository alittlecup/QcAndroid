package com.qingchengfit.fitcoach.http;

import com.paper.paperbaselibrary.utils.AppUtils;
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
import com.qingchengfit.fitcoach.http.bean.PostPrivateGym;
import com.qingchengfit.fitcoach.http.bean.PostStudents;
import com.qingchengfit.fitcoach.http.bean.PushBody;
import com.qingchengfit.fitcoach.http.bean.QcAddGymResponse;
import com.qingchengfit.fitcoach.http.bean.QcAddOrganizationResponse;
import com.qingchengfit.fitcoach.http.bean.QcAllCoursePlanResponse;
import com.qingchengfit.fitcoach.http.bean.QcAllStudentResponse;
import com.qingchengfit.fitcoach.http.bean.QcCertificateDetailResponse;
import com.qingchengfit.fitcoach.http.bean.QcCertificatesReponse;
import com.qingchengfit.fitcoach.http.bean.QcCoachRespone;
import com.qingchengfit.fitcoach.http.bean.QcCoachSystemDetailResponse;
import com.qingchengfit.fitcoach.http.bean.QcCoachSystemResponse;
import com.qingchengfit.fitcoach.http.bean.QcCourseResponse;
import com.qingchengfit.fitcoach.http.bean.QcDrawerResponse;
import com.qingchengfit.fitcoach.http.bean.QcEvaluateResponse;
import com.qingchengfit.fitcoach.http.bean.QcExperienceResponse;
import com.qingchengfit.fitcoach.http.bean.QcMeetingResponse;
import com.qingchengfit.fitcoach.http.bean.QcMyhomeResponse;
import com.qingchengfit.fitcoach.http.bean.QcNotiDetailResponse;
import com.qingchengfit.fitcoach.http.bean.QcNotificationResponse;
import com.qingchengfit.fitcoach.http.bean.QcPrivateGymReponse;
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
import com.qingchengfit.fitcoach.http.bean.QcScheduleGlanceResponse;
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
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
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
        okHttpClient.setConnectTimeout(30, TimeUnit.SECONDS);
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
                            }
                            if (responToken != null) {
                                request.addHeader("X-CSRFToken", responToken.data.token);
                                request.addHeader("Cookie", "csrftoken=" + responToken.data.token + ";sessionid=" +
                                        PreferenceUtils.getPrefString(App.AppContex, "session_id", ""));
                                request.addHeader("Cache-Control", "max-age=0");
                                request.addHeader("User-Agent", "FitnessTrainerAssistant/" + AppUtils.getAppVer(App.AppContex) + " Android " + android.os.Build.VERSION.RELEASE + " " + android.os.Build.BRAND + " " + android.os.Build.MODEL + " " + android.os.Build.MANUFACTURER);
                            }
                        }
                )
//                .setErrorHandler(new ErrorHandler() {
//                    @Override
//                    public Throwable handleError(RetrofitError cause) {
//                        if (cause.getKind() == RetrofitError.Kind.NETWORK) {
//                            ToastUtils.show(R.drawable.ic_share_fail,"网络错误");
//                        }
//                        return null;
//                    }
//                })
//                .setErrorHandler(cause -> {
//                    LogUtil.e(cause.getCause().getMessage());
//                    if (cause.getKind() == RetrofitError.Kind.NETWORK) {
//
//                    }
//                    return null;
//                })
                .build();
        RestAdapter restAdapter2 = new RestAdapter.Builder()
                .setEndpoint(Configs.Server)
                .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
                .setRequestInterceptor(new RequestInterceptor() {
                    @Override
                    public void intercept(RequestFacade request) {
                        request.addHeader("Cookie", "sessionid=" +
                                PreferenceUtils.getPrefString(App.AppContex, "session_id", ""));
                        request.addHeader("User-Agent", "FitnessTrainerAssistant/" + AppUtils.getAppVer(App.AppContex) + " Android " + android.os.Build.VERSION.RELEASE + " " + android.os.Build.BRAND + " " + android.os.Build.MODEL + " " + android.os.Build.MANUFACTURER);
                    }
                })
                .setClient(new OkClient(okHttpClient))
//                .setErrorHandler(new ErrorHandler() {
//                    @Override
//                    public Throwable handleError(RetrofitError cause) {
//                        if (cause.getKind().equals(RetrofitError.Kind.NETWORK)) {
//                            ToastUtils.show(R.drawable.ic_share_fail,"网络错误");
//                        }
//                        return null;
//                    }
//                })
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

        @POST("/api/users/{id}/")
        rx.Observable<QcResponUserInfo> qcGetUserInfo(@Path("id") String id);

        @GET("/api/android/coaches/{id}/welcome/")
        rx.Observable<QcResponDrawer> getDrawerInfo(@Path("id") String id);

        @GET("/api/coaches/{id}/systems/")
        rx.Observable<QcResponCoachSys> qcGetSystem(@Path("id") String id, @Header("Cookie") String session_id);

        //获取用户详情
        @GET("/api/coaches/{id}/detail/")
        rx.Observable<QcMyhomeResponse> qcGetDetail(@Path("id") String id);


        //获取通知 分页和不分页接口 ,后者只为拿 未读
        @GET("/api/messages/")
        rx.Observable<QcNotificationResponse> qcGetMessages(@QueryMap HashMap<String, Integer> params);
        @GET("/api/messages/")
        rx.Observable<QcNotificationResponse> qcGetMessages();

        //通知详情
        @GET("/api/messages/{id}/")
        rx.Observable<QcNotiDetailResponse> qcGetMsgDetails(@Path("id") int id);

        //教练详情
        @GET("/api/coaches/{id}/")
        rx.Observable<QcCoachRespone> qcGetCoach(@Path("id") int id);

        @Deprecated //版本号 现在走fir
        @GET("/api/app/version/")
        rx.Observable<QcVersionResponse> qcGetVersion();

        //获取认证列表
        @GET("/api/coaches/{id}/certificates/")
        rx.Observable<QcCertificatesReponse> qcGetCertificates(@Path("id") int id);

        //获取工作经验列表
        @GET("/api/coaches/{id}/experiences/")
        rx.Observable<QcExperienceResponse> qcGetExperiences(@Path("id") int id);

        //获取认证列表
        @GET("/api/certificates/{id}/")
        rx.Observable<QcCertificateDetailResponse> qcGetCertificateDetail(@Path("id") int id);

        //获取评价 评分
        @GET("/api/coaches/{id}/evaluate/")
        rx.Observable<QcEvaluateResponse> qcGetEvaluate(@Path("id") int id);

        //搜索健身房
        @GET("/api/gym/search/")
        rx.Observable<QcSerachGymRepsonse> qcSearchGym(@QueryMap Map<String, String> params);        //搜索健身房

        //搜索热门健身房
        @GET("/api/gym/")
        rx.Observable<QcSerachGymRepsonse> qcHotGym(@QueryMap Map<String, String> params);

        //搜索机构
        @GET("/api/organizations/search/")
        rx.Observable<QcSearchOrganResponse> qcSearchOrganization(@QueryMap Map<String, String> params);

        //热门机构
        @GET("/api/organizations/")
        rx.Observable<QcSearchOrganResponse> qcHotOrganization(@QueryMap Map<String, String> params);

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
        @GET("/api/coaches/{id}/systems/report/schedules/")
        rx.Observable<QcStatementDetailRespone> qcGetStatementDatail(@Path("id") int id, @QueryMap Map<String, String> params);

        //获取教练销售详情
        @GET("/api/coaches/{id}/systems/report/sale/")
        rx.Observable<QcSaleDetailRespone> qcGetSaleDatail(@Path("id") int id, @QueryMap Map<String, String> params);


        //获取教练课程
        @GET("/api/coaches/{id}/systems/courses/")
        rx.Observable<QcCourseResponse> qcGetSystemCourses(@Path("id") int id, @QueryMap Map<String, String> params);


        //获取教练某个系统下的学员
        @GET("/api/coaches/{id}/systems/users/")
        rx.Observable<QcStudentResponse> qcGetSystemStudent(@Path("id") int id, @QueryMap Map<String, String> params);

        //获取教练所有学员
        @GET("/api/coaches/{id}/users/")
        rx.Observable<QcAllStudentResponse> qcGetAllStudent(@Path("id") int id);

        //获取所有课程计划
        @GET("/api/coaches/{id}/plans/")
        rx.Observable<QcAllCoursePlanResponse> qcGetAllPlans(@Path("id") int id);

        //获取所有健身房充值卡
        @GET("/api/coaches/{id}/systems/cardtpls/")
        rx.Observable<QcSystemCardsResponse> qcGetSystemCard(@Path("id") int id, @QueryMap Map<String, String> params);

        @GET("/api/android/coaches/{id}/")
        rx.Observable<QcDrawerResponse> qcGetDrawerInfo(@Path("id") int id);

        //获取预约预览
        @GET("/api/coaches/{id}/schedules/glance/")
        rx.Observable<QcScheduleGlanceResponse> qcGetScheduleGlance(@Path("id") int id, @QueryMap Map<String, String> params);

        //获取个人的健身房
        @GET("/api/coaches/{id}/personal/system/")
        rx.Observable<QcPrivateGymReponse> qcGetPrivateGym(@Path("id") int id);

        @GET("/api/meetings/")
        rx.Observable<QcMeetingResponse> qcGetMeetingList();

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

        //修改教练信息
        @PUT("/api/coaches/{id}/")
        rx.Observable<QcResponse> qcModifyCoach(@Path("id") int id, @Body ModifyCoachInfo coachInfo);

        //修改密码
        @POST("/api/coaches/{id}/change/password/")
        rx.Observable<QcResponse> qcMoidfyPw(@Path("id") int id, @Body ModifyPwBean modifyPwBean);

        //发送意见
        @POST("/api/feedback/")
        rx.Observable<QcEvaluateResponse> qcFeedBack(@Body FeedBackBean bean);

        //新增认证
        @POST("/api/certificates/")
        rx.Observable<QcResponse> qcAddCertificate(@Body AddCertificate addExperience);

        //修改认证
        @PUT("/api/certificates/{id}/")
        rx.Observable<QcResponse> qcEditCertificate(@Path("id") int id, @Body AddCertificate addExperience);

        //删除认证
        @DELETE("/api/certificates/{id}/")
        rx.Observable<QcResponse> qcDelCertificate(@Path("id") int id);

        //新增工作经验
        @POST("/api/experiences/")
        rx.Observable<QcResponse> qcAddExperience(@Body AddWorkExperience addWorkExperience);

        //修改工作经验
        @PUT("/api/experiences/{id}/")
        rx.Observable<QcResponse> qcEditExperience(@Path("id") int id, @Body AddWorkExperience addWorkExperience);

        //删除工作经验
        @DELETE("/api/experiences/{id}/")
        rx.Observable<QcResponse> qcDelExperience(@Path("id") int id);

        //新增健身房
        @POST("/api/gym/")
        rx.Observable<QcAddGymResponse> qcAddGym(@Body AddGymBean addGymBean);

        //新增组织
        @POST("/api/organizations/")
        rx.Observable<QcAddOrganizationResponse> qcAddOrganization(@Body OrganizationBean organizationBean);

        //修改电话号码
        @POST("/api/coaches/{id}/change/phone/")
        rx.Observable<QcResponse> qcModifyPhoneNum(@Path("id") int id, @Body ModifyPhoneNum modifyPwBean);

        //修改个人描述
        @POST("/api/coaches/{id}/change/description/")
        rx.Observable<QcResponse> qcModifyDes(@Path("id") int id, @Body ModifyDes modifyDes);

        //上传手机用户
        @POST("/api/coaches/{id}/systems/users/bulk/create/")
        rx.Observable<QcResponse> qcPostCreatStudents(@Path("id") int id, @Body PostStudents students);

        //修改个人健身房
        @PUT("/api/coaches/{id}/personal/system/")
        rx.Observable<QcResponse> qcPostPrivateGym(@Path("id") int id, @Body PostPrivateGym gym);

        //新建个人健身房
        @POST("/api/coaches/{id}/personal/system/")
        rx.Observable<QcResponse> qcCreatePrivateGym(@Path("id") int id, @Body PostPrivateGym gym);

        @DELETE("/api/coaches/{id}/personal/system/")
        rx.Observable<QcResponse> qcDelPrivateGym(@Path("id") int id);

        @POST("/api/messages/clear/")
        rx.Observable<QcResponse> qcClearNotification();

        @POST("/api/coaches/{id}/push/update/")
        rx.Observable<QcResponse> qcPostPushId(@Path("id") int id,@Body PushBody body);

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
