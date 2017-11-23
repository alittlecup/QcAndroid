package cn.qingchengfit.student.respository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.repository.IStudentModel;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 返回的LiveData要写成局部变量
 * Created by huangbaole on 2017/11/17.
 */
@Singleton
public class StudentRespository {

    @Inject
    IStudentModel remoteService;

    @Inject
    public StudentRespository() {

    }

    public LiveData<AttendanceCharDataBean> qcGetAttendanceChart(String id, HashMap<String, Object> params) {
        MutableLiveData<AttendanceCharDataBean> attendanceCharDate = new MutableLiveData<>();

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
        MutableLiveData<AbsentceListWrap> userAbsence = new MutableLiveData<>();

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


    public LiveData<AttendanceListWrap> qcGetUsersAttendances(String id, HashMap<String, Object> params) {
        MutableLiveData<AttendanceListWrap> attendanceResponse = new MutableLiveData<>();

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

    }


    public LiveData<List<StudentWIthCount>> qcGetNotSignStudent(String staffId, HashMap<String, Object> params) {
        MutableLiveData<List<StudentWIthCount>> nosignStudents = new MutableLiveData<>();

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
    }


    public LiveData<StudentTransferBean> qcGetTrackStudentsConver(String staff_id, HashMap<String, Object> params) {
        MutableLiveData<StudentTransferBean> studentConver = new MutableLiveData<>();

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
    }


    public LiveData<FollowUpDataStatistic> qcGetTrackStudentsStatistics(String staff_id, HashMap<String, Object> params) {
        MutableLiveData<FollowUpDataStatistic> followUpDataStatisticMutableLiveData = new MutableLiveData<>();
        remoteService
                .qcGetTrackStudentsStatistics(staff_id, params)
                .compose(RxHelper.schedulersTransformer())
                .map(new HttpCheckFunc<>())
                .subscribe(new CustomSubscriber<FollowUpDataStatistic>() {
                    @Override
                    public void onNext(FollowUpDataStatistic followUpDataStatistic) {
                        followUpDataStatisticMutableLiveData.setValue(followUpDataStatistic);
                    }
                });
        return followUpDataStatisticMutableLiveData;
    }

    public LiveData<StudentListWrappeForFollow> qcGetTrackStudents(String staff_id, String type, HashMap<String, Object> params) {
        MutableLiveData<StudentListWrappeForFollow> followUpStatusStudents = new MutableLiveData<>();

        remoteService
                .qcGetTrackStudents(staff_id, type, params)
                .compose(RxHelper.schedulersTransformer())
                .map(new HttpCheckFunc<>())
                .subscribe(new CustomSubscriber<StudentListWrappeForFollow>() {
                    @Override
                    public void onNext(StudentListWrappeForFollow followUpDataStatistic) {
                        followUpStatusStudents.setValue(followUpDataStatistic);
                    }
                });
        return followUpStatusStudents;
    }

    public LiveData<AllotDataResponseWrap> qcGetStaffList(String staff_id, String type, HashMap<String, Object> params) {
        MutableLiveData<AllotDataResponseWrap> allotDataResponseWrapM = new MutableLiveData<>();
        remoteService
                .qcGetStaffList(staff_id, type, params)
                .compose(RxHelper.schedulersTransformer())
                .map(new HttpCheckFunc<>())
                .subscribe(new CustomSubscriber<AllotDataResponseWrap>() {
                    @Override
                    public void onNext(AllotDataResponseWrap allotDataResponseWrap) {
                        allotDataResponseWrapM.setValue(allotDataResponseWrap);
                    }
                });
        return allotDataResponseWrapM;
    }

    public LiveData<StudentListWrapper> qcGetAllotStaffMembers(String staff_id, String type, HashMap<String, Object> params) {
        MutableLiveData<StudentListWrapper> data = new MutableLiveData<>();
        remoteService
                .qcGetAllotStaffMembers(staff_id, type, params)
                .compose(RxHelper.schedulersTransformer())
                .map(new HttpCheckFunc<>())
                .subscribe(new CustomSubscriber<StudentListWrapper>() {
                    @Override
                    public void onNext(StudentListWrapper allotDataResponseWrap) {
                        data.setValue(allotDataResponseWrap);
                    }
                });
        return data;
    }

    public LiveData<Boolean> qcRemoveStaff(String staff_id, String type, HashMap<String, Object> params) {
        MutableLiveData<Boolean> data = new MutableLiveData<>();
        remoteService
                .qcRemoveStaff(staff_id, type, params)
                .compose(RxHelper.schedulersTransformer())
                .map(qcResponse -> qcResponse.status == 200)
                .subscribe(new CustomSubscriber<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        data.setValue(aBoolean);
                    }
                });
        return data;
    }

}
