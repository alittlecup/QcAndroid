package cn.qingchengfit.staffkit.views.main;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saasbase.db.GymBaseInfoAction;
import cn.qingchengfit.staffkit.rest.RestRepository;
import io.reactivex.Flowable;
import io.reactivex.disposables.Disposable;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
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
 * Created by Paper on 16/2/16 2016.
 */
public class GymListPresenter extends BasePresenter {

    @Inject GymWrapper gymWrapper;
    @Inject LoginStatus loginStatus;
    @Inject GymBaseInfoAction gymBaseInfoAction;
    private RestRepository mRestRepository;
    private GymListView mView;
    private Disposable dispose;

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
        RxRegiste(gymBaseInfoAction.getAllGyms()
          .flatMap(services -> {
              List<CoachService> ret = new ArrayList<CoachService>();
              for (CoachService service : services) {
                  if (service.brand_id().equals(gymWrapper.brand_id())) ret.add(service);
              }
              return Flowable.just(ret);
          })
            .onBackpressureDrop()
            .subscribeOn(io.reactivex.schedulers.Schedulers.io())
            .observeOn(io.reactivex.android.schedulers.AndroidSchedulers.mainThread())
            .subscribe(services -> mView.onServiceList(services)));
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        mView = null;
        if (dispose != null && !dispose.isDisposed())
            dispose.dispose();
    }

    void loadData() {
        RxRegiste(mRestRepository.getGet_api()
            .qcGetCoachService(loginStatus.staff_id(), null)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(qcResponse -> {
                if (ResponseConstant.checkSuccess(qcResponse)) {
                    gymBaseInfoAction.writeGyms(qcResponse.getData().services);
                } else {
                    mView.onShowError(qcResponse.getMsg());
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
