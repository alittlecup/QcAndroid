package cn.qingchengfit.saasbase.staff.presenter;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saasbase.network.response.QcResponseData;
import cn.qingchengfit.saasbase.repository.GetApi;
import cn.qingchengfit.saasbase.staff.model.StaffShipResponse;
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
 * Created by Paper on 16/5/11 2016.
 */
public class StaffListPresenter extends BasePresenter {

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject QcRestRepository mRestRepository;
    private StaffListView view;
    private CoachUseCase useCase;
    private Subscription spQuery;

    @Inject public StaffListPresenter(CoachUseCase useCase) {
        this.useCase = useCase;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (StaffListView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
        if (spQuery != null) spQuery.unsubscribe();
    }

    public void queryData(String staffId, String keyword) {
        spQuery = useCase.getAllStaffs(staffId, gymWrapper.id(), gymWrapper.model(), keyword, new Action1<QcResponseData<StaffShipResponse>>() {
            @Override public void call(QcResponseData<StaffShipResponse> qcResponseStaffs) {
                if (qcResponseStaffs.getStatus() == ResponseConstant.SUCCESS) {
                    view.onData(qcResponseStaffs.data.ships);
                } else {
                    view.onFailed();
                    // ToastUtils.logHttp(qcResponseStaffs);
                }
            }
        });
    }

    public void querSelfInfo(String id) {
        RxRegiste(mRestRepository.createRxJava1Api(GetApi.class)
            .qcGetSelfInfo(id).onBackpressureBuffer().subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(qcResponse -> {
                if (ResponseConstant.checkSuccess(qcResponse)) {
                    view.onSelfInfo(qcResponse.getData().staff);
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                }
            }));
    }
}
