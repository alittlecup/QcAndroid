package cn.qingchengfit.staffkit.views.gym.couse;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.responese.CourseTypeSample;
import cn.qingchengfit.staffkit.usecase.GymUseCase;
import rx.Subscription;

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

@Deprecated public class CourseDetailPresenter extends BasePresenter {

    private CourseDetailView view;
    private Subscription spDel, spFix;
    private GymUseCase useCase;
    private CoachService coachService;

    public CourseDetailPresenter(GymUseCase useCase, CoachService coachService) {
        this.useCase = useCase;
        this.coachService = coachService;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (CourseDetailView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
        if (spDel != null) spDel.unsubscribe();
        if (spFix != null) spFix.unsubscribe();
    }

    public void fixCourse(CourseTypeSample course) {

    }

    //    public void delCourse
}
