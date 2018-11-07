package com.qingchengfit.fitcoach.http;

import android.os.Build;
import cn.qingchengfit.Constants;
import cn.qingchengfit.bean.BrandBody;
import cn.qingchengfit.bean.ChangeBrandCreatorBody;
import cn.qingchengfit.bean.CoachInitBean;
import cn.qingchengfit.bean.QcResponsePage;
import cn.qingchengfit.bean.QcResponseSpaces;
import cn.qingchengfit.bean.SyncExpBody;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.model.body.ClearNotiBody;
import cn.qingchengfit.model.body.PostCommentBody;
import cn.qingchengfit.model.responese.ArticleCommentListData;
import cn.qingchengfit.model.responese.GymDetail;
import cn.qingchengfit.model.responese.Notification;
import cn.qingchengfit.model.responese.NotificationGlance;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponToken;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.recruit.views.organization.OrganizationBean;
import cn.qingchengfit.recruit.views.organization.QcAddOrganizationResponse;
import cn.qingchengfit.recruit.views.organization.QcSearchOrganResponse;
import cn.qingchengfit.recruit.views.organization.QcSerachGymRepsonse;
import cn.qingchengfit.saasbase.cards.network.response.CardTplListWrap;
import cn.qingchengfit.saasbase.chat.model.ChatFriendsData;
import cn.qingchengfit.saasbase.chat.model.RecordWrap;
import cn.qingchengfit.saasbase.course.batch.network.body.ArrangeBatchBody;
import cn.qingchengfit.saasbase.course.batch.network.body.SingleBatchBody;
import cn.qingchengfit.saasbase.course.course.network.body.CourseBody;
import cn.qingchengfit.saasbase.course.course.network.body.EditJacketBody;
import cn.qingchengfit.saasbase.gymconfig.bean.GymTypeData;
import cn.qingchengfit.saasbase.network.body.CreatBrandBody;
import cn.qingchengfit.saasbase.network.response.QcResponseSystenInit;
import cn.qingchengfit.saasbase.staff.network.response.SalerListWrap;
import cn.qingchengfit.saascommon.qrcode.model.ScanBody;
import cn.qingchengfit.saasbase.report.bean.CourseReportDetail;
import cn.qingchengfit.saasbase.report.bean.CourseTypeSample;
import cn.qingchengfit.saasbase.report.bean.GymCardtpl;
import cn.qingchengfit.saasbase.report.bean.QcResponseStatementDetail;
import cn.qingchengfit.saasbase.report.bean.StatementGlanceResp;
import cn.qingchengfit.student.bean.StudentBeanListWrapper;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.PreferenceUtils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qingchengfit.fitcoach.App;
import com.qingchengfit.fitcoach.BuildConfig;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.fragment.protocol.CheckProtocolModel;
import com.qingchengfit.fitcoach.fragment.statement.model.CourseTypeSamples;
import com.qingchengfit.fitcoach.fragment.statement.model.QcResponseSaleDetail;
import com.qingchengfit.fitcoach.fragment.statement.model.Sellers;
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
import com.qingchengfit.fitcoach.http.bean.DelCourseManage;
import com.qingchengfit.fitcoach.http.bean.FeedBackBean;
import com.qingchengfit.fitcoach.http.bean.FixBatchBean;
import com.qingchengfit.fitcoach.http.bean.GetBatchesResponse;
import com.qingchengfit.fitcoach.http.bean.GetCodeBean;
import com.qingchengfit.fitcoach.http.bean.HidenBean;
import com.qingchengfit.fitcoach.http.bean.LoginBean;
import com.qingchengfit.fitcoach.http.bean.ModifyDes;
import com.qingchengfit.fitcoach.http.bean.OneExperienceResponse;
import com.qingchengfit.fitcoach.http.bean.PostPrivateGym;
import com.qingchengfit.fitcoach.http.bean.PostStudents;
import com.qingchengfit.fitcoach.http.bean.PushBody;
import com.qingchengfit.fitcoach.http.bean.QcAddGymResponse;
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
import com.qingchengfit.fitcoach.http.bean.QcReponseBrandDetailShops;
import com.qingchengfit.fitcoach.http.bean.QcReportGlanceResponse;
import com.qingchengfit.fitcoach.http.bean.QcResponCheckPhone;
import com.qingchengfit.fitcoach.http.bean.QcResponCoachSys;
import com.qingchengfit.fitcoach.http.bean.QcResponCode;
import com.qingchengfit.fitcoach.http.bean.QcResponDrawer;
import com.qingchengfit.fitcoach.http.bean.QcResponLogin;
import com.qingchengfit.fitcoach.http.bean.QcResponUserInfo;
import com.qingchengfit.fitcoach.http.bean.QcResponeSingleImageWall;
import com.qingchengfit.fitcoach.http.bean.QcResponsCreatBrand;
import com.qingchengfit.fitcoach.http.bean.QcResponseActivities;
import com.qingchengfit.fitcoach.http.bean.QcResponseBrands;
import com.qingchengfit.fitcoach.http.bean.QcResponseCourseDetail;
import com.qingchengfit.fitcoach.http.bean.QcResponseCourseList;
import com.qingchengfit.fitcoach.http.bean.QcResponseCoursePlan;
import com.qingchengfit.fitcoach.http.bean.QcResponseCourseTeacher;
import com.qingchengfit.fitcoach.http.bean.QcResponseGroupCourse;
import com.qingchengfit.fitcoach.http.bean.QcResponseGroupDetail;
import com.qingchengfit.fitcoach.http.bean.QcResponseJacket;
import com.qingchengfit.fitcoach.http.bean.QcResponsePermission;
import com.qingchengfit.fitcoach.http.bean.QcResponsePrivateBatchDetail;
import com.qingchengfit.fitcoach.http.bean.QcResponsePrivateCourse;
import com.qingchengfit.fitcoach.http.bean.QcResponseSchedulePhotos;
import com.qingchengfit.fitcoach.http.bean.QcResponseServiceDetail;
import com.qingchengfit.fitcoach.http.bean.QcResponseShopComment;
import com.qingchengfit.fitcoach.http.bean.QcResponseSingleBatch;
import com.qingchengfit.fitcoach.http.bean.QcSaleGlanceResponse;
import com.qingchengfit.fitcoach.http.bean.QcScheduleGlanceResponse;
import com.qingchengfit.fitcoach.http.bean.QcSchedulesResponse;
import com.qingchengfit.fitcoach.http.bean.QcServiceDetialResponse;
import com.qingchengfit.fitcoach.http.bean.QcStatementDetailRespone;
import com.qingchengfit.fitcoach.http.bean.QcStudentResponse;
import com.qingchengfit.fitcoach.http.bean.QcSystemCardsResponse;
import com.qingchengfit.fitcoach.http.bean.QcVersionResponse;
import com.qingchengfit.fitcoach.http.bean.RegisteBean;
import com.qingchengfit.fitcoach.http.bean.StudentCarsResponse;
import com.qingchengfit.fitcoach.http.bean.StudentCourseResponse;
import com.qingchengfit.fitcoach.http.bean.StudentInfoResponse;
import io.reactivex.Flowable;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

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
    HttpLoggingInterceptor interceptor =
        new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
          @Override public void log(String message) {
            LogUtil.d(message);
          }
        });
    File cacheFile = new File(App.AppContex.getExternalCacheDir(), "http_cache");
    Cache cache = new Cache(cacheFile, 1024 * 1024 * 20); //100Mb
    interceptor.setLevel(
        BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE);
    OkHttpClient client =
        new OkHttpClient.Builder().cache(cache).addNetworkInterceptor(new Interceptor() {
          @Override public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            if (!request.method().equalsIgnoreCase("GET")) {
              String token = getApi.qcGetToken().execute().body().data.token;
              request = request.newBuilder()
                  .addHeader("Connection", "close")
                  .addHeader("X-CSRFToken", token)
                  .addHeader("Cookie",
                      "csrftoken=" + token + ";" + QcRestRepository.getSessionCookie(App.AppContex))
                  .addHeader("User-Agent", " FitnessTrainerAssistant/"
                      + AppUtils.getAppVer(App.AppContex)
                      + " Android  OEM:"
                      + App.AppContex.getString(R.string.oem_tag)
                      + "  QingchengApp/Trainer")
                  .build();
            } else {
              request = request.newBuilder()
                  .addHeader("Connection", "close")
                  .addHeader("max-age", "5")
                  .addHeader("Cache-Control", "public")
                  .addHeader("Cookie", QcRestRepository.getSessionCookie(App.AppContex))
                  .addHeader("User-Agent", " FitnessTrainerAssistant/"
                      + AppUtils.getAppVer(App.AppContex)
                      + " Android  OEM:"
                      + App.AppContex.getString(R.string.oem_tag)
                      + "  QingchengApp/Trainer")
                  .build();
            }
            return chain.proceed(request);
          }
        }).addNetworkInterceptor(interceptor).readTimeout(3, TimeUnit.MINUTES).build();
    Gson customGsonInstance = new GsonBuilder().enableComplexMapKeySerialization().create();
    Retrofit getApiAdapter = new Retrofit.Builder().baseUrl(Configs.Server)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(customGsonInstance))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .build();

    Retrofit postApiAdapter = new Retrofit.Builder().baseUrl(Configs.Server)
        .addConverterFactory(GsonConverterFactory.create(customGsonInstance))
        .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
        .client(client)
        .build();

    getApi = getApiAdapter.create(GetApi.class);
    postApi = postApiAdapter.create(PostApi.class);
  }

  public static QcCloudClient getApi() {
    if (client == null) {
      return new QcCloudClient();
    } else {
      return client;
    }
  }


  public interface GetApi {

    @GET("/api/csrftoken/") Call<QcResponToken> qcGetToken();

    @POST("/api/users/{id}/") rx.Observable<QcResponUserInfo> qcGetUserInfo(@Path("id") String id);

    @GET("/api/android/coaches/{id}/welcome/") rx.Observable<QcResponDrawer> getDrawerInfo(
        @Path("id") String id);

    @GET("/api/qingcheng/activities/notify/")
    rx.Observable<QcResponseActivities> getActivitiesCount();


    @GET("/api/v1/coaches/{id}/gyms/welcome/") rx.Observable<QcDataResponse<GymDetail>> qcGetGymWelcome(
        @Path("id") String staffid, @Query("id") String id, @Query("model") String model);


    @GET("/api/coaches/{id}/systems/") rx.Observable<QcResponCoachSys> qcGetSystem(
        @Path("id") String id, @Header("Cookie") String session_id);

    @GET("/api/users/{user_id}/brands/") rx.Observable<QcResponseBrands> qcGetBrands(
        @Path("user_id") String id);

    @GET("/api/v1/coaches/{coach_id}/brands/") rx.Observable<QcResponseBrands> qcGetTrainerBrands(
        @Path("coach_id") String id);

    @GET("/api/brands/{id}") rx.Observable<QcResponse> qcGetBrandDetail(
        @Path("id") String brand_id);

    //获取用户详情
    @GET("/api/coaches/{id}/detail/") rx.Observable<QcMyhomeResponse> qcGetDetail(
        @Path("id") String id);

    @GET("/api/v2/coaches/{coach_id}/permissions/")
    rx.Observable<QcResponsePermission> qcGetPermission(@Path("coach_id") String coach_id,
        @QueryMap HashMap<String, Object> params);

    //获取销售 卖卡  包含销售和教练
    @GET("/api/coaches/{coach_id}/sellers/") rx.Observable<QcDataResponse<SalerListWrap>> qcGetSalers(@Path("coach_id") String staff_id,
        @QueryMap HashMap<String, Object> params);

    //
    //        //获取通知 分页和不分页接口 ,后者只为拿 未读
    //        @GET("/api/messages/")
    //        rx.Observable<QcNotificationResponse> qcGetMessages(@QueryMap HashMap<String, Integer> params);
    //        @GET("/api/messages/")
    //        rx.Observable<QcNotificationResponse> qcGetMessages();

    //获取通知 分页和不分页接口 ,后者只为拿 未读
    @GET("/api/notifications/") rx.Observable<QcNotificationResponse> qcGetMessages(
        @Query("coach_id") int id, @QueryMap HashMap<String, Object> params);

    @GET("/api/notifications/?tab=COACH_0") rx.Observable<QcNotificationResponse> qcGetMessages(
        @Query("coach_id") int id);

    //通知详情
    @GET("/api/messages/{id}/") rx.Observable<QcNotiDetailResponse> qcGetMsgDetails(
        @Path("id") int id);

    //教练详情
    @GET("/api/coaches/{id}/") rx.Observable<QcCoachRespone> qcGetCoach(@Path("id") int id);

    @Deprecated //版本号 现在走fir
    @GET("/api/app/version/") rx.Observable<QcVersionResponse> qcGetVersion();

    //获取认证列表
    @GET("/api/coaches/{id}/certificates/") rx.Observable<QcCertificatesReponse> qcGetCertificates(
        @Path("id") int id);

    //获取工作经验列表
    @GET("/api/coaches/{id}/experiences/") rx.Observable<QcExperienceResponse> qcGetExperiences(
        @Path("id") int id);

    //获取单条工作经验
    @GET("/api/experiences/{id}/") rx.Observable<OneExperienceResponse> qcGetExperience(
        @Path("id") int id);

    //获取认证列表
    @GET("/api/certificates/{id}/")
    rx.Observable<QcCertificateDetailResponse> qcGetCertificateDetail(@Path("id") int id);

    //获取评价 评分
    @GET("/api/coaches/{id}/evaluate/") rx.Observable<QcEvaluateResponse> qcGetEvaluate(
        @Path("id") int id);

    //搜索健身房
    @GET("/api/gym/search/") rx.Observable<QcSerachGymRepsonse> qcSearchGym(
        @QueryMap Map<String, String> params);        //搜索健身房

    //搜索热门健身房
    @GET("/api/gym/") rx.Observable<QcSerachGymRepsonse> qcHotGym(
        @QueryMap Map<String, String> params);

    //搜索机构
    @GET("/api/organizations/search/") rx.Observable<QcSearchOrganResponse> qcSearchOrganization(
        @QueryMap Map<String, String> params);

    //热门机构
    @GET("/api/organizations/") rx.Observable<QcSearchOrganResponse> qcHotOrganization(
        @QueryMap Map<String, String> params);

    //获取教练日程
    @Deprecated @GET("/api/coaches/{id}/schedules/")
    rx.Observable<QcSchedulesResponse> qcGetCoachSchedule(@Path("id") int id,
        @QueryMap Map<String, String> params);

    @GET("/api/v1/coaches/{id}/schedules/") rx.Observable<QcSchedulesResponse> qcGetCoachScheduleV1(
        @Path("id") int id, @QueryMap Map<String, String> params);

    //获取教练预约概览
    @GET("/api/v2/coaches/{id}/reports/schedules/glance/")
    rx.Observable<QcReportGlanceResponse> qcGetCoachReportGlance(@Path("id") int id,
        @QueryMap HashMap<String, Object> params);

    //获取教练预约概览
    @GET("/api/v2/coaches/{id}/reports/sale/glance/")
    rx.Observable<QcSaleGlanceResponse> qcGetCoachSaleGlance(@Path("id") int id,
        @QueryMap HashMap<String, Object> params);

    //获取教练系统
    @Deprecated @GET("/api/coaches/{id}/systems/")
    rx.Observable<QcCoachSystemResponse> qcGetCoachSystem(@Path("id") int id);

    //获取教练系统
    @GET("/api/v1/coaches/{id}/services/") rx.Observable<QcCoachServiceResponse> qcGetCoachService(
        @Path("id") int id);

    //获取教练系统带权限
    @GET("/api/v2/coaches/{id}/method/shops/")
    rx.Observable<QcCoachServiceResponse> qcGetCoachServicePermission(@Path("id") int id,
        @Query("key") String key);

    //获取教练系统
    @GET("/api/coaches/{id}/systems/detail/")
    rx.Observable<QcCoachSystemDetailResponse> qcGetCoachSystemDetail(@Path("id") int id);

    @GET("/api/v1/coaches/{id}/reports/schedules/")
    rx.Observable<QcStatementDetailRespone> qcGetStatementDatail(@Path("id") int id,
        @QueryMap Map<String, String> params);

    @Deprecated @GET("/api/v1/services/detail/")
    rx.Observable<QcServiceDetialResponse> qcGetServiceDetail(@QueryMap Map<String, String> params);

    //获取教练销售 充值卡信息
    @GET("/api/v1/coaches/{id}/reports/sale/cardtpls/")
    rx.Observable<QcCardsResponse> qcGetSaleCard(@Path("id") int id);

    //获取教练课程
    @GET("/api/coaches/{id}/systems/courses/")
    rx.Observable<QcDataResponse<CourseTypeSample>> qcGetSystemCourses(@Path("id") int id,
        @QueryMap HashMap<String, Object> params);

    //获取教练某个系统下的学员
    @GET("/api/coaches/{id}/systems/users/") rx.Observable<QcStudentResponse> qcGetSystemStudent(
        @Path("id") int id, @QueryMap Map<String, String> params);

    //获取教练所有学员
    @GET("/api/v2/coaches/{id}/students/") rx.Observable<QcAllStudentResponse> qcGetAllStudent(
        @Path("id") int id, @QueryMap HashMap<String, Object> params);

    @GET("api/v2/coaches/{id}/cashier/users/")
    rx.Observable<QcDataResponse<StudentBeanListWrapper>> qcLoadStudentByPhone(@Path("id") String id,
        @QueryMap Map<String, Object> params);

    //获取所有课程计划
    @GET("/api/v2/coaches/{id}/plantpls/") rx.Observable<QcAllCoursePlanResponse> qcGetAllPlans(
        @Path("id") int id, @QueryMap Map<String, Object> params);

    //获取所有课程计划
    @GET("/api/v2/coaches/{id}/plantpls/?show_all=1")
    rx.Observable<QcAllCoursePlanResponse> qcGetGymAllPlans(@Path("id") int id,
        @QueryMap Map<String, Object> params);

    //获取所有健身房充值卡
    @GET("/api/coaches/{id}/systems/cardtpls/")
    rx.Observable<QcSystemCardsResponse> qcGetSystemCard(@Path("id") int id,
        @QueryMap Map<String, String> params);

    @GET("/api/android/coaches/{id}/") rx.Observable<QcDrawerResponse> qcGetDrawerInfo(
        @Path("id") int id, @QueryMap Map<String, String> params);

    //获取预约预览
    @GET("/api/v1/coaches/{id}/schedules/glance/")
    rx.Observable<QcScheduleGlanceResponse> qcGetScheduleGlance(@Path("id") int id,
        @QueryMap Map<String, String> params);

    //获取个人的健身房
    @GET("/api/coaches/{id}/personal/system/") rx.Observable<QcPrivateGymReponse> qcGetPrivateGym(
        @Path("id") int id);

    @GET("/api/v1/coaches/{id}/shop/") rx.Observable<QcGymDetailResponse> qcGetGymDetail(
        @Path("id") int id, @QueryMap Map<String, String> params);

    @GET("/api/meetings/") rx.Observable<QcMeetingResponse> qcGetMeetingList(
        @QueryMap Map<String, String> params);

    //所有的团课排期
    @GET("/api/v1/coaches/{coach_id}/batches/{batch_id}/{schedules}/?order_by=start")
    rx.Observable<QcBatchResponse> qcGetGroupManageDetail(@Path("coach_id") int coach_id,
        @Path("batch_id") String batch_id, @Path("schedules") String schedules,
        @QueryMap Map<String, String> params);

    //排期列表
    @GET("/api/v1/coaches/{coach_id}/courses/{course_id}/batches/")
    rx.Observable<GetBatchesResponse> qcGetGroupManage(@Path("coach_id") int coach_id,
        @Path("course_id") String course_id, @QueryMap Map<String, String> params);

    @GET("/api/v1/coaches/{coach_id}/courses/{course_id}/")
    rx.Observable<QcOneCourseResponse> qcGetOneCourse(@Path("coach_id") int coach_id,
        @Path("course_id") String course_id, @QueryMap Map<String, String> params);

    //学员基础信息
    @GET("/api/v2/coaches/{coach_id}/students/{id}/")
    rx.Observable<StudentInfoResponse> qcGetStudentInfo(@Path("coach_id") String coach_id,
        @Path("id") String student_id, @QueryMap Map<String, String> params);

    //学员课程列表
    @GET("/api/students/{id}/schedules/") rx.Observable<StudentCourseResponse> qcGetStuedntCourse(
        @Path("id") String student_id, @QueryMap Map<String, String> params);

    //学员卡列表
    @GET("/api/students/{id}/cards/") rx.Observable<StudentCarsResponse> qcGetStuedntCard(
        @Path("id") String student_id, @QueryMap Map<String, String> params);

    //学员卡
    @GET("/api/students/{id}/measures/") rx.Observable<BodyTestReponse> qcGetStuedntBodyTest(
        @Path("id") String student_id, @QueryMap Map<String, String> params);

    //体测模板接口
    @GET("/api/measures/tpl/") rx.Observable<QcBodyTestTemplateRespone> qcGetBodyTestModel(
        @QueryMap Map<String, String> params);

    //获取体测数据
    @GET("/api/measures/{measure_id}/") rx.Observable<QcGetBodyTestResponse> qcGetBodyTest(
        @Path("measure_id") String measure_id, @QueryMap Map<String, String> params);

    /**
     * 课程
     */

    //获取健身房课程列表
    @GET("/api/v2/coaches/{id}/courses/?&show_all=1")
    rx.Observable<QcResponseCourseList> qcGetCourses(@Path("id") String coach_id,
        @QueryMap HashMap<String, Object> params, @Query("is_private") int is_private);

    //获取健身房全部课程列表
    @GET("/api/v2/coaches/{id}/courses/?&show_all=1")
    rx.Observable<QcDataResponse<CourseTypeSamples>> qcGetAllCourses(@Path("id") String coach_id,
        @QueryMap HashMap<String, Object> params);

    @GET("/api/v2/coaches/{id}/courses/?&show_all=1")
    rx.Observable<QcResponseCourseList> qcGetCoursesAll(@Path("id") String coach_id,
        @QueryMap HashMap<String, Object> params);

    /**
     * 获取课程计划
     */
    @GET("/api/v1/coaches/{coach_id}/plantpls/?show_all=1")
    rx.Observable<QcResponseCoursePlan> qcGetCoursePlan(@Path("coach_id") String id,
        @QueryMap HashMap<String, Object> params);

    /**
     * 获取课程计划
     */
    @GET("/api/v2/coaches/{coach_id}/plantpls/all/?show_all=1")
    rx.Observable<QcResponseCoursePlan> qcGetCoursePlanAll(@Path("coach_id") String id);

    /**
     * 获取课程下教练
     */
    @GET("/api/v1/coaches/{coach_id}/courses/teachers/")
    rx.Observable<QcResponseCourseTeacher> qcGetCourseTeacher(@Path("coach_id") String coach_id,
        @Query("course_id") String id, @QueryMap HashMap<String, Object> params);

    /**
     * 课程下照片
     */
    @GET("/api/v2/coaches/{coach_id}/courses/schedules/photos/")
    rx.Observable<QcResponseSchedulePhotos> qcGetSchedulePhotos(@Path("coach_id") String coach_id,
        @Query("course_id") String id, @Query("page") int page,
        @QueryMap HashMap<String, Object> params);

    /**
     * 获取课程详情
     */
    @GET("/api/v1/coaches/{coach_id}/courses/{course_id}/")
    rx.Observable<QcResponseCourseDetail> qcGetCourseDetail(@Path("coach_id") String coach_id,
        @Path("course_id") String id, @QueryMap HashMap<String, Object> params);

    /**
     * 分场馆评分
     */
    @GET("/api/v1/coaches/{coach_id}/courses/shops/score/")
    rx.Observable<QcResponseShopComment> qcGetShopComment(@Path("coach_id") String coach_id,
        @Query("course_id") String id, @QueryMap HashMap<String, Object> params);

    @GET("/api/v2/coaches/{coach_id}/courses/photos/") rx.Observable<QcResponseJacket> qcGetJacket(
        @Path("coach_id") String id, @Query("course_id") String course_id,
        @QueryMap HashMap<String, Object> params);

    /**
     * ***********************************    end     ********************
     */

    /**
     * 工作人员权限
     */
    @GET("/api/v2/coaches/{id}/staffs/permissions/")
    rx.Observable<QcResponsePermission> qcStaffPmission(@Path("id") String coach_id,
        @QueryMap HashMap<String, Object> params);
    //        @GET("/api/v1/coaches/{id}/permissions/")
    //        rx.Observable<QcResponsePermission> qcPermission(@Path("id") String coach_id, @QueryMap HashMap<String, Object> params);

    //获取团课排课
    @GET("/api/v1/coaches/{id}/group/courses/")
    rx.Observable<QcResponseGroupCourse> qcGetGroupCourse(@Path("id") String coach_id,
        @Query("id") String gym_id, @Query("model") String gym_model,
        @Query("brand_id") String brand_id);

    //
    //获取私教排课
    @GET("/api/v1/coaches/{id}/private/coaches/")
    rx.Observable<QcResponsePrivateCourse> qcGetPrivateCrourse(@Path("id") String coach_id,
        @Query("id") String gym_id, @Query("model") String gym_model,
        @Query("brand_id") String brand_id);

    //获取团课排期
    @GET("/api/v1/coaches/{id}/batches/") rx.Observable<QcResponseGroupDetail> qcGetGroupCourses(
        @Path("id") String coach_id, @Query("id") String gym_id, @Query("model") String gym_model,
        @Query("is_private") int isPrivate);

    @GET("/api/v1/coaches/{id}/{type}/{single_id}/")
    rx.Observable<QcResponseSingleBatch> qcGetSingleBatch(@Path("id") String coach_id,
        @Path("type") String type, @Path("single_id") String single_id,
        @QueryMap HashMap<String, Object> params);
    //
    ////排课填充
    //@GET("/api/v1/coaches/{id}/{type}/arrange/template/") rx.Observable<QcResponseBtachTemplete> qcGetBatchTemplate(
    //    @Path("id") String id, @Path("type") String type, @Query("id") String gymid, @Query("model") String gymmodel,
    //    @Nullable @Query("teacher_id") String teacher_id, @Query("course_id") String course_id);

    //获取某个排期的详情
    @GET("/api/v1/coaches/{id}/batches/{batch_id}/")
    rx.Observable<QcResponsePrivateBatchDetail> qcGetBatchDetail(@Path("id") String coach_id,
        @Path("batch_id") String batch_id, @Query("id") String gym_id,
        @Query("model") String gym_model);

    //获取场地列表
    @GET("/api/v1/coaches/{coach_id}/spaces/") rx.Observable<QcResponseSpaces> qcGetSpace(
        @Path("coach_id") String id, @Query("id") String gymid, @Query("model") String gymmodel);

    //拉去卡列表
    @GET("/api/v2/coaches/{coach_id}/cardtpls/") rx.Observable<QcResponseCardTpls> qcGetCardTpls(
        @Path("coach_id") String id, @Query("id") String gymid, @Query("model") String gymmodel);

    @GET("/api/v1/coaches/{coach_id}/gyms/welcome/")
    rx.Observable<QcResponseServiceDetail> qcGetCoachServer(@Path("coach_id") String id,
        @QueryMap HashMap<String, Object> params);

    /**
     * 品牌相关
     */
    @GET("/api/v2/coaches/{coach_id}/staffs/brands/{brand_id}/shops/")
    rx.Observable<QcReponseBrandDetailShops> qcGetBrandShops(@Path("coach_id") String coach_id,
        @Path("brand_id") String brand_id);

    /**
     * 照片墙
     */
    @GET("/api/coaches/photos/") rx.Observable<QcResponeSingleImageWall> qcGetImageWalls();

    /**
     * 获取订单
     */
    @GET("/api/order-center/") rx.Observable<QcResponsePage> qcGetOrderList();

    /**
     * 聊天相关
     */
    @GET("/api/im/gym/contacts/")
    rx.Observable<QcDataResponse<ChatFriendsData>> qcQueryChatFriends();

    /*
     *
     *  评论相关
     */
    @GET("/api/news/{news_id}/comment/")
    rx.Observable<QcDataResponse<ArticleCommentListData>> qcQueryComments(
        @Path("news_id") String id, @QueryMap HashMap<String, Object> params);

    @GET("/api/my/news/replies/")
    rx.Observable<QcDataResponse<ArticleCommentListData>> qcQueryReplies(
        @QueryMap HashMap<String, Object> params);

    @GET("/api/v2/notifications/?order_by=-created_at")
    rx.Observable<QcDataResponse<Notification>> qcGetNotification(
        @QueryMap HashMap<String, Object> query);

    @GET("/api/v2/notifications/index/")
    rx.Observable<QcDataResponse<List<NotificationGlance>>> qcGetNotificationIndex(
        @Query("type_json") String query);

    //报表
    @GET("/api/v2/coaches/{id}/reports/schedules/glance/")
    rx.Observable<QcDataResponse<StatementGlanceResp>> qcGetReportGlance(@Path("id") int id,
        @Query("brand_id") String brand_id, @Query("shop_id") String shop_id,
        @Query("id") String gymid, @Query("model") String model);

    @GET("/api/staffs/{id}/reports/sells/glance/")
    rx.Observable<QcDataResponse<StatementGlanceResp>> qcGetSaleGlance(@Path("id") int id,
        @Query("brand_id") String brand_id, @Query("shop_id") String shop_id,
        @Query("id") String gymid, @Query("model") String model);

    @GET("/api/v2/coaches/{id}/reports/schedules/")
    rx.Observable<QcResponseStatementDetail> qcGetStatementDatail(@Path("id") int id,
        @Query("start") String start, @Query("end") String end,
        @QueryMap HashMap<String, Object> params);

    @GET("/api/coaches/{staff_id}/sellers/")
    rx.Observable<QcDataResponse<Sellers>> qcGetSalersAndCoach(@Path("staff_id") int staff_id,
        @Query("brand_id") String brandid, @Query("id") String gymid, @Query("model") String model);

    @GET("/api/v2/coaches/{id}/method/courses/?&show_all=1")
    rx.Observable<QcDataResponse<CourseTypeSamples>> qcGetCoursesPermission(
        @Path("id") int staff_id, @Query("is_private") int is_private,
        @QueryMap HashMap<String, Object> params);

    @GET("/api/v2/coaches/{staff_id}/reports/schedules/{schedule_id}/")
    rx.Observable<QcDataResponse<CourseReportDetail>> qcGetCourseReportDetail(
        @Path("staff_id") String staffId, @Path("schedule_id") String scheduleId,
        @QueryMap HashMap<String, Object> params);

    @GET("/api/v2/coaches/{id}/reports/sells/") rx.Observable<QcResponseSaleDetail> qcGetSaleDatail(
        @Path("id") String id,

        @Query("start") String start, @Query("end") String end,
        @QueryMap HashMap<String, Object> params);

    //获取某个健身房的卡模板
    @GET("/api/v2/coaches/{id}/cardtpls/?show_all=1")
    rx.Observable<QcDataResponse<GymCardtpl>> qcGetGymCardtpl(@Path("id") String id,
        @QueryMap HashMap<String, Object> params, @Query("type") String type);

    @GET("/api/v2/staffs/{id}/cardtpls/all/?show_all=1&order_by=-id")
    rx.Observable<QcDataResponse<CardTplListWrap>> qcGetCardTpls(@Path("id") String id,
        @QueryMap HashMap<String, Object> params, @Query("type") String type,
        @Query("is_enable") String isEnable);

    //获取消息首页求职招聘信息列表
    @GET("/api/user/job/records/")
    rx.Observable<QcDataResponse<RecordWrap>> qcGetRecruitMessageList();

    //判断是否同意用户协议
    @GET(" /api/user/check/read_agreement/")
    rx.Observable<cn.qingchengfit.network.response.QcDataResponse<CheckProtocolModel>> qcCheckProtocol(
        @QueryMap HashMap<String, Object> params);

    //获取场馆类型
    @GET("/api/v2/gym/types/")rx.Observable<QcDataResponse<GymTypeData>> qcGetGymType();
  }

  public interface PostApi {
    @GET("/api/csrftoken/") Call<QcResponToken> qcGetToken();

    @PUT("/api/v2/coaches/{coach_id}/gyms/update/") rx.Observable<QcResponse> qcUpdateGym(
        @Path("coach_id") String id, @QueryMap HashMap<String, Object> params, @Body Shop shop);

    //登录
    @POST("/api/user/login/?session_config=true") rx.Observable<QcResponLogin> qcLogin(
        @Body LoginBean loginBean);

    //注册
    @POST("/api/user/register/?session_config=true") rx.Observable<QcResponLogin> qcRegister(
        @Body RegisteBean params);

    //创建品牌
    @POST("/api/brands/") rx.Observable<QcResponsCreatBrand> qcCreatBrand(
        @Body CreatBrandBody body);

    //修改品牌
    @PUT("/api/brands/{id}/") rx.Observable<QcResponsCreatBrand> qcEditBrand(@Path("id") String id,
        @Body BrandBody body);

    @POST("/") rx.Observable<QcResponse> qcChangeBrandUser(@Path("id") String brandid,
        ChangeBrandCreatorBody body);

    //初始化系统
    @POST("/api/coach/systems/initial/") rx.Observable<QcResponseSystenInit> qcInit(
        @Body CoachInitBean body);

    //教练离职
    @POST("/api/v2/coaches/{coach_id}/dimission/") rx.Observable<QcResponseSystenInit> qcQuitGym(
        @Path("coach_id") String coachid, @QueryMap HashMap<String, Object> params);

    //获取电话验证码
    @POST("/api/send/verify/") rx.Observable<QcResponse> qcGetCode(@Body GetCodeBean account);

    @POST("/api/check/verify/") rx.Observable<QcResponCode> qcCheckCode(@Body CheckCode checkCode);

    @POST("/api/users/phone/check/") rx.Observable<QcResponCheckPhone> qcCheckPhone(
        @Body CheckPhoneBean phone);

    @PUT("/api/users/{id}/") rx.Observable<QcResponse> qcModifyInfo(@Path("id") String id);

    ////修改密码
    //@POST("/api/coaches/{id}/change/password/") rx.Observable<QcResponse> qcMoidfyPw(@Path("id") int id,
    //    @Body ModifyPwBean modifyPwBean);

    //发送意见
    @POST("/api/feedback/") rx.Observable<QcEvaluateResponse> qcFeedBack(@Body FeedBackBean bean);

    //新增认证
    @POST("/api/certificates/") rx.Observable<QcResponse> qcAddCertificate(
        @Body AddCertificate addExperience);

    //修改认证
    @PUT("/api/certificates/{id}/") rx.Observable<QcResponse> qcEditCertificate(@Path("id") int id,
        @Body AddCertificate addExperience);

    //删除认证
    @DELETE("/api/certificates/{id}/") rx.Observable<QcResponse> qcDelCertificate(
        @Path("id") int id);

    //新增工作经验
    @POST("/api/experiences/") rx.Observable<QcResponse> qcAddExperience(
        @Body AddWorkExperience addWorkExperience);

    //修改工作经验
    @PUT("/api/experiences/{id}/") rx.Observable<QcResponse> qcEditExperience(@Path("id") int id,
        @Body AddWorkExperience addWorkExperience);

    @PUT("/api/experiences/{id}/") rx.Observable<QcResponse> qcEditSyncExperience(
        @Path("id") int id, @Body SyncExpBody addWorkExperience);

    @POST("/api/experiences/{id}/hidden/") rx.Observable<QcResponse> qcHidenExperience(
        @Path("id") int id, @Body HidenBean hidenBean);

    @POST("/api/certificates/{id}/hidden/") rx.Observable<QcResponse> qcHidenCertificates(
        @Path("id") int id, @Body HidenBean hidenBean);

    //删除工作经验
    @DELETE("/api/experiences/{id}/") rx.Observable<QcResponse> qcDelExperience(@Path("id") int id);

    //修改健身房
    @PUT("/api/gym/{id}/") rx.Observable<QcResponse> qcEditGym(
            @Path("id") String id, @QueryMap HashMap<String, Object> params, @Body Shop shop);

    //删除健身房
    @DELETE("/api/gym/{id}/") rx.Observable<QcResponse> qcDelGym(@Path("id") String id);

    //新增组织
    @POST("/api/organizations/") rx.Observable<QcAddOrganizationResponse> qcAddOrganization(
        @Body OrganizationBean organizationBean);

    ////修改电话号码
    //@POST("/api/coaches/{id}/change/phone/") rx.Observable<QcResponse> qcModifyPhoneNum(@Path("id") int id,
    //    @Body ModifyPhoneNum modifyPwBean);

    //修改个人描述
    @POST("/api/coaches/{id}/change/description/") rx.Observable<QcResponse> qcModifyDes(
        @Path("id") int id, @Body ModifyDes modifyDes);

    //上传手机用户
    @POST("/api/coaches/{id}/systems/users/bulk/create/")
    rx.Observable<QcResponse> qcPostCreatStudents(@Path("id") int id, @Body PostStudents students);

    //修改个人健身房
    @PUT("/api/coaches/{id}/personal/system/") rx.Observable<QcResponse> qcPostPrivateGym(
        @Path("id") int id, @Body PostPrivateGym gym);

    //新建健身房
    @POST("/api/coaches/{id}/personal/system/") rx.Observable<QcResponse> qcCreateGym(
        @Path("id") int id, @Body HashMap<String, Object> body);

    //新建个人健身房
    @POST("/api/coaches/{id}/personal/system/") rx.Observable<QcResponse> qcCreatePrivateGym(
        @Path("id") int id, @Body PostPrivateGym gym);

    //删除个人健身房
    @DELETE("/api/coaches/{id}/personal/system/") rx.Observable<QcResponse> qcDelPrivateGym(
        @Path("id") int id);

    @Deprecated
    //清除notification
    @POST("/api/messages/clear/") rx.Observable<QcResponse> qcClearNotification();

    //百度pushid绑定
    @POST("/api/coaches/{id}/push/update/") rx.Observable<QcResponse> qcPostPushId(
        @Path("id") int id, @Body PushBody body);

    //清除notification
    @PUT("/api/notifications/clear/?tab=COACH_0") rx.Observable<QcResponse> qcClearAllNotification(
        @Query("coach_id") int id);
    //@PUT("/api/notifications/clear/?tab=COACH_0")
    //rx.Observable<QcResponse> qcClearAllNotification(@Query("coach_id") int id);

    //清除某条notification
    @PUT("/api/notifications/clear/") rx.Observable<QcResponse> qcClearOneNotification(
        @Query("coach_id") int id, @Query("id") String notiId);

    @POST("/api/v1/coaches/{id}/courses/") rx.Observable<AddCoourseResponse> qcAddCourse(
        @Path("id") int id, @Body AddCourse addCourse);

    @PUT("/api/v1/coaches/{id}/courses/") rx.Observable<QcResponse> qcEditCourse(@Path("id") int id,
        @Body AddCourse addCourse);

    //        @DELETE("/api/v1/coaches/{id}/courses/")
    //        rx.Observable<QcResponse> qcDelCourse(@Path("id") int id, @QueryMap HashMap<String,String> params);

    @POST("/api/v1/coaches/{id}/students/add/") rx.Observable<QcResponse> qcAddStudent(
        @Path("id") int id, @Body AddStudentBean StudentBean);

    @POST("/api/v1/coaches/{id}/students/add/") rx.Observable<QcResponse> qcAddStudents(
        @Path("id") int id, @Body PostStudents addStudentBeans,
        @QueryMap HashMap<String, Object> params);

    //批量排课
    @POST("/api/v1/coaches/{id}/schedules/batches/") rx.Observable<QcResponse> qcAddCourseManage(
        @Path("id") int id, @Body AddBatchCourse addBatchCourse);

    @POST("/api/v1/coaches/{id}/{schedules}/bulk/delete/")
    rx.Observable<QcResponse> qcDelCourseManage(@Path("id") int id,
        @Path("schedules") String schedules, @Body DelCourseManage delCourseManage);

    //修改单挑排期

    /**
     * @param schedules 私教 timetables
     */
    @Deprecated @PUT("/api/v0/coaches/{coach_id}/{schedules}/{schedule_id}/")
    rx.Observable<QcResponse> qcFixBatch(@Path("coach_id") int coach_id,
        @Path("schedule_id") String schedule_id, @Path("schedules") String schedules,
        @Body FixBatchBean batchBean);

    @PUT("/api/v1/coaches/{id}/{type}/{single_id}/") rx.Observable<QcResponse> qcUpdateSinglebatch(
        @Path("id") String staff_id, @Path("type") String type,
        @Path("single_id") String scheduleid, @Body SingleBatchBody body);

    @POST("/api/measures/") rx.Observable<QcResponse> qcAddBodyTest(
        @Body AddBodyTestBean addBodyTestBean);

    @PUT("/api/measures/{measure_id}/") rx.Observable<QcResponse> qcUpdateBodyTest(
        @Path("measure_id") String id, @Body AddBodyTestBean addBodyTestBean);

    @DELETE("/api/measures/{measure_id}/") rx.Observable<QcResponse> qcDelBodyTest(
        @Path("measure_id") String id, @QueryMap Map<String, String> params);

    //删除学员
    @DELETE("/api/v2/coaches/{coach_id}/students/{id}/") rx.Observable<QcResponse> qcDelStudent(
        @Path("coach_id") String coach_id, @Path("id") String studentid,
        @QueryMap Map<String, String> params);

    /**
     * 创建课程
     */
    @POST("/api/v2/coaches/{id}/courses/") rx.Observable<QcResponse> qcCreateCourse(
        @Path("id") String coachid, @Body CourseBody courseBody,
        @QueryMap HashMap<String, Object> params);

    //修改课程
    @PUT("/api/v2/coaches/{id}/courses/{course_id}/") rx.Observable<QcResponse> qcUpdateCourse(
        @Path("id") String coachid, @Path("course_id") String course_id,
        @QueryMap HashMap<String, Object> params, @Body CourseBody courseBody);

    //删除课程
    @DELETE("/api/v2/coaches/{id}/courses/{course_id}/") rx.Observable<QcResponse> qcDelCourse(
        @Path("id") String coachid, @Path("course_id") String course_id,
        @QueryMap HashMap<String, Object> params);

    //修改封面
    @POST("/api/v2/coaches/{id}/courses/photos/") rx.Observable<QcResponse> qcEditJacket(
        @Path("id") String coachid, @Query("course_id") String course_id,
        @QueryMap HashMap<String, Object> params, @Body EditJacketBody body);

    //修改课程适用场馆
    @PUT("/api/v2/coaches/{coach_id}/courses/{course_id}/shops/")
    rx.Observable<QcResponse> qcEditCourseShops(@Path("coach_id") String coachid,
        @Path("course_id") String course_id, @Body HashMap<String, Object> params);

    /**
     * 排期
     */
    @POST("/api/v1/coaches/{id}/arrange/batches/") rx.Observable<QcResponse> qcArrangeBatch(
        @Path("id") String coach_id, @Query("id") String gymid, @Query("model") String model,
        @Body ArrangeBatchBody body);

    //修改排期
    @PUT("/api/v1/coaches/{id}/batches/{batchid}/") rx.Observable<QcResponse> qcUpdateBatch(
        @Path("id") String coach_id, @Path("batchid") String batchid, @Query("id") String gymid,
        @Query("model") String model, @Body ArrangeBatchBody body);

    //排期检查
    @POST("/api/v1/coaches/{id}/{type}/arrange/check/") rx.Observable<QcResponse> qcCheckBatch(
        @Path("id") String coach_id, @Path("type") String type, @Query("id") String gymid,
        @Query("model") String model, @Body ArrangeBatchBody body);

    //删除排期
    //@DELETE("/api/v1/coaches/{id}/batches/{batchid}/") rx.Observable<QcResponse> qcDelBatch(@Path("id") String coach_id,
    //    @Path("batchid") String batchid, @Query("id") String gymid, @Query("model") String model);
    @DELETE("/api/v1/coaches/{coach_id}/batches/{batch_id}/") rx.Observable<QcResponse> qcDelBatch(
        @Path("coach_id") String coach_id, @Path("batch_id") String batch_id,
        @QueryMap HashMap<String, Object> params);

    /**
     * 扫码
     */
    @PUT("/api/scans/{uuid}/") rx.Observable<QcResponse> qcScans(@Path("uuid") String uuid,
        @Body ScanBody body);

    /**
     * 照片墙添加个人照片
     */
    @POST("/api/coaches/photos/") rx.Observable<QcResponeSingleImageWall> qcUploadWallImage(
        @Body HashMap<String, Object> body);

    @DELETE("/api/coaches/photos/") rx.Observable<QcResponse> qcDeleteWallImage(
        @Query("ids") String ids);

    //文章评论
    @POST("/api/news/{newsid}/comment/") rx.Observable<QcResponse> qcAddComment(
        @Path("newsid") String news_id, @Body PostCommentBody body);

    @PUT("/api/v2/notifications/") rx.Observable<QcResponse> qcClearTypeNoti(
        @Body ClearNotiBody body);
  }

  public interface DownLoadApi {
    @GET("/") Response qcDownload();
  }

  //public interface MutiSystemApi {
  //    @POST("/api/cloud/authenticate/") rx.Observable<QcResponSystem> qcGetSession(@Body GetSysSessionBean phone);
  //}
}
