package cn.qingchengfit.staffkit.views.card.spendrecord;

import android.content.Intent;
import android.text.TextUtils;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.Presenter;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.inject.model.RealcardWrapper;
import cn.qingchengfit.model.responese.QcResponseRealcardHistory;
import cn.qingchengfit.model.responese.StatementBean;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.staffkit.usecase.RealCardUsecase;
import cn.qingchengfit.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;
import rx.functions.Action1;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/4/29 2016.
 */
public class SpendRecordListPresenter implements Presenter {

    @Inject RealCardUsecase usecase;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject RealcardWrapper realCard;
    private int page = 1, total = 2;
    private SpendRecordListView view;
    private Subscription sp;

    @Inject public SpendRecordListPresenter() {
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (SpendRecordListView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        view = null;
        if (sp != null) sp.unsubscribe();
    }

    public void queryData(int year, int month, boolean refresh) {
        if (refresh) page = 1;
        if (page > total) return;

        sp = usecase.getRealCardHistory(realCard.id(), gymWrapper.brand_id(), gymWrapper.id(), gymWrapper.model(),
            DateUtils.getStartDayOfMonth(year, month), DateUtils.getEndDayOfMonthNew(year, month), page,
            new Action1<QcResponseRealcardHistory>() {
                @Override public void call(QcResponseRealcardHistory qcResponseRealcardHistory) {
                    if (qcResponseRealcardHistory.getStatus() == ResponseConstant.SUCCESS) {
                        view.onAccount(qcResponseRealcardHistory.data.stat.total_account, qcResponseRealcardHistory.data.stat.total_cost);
                        total = qcResponseRealcardHistory.data.pages;
                        List<StatementBean> d = new ArrayList<StatementBean>();
                        for (int i = 0; i < qcResponseRealcardHistory.data.card_histories.size(); i++) {
                            QcResponseRealcardHistory.CardHistory history = qcResponseRealcardHistory.data.card_histories.get(i);
                            String pic = "", name = "", content = "", account = "";

                            //                                首次充值：3
                            //                                充值：1
                            //                                消费：2
                            //                                消费(退款):4
                            //                                签到：7
                            //                                取消签到：8
                            //                                请假：9
                            //                                取消请假：10
                            //                                销卡：11
                            //                                取消销卡：12
                            //                                扣费：14
                            account = history.cost;
                            if (history.type_int == 2) {
                                pic = history.order.course.photo;
                                name = history.order.course.name;

                                content = DateUtils.Date2YYYYMMDDHHmm(DateUtils.formatDateFromServer(history.order.start))
                                    + " "
                                    + history.order.username
                                    + " "
                                    + history.order.count
                                    + "人";
                            } else {//非消费
                                pic = history.photo;
                                name = history.type;
                                String Seller = "";
                                if (!TextUtils.isEmpty(history.created_by_name)) Seller = history.created_by_name;

                                content = DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(history.created_at)) + " " + Seller;
                            }
                            StatementBean sss =
                                new StatementBean(DateUtils.formatDateFromServer(history.created_at), pic, name, content, false, true,
                                    false, "", "");
                            sss.account = account;
                            d.add(sss);
                        }

                        view.onRecordList(d, page);
                        page++;
                    } else {

                    }
                }
            });
    }
}
