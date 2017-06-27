package cn.qingchengfit.staffkit.views.main;

import android.content.Intent;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.responese.GymList;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.staffkit.model.dbaction.GymBaseInfoAction;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.CView;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
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
 * Created by Paper on 16/2/16 2016.
 */
public class GymListPresenter extends BasePresenter {

    @Inject GymWrapper gymWrapper;
    @Inject LoginStatus loginStatus;
    private RestRepository mRestRepository;
    private GymListView mView;

    @Inject public GymListPresenter(RestRepository restRepository) {
        this.mRestRepository = restRepository;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        super.attachView(v);
        mView = (GymListView) v;
    }

    public void subscribeGymsByBrandId() {
        RxRegiste(GymBaseInfoAction.getAllGyms().filter(new Func1<List<CoachService>, Boolean>() {
            @Override public Boolean call(List<CoachService> list) {
                return list != null;
            }
        }).flatMap(new Func1<List<CoachService>, Observable<List<CoachService>>>() {
            @Override public Observable<List<CoachService>> call(List<CoachService> coachServices) {
                List<CoachService> ret = new ArrayList<CoachService>();
                for (CoachService service : coachServices) {
                    if (service.brand_id().equals(gymWrapper.brand_id())) ret.add(service);
                }
                return Observable.just(ret);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<List<CoachService>>() {
            @Override public void call(List<CoachService> list) {
                mView.onServiceList(list);
            }
        }));
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        mView = null;
    }

    void loadData() {
        RxRegiste(mRestRepository.getGet_api()
            .qcGetCoachService(loginStatus.staff_id(), null)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<GymList>>() {
                @Override public void call(QcResponseData<GymList> qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        GymBaseInfoAction.writeGyms(qcResponse.getData().services);
                    } else {
                        mView.onShowError(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    mView.onShowError(throwable.getMessage());
                }
            }));
    }

    interface GymListView extends CView {
        void onServiceList(List<CoachService> list);
    }
}
