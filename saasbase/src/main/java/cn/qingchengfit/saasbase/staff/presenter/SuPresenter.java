package cn.qingchengfit.saasbase.staff.presenter;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.saasbase.staff.model.body.ManagerBody;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
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

    @Inject IStaffModel staffModel;
    private StaffDetailView view;

    @Inject public SuPresenter() {

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

    public void onFixStaff(ManagerBody body, String userid) {
        RxRegiste(staffModel.editStaff(userid,body)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(qcResponse -> {
                if (ResponseConstant.checkSuccess(qcResponse)) {
                    view.onFixSuccess();
                } else {
                    view.onFailed(qcResponse.getMsg());
                }
            }, throwable -> view.onFailed(throwable.getMessage())));
    }
}
