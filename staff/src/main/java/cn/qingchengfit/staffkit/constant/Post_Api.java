package cn.qingchengfit.staffkit.constant;

import android.support.v4.util.ArrayMap;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.body.AddDayOffBody;
import cn.qingchengfit.model.body.AddFollowRecordBody;
import cn.qingchengfit.model.body.AddLockerBody;
import cn.qingchengfit.model.body.AheadOffDayBody;
import cn.qingchengfit.model.body.ArrangeBatchBody;
import cn.qingchengfit.model.body.BodyTestBody;
import cn.qingchengfit.model.body.BrandBody;
import cn.qingchengfit.model.body.CardBalanceNotifyBody;
import cn.qingchengfit.model.body.CardtplBody;
import cn.qingchengfit.model.body.ChangeBrandCreatorBody;
import cn.qingchengfit.model.body.ChangeSuBody;
import cn.qingchengfit.model.body.ChargeBody;
import cn.qingchengfit.model.body.CheckCodeBody;
import cn.qingchengfit.model.body.ClearNotiBody;
import cn.qingchengfit.model.body.CourseBody;
import cn.qingchengfit.model.body.CreateCardBody;
import cn.qingchengfit.model.body.DelBatchScheduleBody;
import cn.qingchengfit.model.body.EditJacketBody;
import cn.qingchengfit.model.body.EditWardrobeBody;
import cn.qingchengfit.model.body.FixCard;
import cn.qingchengfit.model.body.GymBody;
import cn.qingchengfit.model.body.HireWardrobeBody;
import cn.qingchengfit.model.body.ManagerBody;
import cn.qingchengfit.model.body.OptionBody;
import cn.qingchengfit.model.body.PostCommentBody;
import cn.qingchengfit.model.body.PushBody;
import cn.qingchengfit.model.body.RenewBody;
import cn.qingchengfit.model.body.ReturnWardrobeBody;
import cn.qingchengfit.model.body.ScanBody;
import cn.qingchengfit.model.body.ShopConfigBody;
import cn.qingchengfit.model.body.ShopsBody;
import cn.qingchengfit.model.body.ShortMsgBody;
import cn.qingchengfit.model.body.SignInBody;
import cn.qingchengfit.model.body.SignInCostBody;
import cn.qingchengfit.model.body.SignInIgnorBody;
import cn.qingchengfit.model.body.SignInManualBody;
import cn.qingchengfit.model.body.SignInNoticeConfigBody;
import cn.qingchengfit.model.body.SignOutBody;
import cn.qingchengfit.model.body.SingleBatchBody;
import cn.qingchengfit.model.body.UpdateCardValidBody;
import cn.qingchengfit.model.body.UpdateModule;
import cn.qingchengfit.model.common.Shop;
import cn.qingchengfit.model.responese.CoachResponse;
import cn.qingchengfit.model.responese.CreatBrand;
import cn.qingchengfit.model.responese.Login;
import cn.qingchengfit.model.responese.QcResponsePayWx;
import cn.qingchengfit.model.responese.QcResponseRenew;
import cn.qingchengfit.model.responese.QcResponseSystenInit;
import cn.qingchengfit.model.responese.ResponseService;
import cn.qingchengfit.model.responese.Space;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.train.model.CreateGroupBody;
import cn.qingchengfit.staffkit.train.model.OperationMemberBody;
import cn.qingchengfit.staffkit.usecase.bean.CreatBrandBody;
import cn.qingchengfit.staffkit.usecase.bean.FeedBackBody;
import cn.qingchengfit.staffkit.usecase.bean.FixPhoneBody;
import cn.qingchengfit.staffkit.usecase.bean.GetCodeBody;
import cn.qingchengfit.staffkit.usecase.bean.LoginBody;
import cn.qingchengfit.staffkit.usecase.bean.ModifyPwBody;
import cn.qingchengfit.staffkit.usecase.bean.OutExcelBody;
import cn.qingchengfit.staffkit.usecase.bean.RegisteBody;
import cn.qingchengfit.staffkit.usecase.bean.SystemInitBody;
import cn.qingchengfit.staffkit.usecase.bean.User_Student;
import java.util.HashMap;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

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
 * Created by Paper on 16/1/18 2016.
 */
public interface Post_Api {

    /**
     * 品牌操作
     */

    @POST("/api/brands/{id}/change_user/") Observable<QcResponse> qcChangeBrandUser(@Path("id") String brand_id,
        @Body ChangeBrandCreatorBody body);

    @PUT("api/brands/{id}/") Observable<QcResponse> qcEditBrand(@Path("id") String brand_id, @Body BrandBody body);

    @PUT("/api/staffs/{staff_id}/shops/detail/") Observable<QcResponse> qcEditShop(@Path("staff_id") String staffId,
        @Body ArrayMap<String, Object> body);

    @DELETE("/api/brands/{id}/") Observable<QcResponse> qcDelbrand(@Path("id") String id);

    /**
     * 删除场馆
     */
    @DELETE("/api/gym/{id}/") Observable<QcResponse> qcDelGym(@Path("id") String id);

    /**
     * 个人操作
     */

    @POST("/api/staffs/login/") Observable<QcDataResponse<Login>> qcLogin(@Body LoginBody loginBody);

    @POST("/api/staffs/login/") Call<Login> qcLoginTest(@Body LoginBody loginBody);

    /**
     * 场馆续费
     */
    @POST("/api/gyms/orders/") rx.Observable<QcResponseRenew> qcCharge(@Body RenewBody body);

    /**
     * 场馆试用
     */
    @POST("/api/v2/staffs/{staff_id}/gyms/trial/") rx.Observable<cn.qingchengfit.network.response.QcResponse> qcGymTrial(
        @Path("staff_id") String staffid, @QueryMap HashMap<String, Object> params);

    /**
     * 修改场馆
     */
    @PUT("/api/staffs/{id}/services/{sid}/") rx.Observable<ResponseService> qcPutService(@Path("id") String id, @Path("sid") String sid,
        @Body Shop shop);

    //注册
    @POST("/api/staffs/register/") rx.Observable<QcDataResponse<Login>> qcRegister(@Body RegisteBody params);

    //获取电话验证码
    @POST("/api/send/verify/") rx.Observable<QcResponse> qcGetCode(@Body GetCodeBody account);

    /**
     * 验证验证码
     */
    @POST("api/check/verify/") rx.Observable<QcResponse> qcCheckCode(@Body CheckCodeBody body);

    //修改密码
    @POST("/api/staffs/{id}/change/password/") rx.Observable<QcResponse> qcMoidfyPw(@Path("id") String id, @Body ModifyPwBody modifyPwBean);

    //修改电话号码
    @POST("/api/staffs/{id}/change/phone/") rx.Observable<QcResponse> qcModifyPhoneNum(@Path("id") String id,
        @Body FixPhoneBody fixPhoneBody);

    //修改信息
    @PUT("/api/staffs/{id}/") rx.Observable<QcResponse> qcModifyStaffs(@Path("id") String id, @Body Staff staffBean);

    //发送意见
    @POST("/api/feedback/") rx.Observable<QcResponse> qcFeedBack(@Body FeedBackBody bean);

    //百度pushid绑定
    @POST("/api/staffs/{id}/push/update/") rx.Observable<QcResponse> qcPostPushId(@Path("id") String id, @Body PushBody body);

    @PUT("/api/notifications/clear/") rx.Observable<QcResponse> qcClearAllNoti(@Query("staff_id") String staffid,
        @QueryMap HashMap<String, Object> paras);

    @PUT("/api/notifications/clear/") rx.Observable<QcResponse> qcClearMutiNoti(@Query("staff_id") String staffid,
        @Body HashMap<String, List<Long>> paras);

    @PUT("/api/v2/notifications/") rx.Observable<QcResponse> qcClearTypeNoti(@Body ClearNotiBody body);

    /**
     * 健身房操作
     */

    @POST("/api/brands/") rx.Observable<QcDataResponse<CreatBrand>> qcCreatBrand(@Body CreatBrandBody body);

    @PUT("/api/staffs/{id}/") rx.Observable<QcResponse> qcFixSelfInfo(@Path("id") String id, @Body CreatBrandBody body);

    @POST("/api/systems/initial/") rx.Observable<QcResponseSystenInit> qcSystemInit(@Body SystemInitBody body);

    @POST("/api/staffs/{id}/shops/") rx.Observable<QcResponse> qcCreateGym(@Path("id") String id, @Query("brand_id") String brand_id,
        @Body GymBody body);

    //修改体测
    @PUT("/api/staffs/{id}/measures/{measure_id}/") rx.Observable<QcResponse> qcUpdateBodyTest(@Path("id") String staff_id,
        @Path("measure_id") String id, @QueryMap HashMap<String, Object> params, @Body BodyTestBody addBodyTestBean);

    //新建体测
    @POST("/api/staffs/{id}/measures/") rx.Observable<QcResponse> qcAddBodyTest(@Path("id") String staff_id,
        @QueryMap HashMap<String, Object> params, @Body BodyTestBody addBodyTestBean);

    @DELETE("/api/measures/{measure_id}/") rx.Observable<QcResponse> qcDelBodyTest(@Path("measure_id") String id,
        @Path("measure_id") String measure_id, @QueryMap HashMap<String, Object> params);

    //新增请假
    @POST("/api/staffs/{id}/leaves/") rx.Observable<QcResponse> qcAddDayOff(@Path("id") String staffid, @Query("brand_id") String brand_id,
        @Query("id") String gymid, @Query("model") String model, @Body AddDayOffBody body);

    //取消请假
    @DELETE("/api/staffs/{id}/leaves/{leave_id}/") rx.Observable<QcResponse> qcDelDayOff(@Path("id") String staffid,
        @Path("leave_id") String leave_id, @Query("brand_id") String brandid, @Query("id") String gymid, @Query("model") String model);

    //提前销假
    @PUT("/api/staffs/{staffid}/leaves/{leave_id}/") rx.Observable<QcResponse> qcAheadDayOff(@Path("staffid") String staffid,
        @Path("leave_id") String leave_id, @QueryMap HashMap<String, Object> params, @Body AheadOffDayBody body);

    /**
     * 场馆自定义 模块
     */
    @PUT("/api/v2/staffs/{staff_id}/gym-module-custom/") rx.Observable<QcResponse> qcUpdateModule(@Path("staff_id") String staffid,
        @Body UpdateModule module_custom, @QueryMap HashMap<String, Object> params);

    /**
     * 具体卡操作
     */
    //充值扣费
    @POST("/api/staffs/{staff_id}/cards/{card_id}/charge/") rx.Observable<QcResponsePayWx> qcCardCharge(@Path("staff_id") String staff_id,
        @Path("card_id") String cardid, @Query("brand_id") String brand_id, @Query("shop_id") String shop_id, @Query("id") String id,
        @Query("model") String model, @Body ChargeBody body);

    @POST("/api/staffs/{staff_id}/cards/orders/") rx.Observable<QcResponsePayWx> qcCardChargeWechat(@Path("staff_id") String staff_id
        //, @Path("card_id") String cardid
        , @Query("brand_id") String brand_id, @Query("shop_id") String shop_id, @Query("id") String id, @Query("model") String model,
        @Body ChargeBody body);

    //购卡
    @POST("/api/staffs/{id}/cards/create/") rx.Observable<QcResponsePayWx> qcCreateRealcard(@Path("id") String staffid,
        @Body CreateCardBody body, @Query("brand_id") String brand_id, @Query("shop_id") String shop_id, @Query("id") String id,
        @Query("model") String model);

    //销卡
    @DELETE("/api/staffs/{id}/cards/{card_id}/") rx.Observable<QcResponse> qcUnregisteCard(@Path("id") String staffid,
        @Path("card_id") String cardid, @Query("brand_id") String brand_id, @Query("id") String id, @Query("model") String model);

    //恢复卡
    @POST("/api/staffs/{id}/cards/{card_id}/recovery/") rx.Observable<QcResponse> qcResumeCard(@Path("id") String staffid,
        @Path("card_id") String cardid, @Query("brand_id") String brand_id, @Query("id") String id, @Query("model") String model);

    //卡修改
    @PUT("/api/staffs/{id}/cards/{card_id}/") rx.Observable<QcResponse> qcUndateCard(@Path("id") String staffid,
        @Path("card_id") String cardid, @Query("brand_id") String brand_id, @Query("id") String id, @Query("model") String model,
        @Body FixCard card);

    //卡修改有效期
    @PUT("/api/staffs/{id}/cards/{card_id}/change-date/") rx.Observable<QcResponse> qcUndateCardValid(@Path("id") String staffid,
        @Path("card_id") String cardid, @QueryMap HashMap<String, Object> params, @Body UpdateCardValidBody body);

    /**
     * 卡类型
     */
    @POST("/api/staffs/{staff_id}/cardtpls/")
    //新建卡模板
    rx.Observable<QcResponse> qcCreateCardtpl(@Path("staff_id") String staffid, @Body CardtplBody body,
        @QueryMap HashMap<String, Object> params);

    @PUT("/api/staffs/{staff_id}/cardtpls/{card_tpl_id}/") rx.Observable<QcResponse> qcUpdateCardtpl(@Path("staff_id") String staffid,
        @Path("card_tpl_id") String card_tpl_id, @Body CardtplBody body, @QueryMap HashMap<String, Object> params);

    /**
     * 停用会员卡种类
     */
    @DELETE("/api/staffs/{staff_id}/cardtpls/{card_tpl_id}/") rx.Observable<QcResponse> qcDelCardtpl(@Path("staff_id") String staffid,
        @Path("card_tpl_id") String card_tpl_id, @Query("id") String gymid, @Query("model") String model,
        @Query("brand_id") String brand_id);

    /**
     * 恢复会员卡种类
     */
    @POST("/api/v2/staffs/{staff_id}/cardtpls/{card_tpl_id}/recovery/") rx.Observable<QcResponse> qcResumeCardtpl(
        @Path("staff_id") String staffid, @Path("card_tpl_id") String card_tpl_id, @QueryMap HashMap<String, Object> params);

    @PUT("/api/staffs/{staff_id}/cardtpls/{cardtpl_id}/shops/") rx.Observable<QcResponse> qcFixGyms(@Path("staff_id") String staffid,
        @Path("cardtpl_id") String card_tpl, @Body ShopsBody body, @QueryMap HashMap<String, Object> params);

    /**
     * 卡规格操作
     */
    @DELETE("/api/staffs/{staff_id}/options/{option_id}/") rx.Observable<QcResponse> qcDelCardStandard(@Path("staff_id") String staffid,
        @Path("option_id") String option_id, @Query("id") String gymid, @Query("model") String model, @Query("brand_id") String brand_id);

    @PUT("/api/staffs/{staff_id}/options/{option_id}/") rx.Observable<QcResponse> qcUpdateCardStandard(@Path("staff_id") String staffid,
        @Path("option_id") String option_id, @Query("id") String gymid, @Query("model") String model, @Query("brand_id") String brand_id,
        @Body OptionBody body);

    @POST("/api/staffs/{staff_id}/cardtpls/{card_tpl_id}/options/") rx.Observable<QcResponse> qcCreateStandard(
        @Path("staff_id") String staffid, @Path("card_tpl_id") String card_tpl_id, @Query("id") String gymid, @Query("model") String model,
        @Query("brand_id") String brand_id, @Body OptionBody body);

    /**
     * 学员操作
     */
    @POST("/api/staffs/{id}/users/") rx.Observable<QcDataResponse> qcCreateStudent(
        @Path("id") String id, @QueryMap HashMap<String, Object> params,
        @Body User_Student student);

    //<<<<<<< HEAD
    //    @PUT("/api/v2/staffs/{staff_id}/users/{id}/") rx.Observable<QcResponseData> qcUpdateStudent(
    //            @Path("staff_id") String staff_id, @Path("id") String id
    //=======
    @PUT("/api/v2/staffs/{staff_id}/users/{id}/") rx.Observable<QcDataResponse> qcUpdateStudent(@Path("staff_id") String staff_id,
        @Path("id") String id, @Query("id") String gymid, @Query("model") String model, @Query("brand_id") String brandid

        , @Body User_Student student);

    @DELETE("/api/staffs/{staff_id}/users/{id}/") rx.Observable<QcResponse> qcDelStudent(@Path("staff_id") String staff_id,
        @Path("id") String id, @Query("id") String gymid, @Query("model") String model, @Query("brand_id") String brandid,
        @Query("shop_ids") String shop_ids);

    @POST("/api/staffs/{id}/users/{user_id}/records/") rx.Observable<QcResponse> qcAddFollowRecord(@Path("id") String id,
        @Path("user_id") String user_id, @Body AddFollowRecordBody body, @Query("brand_id") String brandid, @Query("id") String gymid,
        @Query("model") String model);

    @POST("/api/staffs/{staff_id}/user/photos/") rx.Observable<QcResponse> qcUploadImg(@Path("id") String id,
        @Path("user_id") String user_id, HashMap<String, Object> params);

    /**
     * 课程操作
     */
    /**
     * 创建课程
     */
    @POST("/api/v2/staffs/{id}/courses/") rx.Observable<QcResponse> qcCreateCourse(@Path("id") String staffid, @Body CourseBody courseBody,
        @QueryMap HashMap<String, Object> params);

    //修改课程
    @PUT("/api/v2/staffs/{id}/courses/{course_id}/") rx.Observable<QcResponse> qcUpdateCourse(@Path("id") String staffid,
        @Path("course_id") String course_id, @QueryMap HashMap<String, Object> params, @Body CourseBody courseBody);

    //删除课程
    @DELETE("/api/v2/staffs/{id}/courses/{course_id}/") rx.Observable<QcResponse> qcDelCourse(@Path("id") String staffid,
        @Path("course_id") String course_id, @QueryMap HashMap<String, Object> params);

    //修改封面
    @POST("/api/v2/staffs/{id}/courses/photos/") rx.Observable<QcResponse> qcEditJacket(@Path("id") String staffid,
        @Query("course_id") String course_id, @QueryMap HashMap<String, Object> params, @Body EditJacketBody body);

    //修改课程适用场馆
    @PUT("/api/v2/staffs/{staff_id}/courses/{course_id}/shops/") rx.Observable<QcResponse> qcEditCourseShops(
        @Path("staff_id") String staffid, @Path("course_id") String course_id, @Body HashMap<String, Object> params);

    /**
     * 场地操作
     */
    //新建场地
    @POST("/api/staffs/{staff_id}/spaces/") rx.Observable<QcResponse> qcCreateSpace(@Path("staff_id") String staff_id,
        @Query("id") String gymid, @Query("model") String model, @Query("brand_id") String brand_id, @Body Space space);

    //删除场地
    @DELETE("/api/staffs/{id}/spaces/{space_id}/") rx.Observable<QcResponse> qcDelSpace(@Path("id") String id,
        @Path("space_id") String space_id, @Query("id") String gymid, @Query("model") String model, @Query("brand_id") String brand_id);

    @PUT("/api/staffs/{id}/spaces/{space_id}/") rx.Observable<QcResponse> qcUpdateSpace(@Path("id") String staff_id,
        @Path("space_id") String space_id, @Query("id") String gymid, @Query("model") String model, @Query("brand_id") String brand_id,
        @Body Space space);

    /**
     * 排期
     */
    @POST("/api/staffs/{id}/arrange/batches/") rx.Observable<QcResponse> qcArrangeBatch(@Path("id") String staff_id,
        @Query("id") String gymid, @Query("model") String model, @Body ArrangeBatchBody body);

    @PUT("/api/staffs/{id}/batches/{batchid}/") rx.Observable<QcResponse> qcUpdateBatch(@Path("id") String staff_id,
        @Path("batchid") String batchid, @Query("id") String gymid, @Query("model") String model, @Body ArrangeBatchBody body);

    @POST("/api/staffs/{id}/{type}/arrange/check/") rx.Observable<QcResponse> qcCheckBatch(@Path("id") String staff_id,
        @Path("type") String type, @Query("id") String gymid, @Query("model") String model, @Body ArrangeBatchBody body);

    //删除排期
    @POST("api/staffs/{id}/{type}/bulk/delete/") rx.Observable<QcResponse> qcDelBatchSchedule(@Path("id") String staff_id,
        @Path("type") String type, @Body DelBatchScheduleBody body);

    @DELETE("/api/staffs/{id}/batches/{batch_id}/") rx.Observable<QcResponse> delBatch(@Path("id") String staff_id,
        @Path("batch_id") String batch_id, @QueryMap HashMap<String, Object> paras);

    @PUT("api/staffs/{id}/{type}/{scheduleid}/") rx.Observable<QcResponse> qcUpdateBatchSchedule(@Path("id") String staff_id,
        @Path("type") String type, @Path("scheduleid") String scheduleid, @Body SingleBatchBody body);

    /**
     * 教练
     */
    @POST("/api/staffs/{id}/coaches/") rx.Observable<QcDataResponse<CoachResponse>> qcAddCoach(@Path("id") String id,
        @Query("id") String gymid, @Query("model") String model, @Body Staff coach);

    @PUT("/api/staffs/{id}/coaches/{cid}/") rx.Observable<QcResponse> qcFixCoach(@Path("id") String id, @Path("cid") String cid,
        @Query("id") String gymid, @Query("model") String model, @Body Staff coach);

    @DELETE("/api/staffs/{id}/coaches/{cid}/") rx.Observable<QcResponse> qcDelCoach(@Path("id") String id, @Path("cid") String cid,
        @Query("id") String gymid, @Query("model") String model);

    /**
     * 工作人员
     */
    @POST("/api/staffs/{id}/users/") rx.Observable<QcResponse> qcCreateManager(@Path("id") String id, @Query("id") String gymid,
        @Query("model") String model, @Body ManagerBody body);

    @PUT("/api/staffs/{id}/users/{mid}/") rx.Observable<QcResponse> qcUpdateManager(@Path("id") String id, @Path("mid") String cid,
        @Query("id") String gymid, @Query("model") String model, @Body ManagerBody body);

    @DELETE("/api/staffs/{id}/managers/{mid}/") rx.Observable<QcResponse> qcDelManager(@Path("id") String id, @Path("mid") String cid,
        @Query("id") String gymid, @Query("model") String model);

    @PUT("/api/staffs/{staff_id}/superuser/") rx.Observable<QcResponse> qcChangeSu(@Path("staff_id") String staffid,
        @QueryMap HashMap<String, Object> params, @Body ChangeSuBody body);

    /**
     * 报表操作
     */
    @POST("/api/staffs/{staff_id}/excel/tasks/") rx.Observable<QcResponse> qcOutExcel(@Path("staff_id") String staff_id,
        @Body OutExcelBody body);

    /**
     *
     */
    @PUT("/api/scans/{uuid}/") rx.Observable<QcResponse> qcScans(@Path("uuid") String uuid, @Body ScanBody body);

    /**
     * 健身房所有设置
     * http://192.168.1.7:8000/api/staffs/3281/shops/configs/?id=5370&model=staff_gym
     * {@link ShopConfigs}
     */
    @PUT("/api/staffs/{staff_id}/shops/configs/") rx.Observable<cn.qingchengfit.network.response.QcDataResponse> qcShopConfigs(
        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params, @Body ShopConfigBody body);

    /**
     * 签到扣费设置
     * http://192.168.1.7:8000/api/v2/staffs/3281/checkin/settings/?id=5370&model=staff_gym
     */
    @PUT("/api/v2/staffs/{staff_id}/checkin/settings/")
    rx.Observable<cn.qingchengfit.network.response.QcDataResponse> qcPutSignInCostConfig(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params, @Body SignInCostBody body);

    /**
     * 确认签到
     * http://192.168.1.106:8000/api/staffs/3281/doublecheckin/?id=5370&model=staff_gym
     */
    @PUT("/api/v2/staffs/{staff_id}/doublecheckin/") rx.Observable<QcResponse> qcPutDoubleCheckin(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params, @Body SignInBody body);

    /**
     * 确认签出
     * http://192.168.1.106:8000/api/staffs/3281/doublecheckout/?id=5370&model=staff_gym
     */
    @PUT("/api/v2/staffs/{staff_id}/doublecheckout/") rx.Observable<QcResponse> qcPutDoubleCheckout(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params, @Body SignOutBody body);

    /**
     * 手动签到
     * http://192.168.1.106:8000/api/staffs/3281/checkin/?id=5370&model=staff_gym
     * POST
     */
    @POST("/api/v2/staffs/{staff_id}/checkin/") rx.Observable<QcResponse> qcPostCheckInMaual(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params, @Body SignInManualBody body);

    /**
     * 手动签出
     * http://192.168.1.106:8000/api/staffs/3281/checkin/?id=5370&model=staff_gym
     * PUT
     */
    @PUT("/api/v2/staffs/{staff_id}/checkin/") rx.Observable<QcResponse> qcPutCheckOutMaual(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params, @Body SignOutBody body);

    /**
     * 工作人员忽略签到/签出
     * http://192.168.1.106:8000/api/v2/staffs/3281/checkin/ignore?id=5370&model=staff_gym
     */
    @POST("/api/v2/staffs/{staff_id}/checkin/ignore/") rx.Observable<QcResponse> qcPostIgnore(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params, @Body SignInIgnorBody body);

    /**
     * 签到撤销
     * http://192.168.1.106:8000/api/staffs/3281/checkin/?id=5370&model=staff_gym
     * DELETE
     */
    @DELETE("/api/v2/staffs/{staff_id}/checkin/") rx.Observable<QcResponse> qcDeleteCheckin(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params);

    /**
     * 签到照片
     * http://192.168.1.106:8000/api/staffs/3281/user/photos/?id=5370&model=staff_gym
     * POST:{"user_id":162, "photo": "照片url"}
     */
    @POST("/api/staffs/{staff_id}/user/photos/") rx.Observable<QcResponse> qcUploadStuImg(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params, @Body HashMap<String, Object> body);

    /**
     * 更衣柜
     */
    //增加
    @POST("/api/v2/staffs/{staff_id}/lockers/") rx.Observable<QcResponse> qcAddLocker(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params, @Body AddLockerBody body);

    //删除
    @DELETE("/api/v2/staffs/{staff_id}/lockers/{locker_id}/") rx.Observable<QcResponse> qcDelLocker(@Path("staff_id") String staff_id,
        @Path("locker_id") String lockerid, @QueryMap HashMap<String, Object> params);

    //修改
    @PUT("/api/v2/staffs/{staff_id}/lockers/{locker_id}/") rx.Observable<QcResponse> qcEditLocker(@Path("staff_id") String staff_id,
        @Path("locker_id") String lockerid, @QueryMap HashMap<String, Object> params, @Body EditWardrobeBody body);

    //归还
    @PUT("/api/v2/staffs/{staff_id}/lockers/return/") rx.Observable<QcResponse> qcReturnLockers(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params, @Body ReturnWardrobeBody body);

    //租用
    @PUT("/api/v2/staffs/{staff_id}/lockers/borrow/") rx.Observable<QcResponse> qcHireLocker(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params, @Body HireWardrobeBody body);

    /**
     * 更衣柜区域
     */
    //新增
    @POST("/api/v2/staffs/{staff_id}/locker/regions/") rx.Observable<QcResponse> qcAddLockerRegion(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params, @Body HashMap<String, Object> body);

    //修改
    @PUT("/api/v2/staffs/{staff_id}/locker/regions/") rx.Observable<QcResponse> qcEditLockerRegion(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params, @Body HashMap<String, Object> body);

    //删除
    @DELETE("/api/v2/staffs/{staff_id}/locker/regions/") rx.Observable<QcResponse> qcDelLockerRegion(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params);

    //续租
    @PUT("/api/v2/staffs/{staff_id}/lockers/long/delay/") rx.Observable<QcResponse> qcContinueLocker(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params, @Body HashMap<String, Object> body);

    /**
     * 签到签出通知设置
     * /api/staffs/3281/checkin/notification/configs/
     */
    @PUT("/api/staffs/{staff_id}/checkin/notification/configs/") rx.Observable<QcResponse> qcPutCheckinNoticeCOnfig(
        @Path("staff_id") String staff_id, @Body SignInNoticeConfigBody body);

    /**
     * 移除名下单个会员
     * DELETE http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?brand_id=2&shop_id=1&user_id=1&seller_id=1
     * DELETE http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?id=5370&model=staff_gym?user_id=2&seller_id=1
     */
    @DELETE("/api/v2/staffs/{staff_id}/sellers/users/") rx.Observable<QcResponse> qcDeleteStudent(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params);

    /**
     * 批量移除某个销售名下会员:
     * POST http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/remove/?brand_id=2&shop_id=1
     * POST http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/remove/?id=5370&model=staff_gym
     * POST {"user_ids":"1,3,2", "seller_id":5}
     */
    @POST("/api/v2/staffs/{staff_id}/sellers/users/remove/") rx.Observable<QcResponse> qcDeleteStudents(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params, @Body HashMap<String, Object> body);

    /**
     * /**
     * 批量变更销售
     * http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?brand_id=2&shop_id=1
     * http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?id=5370&model=staff_gym
     * PUT {"user_ids":"1,2,3,4,5", "seller_ids":"10,11"}
     *
     * @param body user_ids  seller_ids
     */
    @PUT("/api/v2/staffs/{staff_id}/sellers/users/") rx.Observable<QcResponse> qcModifySellers(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params, @Body HashMap<String, Object> body);

    /**
     * 批量添加名下会员:
     * http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?brand_id=2&shop_id=1
     * http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?id=5370&model=staff_gym
     * POST {"user_ids":"1,2,3,4,5", "seller_id":10}
     */
    @POST("/api/v2/staffs/{staff_id}/sellers/users/") rx.Observable<QcResponse> qcAddStudents(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params, @Body HashMap<String, Object> body);

    /**
     * POST 新增来源
     * /api/v2/staffs/:staff_id/users/origins/?brand_id=&shop_id= 或者 id=&model=
     */
    @POST("/api/v2/staffs/{staff_id}/users/origins/") rx.Observable<QcResponse> qcAddOrigin(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params, @Body HashMap<String, Object> body);

    @POST("/api/v2/staffs/{staff_id}/dimission/") rx.Observable<QcResponse> qcQuitGym(@Path("staff_id") String staff_id,
        @QueryMap HashMap<String, Object> params);

    /**
     * Staff - 模块-积分-开关-修改
     * PUT /api/v2/staffs/:id/modules/
     * score  Boolean  是否开启积分功能
     */
    @PUT("/api/v2/staffs/{staff_id}/modules/") rx.Observable<cn.qingchengfit.network.response.QcDataResponse> qcPutScoreStatus(
        @Path("staff_id") String staff_id, @QueryMap ArrayMap<String, String> params, @Body ArrayMap<String, Object> body);

    /**
     * Staff - 模块-积分规则-修改
     * /api/v2/staffs/:id/scores/rules/
     */
    @PUT("/api/v2/staffs/{staff_id}/scores/rules/") rx.Observable<cn.qingchengfit.network.response.QcDataResponse> qcPutScoreRules(
        @Path("staff_id") String staff_id, @QueryMap ArrayMap<String, String> params, @Body ArrayMap<String, Object> body);

    /**
     * Staff - 模块-积分优惠奖励-新增
     * POST /api/v2/staffs/:id/scores/favors/
     */
    @POST("/api/v2/staffs/{staff_id}/scores/favors/") rx.Observable<cn.qingchengfit.network.response.QcDataResponse> qcPostScoreRulesAward(
        @Path("staff_id") String staff_id, @QueryMap ArrayMap<String, String> params, @Body ArrayMap<String, Object> body);

    /**
     * Staff - 模块-积分优惠奖励-修改
     * PUT /api/v2/staffs/:id/scores/favors/:favor_id/
     */
    @PUT("/api/v2/staffs/{staff_id}/scores/favors/{favor_id}/")
    rx.Observable<cn.qingchengfit.network.response.QcDataResponse> qcPutScoreRulesAward(@Path("staff_id") String staff_id,
        @Path("favor_id") String favor_id, @QueryMap ArrayMap<String, String> params, @Body ArrayMap<String, Object> body);

    /**
     * Staff - 模块-积分历史-新增
     * POST /api/v2/staffs/:id/users/:user_id/scores/histories/
     */
    @POST("/api/v2/staffs/{staff_id}/users/{user_id}/scores/histories/")
    rx.Observable<cn.qingchengfit.network.response.QcDataResponse> qcPostScoreHistory(@Path("staff_id") String staff_id,
        @Path("user_id") String user_id, @QueryMap ArrayMap<String, String> params, @Body ArrayMap<String, Object> body);

    //修改余额不足提醒规则
    @PUT("api/v2/staffs/{staff_id}/users/configs/") rx.Observable<cn.qingchengfit.network.response.QcDataResponse> qcPostBalanceCondition(
        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params, @Body CardBalanceNotifyBody body);

    //修改自动提醒短信规则
    @PUT("api/staffs/{staff_id}/shops/configs/") rx.Observable<cn.qingchengfit.network.response.QcDataResponse> qcChangeAutoNotify(
        @Path("staff_id") String staff_id, @QueryMap HashMap<String, Object> params, @Body CardBalanceNotifyBody body);

    //修改余额不足提醒
    @PUT("/api/staffs/{staff_id}/configs/") rx.Observable<QcResponse> qcPostBalanceNotify(@Path("staff_id") String staff_id,
        @Body CardBalanceNotifyBody body);

    /**
     * 短信相关
     */
    @POST("/api/staffs/{staffid}/group/messages/") rx.Observable<QcResponse> qcPostShortMsg(@Path("staffid") String staffid,
        @Body ShortMsgBody body, @QueryMap HashMap<String, Object> params);

    @DELETE("/api/staffs/{staffid}/group/message/detail/") rx.Observable<QcResponse> qcDelShortMsg(@Path("staffid") String staffid,
        @Query("message_id") String messageid, @QueryMap HashMap<String, Object> params);

    @PUT("/api/staffs/{staffid}/group/message/detail/") rx.Observable<QcResponse> qcPutShortMsg(@Path("staffid") String staffid,
        @Body ShortMsgBody body, @QueryMap HashMap<String, Object> params);

    //文章评论
    @POST("/api/news/{newsid}/comment/") rx.Observable<QcResponse> qcAddComment(@Path("newsid") String news_id, @Body PostCommentBody body);

    //新建分组
    @POST("api/staffs/competitions/teams/") rx.Observable<QcResponse> qcPostCreateGroup(@Body CreateGroupBody createGroupBody);

    //删除报名表分组
    @DELETE("/api/staffs/competitions/teams/{team_id}/") rx.Observable<QcResponse> qcDeleteGroup(@Path("team_id") String team_id);

    //添加&删除分组成员
    @PUT("/api/staffs/competitions/teams/{team_id}/") rx.Observable<QcResponse> qcPostMemberOperation(@Path("team_id") String team_id,
        @Body OperationMemberBody body);

    //分配教练
    @PUT("/api/v2/staffs/{staff_id}/coaches/users/") rx.Observable<QcResponse> qcAllocateCoach(@Path("staff_id") String staff_id,
        @Body HashMap<String, Object> body);

    //分配教练
    @POST("/api/v2/staffs/{staff_id}/coaches/users/remove/") rx.Observable<QcResponse> qcRemoveStudent(@Path("staff_id") String staff_id,
        @Body HashMap<String, Object> body);

    //导入导出
    @POST("/api/staffs/{staff_id}/export/do/") rx.Observable<QcResponse> qcDataImport(@Path("staff_id") String staff_id,
        @Body HashMap<String, Object> body);

    //发送邮件
    @POST("/api/staffs/{staff_id}/export/mail/") rx.Observable<QcResponse> qcSendMail(@Path("staff_id") String staff_id,
        @Body HashMap<String, Object> body);
}
