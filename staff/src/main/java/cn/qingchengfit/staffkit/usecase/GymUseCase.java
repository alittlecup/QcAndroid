package cn.qingchengfit.staffkit.usecase;

import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.base.Space;
import cn.qingchengfit.model.body.CourseBody;
import cn.qingchengfit.model.responese.GymDetail;
import cn.qingchengfit.model.responese.GymList;
import cn.qingchengfit.model.responese.GymSites;
import cn.qingchengfit.model.responese.ResponseService;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import timber.log.Timber;

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

  StaffRespository restRepository;

  @Inject public GymUseCase(StaffRespository restRepository) {
    this.restRepository = restRepository;
  }

  public Subscription getGymDetail(String id, String model, Action1<ResponseService> action1) {
    //本地缓存
    return restRepository.getStaffAllApi()
        .qcGetService(App.staffId, id, model)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(action1, new NetWorkThrowable());
  }

  public Subscription getGymList(String brand_id, final Action1<List<CoachService>> action1) {
    return restRepository.getStaffAllApi()
        .qcGetCoachService(App.staffId, brand_id)
        .retry(new Func2<Integer, Throwable, Boolean>() {
          @Override public Boolean call(Integer integer, Throwable throwable) {
            return integer < 3 && throwable instanceof SocketTimeoutException;
          }
        })
        .doOnError(throwable -> {
          if (throwable != null) Timber.e("retrofit:" + throwable.getMessage());
        })
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<GymList>>() {
          @Override public void call(QcDataResponse<GymList> qcResponseGymList) {
            //                        GymBaseInfoAction.writeGyms(qcResponseGymList.data.services);
            if (ResponseConstant.checkSuccess(qcResponseGymList)) {
              action1.call(qcResponseGymList.data.services);
            } else {
              action1.call(new ArrayList<CoachService>());
            }
          }
        }, new NetWorkThrowable());
  }

  public Subscription querySite(String gymid, String gymModel,
      Action1<QcDataResponse<GymSites>> action1) {
    return restRepository.getStaffAllApi()
        .qcGetGymSites(App.staffId, gymid, gymModel)
        .observeOn(AndroidSchedulers.mainThread())
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .subscribe(action1, new NetWorkThrowable());
  }

  public Subscription createCourses(CourseBody body, String gymid, String model,
      Action1<QcResponse> action1) {
    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("id", gymid);
    hashMap.put("model", model);
    return restRepository.getStaffAllApi()
        .qcCreateCourse(App.staffId, body, hashMap)
        .observeOn(AndroidSchedulers.mainThread())
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .subscribe(action1, new NetWorkThrowable());
  }

  public Subscription updateCourses(String course_id, CourseBody body, String gymid,
      String gymmodel, Action1<QcResponse> action1) {
    HashMap<String, Object> hashMap = new HashMap<>();
    hashMap.put("id", gymid);
    hashMap.put("model", gymmodel);
    return restRepository.getStaffAllApi()
        .qcUpdateCourse(App.staffId, course_id, hashMap, body)
        .observeOn(AndroidSchedulers.mainThread())
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .subscribe(action1, new NetWorkThrowable());
  }


  public Subscription delSite(String siteid, String gymid, String gymmodel,
      Action1<QcResponse> action1) {
    return restRepository.getStaffAllApi()
        .qcDelSpace(App.staffId, siteid, gymid, gymmodel, null)
        .observeOn(AndroidSchedulers.mainThread())
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .subscribe(action1, new NetWorkThrowable());
  }

  public Subscription updateSite(String siteid, String gymid, String gymmodel, String brandid,
      Space space, Action1<QcResponse> action1) {
    return restRepository.getStaffAllApi()
        .qcUpdateSpace(App.staffId, siteid, gymid, gymmodel, brandid, space)
        .observeOn(AndroidSchedulers.mainThread())
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .subscribe(action1, new NetWorkThrowable());
  }

  public Subscription getGymWelcom(String id, String model,
      Action1<QcDataResponse<GymDetail>> action1) {
    return restRepository.getStaffAllApi()
        .qcGetGymDetail(App.staffId, id, model)
        .observeOn(AndroidSchedulers.mainThread())
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .subscribe(action1, new NetWorkThrowable());
  }
}
