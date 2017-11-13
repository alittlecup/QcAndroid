package cn.qingchengfit.saasbase.repository;

import java.util.HashMap;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.staff.network.response.SalerListWrap;
import cn.qingchengfit.saasbase.staff.network.response.SalerTeachersListWrap;
import cn.qingchengfit.saasbase.staff.network.response.SalerUserListWrap;
import cn.qingchengfit.saasbase.student.network.body.AddStdudentBody;
import cn.qingchengfit.saasbase.student.network.body.AllotDataResponseWrap;
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
    Observable<QcDataResponse<StudentListWrapper>> getAllStudentNoPermission();

    /**
     * 新增会员
     */
    Observable<QcDataResponse> addStudent(AddStdudentBody body);

    /**
     * 会员卡可绑定的会员列表
     */
    Observable<QcDataResponse<StudentListWrapper>> qcGetCardBundldStudents(String id, HashMap<String, Object> params);

    /**
     * 工作人员下所有会员
     *
     * @param id
     * @param params
     * @return
     */
    Observable<QcDataResponse<StudentListWrapper>> qcGetAllStudents(String id, HashMap<String, Object> params);

    /**
     * 教练分配
     *
     * @param staff_id
     * @param params
     * @return
     */
    Observable<QcDataResponse<AllotDataResponseWrap>> qcGetCoachList(String staff_id, HashMap<String, Object> params);

    /**
     * 销售列表预览接口
     * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/preview/?brand_id=2&shop_id=1
     * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/preview/?id=5370&model=staff_gym
     */
    Observable<QcDataResponse<AllotDataResponseWrap>> qcGetAllotSalesPreView(String staff_id, HashMap<String, Object> params);

    /**
     * 销售名下会员列表接口(无销售的会员列表不需要传seller_id)
     * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?brand_id=2&shop_id=2&seller_id=1
     * GET http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?id=5370&model=staff_gym&seller_id=1
     */
    Observable<QcDataResponse<StudentListWrapper>> qcGetAllotSaleOwenUsers(String staff_id, HashMap<String, Object> params);

    /**
     * 教练名下会员列表接口(无销售的会员列表不需要传seller_id)
     */
    Observable<QcDataResponse<StudentWithCoashListWrap>> qcGetCoachStudentDetail(String staff_id, HashMap<String, Object> params);

    /**
     * 获取销售 卖卡  包含销售 不包含教练
     */
    Observable<QcDataResponse<SalerUserListWrap>> qcGetSalers(String staff_id,
                                                              String brandid,
                                                              String shopid,
                                                              String gymid,
                                                              String model);

    /**
     * /**
     * 批量变更销售
     * http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?brand_id=2&shop_id=1
     * http://127.0.0.1:9000/api/v2/staffs/3281/sellers/users/?id=5370&model=staff_gym
     * PUT {"user_ids":"1,2,3,4,5", "seller_ids":"10,11"}
     *
     * @param body user_ids  seller_ids
     */
    Observable<QcResponse> qcModifySellers(String staff_id,
                                           HashMap<String, Object> params,
                                           HashMap<String, Object> body);

    /**
     * 获取教练列表
     *
     * @param staff_id
     * @param params
     * @return
     */
    Observable<QcDataResponse<SalerTeachersListWrap>> qcGetAllAllocateCoaches(
            String staff_id, HashMap<String, Object> params);

    /**
     * 分配教练
     */
    Observable<QcResponse> qcAllocateCoach(String staff_id,
                                           HashMap<String, Object> body);

    /**
     * 批量移除某个教练名下会员
     */
    Observable<QcResponse> qcRemoveStudent(String staff_id,
                                           HashMap<String, Object> body);

    /**
     * 批量移除某个销售名下会员:
     */
    Observable<QcResponse> qcDeleteStudents(String staff_id,
                                            HashMap<String, Object> body);


    /**
     * 数据统计
     * /api/staffs/:staff_id/users/stat/?brand_id=&shop_id= 或者 id=&model=
     * GET参数:status,[start][end][seller_id(无销售seller_id=0)]
     */
    Observable<QcDataResponse<FollowUpDataStatistic>> qcGetTrackStudentsStatistics(String staff_id,
                                                                                   HashMap<String, Object> params);


    /**
     * 新增注册
     */
    Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentCreate(
            String staff_id, HashMap<String, Object> params);

    /**
     * 新增跟进
     */
    Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentFollow(
            String staff_id, HashMap<String, Object> params);

    /**
     * 新增学员
     */
    Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentMember(
            String staff_id, HashMap<String, Object> params);


    /**
     * 具有名下会员的销售列表
     * /api/staffs/:staff_id/filter/sellers/?brand_id=&shop_id= 或者 id=&model=
     */
    Observable<QcDataResponse<SalerListWrap>> qcGetTrackStudentsFilterSalers(
            String staff_id, HashMap<String, Object> params);


    /**
     * 转换率
     * /api/staffs/:staff_id/users/conver/stat/?brand_id=&shop_id= 或者 id=&model=
     * GET参数:[start] [end] [seller_id(无销售seller_id=0)]
     */
    Observable<QcDataResponse<StudentTransferBean>> qcGetTrackStudentsConver(
            String staff_id, HashMap<String, Object> params);


}
