package com.qingchengfit.fitcoach.http;

import cn.qingchengfit.widgets.utils.AppUtils;
import cn.qingchengfit.widgets.utils.PreferenceUtils;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.BuildConfig;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.RevenUtils;
import com.qingchengfit.fitcoach.bean.ArrangeBatchBody;
import com.qingchengfit.fitcoach.bean.CoachInitBean;
import com.qingchengfit.fitcoach.bean.QcResponseSpaces;
import com.qingchengfit.fitcoach.bean.QcResponseSystenInit;
import com.qingchengfit.fitcoach.http.bean.AddBatchCourse;
import com.qingchengfit.fitcoach.http.bean.AddBodyTestBean;
import com.qingchengfit.fitcoach.http.bean.AddCertificate;
import com.qingchengfit.fitcoach.http.bean.AddCoourseResponse;
import com.qingchengfit.fitcoach.http.bean.AddCourse;
import com.qingchengfit.fitcoach.http.bean.AddGymPostBean;
import com.qingchengfit.fitcoach.http.bean.AddStudentBean;
import com.qingchengfit.fitcoach.http.bean.AddWorkExperience;
import com.qingchengfit.fitcoach.http.bean.BodyTestReponse;
import com.qingchengfit.fitcoach.http.bean.CheckCode;
import com.qingchengfit.fitcoach.http.bean.CheckPhoneBean;
import com.qingchengfit.fitcoach.http.bean.CourseBody;
import com.qingchengfit.fitcoach.http.bean.CreatBrandBody;
import com.qingchengfit.fitcoach.http.bean.DelCourseManage;
import com.qingchengfit.fitcoach.http.bean.EditJacketBody;
import com.qingchengfit.fitcoach.http.bean.FeedBackBean;
import com.qingchengfit.fitcoach.http.bean.FixBatchBean;
import com.qingchengfit.fitcoach.http.bean.GetBatchesResponse;
import com.qingchengfit.fitcoach.http.bean.GetCodeBean;
import com.qingchengfit.fitcoach.http.bean.GetSysSessionBean;
import com.qingchengfit.fitcoach.http.bean.HidenBean;
import com.qingchengfit.fitcoach.http.bean.LoginBean;
import com.qingchengfit.fitcoach.http.bean.ModifyCoachInfo;
import com.qingchengfit.fitcoach.http.bean.ModifyDes;
import com.qingchengfit.fitcoach.http.bean.ModifyPhoneNum;
import com.qingchengfit.fitcoach.http.bean.ModifyPwBean;
import com.qingchengfit.fitcoach.http.bean.OneExperienceResponse;
import com.qingchengfit.fitcoach.http.bean.OrganizationBean;
import com.qingchengfit.fitcoach.http.bean.PostPrivateGym;
import com.qingchengfit.fitcoach.http.bean.PostStudents;
import com.qingchengfit.fitcoach.http.bean.PushBody;
import com.qingchengfit.fitcoach.http.bean.QcAddGymResponse;
import com.qingchengfit.fitcoach.http.bean.QcAddOrganizationResponse;
import com.qingchengfit.fitcoach.http.bean.QcAllCoursePlanResponse;
import com.qingchengfit.fitcoach.http.bean.QcAllStudentResponse;
import com.qingchengfit.fitcoach.http.bean.QcBatchResponse;
import com.qingchengfit.fitcoach.http.bean.QcBodyTestTemplateRespone;
import com.qingchengfit.fitcoach.http.bean.QcCardsResponse;
import com.qingchengfit.fitcoach.http.bean.QcCertificateDetailResponse;
import com.qingchengfit.fitcoach.http.bean.QcCertificatesReponse;
import com.qingchengfit.fitcoach.http.bean.QcCoachRespone;
import com.qingchengfit.fitcoach.http.bean.QcCoachServiceResponse;
import com.qingchengfit.fitcoach.http.bean.QcCoachSystemDetailResponse;
import com.qingchengfit.fitcoach.http.bean.QcCoachSystemResponse;
import com.qingchengfit.fitcoach.http.bean.QcCourseResponse;
import com.qingchengfit.fitcoach.http.bean.QcDrawerResponse;
import com.qingchengfit.fitcoach.http.bean.QcEvaluateResponse;
import com.qingchengfit.fitcoach.http.bean.QcExperienceResponse;
import com.qingchengfit.fitcoach.http.bean.QcGetBodyTestResponse;
import com.qingchengfit.fitcoach.http.bean.QcGymDetailResponse;
import com.qingchengfit.fitcoach.http.bean.QcMeetingResponse;
import com.qingchengfit.fitcoach.http.bean.QcMyhomeResponse;
import com.qingchengfit.fitcoach.http.bean.QcNotiDetailResponse;
import com.qingchengfit.fitcoach.http.bean.QcNotificationResponse;
import com.qingchengfit.fitcoach.http.bean.QcOneCourseResponse;
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
import com.qingchengfit.fitcoach.http.bean.QcResponsCreatBrand;
import com.qingchengfit.fitcoach.http.bean.QcResponse;
import com.qingchengfit.fitcoach.http.bean.QcResponseBrands;
import com.qingchengfit.fitcoach.http.bean.QcResponseCourseDetail;
import com.qingchengfit.fitcoach.http.bean.QcResponseCourseList;
import com.qingchengfit.fitcoach.http.bean.QcResponseCoursePlan;
import com.qingchengfit.fitcoach.http.bean.QcResponseCourseTeacher;
import com.qingchengfit.fitcoach.http.bean.QcResponseGroupCourse;
import com.qingchengfit.fitcoach.http.bean.QcResponseGroupDetail;
import com.qingchengfit.fitcoach.http.bean.QcResponseJacket;
import com.qingchengfit.fitcoach.http.bean.QcResponsePrivateBatchDetail;
import com.qingchengfit.fitcoach.http.bean.QcResponsePrivateCourse;
import com.qingchengfit.fitcoach.http.bean.QcResponseSchedulePhotos;
import com.qingchengfit.fitcoach.http.bean.QcResponseShopComment;
import com.qingchengfit.fitcoach.http.bean.QcSaleDetailRespone;
import com.qingchengfit.fitcoach.http.bean.QcSaleGlanceResponse;
import com.qingchengfit.fitcoach.http.bean.QcScheduleGlanceResponse;
import com.qingchengfit.fitcoach.http.bean.QcSchedulesResponse;
import com.qingchengfit.fitcoach.http.bean.QcSearchOrganResponse;
import com.qingchengfit.fitcoach.http.bean.QcSerachGymRepsonse;
import com.qingchengfit.fitcoach.http.bean.QcServiceDetialResponse;
import com.qingchengfit.fitcoach.http.bean.QcStatementDetailRespone;
import com.qingchengfit.fitcoach.http.bean.QcStudentResponse;
import com.qingchengfit.fitcoach.http.bean.QcSystemCardsResponse;
import com.qingchengfit.fitcoach.http.bean.QcVersionResponse;
import com.qingchengfit.fitcoach.http.bean.RegisteBean;
import com.qingchengfit.fitcoach.http.bean.StudentCarsResponse;
import com.qingchengfit.fitcoach.http.bean.StudentCourseResponse;
import com.qingchengfit.fitcoach.http.bean.StudentInfoResponse;
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
import retrofit.http.Query;
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
        RestAdapter restAdapter = new RestAdapter.Builder().setEndpoint(Configs.Server)
            .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.FULL)
            .setClient(new OkClient(okHttpClient))
            .setRequestInterceptor(request -> {
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
                    request.addHeader("User-Agent", "FitnessTrainerAssistant/"
                        + AppUtils.getAppVer(App.AppContex)
                        + " Android "
                        + android.os.Build.VERSION.RELEASE
                        + " "
                        + android.os.Build.BRAND
                        + " "
                        + android.os.Build.MODEL
                        + " "
                        + android.os.Build.MANUFACTURER
                        + "  OEM:"
                        + App.AppContex.getString(R.string.oem_tag));
                }
            })
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
        RestAdapter restAdapter2 = new RestAdapter.Builder().setEndpoint(Configs.Server)
            .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.FULL)
            .setRequestInterceptor(new RequestInterceptor() {
                @Override public void intercept(RequestFacade request) {
                    request.addHeader("Cookie", "sessionid=" + PreferenceUtils.getPrefString(App.AppContex, "session_id", ""));
                    request.addHeader("User-Agent", "FitnessTrainerAssistant/"
                        + AppUtils.getAppVer(App.AppContex)
                        + " Android "
                        + android.os.Build.VERSION.RELEASE
                        + " "
                        + android.os.Build.BRAND
                        + " "
                        + android.os.Build.MODEL
                        + " "
                        + android.os.Build.MANUFACTURER
                        + "  OEM:"
                        + App.AppContex.getString(R.string.oem_tag));
                }
            })
            .setClient(new OkClient(okHttpClient))
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
        } else {
            return client;
        }
    }

    public RestAdapter.Builder getRestAdapter() {
        return new RestAdapter.Builder().setClient(new OkClient(okHttpClient))
            .setEndpoint(Configs.Server)
            .setLogLevel(BuildConfig.DEBUG ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE)
            .setRequestInterceptor(request -> {
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
            });
    }

    public interface GetApi {
        //获取token
        @GET("/api/csrftoken/") QcResponToken qcGetToken();

        @POST("/api/users/{id}/") rx.Observable<QcResponUserInfo> qcGetUserInfo(@Path("id") String id);

        @GET("/api/android/coaches/{id}/welcome/") rx.Observable<QcResponDrawer> getDrawerInfo(@Path("id") String id);

        @GET("/api/coaches/{id}/systems/") rx.Observable<QcResponCoachSys> qcGetSystem(@Path("id") String id,
            @Header("Cookie") String session_id);

        @GET("/api/users/{user_id}/brands/") rx.Observable<QcResponseBrands> qcGetBrands(@Path("user_id") String id);

        //获取用户详情
        @GET("/api/coaches/{id}/detail/") rx.Observable<QcMyhomeResponse> qcGetDetail(@Path("id") String id);

        //
        //        //获取通知 分页和不分页接口 ,后者只为拿 未读
        //        @GET("/api/messages/")
        //        rx.Observable<QcNotificationResponse> qcGetMessages(@QueryMap HashMap<String, Integer> params);
        //        @GET("/api/messages/")
        //        rx.Observable<QcNotificationResponse> qcGetMessages();

        //获取通知 分页和不分页接口 ,后者只为拿 未读
        @GET("/api/notifications/") rx.Observable<QcNotificationResponse> qcGetMessages(@Query("coach_id") int id,
            @QueryMap HashMap<String, String> params);

        @GET("/api/notifications/?tab=COACH_0") rx.Observable<QcNotificationResponse> qcGetMessages(@Query("coach_id") int id);

        //通知详情
        @GET("/api/messages/{id}/") rx.Observable<QcNotiDetailResponse> qcGetMsgDetails(@Path("id") int id);

        //教练详情
        @GET("/api/coaches/{id}/") rx.Observable<QcCoachRespone> qcGetCoach(@Path("id") int id);

        @Deprecated //版本号 现在走fir
        @GET("/api/app/version/") rx.Observable<QcVersionResponse> qcGetVersion();

        //获取认证列表
        @GET("/api/coaches/{id}/certificates/") rx.Observable<QcCertificatesReponse> qcGetCertificates(@Path("id") int id);

        //获取工作经验列表
        @GET("/api/coaches/{id}/experiences/") rx.Observable<QcExperienceResponse> qcGetExperiences(@Path("id") int id);

        //获取单条工作经验
        @GET("/api/experiences/{id}/") rx.Observable<OneExperienceResponse> qcGetExperience(@Path("id") int id);

        //获取认证列表
        @GET("/api/certificates/{id}/") rx.Observable<QcCertificateDetailResponse> qcGetCertificateDetail(@Path("id") int id);

        //获取评价 评分
        @GET("/api/coaches/{id}/evaluate/") rx.Observable<QcEvaluateResponse> qcGetEvaluate(@Path("id") int id);

        //搜索健身房
        @GET("/api/gym/search/") rx.Observable<QcSerachGymRepsonse> qcSearchGym(@QueryMap Map<String, String> params);        //搜索健身房

        //搜索热门健身房
        @GET("/api/gym/") rx.Observable<QcSerachGymRepsonse> qcHotGym(@QueryMap Map<String, String> params);

        //搜索机构
        @GET("/api/organizations/search/") rx.Observable<QcSearchOrganResponse> qcSearchOrganization(@QueryMap Map<String, String> params);

        //热门机构
        @GET("/api/organizations/") rx.Observable<QcSearchOrganResponse> qcHotOrganization(@QueryMap Map<String, String> params);

        //获取教练日程
        @Deprecated @GET("/api/coaches/{id}/schedules/") rx.Observable<QcSchedulesResponse> qcGetCoachSchedule(@Path("id") int id,
            @QueryMap Map<String, String> params);

        @GET("/api/v1/coaches/{id}/schedules/") rx.Observable<QcSchedulesResponse> qcGetCoachScheduleV1(@Path("id") int id,
            @QueryMap Map<String, String> params);

        //获取教练预约概览
        @GET("/api/v2/coaches/{id}/reports/schedules/glance/") rx.Observable<QcReportGlanceResponse> qcGetCoachReportGlance(
            @Path("id") int id,@QueryMap HashMap<String,Object> params);

        //获取教练预约概览
        @GET("/api/v2/coaches/{id}/reports/sale/glance/") rx.Observable<QcSaleGlanceResponse> qcGetCoachSaleGlance(@Path("id") int id,@QueryMap HashMap<String,Object> params);

        //获取教练系统
        @Deprecated @GET("/api/coaches/{id}/systems/") rx.Observable<QcCoachSystemResponse> qcGetCoachSystem(@Path("id") int id);

        //获取教练系统
        @GET("/api/v1/coaches/{id}/services/") rx.Observable<QcCoachServiceResponse> qcGetCoachService(@Path("id") int id);

        //获取教练系统
        @GET("/api/coaches/{id}/systems/detail/") rx.Observable<QcCoachSystemDetailResponse> qcGetCoachSystemDetail(@Path("id") int id);

        @GET("/api/v1/coaches/{id}/reports/schedules/") rx.Observable<QcStatementDetailRespone> qcGetStatementDatail(@Path("id") int id,
            @QueryMap Map<String, String> params);

        @GET("/api/v1/services/detail/") rx.Observable<QcServiceDetialResponse> qcGetServiceDetail(@QueryMap Map<String, String> params);

        //获取教练销售详情
        @GET("/api/v2/coaches/{id}/reports/sells/") rx.Observable<QcSaleDetailRespone> qcGetSaleDatail(@Path("id") int id,
            @QueryMap Map<String, String> params);

        //获取教练销售 充值卡信息
        @GET("/api/v1/coaches/{id}/reports/sale/cardtpls/") rx.Observable<QcCardsResponse> qcGetSaleCard(@Path("id") int id);

        //获取教练课程
        @GET("/api/coaches/{id}/systems/courses/") rx.Observable<QcCourseResponse> qcGetSystemCourses(@Path("id") int id,
            @QueryMap Map<String, String> params);

        //获取教练某个系统下的学员
        @GET("/api/coaches/{id}/systems/users/") rx.Observable<QcStudentResponse> qcGetSystemStudent(@Path("id") int id,
            @QueryMap Map<String, String> params);

        //获取教练所有学员
        @GET("/api/v2/coaches/{id}/students/") rx.Observable<QcAllStudentResponse> qcGetAllStudent(@Path("id") int id,@QueryMap HashMap<String,Object> params);

        //获取所有课程计划
        @GET("/api/coaches/{id}/plans/") rx.Observable<QcAllCoursePlanResponse> qcGetAllPlans(@Path("id") int id);

        //获取所有健身房充值卡
        @GET("/api/coaches/{id}/systems/cardtpls/") rx.Observable<QcSystemCardsResponse> qcGetSystemCard(@Path("id") int id,
            @QueryMap Map<String, String> params);

        @GET("/api/android/coaches/{id}/") rx.Observable<QcDrawerResponse> qcGetDrawerInfo(@Path("id") int id,
            @QueryMap Map<String, String> params);

        //获取预约预览
        @GET("/api/coaches/{id}/schedules/glance/") rx.Observable<QcScheduleGlanceResponse> qcGetScheduleGlance(@Path("id") int id,
            @QueryMap Map<String, String> params);

        //获取个人的健身房
        @GET("/api/coaches/{id}/personal/system/") rx.Observable<QcPrivateGymReponse> qcGetPrivateGym(@Path("id") int id);

        @GET("/api/v1/coaches/{id}/shop/") rx.Observable<QcGymDetailResponse> qcGetGymDetail(@Path("id") int id,
            @QueryMap Map<String, String> params);

        @GET("/api/meetings/") rx.Observable<QcMeetingResponse> qcGetMeetingList(@QueryMap Map<String, String> params);

        //所有的团课排期
        @GET("/api/v1/coaches/{coach_id}/batches/{batch_id}/{schedules}/") rx.Observable<QcBatchResponse> qcGetGroupManageDetail(
            @Path("coach_id") int coach_id, @Path("batch_id") String batch_id, @Path("schedules") String schedules,
            @QueryMap Map<String, String> params);

        //排期列表
        @GET("/api/v1/coaches/{coach_id}/courses/{course_id}/batches/") rx.Observable<GetBatchesResponse> qcGetGroupManage(
            @Path("coach_id") int coach_id, @Path("course_id") String course_id, @QueryMap Map<String, String> params);

        @GET("/api/v1/coaches/{coach_id}/courses/{course_id}/") rx.Observable<QcOneCourseResponse> qcGetOneCourse(
            @Path("coach_id") int coach_id, @Path("course_id") String course_id, @QueryMap Map<String, String> params);

        //学员基础信息
        @GET("/api/students/{id}/") rx.Observable<StudentInfoResponse> qcGetStudentInfo(@Path("id") String student_id,
            @QueryMap Map<String, String> params);

        //学员课程列表
        @GET("/api/students/{id}/schedules/") rx.Observable<StudentCourseResponse> qcGetStuedntCourse(@Path("id") String student_id,
            @QueryMap Map<String, String> params);

        //学员卡列表
        @GET("/api/students/{id}/cards/") rx.Observable<StudentCarsResponse> qcGetStuedntCard(@Path("id") String student_id,
            @QueryMap Map<String, String> params);

        //学员卡
        @GET("/api/students/{id}/measures/") rx.Observable<BodyTestReponse> qcGetStuedntBodyTest(@Path("id") String student_id,
            @QueryMap Map<String, String> params);

        //体测模板接口
        @GET("/api/measures/tpl/") rx.Observable<QcBodyTestTemplateRespone> qcGetBodyTestModel(@QueryMap Map<String, String> params);

        //获取体测数据
        @GET("/api/measures/{measure_id}/") rx.Observable<QcGetBodyTestResponse> qcGetBodyTest(@Path("measure_id") String measure_id,
            @QueryMap Map<String, String> params);

        /**
         * 课程
         */

        //获取健身房课程列表
        @GET("/api/v2/coaches/{id}/courses/?&show_all=1") rx.Observable<QcResponseCourseList> qcGetCourses(@Path("id") String coach_id,
            @QueryMap HashMap<String, String> params, @Query("is_private") int is_private);

        /**
         * 获取课程计划
         */
        @GET("/api/v1/coaches/{coach_id}/plantpls/?show_all=1") rx.Observable<QcResponseCoursePlan> qcGetCoursePlan(
            @Path("coach_id") String id, @QueryMap HashMap<String, String> params);

        /**
         * 获取课程下教练
         */
        @GET("/api/v1/coaches/{coach_id}/courses/teachers/") rx.Observable<QcResponseCourseTeacher> qcGetCourseTeacher(
            @Path("coach_id") String coach_id, @Query("course_id") String id, @QueryMap HashMap<String, String> params);

        /**
         * 课程下照片
         */
        @GET("/api/v1/coaches/{coach_id}/courses/schedules/photos/") rx.Observable<QcResponseSchedulePhotos> qcGetSchedulePhotos(
            @Path("coach_id") String coach_id, @Query("course_id") String id, @Query("page") int page,
            @QueryMap HashMap<String, String> params);

        /**
         * 获取课程详情
         */
        @GET("/api/v1/coaches/{coach_id}/courses/{course_id}/") rx.Observable<QcResponseCourseDetail> qcGetCourseDetail(
            @Path("coach_id") String coach_id, @Path("course_id") String id, @QueryMap HashMap<String, String> params);

        /**
         * 分场馆评分
         */
        @GET("/api/v1/coaches/{coach_id}/courses/shops/score/") rx.Observable<QcResponseShopComment> qcGetShopComment(
            @Path("coach_id") String coach_id, @Query("course_id") String id, @QueryMap HashMap<String, String> params);

        @GET("/api/v1/coaches/{coach_id}/courses/photos/") rx.Observable<QcResponseJacket> qcGetJacket(@Path("coach_id") String id,
            @Query("course_id") String course_id, @QueryMap HashMap<String, String> params);

        /**
         * ***********************************    end     ********************
         */

        /**
         * 权限
         */

        //        @GET("/api/v1/coaches/{id}/permissions/")
        //        rx.Observable<QcResponsePermission> qcPermission(@Path("id") String coach_id, @QueryMap HashMap<String, String> params);

        //获取团课排课
        @GET("/api/v1/coaches/{id}/group/courses/") rx.Observable<QcResponseGroupCourse> qcGetGroupCourse(@Path("id") String coach_id,
            @Query("id") String gym_id, @Query("model") String gym_model, @Query("brand_id") String brand_id);
        //
        //获取私教排课
        @GET("/api/v1/coaches/{id}/private/coaches/") rx.Observable<QcResponsePrivateCourse> qcGetPrivateCrourse(@Path("id") String coach_id,
            @Query("id") String gym_id, @Query("model") String gym_model, @Query("brand_id") String brand_id);


        //获取团课排期
        @GET("/api/v1/coaches/{id}/batches/")
        rx.Observable<QcResponseGroupDetail> qcGetGroupCourses(
            @Path("id") String coach_id, @Query("id") String gym_id, @Query("model") String gym_model,@Query("is_private") int isPrivate);

        //排课填充
        @GET("/api/v1/coaches/{id}/{type}/arrange/template/")
        rx.Observable<QcResponseBtachTemplete> qcGetBatchTemplate(@Path("id") String id,
            @Path("type") String type, @Query("id") String gymid, @Query("model") String gymmodel, @Query("teacher_id") String teacher_id,
            @Query("course_id") String course_id);
        //获取某个排期的详情
        @GET("/api/v1/coaches/{id}/batches/{batch_id}/")
        rx.Observable<QcResponsePrivateBatchDetail> qcGetBatchDetail(@Path("id") String coach_id, @Path("batch_id") String batch_id, @Query("id") String gym_id, @Query("model") String gym_model);


        //获取场地列表
        @GET("/api/v1/coaches/{coach_id}/spaces/")
        rx.Observable<QcResponseSpaces> qcGetSpace(@Path("coach_id") String id, @Query("id") String gymid, @Query("model") String gymmodel);
        //拉去卡列表
        @GET("/api/v2/coaches/{coach_id}/cardtpls/")
        rx.Observable<QcResponse> qcGetCard(@Path("coach_id") String id, @Query("id") String gymid, @Query("model") String gymmodel);

    }

    public interface PostApi {

        //登录
        @POST("/api/coaches/login/") rx.Observable<QcResponLogin> qcLogin(@Body LoginBean loginBean);

        //注册
        @POST("/api/coaches/register/") rx.Observable<QcResponLogin> qcRegister(@Body RegisteBean params);

        //创建品牌
        @POST("/api/brands/") rx.Observable<QcResponsCreatBrand> qcCreatBrand(@Body CreatBrandBody body);

        //初始化系统
        @POST("/api/coach/systems/initial/") rx.Observable<QcResponseSystenInit> qcInit(@Body CoachInitBean body);

        //获取电话验证码
        @POST("/api/send/verify/") rx.Observable<QcResponse> qcGetCode(@Body GetCodeBean account);

        @POST("/api/check/verify/") rx.Observable<QcResponCode> qcCheckCode(@Body CheckCode checkCode);

        @POST("/api/users/phone/check/") rx.Observable<QcResponCheckPhone> qcCheckPhone(@Body CheckPhoneBean phone);

        @PUT("/api/users/{id}/") rx.Observable<QcResponse> qcModifyInfo(@Path("id") String id);

        //修改教练信息
        @PUT("/api/coaches/{id}/") rx.Observable<QcResponse> qcModifyCoach(@Path("id") int id, @Body ModifyCoachInfo coachInfo);

        //修改密码
        @POST("/api/coaches/{id}/change/password/") rx.Observable<QcResponse> qcMoidfyPw(@Path("id") int id,
            @Body ModifyPwBean modifyPwBean);

        //发送意见
        @POST("/api/feedback/") rx.Observable<QcEvaluateResponse> qcFeedBack(@Body FeedBackBean bean);

        //新增认证
        @POST("/api/certificates/") rx.Observable<QcResponse> qcAddCertificate(@Body AddCertificate addExperience);

        //修改认证
        @PUT("/api/certificates/{id}/") rx.Observable<QcResponse> qcEditCertificate(@Path("id") int id, @Body AddCertificate addExperience);

        //删除认证
        @DELETE("/api/certificates/{id}/") rx.Observable<QcResponse> qcDelCertificate(@Path("id") int id);

        //新增工作经验
        @POST("/api/experiences/") rx.Observable<QcResponse> qcAddExperience(@Body AddWorkExperience addWorkExperience);

        //修改工作经验
        @PUT("/api/experiences/{id}/") rx.Observable<QcResponse> qcEditExperience(@Path("id") int id,
            @Body AddWorkExperience addWorkExperience);

        @POST("/api/experiences/{id}/hidden/") rx.Observable<QcResponse> qcHidenExperience(@Path("id") int id, @Body HidenBean hidenBean);

        @POST("/api/certificates/{id}/hidden/") rx.Observable<QcResponse> qcHidenCertificates(@Path("id") int id,
            @Body HidenBean hidenBean);

        //删除工作经验
        @DELETE("/api/experiences/{id}/") rx.Observable<QcResponse> qcDelExperience(@Path("id") int id);

        //新增健身房
        @POST("/api/gym/") rx.Observable<QcAddGymResponse> qcAddGym(@Body AddGymPostBean addGymBean);

        //新增组织
        @POST("/api/organizations/") rx.Observable<QcAddOrganizationResponse> qcAddOrganization(@Body OrganizationBean organizationBean);

        //修改电话号码
        @POST("/api/coaches/{id}/change/phone/") rx.Observable<QcResponse> qcModifyPhoneNum(@Path("id") int id,
            @Body ModifyPhoneNum modifyPwBean);

        //修改个人描述
        @POST("/api/coaches/{id}/change/description/") rx.Observable<QcResponse> qcModifyDes(@Path("id") int id, @Body ModifyDes modifyDes);

        //上传手机用户
        @POST("/api/coaches/{id}/systems/users/bulk/create/") rx.Observable<QcResponse> qcPostCreatStudents(@Path("id") int id,
            @Body PostStudents students);

        //修改个人健身房
        @PUT("/api/coaches/{id}/personal/system/") rx.Observable<QcResponse> qcPostPrivateGym(@Path("id") int id, @Body PostPrivateGym gym);

        //新建健身房
        @POST("/api/coaches/{id}/personal/system/") rx.Observable<QcResponse> qcCreateGym(@Path("id") int id,
            @Body HashMap<String, Object> body);

        //新建个人健身房
        @POST("/api/coaches/{id}/personal/system/") rx.Observable<QcResponse> qcCreatePrivateGym(@Path("id") int id,
            @Body PostPrivateGym gym);

        //删除个人健身房
        @DELETE("/api/coaches/{id}/personal/system/") rx.Observable<QcResponse> qcDelPrivateGym(@Path("id") int id);

        @Deprecated
        //清除notification
        @POST("/api/messages/clear/") rx.Observable<QcResponse> qcClearNotification();

        //百度pushid绑定
        @POST("/api/coaches/{id}/push/update/") rx.Observable<QcResponse> qcPostPushId(@Path("id") int id, @Body PushBody body);

        //清除notification
        @PUT("/api/notifications/clear/?type=COACH_0") rx.Observable<QcResponse> qcClearAllNotification(@Query("coach_id") int id);

        //清除某条notification
        @PUT("/api/notifications/clear/") rx.Observable<QcResponse> qcClearOneNotification(@Query("coach_id") int id,
            @Query("id") String notiId);

        @POST("/api/v1/coaches/{id}/courses/") rx.Observable<AddCoourseResponse> qcAddCourse(@Path("id") int id, @Body AddCourse addCourse);

        @PUT("/api/v1/coaches/{id}/courses/") rx.Observable<QcResponse> qcEditCourse(@Path("id") int id, @Body AddCourse addCourse);

        //        @DELETE("/api/v1/coaches/{id}/courses/")
        //        rx.Observable<QcResponse> qcDelCourse(@Path("id") int id, @QueryMap HashMap<String,String> params);

        @POST("/api/v1/coaches/{id}/students/add/") rx.Observable<QcResponse> qcAddStudent(@Path("id") int id,
            @Body AddStudentBean StudentBean);

        @POST("/api/v1/coaches/{id}/students/add/") rx.Observable<QcResponse> qcAddStudents(@Path("id") int id,
            @Body PostStudents addStudentBeans);

        //批量排课
        @POST("/api/v1/coaches/{id}/schedules/batches/") rx.Observable<QcResponse> qcAddCourseManage(@Path("id") int id,
            @Body AddBatchCourse addBatchCourse);

        @POST("/api/v1/coaches/{id}/{schedules}/bulk/delete/") rx.Observable<QcResponse> qcDelCourseManage(@Path("id") int id,
            @Path("schedules") String schedules, @Body DelCourseManage delCourseManage);

         //修改单挑排期

        /**
         * @param schedules 私教 timetables
         */
        @PUT("/api/v1/coaches/{coach_id}/{schedules}/{schedule_id}/") rx.Observable<QcResponse> qcFixBatch(@Path("coach_id") int coach_id,
            @Path("schedule_id") String schedule_id, @Path("schedules") String schedules, @Body FixBatchBean batchBean);

        @POST("/api/measures/") rx.Observable<QcResponse> qcAddBodyTest(@Body AddBodyTestBean addBodyTestBean);

        @PUT("/api/measures/{measure_id}/") rx.Observable<QcResponse> qcUpdateBodyTest(@Path("measure_id") String id,
            @Body AddBodyTestBean addBodyTestBean);

        @DELETE("/api/measures/{measure_id}/") rx.Observable<QcResponse> qcDelBodyTest(@Path("measure_id") String id,
            @QueryMap Map<String, String> params);

        //删除学员
        @DELETE("/api/students/{id}/") rx.Observable<QcResponse> qcDelStudent(@Path("id") String studentid,
            @QueryMap Map<String, String> params);

        /**
         * 创建课程
         */
        @POST("/api/v2/coaches/{id}/courses/") rx.Observable<QcResponse> qcCreateCourse(@Path("id") String coachid,
            @Body CourseBody courseBody, @QueryMap HashMap<String, String> params);

        //修改课程
        @PUT("/api/v1/coaches/{id}/courses/{course_id}/") rx.Observable<QcResponse> qcUpdateCourse(@Path("id") String coachid,
            @Path("course_id") String course_id, @QueryMap HashMap<String, String> params, @Body CourseBody courseBody);

        //删除课程
        @DELETE("/api/v1/coaches/{id}/courses/{course_id}/") rx.Observable<QcResponse> qcDelCourse(@Path("id") String coachid,
            @Path("course_id") String course_id, @QueryMap HashMap<String, String> params);

        //修改封面
        @POST("/api/v1/coaches/{id}/courses/photos/") rx.Observable<QcResponse> qcEditJacket(@Path("id") String coachid,
            @Query("course_id") String course_id, @QueryMap HashMap<String, String> params, @Body EditJacketBody body);

        //修改课程适用场馆
        @PUT("/api/v2/coaches/{coach_id}/courses/{course_id}/shops/") rx.Observable<QcResponse> qcEditCourseShops(
            @Path("coach_id") String coachid, @Path("course_id") String course_id, @Body HashMap<String, String> params);

        /**
         * 排期
         */
        @POST("/api/v1/coaches/{id}/arrange/batches/") rx.Observable<QcResponse> qcArrangeBatch(@Path("id") String coach_id,
            @Query("id") String gymid, @Query("model") String model, @Body ArrangeBatchBody body);
        //修改排期
        @PUT("/api/v1/coaches/{id}/batches/{batchid}/") rx.Observable<QcResponse> qcUpdateBatch(@Path("id") String coach_id,
            @Path("batchid") String batchid, @Query("id") String gymid, @Query("model") String model, @Body ArrangeBatchBody body);
        //排期检查
        @POST("/api/v1/coaches/{id}/{type}/arrange/check/") rx.Observable<QcResponse> qcCheckBatch(@Path("id") String coach_id,
            @Path("type") String type, @Query("id") String gymid, @Query("model") String model, @Body ArrangeBatchBody body);
        //删除排期
        //@DELETE("/api/v1/coaches/{id}/batches/{batchid}/") rx.Observable<QcResponse> qcDelBatch(@Path("id") String coach_id,
        //    @Path("batchid") String batchid, @Query("id") String gymid, @Query("model") String model);
        @DELETE("/api/v1/coaches/{coach_id}/batches/{batch_id}/") rx.Observable<QcResponse> qcDelBatch(@Path("coach_id") String coach_id,
            @Path("batch_id") String batch_id, @QueryMap HashMap<String, String> params);

    }

    public interface DownLoadApi {
        @GET("/") Response qcDownload();
    }

    public interface MutiSystemApi {
        @POST("/api/cloud/authenticate/") rx.Observable<QcResponSystem> qcGetSession(@Body GetSysSessionBean phone);
    }
}
