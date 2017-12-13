package cn.qingchengfit.student.respository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;


import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.bill.filter.model.FilterModel;
import cn.qingchengfit.saasbase.repository.IStudentModel;
import cn.qingchengfit.saasbase.staff.network.response.SalerListWrap;
import cn.qingchengfit.saasbase.staff.network.response.SalerTeachersListWrap;
import cn.qingchengfit.saasbase.staff.network.response.SalerUserListWrap;
import cn.qingchengfit.saasbase.student.bean.SourceBeans;
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
import cn.qingchengfit.saasbase.common.mvvm.LiveDataReactiveStreams;
import cn.qingchengfit.student.respository.local.LocalRespository;
import cn.qingchengfit.saasbase.common.remote.HttpException;
import rx.Observable;

/**
 * 继承自StudentRespository,为Student模块的唯一数据来源
 * 作为M-V-VM中的Model层，应该持有两个两个数据源，一个是服务端数据源，另一个是本地持久化的数据源（目前没有）
 * 在Model层中应该是用RxJava来进行数据的响应式处理，然后在传递给VM层时，统一转换为LiveData.
 * 由于目前是用的RxJava1,在{@link StudentRespository}中写了一个toLiveData()函数，
 * 用于将Observable转化为Publisher,再变成LiveData，同时在转化的时候，对Observable对象进行了统一的线程处理。
 * <p>
 * 关于从Observablez转化为LiveData的时候，会遇到的一个问题就是由于数据的来源是有一定状态的，比如请求成功，请求失败或者请求中等
 * 在转化的时候，这些状态是由Observable订阅的时候进行处理，但是当使用{@link android.arch.lifecycle.LiveDataReactiveStreams}
 * 进行转化的时候回默认将异常的处理屏蔽掉，这里目前有两种试下的思路
 * <p>
 * 1. 使用RxBus事件总线，直接将异常的处理传递处理异常的地方。
 * 2. 使用数据包装类，将数据源的数据与当前的数据状态包装到一个类中{@link Resource}
 * <p>
 * 目前的前提是当使用{@link android.arch.lifecycle.LiveDataReactiveStreams} 进行转型的时候，如果事件流中有出现异常就是走到onError
 * 那么会直接抛异常。有两个方式去进行规避
 * 1. 自己写一个转换的方式不去抛 异常
 * 2. 使用操作符onErrorReturn等
 * <p>
 * Created by huangbaole on 2017/11/17.
 */
@Singleton
public class StudentRespositoryImpl implements StudentRespository {

    @Inject
    IStudentModel remoteService;

    @Inject
    LocalRespository localRespository;


    @Inject
    public StudentRespositoryImpl() {
    }

    static <T> LiveData<T> toLiveData(Observable<QcDataResponse<T>> observable) {
        return LiveDataReactiveStreams.fromPublisher(observable.compose(RxHelper.schedulersTransformer()));
    }

    public LiveData<AttendanceCharDataBean> qcGetAttendanceChart(String id, HashMap<String, Object> params) {
        return toLiveData(remoteService.qcGetAttendanceChart(id, params));
    }

    public LiveData<AbsentceListWrap> qcGetUsersAbsences(String id, HashMap<String, Object> params) {
        return toLiveData(remoteService.qcGetUsersAbsences(id, params));
    }


    public LiveData<AttendanceListWrap> qcGetUsersAttendances(String id, HashMap<String, Object> params) {
        return toLiveData(remoteService.qcGetUsersAttendances(id, params));

    }


    public LiveData<List<StudentWIthCount>> qcGetNotSignStudent(String staffId, HashMap<String, Object> params) {
        return toLiveData(remoteService.qcGetNotSignStudent(staffId, params));
    }


    public LiveData<StudentTransferBean> qcGetTrackStudentsConver(String staff_id, HashMap<String, Object> params) {

        return toLiveData(remoteService.qcGetTrackStudentsConver(staff_id, params));
    }


    public LiveData<FollowUpDataStatistic> qcGetTrackStudentsStatistics(String staff_id, HashMap<String, Object> params) {
        return toLiveData(remoteService.qcGetTrackStudentsStatistics(staff_id, params));

    }

    public LiveData<StudentListWrappeForFollow> qcGetTrackStudents(String staff_id, String type, HashMap<String, Object> params) {

        return toLiveData(remoteService.qcGetTrackStudents(staff_id, type, params));
    }

    public LiveData<AllotDataResponseWrap> qcGetStaffList(String staff_id, String type, HashMap<String, Object> params) {
        return toLiveData(remoteService
                .qcGetStaffList(staff_id, type, params));
    }

    public LiveData<StudentListWrapper> qcGetAllotStaffMembers(String staff_id, String type, HashMap<String, Object> params) {
        return toLiveData(remoteService
                .qcGetAllotStaffMembers(staff_id, type, params));

    }

    public LiveData<Boolean> qcRemoveStaff(String staff_id, String type, HashMap<String, Object> params) {
        return toLiveData(remoteService.qcRemoveStaff(staff_id, type, params).map(qcResponse -> {
            QcDataResponse<Boolean> objectQcDataResponse = new QcDataResponse<>();
            if (qcResponse.status == 200) {
                objectQcDataResponse.setData(true);
            } else {
                throw new HttpException(qcResponse.getMsg(), 0);
            }
            return objectQcDataResponse;
        }));

    }

    @Override
    public LiveData<SalerTeachersListWrap> qcGetAllAllocateCoaches(String staff_id, HashMap<String, Object> params) {

        return toLiveData(remoteService.qcGetAllAllocateCoaches(staff_id, params));
    }

    @Override
    public LiveData<Boolean> qcAllocateCoach(String staff_id, HashMap<String, Object> body) {
        return toLiveData(remoteService.qcAllocateCoach(staff_id, body).map(qcResponse -> {
            QcDataResponse<Boolean> objectQcDataResponse = new QcDataResponse<>();
            if (qcResponse.status == 200) {
                objectQcDataResponse.setData(true);
            } else {
                throw new HttpException(qcResponse.getMsg(), 0);
            }
            return objectQcDataResponse;
        }));
    }

    @Override
    public LiveData<SalerUserListWrap> qcGetSalers(String staff_id, String brandid, String shopid, String gymid, String model) {
        return toLiveData(remoteService.qcGetSalers(staff_id, brandid, shopid, gymid, model));
    }

    @Override
    public LiveData<Boolean> qcModifySellers(String staff_id, HashMap<String, Object> params, HashMap<String, Object> body) {
        return toLiveData(remoteService.qcModifySellers(staff_id, params, body).map(qcResponse -> {
            QcDataResponse<Boolean> objectQcDataResponse = new QcDataResponse<>();
            if (qcResponse.status == 200) {
                objectQcDataResponse.setData(true);
            } else {
                throw new HttpException(qcResponse.getMsg(), 0);
            }
            return objectQcDataResponse;
        }));
    }

    @Override
    public LiveData<StudentListWrapper> qcGetAllStudents(String id, HashMap<String, Object> params) {
        return toLiveData(remoteService.qcGetAllStudents(id, params));
    }

    @Override
    public LiveData<List<FilterModel>> qcGetFilterModelFromLocal() {
        MutableLiveData<List<FilterModel>> filte = new MutableLiveData<>();
        filte.setValue(localRespository.getAssetsFilterModels());
        return filte;
    }

    @Override
    public LiveData<SalerUserListWrap> qcGetTrackStudentsRecommends(String id, HashMap<String, Object> params) {
        return toLiveData(remoteService.qcGetTrackStudentsRecommends(id, params));
    }

    @Override
    public LiveData<SourceBeans> qcGetTrackStudentsOrigins(String id, HashMap<String, Object> params) {
        return toLiveData(remoteService.qcGetTrackStudentsOrigins(id, params));
    }

    @Override
    public LiveData<SalerListWrap> qcGetTrackStudentsFilterSalers(String id, HashMap<String, Object> params) {
        return toLiveData(remoteService.qcGetTrackStudentsFilterSalers(id,params));
    }


}
