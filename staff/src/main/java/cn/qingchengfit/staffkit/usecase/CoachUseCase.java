package cn.qingchengfit.staffkit.usecase;

import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.body.ManagerBody;
import cn.qingchengfit.model.responese.QcResponsePostions;
import cn.qingchengfit.model.responese.StaffShipResponse;
import cn.qingchengfit.model.responese.Staffs;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.rest.RestRepository;
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
    RestRepository restRepository;

    @Inject public CoachUseCase(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    public Subscription getAllCoach(String gymid, String gymModel, String keyword, Action1<QcDataResponse<Staffs>> action1) {
        return restRepository.getGet_api()
            .qcGetGymCoaches(App.staffId, gymid, gymModel, keyword)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription getAllStaffs(String gymid, String gymModel, String keyword, Action1<QcDataResponse<StaffShipResponse>> action1) {
        return restRepository.getGet_api()
            .qcGetStaffs(App.staffId, gymid, gymModel, null, keyword)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription fixCoach(String gymid, String gymModel, String coachid, Staff coach, Action1<QcResponse> action1) {
        return restRepository.getPost_api()
            .qcFixCoach(App.staffId, coachid, gymid, gymModel, coach)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription delCoach(String gymid, String gymModel, String coachid, Action1<QcResponse> action1) {
        return restRepository.getPost_api()
            .qcDelCoach(App.staffId, coachid, gymid, gymModel)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription createManager(String gymid, String gymModel, ManagerBody body, Action1<QcResponse> action1) {
        return restRepository.getPost_api()
            .qcCreateManager(App.staffId, gymid, gymModel, body)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription updateManager(String gymid, String gymmodel, ManagerBody body, Action1<QcResponse> action1) {
        String id = body.getId();
        ManagerBody b = new ManagerBody();
        b.setPosition_id(body.getPosition_id());
        b.setPhone(body.getPhone());
        b.setUsername(body.getUsername());
        b.setAvatar(body.getAvatar());
        b.setGender(body.getGender());
        b.setArea_code(body.getArea_code());
        return restRepository.getPost_api()
            .qcUpdateManager(App.staffId, id, gymid, gymmodel, b)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription delManager(String gymid, String gymmodel, String id, Action1<QcResponse> action1) {
        return restRepository.getPost_api()
            .qcDelManager(App.staffId, id, gymid, gymmodel)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }

    public Subscription queryPostions(String gymid, String gymmodel, Action1<QcResponsePostions> action1) {
        return restRepository.getGet_api()
            .qcGetPostions(App.staffId, gymid, gymmodel)
            .observeOn(AndroidSchedulers.mainThread())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .subscribe(action1, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            });
    }
}
