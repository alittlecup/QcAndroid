package cn.qingchengfit.saasbase.student.network.api;

import java.util.HashMap;
import java.util.List;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.staff.network.response.SalerListWrap;
import cn.qingchengfit.saasbase.staff.network.response.SalerTeachersListWrap;
import cn.qingchengfit.saasbase.staff.network.response.SalerUserListWrap;
import cn.qingchengfit.saasbase.student.bean.StudentWIthCount;
import cn.qingchengfit.saasbase.student.network.body.AbsentceListWrap;
import cn.qingchengfit.saasbase.student.network.body.AllotDataResponseWrap;
import cn.qingchengfit.saasbase.student.network.body.AttendanceCharDataBean;
import cn.qingchengfit.saasbase.student.network.body.AttendanceListWrap;
import cn.qingchengfit.saasbase.student.network.body.FollowUpDataStatistic;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrappeForFollow;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrapper;
import cn.qingchengfit.saasbase.student.network.body.StudentTransferBean;
import cn.qingchengfit.saasbase.student.network.body.StudentWithCoashListWrap;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by huangbaole on 2017/10/25.
 */

public interface StudentApi {
    /**
     * 工作人员下所有会员
     *
     * @GET("/api/staffs/{id}/users/all/?show_all=1")
     */
    @GET("/api/staffs/{id}/users/?show_all=1")
    Observable<QcDataResponse<StudentListWrapper>> qcGetAllStudents(@Path("id") String id,
                                                                    @QueryMap HashMap<String, Object> params);

    /**
     * 会员卡可绑定的会员列表
     */
    @GET("/api/staffs/{staff_id}/method/users/?show_all=1")
    Observable<QcDataResponse<StudentListWrapper>> qcGetCardBundldStudents(@Path("staff_id") String id,
                                                                           @QueryMap HashMap<String, Object> params);

    /**
     * 教练分配
     */
    @GET("api/v2/staffs/{staff_id}/coaches/preview/")
    Observable<QcDataResponse<AllotDataResponseWrap>> qcGetCoachList(@Path("staff_id") String staff_id,
                                                                     @QueryMap HashMap<String, Object> params);

    /**
     * 销售列表预览接口
     * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/preview/?brand_id=2&shop_id=1
     * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/preview/?id=5370&model=staff_gym
     */
    @GET("/api/v2/staffs/{staff_id}/sellers/preview/")
    Observable<QcDataResponse<AllotDataResponseWrap>> qcGetAllotSalesPreView(@Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

    /**
     * 销售名下会员列表接口(无销售的会员列表不需要传seller_id)
     * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?brand_id=2&shop_id=2&seller_id=1
     * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?id=5370&model=staff_gym&seller_id=1
     */
    @GET("/api/v2/staffs/{staff_id}/sellers/users/")
    Observable<QcDataResponse<StudentListWrapper>> qcGetAllotSaleOwenUsers(@Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);


    @GET("/api/v2/staffs/{staff_id}/coaches/users/")
    Observable<QcDataResponse<StudentWithCoashListWrap>> qcGetCoachStudentDetail(@Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

    //获取销售 卖卡  包含销售 不包含教练
    @GET("/api/staffs/{staff_id}/sellers-without-coach/")
    Observable<QcDataResponse<SalerUserListWrap>> qcGetSalers(@Path("staff_id") String staff_id,
                                                              @Query("brand_id") String brandid,
                                                              @Query("shop_id") String shopid,
                                                              @Query("id") String gymid,
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
    @PUT("/api/v2/staffs/{staff_id}/sellers/users/")
    Observable<QcResponse> qcModifySellers(@Path("staff_id") String staff_id,
                                           @QueryMap HashMap<String, Object> params,
                                           @Body HashMap<String, Object> body);

    /**
     * 获取教练列表
     *
     * @param staff_id
     * @param params
     * @return
     */
    @GET("/api/staffs/{staff_id}/coaches/")
    Observable<QcDataResponse<SalerTeachersListWrap>> qcGetAllAllocateCoaches(
            @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

    /**
     * 分配教练
     */
    @PUT("/api/v2/staffs/{staff_id}/coaches/users/")
    Observable<QcResponse> qcAllocateCoach(@Path("staff_id") String staff_id,
                                           @Body HashMap<String, Object> body);

    /**
     * 移除教练
     */
    @POST("/api/v2/staffs/{staff_id}/coaches/users/remove/")
    Observable<QcResponse> qcRemoveStudent(@Path("staff_id") String staff_id,
                                           @Body HashMap<String, Object> body);

    /**
     * 批量移除某个销售名下会员:
     * POST http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/remove/?brand_id=2&shop_id=1
     * POST http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/remove/?id=5370&model=staff_gym
     * POST {"user_ids":"1,3,2", "seller_id":5}
     */
    @POST("/api/v2/staffs/{staff_id}/sellers/users/remove/")
    Observable<QcResponse> qcDeleteStudents(@Path("staff_id") String staff_id,
                                            @Body HashMap<String, Object> body);


    /**
     * 数据统计
     * /api/staffs/:staff_id/users/stat/?brand_id=&shop_id= 或者 id=&model=
     * GET参数:status,[start][end][seller_id(无销售seller_id=0)]
     */
    @GET("/api/staffs/{staff_id}/users/stat/")
    Observable<QcDataResponse<FollowUpDataStatistic>> qcGetTrackStudentsStatistics(@Path("staff_id") String staff_id,
                                                                                   @QueryMap HashMap<String, Object> params);


    /**
     * 新增注册
     */
    @GET("/api/staffs/{staff_id}/users/new/create/")
    Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentCreate(
            @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

    /**
     * 新增跟进
     */
    @GET("/api/staffs/{staff_id}/users/new/follow/")
    Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentFollow(
            @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

    /**
     * 新增学员
     */
    @GET("/api/staffs/{staff_id}/users/new/member/")
    Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentMember(
            @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);

    /**
     * 具有名下会员的销售列表
     * /api/staffs/:staff_id/filter/sellers/?brand_id=&shop_id= 或者 id=&model=
     */
    @GET("/api/staffs/{staff_id}/filter/sellers/?show_all=1")
    Observable<QcDataResponse<SalerListWrap>> qcGetTrackStudentsFilterSalers(
            @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);


    /**
     * 转换率
     * /api/staffs/:staff_id/users/conver/stat/?brand_id=&shop_id= 或者 id=&model=
     * GET参数:[start] [end] [seller_id(无销售seller_id=0)]
     */
    @GET("/api/staffs/{staff_id}/users/conver/stat/")
    Observable<QcDataResponse<StudentTransferBean>> qcGetTrackStudentsConver(
            @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params);


    /**
     * 获取缺勤图表数据
     */
    @GET("/api/staffs/{id}/users/attendance/glance/")
    Observable<QcDataResponse<AttendanceCharDataBean>> qcGetAttendanceChart(
            @Path("id") String id, @QueryMap HashMap<String, Object> params);


    /**
     * 缺勤列表
     *
     * @param params 缺勤<7： absence__lt=7，
     *               7-30天：absence__gte=7&absence__lte=30,
     *               缺勤>60天：absence__ge=60
     * @return "attendances": [{"user": {"username": "俞小西","gender": 1,"id": 2131,"avatar":
     * "http://zoneke-img.b0.upaiyun.com/9360bb9fb982a95c915edf44f611678f.jpeg!120x120","phone":
     * "18611985427"},"absence": 390,"date_and_time": "2016-01-30 13:30-14:30","id": 5933,"title": "娜娜私教 杨娜娜"},]
     */
    @GET("/api/staffs/{staff_id}/users/absence/")
    Observable<QcDataResponse<AbsentceListWrap>> qcGetUsersAbsences(@Path("staff_id") String id
            , @QueryMap HashMap<String, Object> params);


    /**
     * @param params 必传start, end，
     *               可选排序字段 加 “-”说明是倒序
     *               order_by=
     *               days      -days
     *               group     -group
     *               private     -private
     *               checkin   -checkin
     * @return "attendances": [{"checkin": 0,"group": 139,"user": {"username": "孙正其","gender": 0,"id": 2219,"avatar":
     * "http://zoneke-img.b0.upaiyun.com/a15bec431224aa638a4b8eccb2e96955.jpg!120x120","phone": "18536668518"},"private_count": 8,"days":
     * 142},
     */
    @GET("/api/staffs/{staff_id}/users/attendance/")
    Observable<QcDataResponse<AttendanceListWrap>> qcGetUsersAttendances(
            @Path("staff_id") String id, @QueryMap HashMap<String, Object> params);


    /**
     * 获取未签课的学员
     */
    @GET("/api/staffs/{staff_id}/users/checkin/records/")
    Observable<QcDataResponse<List<StudentWIthCount>>> qcGetNotSignStudent(
            @Path("staff_id") String staffId, @QueryMap HashMap<String, Object> params);
}
