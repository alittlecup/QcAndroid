package cn.qingchengfit.staffkit.usecase;

import cn.qingchengfit.model.body.AddFollowRecordBody;
import cn.qingchengfit.model.responese.FollowRecords;
import cn.qingchengfit.model.responese.QcResponseStudentCards;
import cn.qingchengfit.model.responese.QcResponseStudentInfo;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.usecase.bean.User_Student;
import java.util.HashMap;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/3/8 2016.
 */
@Deprecated public class StudentUsecase {

    @Inject RestRepository restRepository;

    @Inject public StudentUsecase(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    //    public Subscription queryStudentList(String brandid ,String id,String model, Action1<QcResponseAllStudent> action1) {
    //
    //        return restRepository.getGet_api().qcGetAllStudents(App.staffId,(!StringUtils.isEmpty(id) && !StringUtils.isEmpty(model))?null: brandid, id, model)
  //                .onBackpressureBuffer().subscribeOn(Schedulers.io())
    //                .observeOn(Schedulers.io())
    //                .subscribe(action1, new Action1<Throwable>() {
    //                    @Override
    //                    public void call(Throwable throwable) {
    //
    //                    }
    //                });
    //    }

    //    public Subscription queryStudentList(String id,String model,Action1<QcResponseAllStudent> action1) {
    //
    //        return restRepository.getGet_api().qcGetAllStudents(App.staffId, null, id, model)
  //                .onBackpressureBuffer().subscribeOn(Schedulers.io())
    //                .observeOn(AndroidSchedulers.mainThread())
    //                .subscribe(action1, new NetError());
    //    }
    //

    //public Subscription queryClassRecords(String studentid, String gymid, String gymmoedl, String brand_id, Action1<QcResponseData<ClassRecords>> action1) {
    //return restRepository.getGet_api().qcGetStudentClassRecords(App.staffId, studentid, gymid, gymmoedl, brand_id)
  //        .onBackpressureBuffer().subscribeOn(Schedulers.io())
    //        .observeOn(AndroidSchedulers.mainThread())
    //        .subscribe(action1, new Action1<Throwable>() {
    //            @Override
    //            public void call(Throwable throwable) {
    //
    //            }
    //        });
    //}

    public Subscription queryCards(String studentid, String gymid, String gymmoedl, String brand_id,
        Action1<QcResponseStudentCards> action1) {
        return restRepository.getGet_api()
            .qcGetStudentCards(App.staffId, studentid, gymid, gymmoedl, brand_id)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(action1, new NetWorkThrowable());
    }

    public Subscription queryFollowRecord(String studentid, String gymid, String gymmoedl, String brand_id, int page,
        Action1<QcDataResponse<FollowRecords>> action1) {
        return restRepository.getGet_api()
            .qcGetStudentFollow(App.staffId, studentid, gymid, gymmoedl, brand_id, page)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(action1, new NetWorkThrowable());
    }

    public Subscription queryBaseInfo(String studentid, String gymid, String gymmoedl, String brand_id,
        Action1<QcResponseStudentInfo> action1) {
        return restRepository.getGet_api()
            .qcGetStudentInfo(App.staffId, studentid, gymid, gymmoedl, brand_id)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(action1, new NetWorkThrowable());
    }

    /**
     * 修改学员
     */
    public Subscription updateStudent(String id, String gymid, String gymmodel, String brandid, User_Student user_student,
        Action1<QcDataResponse> action1) {
        user_student.setJoined_at(null);
        return restRepository.getPost_api()
            .qcUpdateStudent(App.staffId, id, gymid, gymmodel, brandid, user_student)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(action1, new NetWorkThrowable());
    }

    /**
     * 添加学员
     */
    public Subscription addStudent(User_Student user_student, HashMap<String, Object> params,
        Action1<QcDataResponse> action1) {
        user_student.setJoined_at(null);
        return restRepository.getPost_api().qcCreateStudent(App.staffId, params, user_student)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(action1, new NetWorkThrowable());
    }

    /**
     * 删除学员
     */
    public Subscription delStudent(String id, String gymid, String gymmodel, String brandid, String shop_ids, Action1<QcResponse> action1) {
        return restRepository.getPost_api()
            .qcDelStudent(App.staffId, id, gymid, gymmodel, brandid, shop_ids)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(action1, new NetWorkThrowable());
    }

    public Subscription addFollowRecord(String user_id, String brandid, String gymid, String gymmodel, AddFollowRecordBody body,
        Action1<QcResponse> action1) {
        return restRepository.getPost_api()
            .qcAddFollowRecord(App.staffId, user_id, body, brandid, gymid, gymmodel)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(action1, new NetWorkThrowable());
    }
}
