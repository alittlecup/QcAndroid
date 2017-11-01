package cn.qingchengfit.saasbase.coach.presenter;

import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.network.response.QcResponseData;
import cn.qingchengfit.saasbase.repository.GetApi;
import cn.qingchengfit.saasbase.repository.PostApi;
import cn.qingchengfit.saasbase.staff.model.StaffShipResponse;
import cn.qingchengfit.saasbase.staff.model.Staffs;
import cn.qingchengfit.saasbase.staff.model.body.ManagerBody;
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
 * Created by Paper on 16/1/29 2016.
 */
public class CoachUseCase {
  @Inject QcRestRepository restRepository;

    @Inject public CoachUseCase() {
    }

    public Subscription getAllCoach(String staffId, String gymid, String gymModel, String keyword, Action1<QcResponseData<Staffs>> action1) {
        return restRepository.createGetApi(GetApi.class)
            .qcGetGymCoaches(staffId, gymid, gymModel, keyword)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription getAllStaffs(String staffId, String gymid, String gymModel, String keyword, Action1<QcResponseData<StaffShipResponse>> action1) {
        return restRepository.createGetApi(GetApi.class)
            .qcGetStaffs(staffId, gymid, gymModel, null, keyword)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription fixCoach(String staffId, String gymid, String gymModel, String coachid, Staff coach, Action1<QcResponse> action1) {
        return restRepository.createPostApi(PostApi.class)
            .qcFixCoach(staffId, coachid, gymid, gymModel, coach)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription delCoach(String staffId, String gymid, String gymModel, String coachid, Action1<QcResponse> action1) {
        return restRepository.createPostApi(PostApi.class)
            .qcDelCoach(staffId, coachid, gymid, gymModel)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription createManager(String staffId, String gymid, String gymModel, ManagerBody body, Action1<QcResponse> action1) {
        return restRepository.createPostApi(PostApi.class)
            .qcCreateManager(staffId, gymid, gymModel, body)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription updateManager(String staffId, String gymid, String gymmodel, ManagerBody body, Action1<QcResponse> action1) {
        String id = body.getId();
        ManagerBody b = new ManagerBody();
        b.setPosition_id(body.getPosition_id());
        b.setPhone(body.getPhone());
        b.setUsername(body.getUsername());
        b.setAvatar(body.getAvatar());
        b.setGender(body.getGender());
        b.setArea_code(body.getArea_code());
        return restRepository.createPostApi(PostApi.class)
            .qcUpdateManager(staffId, id, gymid, gymmodel, b)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    //public Subscription delManager(String staffId, String gymid, String gymmodel, String id, Action1<QcResponse> action1) {
    //    return restRepository.createPostApi(PostApi.class)
    //        .qcDelManager(staffId, id, gymid, gymmodel)
    //        .observeOn(AndroidSchedulers.mainThread())
    //        .onBackpressureBuffer()
    //        .subscribeOn(Schedulers.io())
    //        .subscribe(action1, new Action1<Throwable>() {
    //            @Override public void call(Throwable throwable) {
    //
    //            }
    //        });
    //}
    //
    //public Subscription queryPostions(String staffId, String gymid, String gymmodel, Action1<PostionListWrap> action1) {
    //    return restRepository.createGetApi(GetApi.class)
    //        .qcGetPostions(staffId, gymid, gymmodel)
    //        .observeOn(AndroidSchedulers.mainThread())
    //        .onBackpressureBuffer()
    //        .subscribeOn(Schedulers.io())
    //        .subscribe(action1, new Action1<Throwable>() {
    //            @Override public void call(Throwable throwable) {
    //
    //            }
    //        });
    //}
}
