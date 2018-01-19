package cn.qingchengfit.staffkit.views.gym.coach;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.usecase.CoachUseCase;
import javax.inject.Inject;
import rx.Subscription;
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
 * Created by Paper on 16/4/20 2016.
 */
public class EditCoachInfoPresenter extends BasePresenter {
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private CoachUseCase usecase;
    private CoachDetailView view;
    private Subscription edSp;
    private Subscription delSp;

    @Inject public EditCoachInfoPresenter(CoachUseCase usecase) {
        this.usecase = usecase;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (CoachDetailView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
        if (edSp != null) edSp.unsubscribe();
        if (delSp != null) delSp.unsubscribe();
    }

    public void editCoach(Staff coach) {
        Staff c = new Staff(coach.username, coach.phone, coach.avatar, coach.gender);
        c.area_code = coach.area_code;

        edSp = usecase.fixCoach(gymWrapper.id(), gymWrapper.model(), coach.id, c, new Action1<QcResponse>() {
            @Override public void call(QcResponse qcResponse) {
                if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                    view.onFixSuccess();
                } else {
                    // ToastUtils.logHttp(qcResponse);
                    view.onFailed();
                }
            }
        });
    }

    public void delCoach(String id) {
        delSp = usecase.delCoach(gymWrapper.id(), gymWrapper.model(), id, new Action1<QcResponse>() {
            @Override public void call(QcResponse qcResponse) {
                if (qcResponse.getStatus() == ResponseConstant.SUCCESS) {
                    view.onDelSuccess();
                } else {
                    view.onFailed();
                }
            }
        });
    }
}
