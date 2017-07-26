package cn.qingchengfit.staffkit.usecase;

import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.body.ArrangeBatchBody;
import cn.qingchengfit.model.body.CourseBody;
import cn.qingchengfit.model.body.DelBatchScheduleBody;
import cn.qingchengfit.model.body.SingleBatchBody;
import cn.qingchengfit.model.responese.CourseSchedules;
import cn.qingchengfit.model.responese.CourseTypeSamples;
import cn.qingchengfit.model.responese.GroupCourseResponse;
import cn.qingchengfit.model.responese.GroupCourseScheduleDetail;
import cn.qingchengfit.model.responese.GymCardtpl;
import cn.qingchengfit.model.responese.GymDetail;
import cn.qingchengfit.model.responese.GymList;
import cn.qingchengfit.model.responese.GymSites;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.QcResponsePrivateBatchDetail;
import cn.qingchengfit.model.responese.QcResponsePrivateCourse;
import cn.qingchengfit.model.responese.QcResponsePrivateDetail;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.model.responese.ResponseService;
import cn.qingchengfit.model.responese.ScheduleTemplete;
import cn.qingchengfit.model.responese.Space;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.rest.RestRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
 * Created by Paper on 16/2/1 2016.
 */
public class GymUseCase {

    RestRepository restRepository;

    @Inject public GymUseCase(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    public Subscription getGymDetail(String id, String model, Action1<ResponseService> action1) {
        //本地缓存
        return restRepository.getGet_api()
            .qcGetService(App.staffId, id, model)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread()).subscribe(action1, new NetWorkThrowable());
    }

    public Subscription getGymList(String brand_id, final Action1<List<CoachService>> action1) {
        return restRepository.qcGetGymList(App.staffId, brand_id)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<GymList>>() {
                @Override public void call(QcResponseData<GymList> qcResponseGymList) {
                    //                        GymBaseInfoAction.writeGyms(qcResponseGymList.data.services);
                    if (qcResponseGymList.status == ResponseConstant.SUCCESS) {
                        action1.call(qcResponseGymList.data.services);
                    } else {
                        action1.call(new ArrayList<CoachService>());
                    }
                }
            }, new NetWorkThrowable());
    }

    public Subscription querySite(String gymid, String gymModel, Action1<QcResponseData<GymSites>> action1) {
        return restRepository.getGet_api()
            .qcGetGymSites(App.staffId, gymid, gymModel)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new NetWorkThrowable());
    }


    public Subscription createCourses(CourseBody body, String gymid, String model, Action1<QcResponse> action1) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", gymid);
        hashMap.put("model", model);
        return restRepository.getPost_api()
            .qcCreateCourse(App.staffId, body, hashMap)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new NetWorkThrowable());
    }

    public Subscription updateCourses(String course_id, CourseBody body, String gymid, String gymmodel, Action1<QcResponse> action1) {
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("id", gymid);
        hashMap.put("model", gymmodel);
        return restRepository.getPost_api()
            .qcUpdateCourse(App.staffId, course_id, hashMap, body)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new NetWorkThrowable());
    }


    public Subscription getGroupCourse(String brandid, String gymid, String model, Action1<QcResponseData<GroupCourseResponse>> action1) {
        return restRepository.getGet_api()
            .qcGetGroupCourse(App.staffId, gymid, model, brandid)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new NetWorkThrowable());
    }

    public Subscription getAllCourse(String brandid, String gymid, String model, Action1<QcResponseData<GroupCourseResponse>> action1) {
        return restRepository.getGet_api()
            .qcGetGroupCourse(App.staffId, gymid, model, brandid)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new NetWorkThrowable());
    }

    public Subscription getPrivateCourse(String brandid, String gymid, String model, Action1<QcResponsePrivateCourse> action1) {
        return restRepository.getGet_api()
            .qcGetPrivateCrourse(App.staffId, gymid, model, brandid)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new NetWorkThrowable());
    }

    public Subscription getPrivateBatches(String coach_id, String gymid, String gymmodle, String brandid,
        Action1<QcResponsePrivateDetail> action1) {
        return restRepository.getGet_api()
            .qcGetPrivateCoaches(App.staffId, coach_id, gymid, gymmodle, brandid)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new NetWorkThrowable());
    }

    public Subscription getGroupBatches(String coach_id, String gymid, String gymmodle, String brandid,
        Action1<QcResponseData<GroupCourseScheduleDetail>> action1) {
        return restRepository.getGet_api()
            .qcGetGroupCourses(App.staffId, coach_id, gymid, gymmodle, brandid)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new NetWorkThrowable());
    }

    public Subscription getBatchDetail(String batch_id, String gymid, String gymmodel, String brandid,
        Action1<QcResponsePrivateBatchDetail> action1) {
        return restRepository.getGet_api()
            .qcGetBatchDetail(App.staffId, batch_id, gymid, gymmodel, brandid)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new NetWorkThrowable());
    }

    public Subscription arrangeBatch(String gymid, String gymmoel, ArrangeBatchBody body, Action1<QcResponse> action1) {
        return restRepository.getPost_api()
            .qcArrangeBatch(App.staffId, gymid, gymmoel, body)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new NetWorkThrowable());
    }

    public Subscription updateBatch(String gymid, String gymmoel, String batchid, ArrangeBatchBody body, Action1<QcResponse> action1) {
        return restRepository.getPost_api()
            .qcUpdateBatch(App.staffId, batchid, gymid, gymmoel, body)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new NetWorkThrowable());
    }

    public Subscription checkBatch(String gymid, String gymmoel, int gymtype, ArrangeBatchBody body, Action1<QcResponse> action1) {
        return restRepository.getPost_api()
            .qcCheckBatch(App.staffId, gymtype == Configs.TYPE_PRIVATE ? "private" : "group", gymid, gymmoel, body)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new NetWorkThrowable());
    }

    public Subscription getBatchTemplete(int gymtype, String gymid, String gymmodel, String teacher_id, String course_id,
        Action1<QcResponseData<ScheduleTemplete>> action1) {
        return restRepository.getGet_api()
            .qcGetBatchTemplate(App.staffId, gymtype == Configs.TYPE_PRIVATE ? "private" : "group", gymid, gymmodel, teacher_id, course_id)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new NetWorkThrowable());
    }

    public Subscription getCourses(String gymid, String gymmode, boolean isPrivate, Action1<QcResponseData<CourseTypeSamples>> action1) {
        return restRepository.getGet_api()
            .qcGetCourses(App.staffId, gymid, gymmode, isPrivate ? 1 : 0)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new NetWorkThrowable());
    }

    public Subscription queryGymCardTpl(String gymid, String model, int type, Action1<QcResponseData<GymCardtpl>> action1) {
        String t = type == 0 ? null : Integer.toString(type);
        return restRepository.getGet_api()
            .qcGetGymCardtpl(App.staffId, gymid, model, t)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new NetWorkThrowable());
    }

    public Subscription queryBatchSchedule(String batchid, int courseType, String brandid, String gymid, String gymmodel,
        Action1<QcResponseData<CourseSchedules>> action1) {
        return restRepository.getGet_api()
            .qcGetbatchSchedules(App.staffId, batchid, courseType == Configs.TYPE_PRIVATE ? "timetables" : "schedules", brandid, gymid,
                gymmodel)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new NetWorkThrowable());
    }

    public Subscription delBatchSchedule(DelBatchScheduleBody body, int courseType, Action1<QcResponse> action1) {
        return restRepository.getPost_api()
            .qcDelBatchSchedule(App.staffId, courseType == Configs.TYPE_PRIVATE ? "timetables" : "schedules", body)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new NetWorkThrowable());
    }

    public Subscription updateBatchSchedule(SingleBatchBody body, String scheduleid, int courseType, Action1<QcResponse> action1) {
        return restRepository.getPost_api()
            .qcUpdateBatchSchedule(App.staffId, courseType == Configs.TYPE_PRIVATE ? "timetables" : "schedules", scheduleid, body)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new NetWorkThrowable());
    }

    public Subscription delSite(String siteid, String gymid, String gymmodel, Action1<QcResponse> action1) {
        return restRepository.getPost_api()
            .qcDelSpace(App.staffId, siteid, gymid, gymmodel, null)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new NetWorkThrowable());
    }

    public Subscription updateSite(String siteid, String gymid, String gymmodel, String brandid, Space space, Action1<QcResponse> action1) {
        return restRepository.getPost_api()
            .qcUpdateSpace(App.staffId, siteid, gymid, gymmodel, brandid, space)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new NetWorkThrowable());
    }

    public Subscription getGymWelcom(String id, String model, Action1<QcResponseData<GymDetail>> action1) {
        return restRepository.getGet_api()
            .qcGetGymDetail(App.staffId, id, model)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new NetWorkThrowable());
    }
}
