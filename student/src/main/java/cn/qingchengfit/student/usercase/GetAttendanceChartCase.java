package cn.qingchengfit.student.usercase;

import javax.inject.Inject;

import cn.qingchengfit.student.respository.StudentRespositoryImpl;

/**
 * Created by huangbaole on 2017/11/28.
 */

public class GetAttendanceChartCase{
    @Inject
    StudentRespositoryImpl respository;

//    public LiveData<Resource<>> get(String staff_id, HashMap params) {
//        return LiveDataReactiveStreams.fromPublisher(RxReactiveStreams.toPublisher(respository.qcGetAttendanceChart(staff_id, params)));
//    }
}
