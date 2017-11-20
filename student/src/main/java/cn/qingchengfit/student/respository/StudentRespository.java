package cn.qingchengfit.student.respository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.repository.IStudentModel;
import cn.qingchengfit.saasbase.student.bean.StudentWIthCount;
import cn.qingchengfit.saasbase.student.network.body.AbsentceListWrap;
import cn.qingchengfit.saasbase.student.network.body.AttendanceCharDataBean;
import cn.qingchengfit.saasbase.student.network.body.AttendanceListWrap;
import cn.qingchengfit.saasbase.student.network.body.FollowUpDataStatistic;
import cn.qingchengfit.saasbase.student.network.body.StudentTransferBean;
import cn.qingchengfit.saasbase.student.other.RxHelper;
import cn.qingchengfit.student.respository.remote.CustomSubscriber;
import cn.qingchengfit.student.respository.remote.HttpCheckFunc;
import rx.Observable;

/**
 * Created by huangbaole on 2017/11/17.
 */
@Singleton
public class StudentRespository {

    MutableLiveData<AttendanceCharDataBean> attendanceCharDate = new MutableLiveData<>();
    MutableLiveData<AbsentceListWrap> userAbsence = new MutableLiveData<>();
    MutableLiveData<AttendanceListWrap> attendanceResponse = new MutableLiveData<>();
    MutableLiveData<List<StudentWIthCount>> nosignStudents = new MutableLiveData<>();
    MutableLiveData<StudentTransferBean> studentConver = new MutableLiveData<>();

    @Inject
    IStudentModel remoteService;

    @Inject
    public StudentRespository() {

    }

    public LiveData<AttendanceCharDataBean> qcGetAttendanceChart(String id, HashMap<String, Object> params) {
        remoteService
                .qcGetAttendanceChart(id, params)
                .compose(RxHelper.schedulersTransformer())
                .map(new HttpCheckFunc<>())
                .subscribe(new CustomSubscriber<AttendanceCharDataBean>() {
                    @Override
                    public void onNext(AttendanceCharDataBean attendanceCharDataBean) {
                        attendanceCharDate.setValue(attendanceCharDataBean);
                    }
                });
        return attendanceCharDate;
    }

    public LiveData<AbsentceListWrap> qcGetUsersAbsences(String id, HashMap<String, Object> params) {
        remoteService
                .qcGetUsersAbsences(id, params)
                .compose(RxHelper.schedulersTransformer())
                .map(new HttpCheckFunc<>())
                .subscribe(new CustomSubscriber<AbsentceListWrap>() {
                    @Override
                    public void onNext(AbsentceListWrap absentceListWrap) {
                        userAbsence.setValue(absentceListWrap);
                    }
                });
        return userAbsence;

    }

    ;

    public LiveData<AttendanceListWrap> qcGetUsersAttendances(String id, HashMap<String, Object> params) {
        remoteService
                .qcGetUsersAttendances(id, params)
                .compose(RxHelper.schedulersTransformer())
                .map(new HttpCheckFunc<>())
                .subscribe(new CustomSubscriber<AttendanceListWrap>() {
                    @Override
                    public void onNext(AttendanceListWrap attendanceListWrap) {
                        attendanceResponse.setValue(attendanceListWrap);
                    }
                });
        return attendanceResponse;

    };


    public LiveData<List<StudentWIthCount>> qcGetNotSignStudent(String staffId, HashMap<String, Object> params) {
        remoteService
                .qcGetNotSignStudent(staffId, params)
                .compose(RxHelper.schedulersTransformer())
                .map(new HttpCheckFunc<>())
                .subscribe(new CustomSubscriber<List<StudentWIthCount>>() {
                    @Override
                    public void onNext(List<StudentWIthCount> studentWIthCountList) {
                        nosignStudents.setValue(studentWIthCountList);
                    }
                });
        return nosignStudents;
    };

    public LiveData<StudentTransferBean> qcGetTrackStudentsConver(String staff_id, HashMap<String, Object> params){
            remoteService
                    .qcGetTrackStudentsConver(staff_id, params)
                    .compose(RxHelper.schedulersTransformer())
                    .map(new HttpCheckFunc<>())
                    .subscribe(new CustomSubscriber<StudentTransferBean>() {
                        @Override
                        public void onNext(StudentTransferBean bean) {
                            studentConver.setValue(bean);
                        }
                    });
        return studentConver;
    };
}
