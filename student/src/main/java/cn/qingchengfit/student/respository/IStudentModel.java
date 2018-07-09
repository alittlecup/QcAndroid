package cn.qingchengfit.student.respository;

import android.support.annotation.IntRange;
import cn.qingchengfit.student.bean.AllotDataResponseWrap;
import cn.qingchengfit.student.bean.ShortMsgBody;
import cn.qingchengfit.student.bean.ShortMsgDetail;
import cn.qingchengfit.student.bean.ShortMsgList;
import io.reactivex.Flowable;
import java.util.HashMap;
import java.util.List;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/9/30.
 */

public interface IStudentModel {
  /**
   * 购卡 绑定会员时选择会员列表
   */
  //Observable<QcDataResponse<StudentListWrapper>> getAllStudentNoPermission();

  /**
   * 新增会员
   */
  //Observable<QcDataResponse> addStudent(AddStudentBody body);

  /**
   * 会员卡可绑定的会员列表
   */
  //Observable<QcDataResponse<StudentListWrapper>> qcGetCardBundldStudents(String id,
  //    HashMap<String, Object> params);

  /**
   * 工作人员下所有会员
   *
   * @param id
   * @param params
   * @return
   */
  //Observable<QcDataResponse<StudentListWrapper>> qcGetAllStudents(String id,
  //    HashMap<String, Object> params);

  /**
   * 教练分配
   *
   * @param staff_id
   * @param params
   * @return
   */
  //Observable<QcDataResponse<AllotDataResponseWrap>> qcGetCoachList(String staff_id,
  //    HashMap<String, Object> params);

  /**
   * 销售列表预览接口
   * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/preview/?brand_id=2&shop_id=1
   * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/preview/?id=5370&model=staff_gym
   */
  //Observable<QcDataResponse<AllotDataResponseWrap>> qcGetAllotSalesPreView(String staff_id,
  //    HashMap<String, Object> params);
  //
  Flowable<QcDataResponse<AllotDataResponseWrap>> qcGetStaffList(String staff_id, String type,
      HashMap<String, Object> params);

  /**
   * 销售名下会员列表接口(无销售的会员列表不需要传seller_id)
   * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?brand_id=2&shop_id=2&seller_id=1
   * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?id=5370&model=staff_gym&seller_id=1
   */
  //Observable<QcDataResponse<StudentListWrapper>> qcGetAllotSaleOwenUsers(String staff_id,
  //    HashMap<String, Object> params);
  //
  //Observable<QcDataResponse<StudentListWrapper>> qcGetAllotStaffMembers(String staff_id,
  //    String type, HashMap<String, Object> params);

  /**
   * 教练名下会员列表接口(无销售的会员列表不需要传seller_id)
   */
  //Observable<QcDataResponse<StudentWithCoashListWrap>> qcGetCoachStudentDetail(String staff_id,
  //    HashMap<String, Object> params);

  /**
   * 获取销售 卖卡  包含销售 不包含教练
   */
  //Observable<QcDataResponse<SalerUserListWrap>> qcGetSalers(String staff_id, String brandid,
  //    String shopid, String gymid, String model);

  /**
   * /**
   * 批量变更销售
   * http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?brand_id=2&shop_id=1
   * http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?id=5370&model=staff_gym
   * PUT {"user_ids":"1,2,3,4,5", "seller_ids":"10,11"}
   *
   * @param body user_ids  seller_ids
   */
  //Observable<QcResponse> qcModifySellers(String staff_id, HashMap<String, Object> params,
  //    HashMap<String, Object> body);

  /**
   * 获取教练列表
   * 获取教练列表
   *
   * @param staff_id
   * @param params
   * @return
   */
  //Observable<QcDataResponse<SalerTeachersListWrap>> qcGetAllAllocateCoaches(String staff_id,
  //    HashMap<String, Object> params);

  /**
   * 分配教练
   */
  //Observable<QcResponse> qcAllocateCoach(String staff_id, HashMap<String, Object> body);

  /**
   * 批量移除某个教练名下会员
   */
  //Observable<QcResponse> qcRemoveStudent(String staff_id, HashMap<String, Object> body);
  //
  /**
   * 批量移除某个销售名下会员:
   */
  //Observable<QcResponse> qcDeleteStudents(String staff_id, HashMap<String, Object> body);

  /**
   * 批量移除某个销售或者教练名下会员:
   */
  //Observable<QcResponse> qcRemoveStaff(String staff_id, String type, HashMap<String, Object> body);

  /**
   * 数据统计
   * /api/staffs/:staff_id/users/stat/?brand_id=&shop_id= 或者 id=&model=
   * GET参数:status,[start][end][seller_id(无销售seller_id=0)]
   */
  //Observable<QcDataResponse<FollowUpDataStatistic>> qcGetTrackStudentsStatistics(String staff_id,
  //    HashMap<String, Object> params);
  //

  /**
   * 新增注册
   */
  //Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentCreate(String staff_id,
  //    HashMap<String, Object> params);

  /**
   * 新增跟进
   */
  //Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentFollow(String staff_id,
  //    HashMap<String, Object> params);

  /**
   * 新增查询
   */
  //Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudents(String staff_id,
  //    String type, HashMap<String, Object> params);

  /**
   * 新增学员
   */
  //Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentMember(String staff_id,
  //    HashMap<String, Object> params);

  /**
   * 具有名下会员的销售列表
   * /api/staffs/:staff_id/filter/sellers/?brand_id=&shop_id= 或者 id=&model=
   */
  //Observable<QcDataResponse<SalerListWrap>> qcGetTrackStudentsFilterSalers(String staff_id,
  //    HashMap<String, Object> params);

  /**
   * 转换率
   * /api/staffs/:staff_id/users/conver/stat/?brand_id=&shop_id= 或者 id=&model=
   * GET参数:[start] [end] [seller_id(无销售seller_id=0)]
   */
  //Observable<QcDataResponse<StudentTransferBean>> qcGetTrackStudentsConver(String staff_id,
  //    HashMap<String, Object> params);

  /**
   * 获取缺勤图表数据
   */
  //Observable<QcDataResponse<AttendanceCharDataBean>> qcGetAttendanceChart(String id,
  //    HashMap<String, Object> params);

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
  //Observable<QcDataResponse<AbsentceListWrap>> qcGetUsersAbsences(String id,
  //    HashMap<String, Object> params);

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
  //Observable<QcDataResponse<AttendanceListWrap>> qcGetUsersAttendances(String id,
  //    HashMap<String, Object> params);

  /**
   * 获取未签课的学员
   */
  //Observable<QcDataResponse<List<StudentWIthCount>>> qcGetNotSignStudent(String staffId,
  //HashMap<String, Object> params);

  /**
   * 推荐人列表
   *
   */
  //Observable<QcDataResponse<SalerUserListWrap>> qcGetTrackStudentsRecommends(String staff_id,
  //    HashMap<String, Object> params);

  /**
   * 来源列表
   */
  //Observable<QcDataResponse<SourceBeans>> qcGetTrackStudentsOrigins(String staff_id,
  //    HashMap<String, Object> params);

  /**
   * 短信部分
   */
  Flowable<QcDataResponse<ShortMsgList>> qcQueryShortMsgList(String id,
      @IntRange(from = 1, to = 2) Integer status, HashMap<String, Object> params);

  Flowable<QcDataResponse<ShortMsgDetail>> qcQueryShortMsgDetail(String id, String messageid,
      HashMap<String, Object> params);

  Flowable<QcResponse> qcPostShortMsg(String staffid, ShortMsgBody body,
      HashMap<String, Object> params);

  Flowable<QcResponse> qcDelShortMsg(String staffid, String messageid,
      HashMap<String, Object> params);

  Flowable<QcResponse> qcPutShortMsg(String staffid, ShortMsgBody body,
      HashMap<String, Object> params);

Flowable<QcDataResponse> qcDataImport(
     String staff_id,  HashMap<String, Object> body);

}
