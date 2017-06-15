package cn.qingchengfit.staffkit.views.gym.coach;

import android.content.Intent;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.inject.model.LoginStatus;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.model.responese.Staffs;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.mvpbase.Presenter;
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
 * Created by Paper on 16/5/11 2016.
 */
public class CoachListPresenter implements Presenter {

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private CoachListView view;
    private CoachUseCase useCase;
    private Subscription sp;

    @Inject public CoachListPresenter(CoachUseCase useCase) {
        this.useCase = useCase;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (CoachListView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        view = null;
        if (sp != null) sp.unsubscribe();
    }

    public void filter(String keyword) {

    }

    public void queryData(String keyword) {
        sp = useCase.getAllCoach(gymWrapper.id(), gymWrapper.model(), keyword, new Action1<QcResponseData<Staffs>>() {
            @Override public void call(QcResponseData<Staffs> qcResponseGymCoach) {
                if (qcResponseGymCoach.getStatus() == ResponseConstant.SUCCESS) {
                    view.onList(qcResponseGymCoach.data.teachers);
                } else {
                    // ToastUtils.logHttp(qcResponseGymCoach);
                    view.onFailed();
                }
            }
        });
    }
}
