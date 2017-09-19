package cn.qingchengfit.staffkit.views.student.detail;

import android.content.Intent;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.Presenter;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.model.StudentWrapper;
import cn.qingchengfit.model.common.Card;
import cn.qingchengfit.model.responese.QcResponseStudentCards;
import cn.qingchengfit.staffkit.usecase.StudentUsecase;
import java.util.ArrayList;
import java.util.List;
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
 * Created by Paper on 16/3/19 2016.
 */
public class StudentsCardsPresenter implements Presenter {

    @Inject StudentUsecase usecase;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject StudentWrapper studentWrapper;
    StudentsCardsView view;

    @Inject public StudentsCardsPresenter() {
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (StudentsCardsView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        view = null;
    }

    public void querData() {
        usecase.queryCards(studentWrapper.id(), gymWrapper.id(), gymWrapper.model(), gymWrapper.brand_id(),
            new Action1<QcResponseStudentCards>() {
                @Override public void call(QcResponseStudentCards qcResponseStudentCards) {
                    List<Card> mData = new ArrayList<>();
                    if (qcResponseStudentCards.data != null && qcResponseStudentCards.data.cards != null) {

                        view.onData(mData.size(), qcResponseStudentCards.data.cards);
                    } else {
                        view.onData(0, mData);
                    }
                }
            });
    }
}
