package cn.qingchengfit.student.respository;

import android.arch.lifecycle.LiveData;
import android.support.annotation.IntRange;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.student.bean.AllotDataResponseWrap;
import cn.qingchengfit.student.bean.ShortMsgDetail;
import cn.qingchengfit.student.bean.ShortMsgList;
import io.reactivex.Flowable;
import java.util.HashMap;

/**
 * Created by huangbaole on 2017/11/29.
 */

public interface StudentRepository {

    //
    //LiveData<AttendanceCharDataBean> qcGetAttendanceChart(String id, HashMap<String, Object> params);
    //
    //LiveData<AbsentceListWrap> qcGetUsersAbsences(String id, HashMap<String, Object> params);
    //
    //LiveData<AttendanceListWrap> qcGetUsersAttendances(String id, HashMap<String, Object> params);
    //
    //
    //LiveData<List<StudentWIthCount>> qcGetNotSignStudent(String staffId, HashMap<String, Object> params);
    //
    //
    //LiveData<StudentTransferBean> qcGetTrackStudentsConver(String staff_id, HashMap<String, Object> params);
    //
    //LiveData<FollowUpDataStatistic> qcGetTrackStudentsStatistics(String staff_id, HashMap<String, Object> params);
    //
    //LiveData<StudentListWrappeForFollow> qcGetTrackStudents(String staff_id, String type, HashMap<String, Object> params);
    //
    LiveData<Resource<AllotDataResponseWrap>> qcGetStaffList(String staff_id, String type, HashMap<String, Object> params);
    //
    //LiveData<StudentListWrapper> qcGetAllotStaffMembers(String staff_id, String type, HashMap<String, Object> params);
    //
    //LiveData<Boolean> qcRemoveStaff(String staff_id, String type, HashMap<String, Object> params);
    //
    //
    //LiveData<SalerTeachersListWrap> qcGetAllAllocateCoaches(String staff_id, HashMap<String, Object> params);
    //
    //
    //LiveData<Boolean> qcAllocateCoach(String staff_id, HashMap<String, Object> body);
    //
    //
    //LiveData<SalerUserListWrap> qcGetSalers(String staff_id,
    //                                        String brandid,
    //                                        String shopid,
    //                                        String gymid,
    //                                        String model);
    //
    //LiveData<Boolean> qcModifySellers(String staff_id, HashMap<String, Object> params, HashMap<String, Object> body);
    //
    //
    //LiveData<StudentListWrapper> qcGetAllStudents(String id, HashMap<String, Object> params);
    //
    //LiveData<List<FilterModel>> qcGetFilterModelFromLocal();
    //
    //LiveData<SalerUserListWrap> qcGetTrackStudentsRecommends(String id, HashMap<String, Object> params);
    //
    //LiveData<SourceBeans> qcGetTrackStudentsOrigins(String id, HashMap<String, Object> params);
    //LiveData<SalerListWrap> qcGetTrackStudentsFilterSalers(String id, HashMap<String, Object> params);



}
