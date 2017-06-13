package cn.qingchengfit.staffkit.views.gym.staff;

import android.content.Intent;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.body.ManagerBody;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.model.responese.QcResponsePostions;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.usecase.CoachUseCase;
import javax.inject.Inject;
import rx.functions.Action1;

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
public class StaffDetailPresenter extends BasePresenter {

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private StaffDetailView view;
    private CoachUseCase useCase;

    @Inject public StaffDetailPresenter(CoachUseCase useCase) {
        this.useCase = useCase;
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

    public void onFixStaff(ManagerBody body) {
        RxRegiste(useCase.updateManager(gymWrapper.id(), gymWrapper.model(), body, new Action1<QcResponse>() {
            @Override public void call(QcResponse qcResponse) {
                if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                    view.onFixSuccess();
                } else {
                    view.onFailed(qcResponse.getMsg());
                }
            }
        }));
    }

    public void onAddStaff(ManagerBody body) {
        RxRegiste(useCase.createManager(gymWrapper.id(), gymWrapper.model(), body, new Action1<QcResponse>() {
            @Override public void call(QcResponse qcResponse) {
                if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                    view.onFixSuccess();
                } else {
                    view.onFailed(qcResponse.getMsg());
                }
            }
        }));
    }

    public void onDelStaff(String id) {
        RxRegiste(useCase.delManager(gymWrapper.id(), gymWrapper.model(), id, new Action1<QcResponse>() {
            @Override public void call(QcResponse qcResponse) {
                if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                    view.onDelSuccess();
                } else {
                    view.onFailed(qcResponse.getMsg());
                }
            }
        }));
    }

    public void queryPostions() {
        RxRegiste(useCase.queryPostions(gymWrapper.id(), gymWrapper.model(), new Action1<QcResponsePostions>() {
            @Override public void call(QcResponsePostions qcResponsePostions) {
                if (qcResponsePostions.getStatus() == ResponseConstant.SUCCESS) {
                    view.onPositions(qcResponsePostions.data.positions);
                } else {
                    view.onFailed(qcResponsePostions.getMsg());
                }
            }
        }));
    }
}
