package cn.qingchengfit.staffkit.views.gym.staff;

import android.content.Intent;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.ManagerBody;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
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
 * Created by Paper on 16/5/12 2016.
 */
public class SuPresenter extends BasePresenter {

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private StaffDetailView view;
    private RestRepository mRestRepository;

    @Inject public SuPresenter(RestRepository useCase) {
        this.mRestRepository = useCase;
    }

    @Override public void onStart() {
        super.onStart();
    }

    @Override public void onStop() {
        super.onStop();
    }

    @Override public void onPause() {
        super.onPause();
    }

    @Override public void attachView(PView v) {
        view = (StaffDetailView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {
        super.attachIncomingIntent(intent);
    }

    @Override public void onCreate() {
        super.onCreate();
    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void onFixStaff(ManagerBody body, String staffid, String userid) {
        RxRegiste(mRestRepository.getPost_api()
            .qcUpdateManager(staffid, userid, gymWrapper.id(), gymWrapper.model(), body)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        view.onFixSuccess();
                    } else {
                        view.onFailed(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onFailed(throwable.getMessage());
                }
            }));
    }
}
