package cn.qingchengfit.student.respository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.LiveDataReactiveStreams;
import android.arch.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.List;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
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
import cn.qingchengfit.saasbase.student.other.RxHelper;
import cn.qingchengfit.student.respository.remote.CustomSubscriber;
import cn.qingchengfit.student.respository.remote.HttpCheckFunc;
import rx.Observable;
import rx.RxReactiveStreams;

/**
 * Created by huangbaole on 2017/11/29.
 */

public interface StudentRespository {


    LiveData<AttendanceCharDataBean> qcGetAttendanceChart(String id, HashMap<String, Object> params);

    LiveData<AbsentceListWrap> qcGetUsersAbsences(String id, HashMap<String, Object> params);

    LiveData<AttendanceListWrap> qcGetUsersAttendances(String id, HashMap<String, Object> params);


    LiveData<List<StudentWIthCount>> qcGetNotSignStudent(String staffId, HashMap<String, Object> params);


    LiveData<StudentTransferBean> qcGetTrackStudentsConver(String staff_id, HashMap<String, Object> params);

    LiveData<FollowUpDataStatistic> qcGetTrackStudentsStatistics(String staff_id, HashMap<String, Object> params);

    LiveData<StudentListWrappeForFollow> qcGetTrackStudents(String staff_id, String type, HashMap<String, Object> params);

    LiveData<AllotDataResponseWrap> qcGetStaffList(String staff_id, String type, HashMap<String, Object> params);

    LiveData<StudentListWrapper> qcGetAllotStaffMembers(String staff_id, String type, HashMap<String, Object> params);

    LiveData<Boolean> qcRemoveStaff(String staff_id, String type, HashMap<String, Object> params);


    LiveData<SalerTeachersListWrap> qcGetAllAllocateCoaches(String staff_id, HashMap<String, Object> params);


    LiveData<Boolean> qcAllocateCoach(String staff_id, HashMap<String, Object> body);


    LiveData<SalerUserListWrap> qcGetSalers(String staff_id,
                                            String brandid,
                                            String shopid,
                                            String gymid,
                                            String model);

    LiveData<Boolean> qcModifySellers(String staff_id, HashMap<String, Object> params, HashMap<String, Object> body);


   LiveData<StudentListWrapper> qcGetAllStudents(String id, HashMap<String, Object> params);
}
