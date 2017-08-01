package cn.qingchengfit.staffkit.views.statement.detail;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.QcResponseStatementDetail;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.staffkit.usecase.NetError;
import cn.qingchengfit.staffkit.usecase.StatementUsecase;
import cn.qingchengfit.utils.StatementCompare;
import java.util.Collections;
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
 * Created by Paper on 16/3/8 2016.
 */
public class StatementDetailPresenter extends BasePresenter {

    @Inject StatementUsecase usecase;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private StatementDetailView view;

    @Inject public StatementDetailPresenter(StatementUsecase usecase) {
        this.usecase = usecase;
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
        this.view = (StatementDetailView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {
        super.attachIncomingIntent(intent);
    }

    @Override public void onCreate() {
        super.onCreate();
    }

    @Override public void unattachView() {
        super.unattachView();
        this.view = null;
    }

    public void queryStatementDetail(final String start, final String end, String course_id, String course_extra, String teacher_id,
        String shop_id) {
        String brandid = gymWrapper.brand_id();
        String sid = shop_id;
        if (!gymWrapper.inBrand()) {
            brandid = null;
        }
        RxRegiste(usecase.queryStatementDetail(start, end, teacher_id, course_id, course_extra, shop_id, brandid, gymWrapper.id(),
            gymWrapper.model()).subscribe(new Action1<QcResponseStatementDetail>() {
            @Override public void call(QcResponseStatementDetail qcResponseStatementDetail) {
                if (qcResponseStatementDetail.getStatus() == ResponseConstant.SUCCESS) {
                    Collections.sort(qcResponseStatementDetail.data.schedules, new StatementCompare());
                    view.onSuccess(qcResponseStatementDetail.data.schedules);
                } else {
                    view.onFailed(qcResponseStatementDetail.getMsg());
                }
            }
        }, new NetError()));
    }
}
