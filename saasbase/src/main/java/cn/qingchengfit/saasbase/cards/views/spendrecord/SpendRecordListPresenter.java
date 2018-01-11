package cn.qingchengfit.saasbase.cards.views.spendrecord;

import android.content.Intent;
import android.text.TextUtils;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.cards.bean.QcResponseRealcardHistory;
import cn.qingchengfit.saasbase.cards.bean.StatementBean;
import cn.qingchengfit.saasbase.constant.Configs;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

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
public class SpendRecordListPresenter extends BasePresenter {

    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject ICardModel cardModel;
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
        super.unattachView();
        view = null;
        if (sp != null) sp.unsubscribe();
    }

    public void queryData(int year, int month, boolean refresh, String cardId) {
        if (refresh) page = 1;
        if (page > total) return;

        sp = cardModel.qcConsumeRecord(cardId, page,DateUtils.getStartDayOfMonth(year, month), DateUtils.getEndDayOfMonthNew(year, month)).observeOn(
            AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Action1<QcDataResponse<QcResponseRealcardHistory>>() {
            @Override public void call(
                QcDataResponse<QcResponseRealcardHistory> response) {
                if (response.getStatus() == ResponseConstant.SUCCESS) {
                    view.onAccount(response.data.stat.total_account, response.data.stat.total_cost);
                    total = response.data.pages;
                    List<StatementBean> d = new ArrayList<StatementBean>();
                    for (int i = 0; i < response.data.card_histories.size(); i++) {
                        QcResponseRealcardHistory.CardHistory history = response.data.card_histories.get(i);
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
                        account = String.valueOf(Math.abs(history.cost) == 0f ? 0 : history.cost);
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
                            if (history.card_type == Configs.CATEGORY_DATE){
                                content =  DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(history.created_at))
                                    + " "
                                    + Seller + " " + DateUtils.getYYYYMMDDfromServer(history.start) + " 至 " + DateUtils.getYYYYMMDDfromServer(history.end);
                            }else {
                                content = DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(history.created_at))
                                    + " "
                                    + Seller;
                            }
                        }
                        StatementBean sss =
                            new StatementBean(DateUtils.formatDateFromServer(history.created_at), pic, name, content, false, true,
                                false, "", "", history.card_type);
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
