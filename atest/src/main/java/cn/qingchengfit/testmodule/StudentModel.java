package cn.qingchengfit.testmodule;

import java.util.HashMap;

import javax.inject.Inject;

import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.repository.IStudentModel;
import cn.qingchengfit.saasbase.staff.network.response.SalerListWrap;
import cn.qingchengfit.saasbase.staff.network.response.SalerTeachersListWrap;
import cn.qingchengfit.saasbase.staff.network.response.SalerUserListWrap;
import cn.qingchengfit.saasbase.student.network.api.StudentApi;
import cn.qingchengfit.saasbase.student.network.body.AddStdudentBody;
import cn.qingchengfit.saasbase.student.network.body.AllotDataResponseWrap;
import cn.qingchengfit.saasbase.student.network.body.FollowUpDataStatistic;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrappeForFollow;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrapper;
import cn.qingchengfit.saasbase.student.network.body.StudentTransferBean;
import cn.qingchengfit.saasbase.student.network.body.StudentWithCoashListWrap;
import rx.Observable;

/**
 * Created by huangbaole on 2017/10/26.
 */

public class StudentModel implements IStudentModel {
    QcRestRepository qcRestRepository;
    StudentApi studentApi;

    public StudentModel(QcRestRepository qcRestRepository) {
        this.qcRestRepository = qcRestRepository;
        studentApi = qcRestRepository.createGetApi(StudentApi.class);
    }

    @Override
    public Observable<QcDataResponse<StudentListWrapper>> getAllStudentNoPermission() {
        return null;
    }

    @Override
    public Observable<QcDataResponse> addStudent(AddStdudentBody body) {
        return null;
    }

    @Override
    public Observable<QcDataResponse<StudentListWrapper>> qcGetCardBundldStudents(String id, HashMap<String, Object> params) {
        return studentApi.qcGetCardBundldStudents(id, params);
    }

    @Override
    public Observable<QcDataResponse<StudentListWrapper>> qcGetAllStudents(String id, HashMap<String, Object> params) {
        return studentApi.qcGetAllStudents(id, params);
    }

    @Override
    public Observable<QcDataResponse<AllotDataResponseWrap>> qcGetCoachList(String staff_id, HashMap<String, Object> params) {
        return studentApi.qcGetCoachList(staff_id,params);
    }

    @Override
    public Observable<QcDataResponse<AllotDataResponseWrap>> qcGetAllotSalesPreView(String staff_id, HashMap<String, Object> params) {
        return studentApi.qcGetAllotSalesPreView(staff_id,params);
    }

    @Override
    public Observable<QcDataResponse<StudentListWrapper>> qcGetAllotSaleOwenUsers(String staff_id, HashMap<String, Object> params) {
        return studentApi.qcGetAllotSaleOwenUsers(staff_id,params);
    }

    @Override
    public Observable<QcDataResponse<StudentWithCoashListWrap>> qcGetCoachStudentDetail(String staff_id, HashMap<String, Object> params) {
        return studentApi.qcGetCoachStudentDetail(staff_id,params);
    }

    @Override
    public Observable<QcDataResponse<SalerUserListWrap>> qcGetSalers(String staff_id, String brandid, String shopid, String gymid, String model) {
        return studentApi.qcGetSalers(staff_id, brandid, shopid, gymid, model);
    }

    @Override
    public Observable<QcResponse> qcModifySellers(String staff_id, HashMap<String, Object> params, HashMap<String, Object> body) {
        return studentApi.qcModifySellers(staff_id, params, body);
    }

    @Override
    public Observable<QcDataResponse<SalerTeachersListWrap>> qcGetAllAllocateCoaches(String staff_id, HashMap<String, Object> params) {
        return studentApi.qcGetAllAllocateCoaches(staff_id, params);
    }

    @Override
    public Observable<QcResponse> qcAllocateCoach(String staff_id, HashMap<String, Object> body) {
        return studentApi.qcAllocateCoach(staff_id,body);
    }

    @Override
    public Observable<QcResponse> qcRemoveStudent(String staff_id, HashMap<String, Object> body) {
        return studentApi.qcRemoveStudent(staff_id, body);
    }

    @Override
    public Observable<QcResponse> qcDeleteStudents(String staff_id, HashMap<String, Object> body) {
        return studentApi.qcDeleteStudents(staff_id, body);
    }

    @Override
    public Observable<QcDataResponse<FollowUpDataStatistic>> qcGetTrackStudentsStatistics(String staff_id, HashMap<String, Object> params) {
        return studentApi.qcGetTrackStudentsStatistics(staff_id, params);
    }

    @Override
    public Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentCreate(String staff_id, HashMap<String, Object> params) {
        return studentApi.qcGetTrackStudentCreate(staff_id,params);
    }

    @Override
    public Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentFollow(String staff_id, HashMap<String, Object> params) {
        return studentApi.qcGetTrackStudentFollow(staff_id, params);
    }

    @Override
    public Observable<QcDataResponse<StudentListWrappeForFollow>> qcGetTrackStudentMember(String staff_id, HashMap<String, Object> params) {
        return studentApi.qcGetTrackStudentMember(staff_id, params);
    }

    @Override
    public Observable<QcDataResponse<SalerListWrap>> qcGetTrackStudentsFilterSalers(String staff_id, HashMap<String, Object> params) {
        return studentApi.qcGetTrackStudentsFilterSalers(staff_id, params);
    }

    @Override
    public Observable<QcDataResponse<StudentTransferBean>> qcGetTrackStudentsConver(String staff_id, HashMap<String, Object> params) {
        return studentApi.qcGetTrackStudentsConver(staff_id, params);
    }
}
