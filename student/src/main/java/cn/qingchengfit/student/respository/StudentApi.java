package cn.qingchengfit.student.respository;

import android.support.annotation.IntRange;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.student.bean.AbsentceListWrap;
import cn.qingchengfit.student.bean.AllotDataResponseWrap;
import cn.qingchengfit.student.bean.AttendanceCharDataBean;
import cn.qingchengfit.student.bean.AttendanceListWrap;
import cn.qingchengfit.student.bean.ClassRecords;
import cn.qingchengfit.student.bean.FollowRecordAdd;
import cn.qingchengfit.student.bean.FollowRecordListWrap;
import cn.qingchengfit.student.bean.FollowRecordStatusListWrap;
import cn.qingchengfit.student.bean.InactiveStat;
import cn.qingchengfit.student.bean.QcStudentBirthdayWrapper;
import cn.qingchengfit.student.bean.SalerListWrap;
import cn.qingchengfit.student.bean.SalerTeachersListWrap;
import cn.qingchengfit.student.bean.SalerUserListWrap;
import cn.qingchengfit.student.bean.ShortMsgBody;
import cn.qingchengfit.student.bean.ShortMsgDetail;
import cn.qingchengfit.student.bean.ShortMsgList;
import cn.qingchengfit.student.bean.SourceBeans;
import cn.qingchengfit.student.bean.StatDate;
import cn.qingchengfit.student.bean.StudentBeanListWrapper;
import cn.qingchengfit.student.bean.StudentInfoGlance;
import cn.qingchengfit.student.bean.StudentListWrappeForFollow;
import cn.qingchengfit.student.bean.StudentListWrapper;
import cn.qingchengfit.student.bean.StudentTransferBean;
import cn.qingchengfit.student.bean.StudentWIthCount;
import io.reactivex.Flowable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

/**
 * Created by huangbaole on 2017/10/25.
 */

public interface StudentApi {
  /**
   * 工作人员下所有会员
   *
   * @GET("/api/staffs/{id}/users/all/?show_all=1")
   */
  @GET("/api/staffs/{id}/users/list/")
  Flowable<QcDataResponse<StudentListWrapper>> qcGetAllStudents(@Path("id") String id,
      @QueryMap Map<String, Object> params);

  @GET("api/v2/staffs/{id}/cashier/users/")
  Flowable<QcDataResponse<StudentBeanListWrapper>>qcLoadStudentByPhone(@Path("id") String id,
      @QueryMap Map<String, Object> params);
  //
  //    /**
  //     * 会员卡可绑定的会员列表
  //     */
  //    @GET("/api/staffs/{staff_id}/method/users/?show_all=1")
  //    Observable<QcDataResponse<StudentListWrapper>> qcGetCardBundldStudents(
  //        @Path("staff_id") String id, @QueryMap HashMap<String, Object> params);
  //
  //    /**
  //     * 教练分配
  //     */
  //    @GET("api/v2/staffs/{staff_id}/coaches/preview/")
  //    Observable<QcDataResponse<AllotDataResponseWrap>> qcGetCoachList(
  //        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);
  //
  //

  //会员卡可绑定的会员列表
  @GET("/api/staffs/{staff_id}/method/users/?show_all=1")
  Flowable<QcDataResponse<StudentBeanListWrapper>> qcGetCardBundldStudents(@Path("staff_id") String id,
      @QueryMap HashMap<String, Object> params);

  //会员卡修改绑定会员列表
  @GET("/api/staffs/{id}/cards/bind/users/")
  Flowable<QcDataResponse<StudentBeanListWrapper>> qcGetBindStudent(@Path("id") String staff,
      @QueryMap HashMap<String, Object> params);

  /**
   * 获取教练分配或者销售分配列表
   * type -coaches 教练
   * - sellers 销售
   */
  @GET("api/v2/staffs/{staff_id}/{type}/preview/")
  Flowable<QcDataResponse<AllotDataResponseWrap>> qcGetStaffList(@Path("staff_id") String staff_id,
      @Path("type") String type, @QueryMap HashMap<String, Object> params);

  //    /**
  //     * 销售列表预览接口
  //     * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/preview/?brand_id=2&shop_id=1
  //     * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/preview/?id=5370&model=staff_gym
  //     */
  //    @GET("/api/v2/staffs/{staff_id}/sellers/preview/")
  //    Observable<QcDataResponse<AllotDataResponseWrap>> qcGetAllotSalesPreView(
  //        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);
  //
  //    /**
  //     * 销售名下会员列表接口(无销售的会员列表不需要传seller_id)
  //     * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?brand_id=2&shop_id=2&seller_id=1
  //     * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?id=5370&model=staff_gym&seller_id=1
  //     */
  //    @GET("/api/v2/staffs/{staff_id}/sellers/users/")
  //    Observable<QcDataResponse<StudentListWrapper>> qcGetAllotSaleOwenUsers(
  //        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  @GET("/api/v2/staffs/{staff_id}/{type}/users/")
  Flowable<QcDataResponse<StudentListWrapper>> qcGetAllotStaffMembers(
      @Path("staff_id") String staff_id, @Path("type") String type,
      @QueryMap HashMap<String, Object> params);

  //
  //
  //    @GET("/api/v2/staffs/{staff_id}/coaches/users/")
  //    Observable<QcDataResponse<StudentWithCoashListWrap>> qcGetCoachStudentDetail(
  //        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);
  //
  //    //获取销售 卖卡  包含销售 不包含教练
  @GET("/api/staffs/{staff_id}/sellers-without-coach/")
  Flowable<QcDataResponse<SalerUserListWrap>> qcGetSalers(@Path("staff_id") String staff_id,
      @Query("brand_id") String brandid, @Query("shop_id") String shopid, @Query("id") String gymid,
      @Query("model") String model);

  /**
   * /**
   * 批量变更销售
   * http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?brand_id=2&shop_id=1
   * http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?id=5370&model=staff_gym
   * PUT {"user_ids":"1,2,3,4,5", "seller_ids":"10,11"}
   *
   * @param body user_ids  seller_ids
   */
  @PUT("/api/v2/staffs/{staff_id}/sellers/users/") Flowable<QcResponse> qcModifySellers(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params,
      @Body HashMap<String, Object> body);

  /**
   * 获取教练列表
   */
  @GET("/api/v2/staffs/{staff_id}/coaches/")
  Flowable<QcDataResponse<SalerTeachersListWrap>> qcGetAllAllocateCoaches(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 分配教练
   */
  @PUT("/api/v2/staffs/{staff_id}/coaches/users/") Flowable<QcResponse> qcAllocateCoach(
      @Path("staff_id") String staff_id, @Body HashMap<String, Object> body);

  //    /**
  //     * 移除教练
  //     */
  //    @POST("/api/v2/staffs/{staff_id}/coaches/users/remove/")
  //    Observable<QcResponse> qcRemoveStudent(@Path("staff_id") String staff_id,
  //        @Body HashMap<String, Object> body);
  //
      /**
       * 移除教练
       */
      @POST("/api/v2/staffs/{staff_id}/{type}/users/remove/")
      Flowable<QcDataResponse> qcRemoveStaff(@Path("staff_id") String staff_id,
          @Path("type") String type, @Body HashMap<String, Object> body);

  //    /**
  //     * 批量移除某个销售名下会员:
  //     * POST http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/remove/?brand_id=2&shop_id=1
  //     * POST http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/remove/?id=5370&model=staff_gym
  //     * POST {"user_ids":"1,3,2", "seller_id":5}
  //     */
  //    @POST("/api/v2/staffs/{staff_id}/sellers/users/remove/")
  //    Observable<QcResponse> qcDeleteStudents(@Path("staff_id") String staff_id,
  //        @Body HashMap<String, Object> body);
  //
  //
  //    /**
  //     * 数据统计
  //     * /api/staffs/:staff_id/users/stat/?brand_id=&shop_id= 或者 id=&model=
  //     * GET参数:status,[start][end][seller_id(无销售seller_id=0)]
  //     */
  //    @GET("/api/staffs/{staff_id}/users/stat/")
  //    Observable<QcDataResponse<FollowUpDataStatistic>> qcGetTrackStudentsStatistics(
  //        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);
  //
  //
  //    /**
  //     * 新增注册
  //     */
  //    @GET("/api/staffs/{staff_id}/users/new/create/")
  //    Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentCreate(
  //        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);
  //

  /**
   * 新增
   */
  @GET("/api/staffs/{staff_id}/users/new/{type}/")
  Flowable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudents(
      @Path("staff_id") String staff_id, @Path("type") String type,
      @QueryMap HashMap<String, Object> params);

  /**
   * 新增跟进
   */
  @GET("/api/staffs/{staff_id}/users/new/follow/")
  Flowable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentFollow(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  //    /**
  //     * 新增学员
  //     */
  //    @GET("/api/staffs/{staff_id}/users/new/member/")
  //    Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentMember(
  //        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);
  //

  /**
   * 具有名下会员的销售列表
   * /api/staffs/:staff_id/filter/sellers/?brand_id=&shop_id= 或者 id=&model=
   */
  @GET("/api/staffs/{staff_id}/filter/sellers/?show_all=1")
  Flowable<QcDataResponse<SalerListWrap>> qcGetTrackStudentsFilterSalers(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);
  /**
   * 具有名下会员的教练列表
   * /api/staffs/:staff_id/filter/sellers/?brand_id=&shop_id= 或者 id=&model=
   */
  @GET("/api/staffs/{staff_id}/filter/coaches/?show_all=1")
  Flowable<QcDataResponse<SalerListWrap>> qcGetTrackStudentsFilterCoaches(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 转换率
   * /api/staffs/:staff_id/users/conver/stat/?brand_id=&shop_id= 或者 id=&model=
   * GET参数:[start] [end] [seller_id(无销售seller_id=0)]
   */
  @GET("/api/staffs/{staff_id}/users/conver/stat/")
  Flowable<QcDataResponse<StudentTransferBean>> qcGetTrackStudentsConver(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 获取缺勤图表数据
   */
  @GET("/api/staffs/{id}/users/attendance/glance/")
  Flowable<QcDataResponse<AttendanceCharDataBean>> qcGetAttendanceChart(@Path("id") String id,
      @QueryMap HashMap<String, Object> params);

  /**
   * 获取缺勤图表数据
   */
  @GET("/api/coaches/{id}/users/attendance/glance/")
  Flowable<QcDataResponse<AttendanceCharDataBean>> qcGetTrainerAttendanceChart(@Path("id") String id,
      @QueryMap HashMap<String, Object> params);


  /**
   * 缺勤列表
   *
   * @param params 缺勤<7： absence__lt=7，
   * 7-30天：absence__gte=7&absence__lte=30,
   * 缺勤>60天：absence__ge=60
   * @return "attendances": [{"user": {"username": "俞小西","gender": 1,"id": 2131,"avatar":
   * "https://img.qingchengfit.cn/9360bb9fb982a95c915edf44f611678f.jpeg!120x120","phone":
   * "18611985427"},"absence": 390,"date_and_time": "2016-01-30 13:30-14:30","id": 5933,"title":
   * "娜娜私教 杨娜娜"},]
   */
  @GET("/api/staffs/{staff_id}/users/absence/")
  Flowable<QcDataResponse<AbsentceListWrap>> qcGetUsersAbsences(@Path("staff_id") String id,
      @QueryMap HashMap<String, Object> params);

  @GET("/api/coaches/{staff_id}/users/absence/")
  Flowable<QcDataResponse<AbsentceListWrap>> qcGetTrainerUsersAbsences(@Path("staff_id") String id,
      @QueryMap HashMap<String, Object> params);
  /**
   * @param params 必传start, end，
   * 可选排序字段 加 “-”说明是倒序
   * order_by=
   * days      -days
   * group     -group
   * private     -private
   * checkin   -checkin
   * @return "attendances": [{"checkin": 0,"group": 139,"user": {"username": "孙正其","gender": 0,"id":
   * 2219,"avatar":
   * "https://img.qingchengfit.cn/a15bec431224aa638a4b8eccb2e96955.jpg!120x120","phone":
   * "18536668518"},"private_count": 8,"days":
   * 142},
   */
  @GET("/api/staffs/{staff_id}/users/attendance/")
  Flowable<QcDataResponse<AttendanceListWrap>> qcGetUsersAttendances(@Path("staff_id") String id,
      @QueryMap HashMap<String, Object> params);

  @GET("/api/coaches/{staff_id}/users/attendance/")
  Flowable<QcDataResponse<AttendanceListWrap>> qcGetTrainerUsersAttendances(@Path("staff_id") String id,
      @QueryMap HashMap<String, Object> params);

  /**
   * 获取未签课的学员
   */
  @GET("/api/staffs/{staff_id}/users/checkin/records/")
  Flowable<QcDataResponse<List<StudentWIthCount>>> qcGetNotSignStudent(
      @Path("staff_id") String staffId, @QueryMap HashMap<String, Object> params);

  @GET("/api/coaches/{staff_id}/users/checkin/records/")
  Flowable<QcDataResponse<List<StudentWIthCount>>> qcGetTrainerNotSignStudent(
      @Path("staff_id") String staffId, @QueryMap HashMap<String, Object> params);


  /**
   * 推荐人列表
   * /api/staffs/:staff_id/users/recommends/?brand_id=&shop_id= 或者 id=&model=
   */
  @GET("/api/staffs/{staff_id}/users/recommends/?show_all=1")
  Flowable<QcDataResponse<SalerUserListWrap>> qcGetTrackStudentsRecommends(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  //

  /**
   * 来源列表
   * /api/v2/staffs/:staff_id/users/origins/?brand_id=&shop_id= 或者 id=&model=
   */
  @GET("/api/staffs/{staff_id}/filter/origins/?show_all=1")
  Flowable<QcDataResponse<SourceBeans>> qcGetTrackStudentsOrigins(@Path("staff_id") String staff_id,
      @QueryMap HashMap<String, Object> params);

  /**
   * 短信部分
   */
  @GET("/api/staffs/{staffid}/group/messages/?order_by=-created_at")
  Flowable<QcDataResponse<ShortMsgList>> qcQueryShortMsgList(@Path("staffid") String id,
      @IntRange(from = 1, to = 2) @Query("status") Integer status,
      @QueryMap HashMap<String, Object> params);

  @GET("/api/staffs/{staffid}/group/message/detail/")
  Flowable<QcDataResponse<ShortMsgDetail>> qcQueryShortMsgDetail(@Path("staffid") String id,
      @Query("message_id") String messageid, @QueryMap HashMap<String, Object> params);

  /**
   * 短信相关
   */
  @POST("/api/staffs/{staffid}/group/messages/") Flowable<QcResponse> qcPostShortMsg(
      @Path("staffid") String staffid, @Body ShortMsgBody body,
      @QueryMap HashMap<String, Object> params);

  @DELETE("/api/staffs/{staffid}/group/message/detail/") Flowable<QcResponse> qcDelShortMsg(
      @Path("staffid") String staffid, @Query("message_id") String messageid,
      @QueryMap HashMap<String, Object> params);

  @PUT("/api/staffs/{staffid}/group/message/detail/") Flowable<QcResponse> qcPutShortMsg(
      @Path("staffid") String staffid, @Body ShortMsgBody body,
      @QueryMap HashMap<String, Object> params);

  //导入导出
  @POST("/api/staffs/{staff_id}/export/do/") Flowable<QcDataResponse> qcDataImport(
      @Path("staff_id") String staff_id, @Body HashMap<String, Object> body);

  //首页-会员首页数据概览
  @GET("/api/staffs/{staff_id}/users/data/glance/")
  Flowable<QcDataResponse<StudentInfoGlance>> qcGetHomePageInfo(@Path("staff_id") String staff_id,
      @QueryMap HashMap<String, Object> params);

  //今日新增-会员分状态按维度统计
  @GET("/api/staffs/{staff_id}/users/dimension/stat/")
  Flowable<QcDataResponse<List<StatDate>>> qcGetIncreaseStat(@Path("staff_id") String staff_id,
      @QueryMap HashMap<String, Object> params);

  //今日跟进-会员分状态按日期范围统计
  @GET("/api/staffs/{staff_id}/users/range/stat/")
  Flowable<QcDataResponse<StatDate>> qcGetFollowStat(@Path("staff_id") String staff_id,
      @QueryMap HashMap<String, Object> params);

  //全部会员-用户不活跃情况统计数据
  @GET("/api/staffs/{staff_id}/users/inactive/stat/")
  Flowable<QcDataResponse<InactiveStat>> qcGetInactiveStat(@Path("staff_id") String staff_id,
      @QueryMap HashMap<String, Object> params);

  //全部会员-销售名下会员列表
  @GET("/api/v2/staffs/{staff_id}/sellers/users/")
  Flowable<QcDataResponse<StudentListWrapper>> qcGetSellerInactiveUsers(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  //会员生日-会员生日统计
  @GET("/api/staffs/{staff_id}/users/birthday/stat/")
  Flowable<QcDataResponse<QcStudentBirthdayWrapper>> qcGetStudentBirthday(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  /**
   * 跟进状态列表
   */
  @GET("/api/staffs/{staff_id}/users/track/status/")
  Flowable<QcDataResponse<FollowRecordStatusListWrap>> qcGetTrackStatus(
      @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

  @POST("/api/staffs/{staff_id}/users/track/status/")
  Flowable<QcDataResponse<Object>> qcAddTrackStatus(@Path("staff_id") String staff_id,
      @Body HashMap<String, Object> params);

  @PUT("/api/staffs/{staff_id}/users/track/status/{track_status_id}/")
  Flowable<QcDataResponse<Object>> qcEditTrackStatus(@Path("staff_id") String staff_id,
      @Path("track_status_id") String track_status_id, @Body HashMap<String, Object> params);

  @DELETE("/api/staffs/{staff_id}/users/track/status/{track_status_id}/")
  Flowable<QcDataResponse<Object>> qcDelTrackStatus(@Path("staff_id") String staff_id,
      @Path("track_status_id") String track_status_id, @QueryMap HashMap<String, Object> params);

  /**
   * 新增跟进记录
   */
  @POST("/api/v2/staffs/{staff_id}/users/{user_id}/records/")
  Flowable<QcDataResponse<Object>> qcAddTrackRecord(@Path("staff_id") String staff_id,
      @Path("user_id") String user_id, @Body FollowRecordAdd body);

  /**
   * 跟进记录列表
   */
  @GET("/api/v2/staffs/{staff_id}/users/{user_id}/records/?format=app&order_by=created_at&show_all=1")
  Flowable<QcDataResponse<FollowRecordListWrap>> qcGetTrackRecords(
      @Path("staff_id") String staff_id, @Path("user_id") String user_id,
      @QueryMap HashMap<String, Object> params);

  /**
   *  教练名下所有会员
   */
  @GET("/api/v2/coaches/{id}/students/") Flowable<QcDataResponse<StudentListWrapper>> qcGetAllStudentWithCoach(
      @Path("id") String id, @QueryMap HashMap<String, Object> params);

  //获取某个学员的课程
  @GET("/api/staffs/{staff_id}/users/attendance/records/")
  Flowable<QcDataResponse<ClassRecords>> qcGetStudentClassRecords(
      @Path("staff_id") String staffid, @Query("user_id") String studentid,
      @QueryMap HashMap<String, Object> params);
}
