package cn.qingchengfit.staffkit.views.wardrobe.choose;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.AllLockers;
import cn.qingchengfit.model.responese.Locker;
import cn.qingchengfit.model.responese.LockerRegion;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * 仅仅单场馆
 */
public class ChooseWardrobePresenter extends BasePresenter {
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private MVPView view;
    private StaffRespository restRepository;

    @Inject public ChooseWardrobePresenter(StaffRespository restRepository) {
        this.restRepository = restRepository;
    }

    public void queryAllLocker(String staffid) {
        RxRegiste(restRepository.getStaffAllApi()
            .qcGetAllLockers(staffid, gymWrapper.getParams())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<AllLockers>>() {
                @Override public void call(QcDataResponse<AllLockers> qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onList(qcResponse.data.lockers, qcResponse.data.regions);
                    } else {
                        view.onShowError(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onShowError(throwable.getMessage());
                }
            }));
    }

    public void searchLocker(String staffid, String key) {
        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("q", key);

        RxRegiste(restRepository.getStaffAllApi()
            .qcGetAllLockers(staffid, params)
            .onBackpressureLatest()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<AllLockers>>() {
                @Override public void call(QcDataResponse<AllLockers> qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onSearch(qcResponse.data.lockers, qcResponse.data.regions);
                    } else {
                        view.onShowError(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onShowError(throwable.getMessage());
                }
            }));

        //        String[] test = {"男", "女", "女2", "女3", "女4", "女5", "女6", "女7", "女8", "女9"};
        //        List<Locker> lockers = new ArrayList<>();
        //        LockerRegion lockerRegion1 = new LockerRegion();
        //        lockerRegion1.id = 1L;
        //        lockerRegion1.name = "A区";
        //        LockerRegion lockerRegion2 = new LockerRegion();
        //        lockerRegion2.id = 2L;
        //        lockerRegion2.name = "B区";
        //
        //
        //        for (int i = 0; i < 10; i++) {
        //            Locker locker = new Locker.Builder()
        //                    .id((long) i)
        //                    .gender(0)
        //                    .name(test[i])
        //                    .is_used(i % 2 == 0 )
        //                    .region(i % 2 == 0 ? lockerRegion1 : lockerRegion2)
        //                    .build();
        //            lockers.add(locker);
        //        }
        //        view.onSearch(lockers);
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    public interface MVPView extends CView {
        void onList(List<Locker> lockers, List<LockerRegion> regions);

        void onSearch(List<Locker> lockers, List<LockerRegion> regions);
    }
}
