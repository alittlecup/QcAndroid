package cn.qingchengfit.staffkit.views.card.cardlist;

import android.content.Intent;
import android.text.TextUtils;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.body.CardBalanceNotifyBody;
import cn.qingchengfit.model.responese.BalanceConfigs;
import cn.qingchengfit.model.responese.BalanceCount;
import cn.qingchengfit.model.responese.BalanceDetail;
import cn.qingchengfit.model.responese.Cards;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.staffkit.model.dbaction.GymBaseInfoAction;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.usecase.RealCardUsecase;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
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
 * 88      88   88 88      88.     88 `88.l
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/3/17 2016.
 */
public class RealCardListPresenter extends BasePresenter {
    public static final String QUERY_STORE_BALANCE = "card_balance_remind_value";
    public static final String QUERY_SECOND_BALANCE = "card_balance_remind_times";
    public static final String QUERY_DAYS_BALANCE = "card_balance_remind_days";
    private static final String QUERY_BANALCE_KEYS = "card_balance_remind_days,card_balance_remind_value,card_balance_remind_times";
    @Inject RealCardUsecase usecase;
    @Inject RestRepository restRepository;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    private RealCardListView view;
    private int page = 1, totalpage = 2;
    private int balance_page = 1, balance_totalpage = 2;
    private OnSettingBalanceListener onSettingBalanceListener;

    @Inject public RealCardListPresenter(RealCardUsecase usecase) {
        this.usecase = usecase;
    }

    @Inject @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        view = (RealCardListView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    public void setOnSettingBalanceListener(OnSettingBalanceListener onSettingBalanceListener) {
        this.onSettingBalanceListener = onSettingBalanceListener;
    }

    @Override public void unattachView() {
        super.unattachView();
        this.view = null;
    }

    public void initPage() {
        page = 1;
        totalpage = 2;
    }

    public void initBalancePage() {
        balance_page = 1;
        balance_totalpage = 2;
    }

    public void filterByCardtype(String keyword, String cardtpl, int cardtplType, final int cardstatus, String shopid) {

        if (page > totalpage) {
            view.onFailed("没有更多数据");
            return;
        }
        HashMap<String, Object> p = new HashMap<>();

        if (cardtplType != 0) {
            p.put("card_tpl_type", cardtplType);
        }
        if (!TextUtils.isEmpty(cardtpl)) {
            p.put("card_tpl_id", cardtpl);
        }
        if (cardstatus == 1) {//无请假 无停卡
            p.put("is_active", "1");
            p.put("is_locked", "0");
            p.put("is_expired", "0");
        } else if (cardstatus == 2) {//请假
            p.put("is_locked", "1");
        } else if (cardstatus == 3) {//停卡
            p.put("is_active", "0");
            p.put("is_locked", "0");
            p.put("is_expired", "0");
        } else if (cardstatus == 4) {
            p.put("is_expired", "1");
        }
        String id = gymWrapper.id();
        String model = gymWrapper.model();
        String brandid = gymWrapper.brand_id();
        if (!TextUtils.isEmpty(shopid)) {
            CoachService gym = GymBaseInfoAction.getGymByShopIdNow(gymWrapper.brand_id(), shopid);
            id = gym.getId();
            model = gym.getModel();
            brandid = null;
        }

        usecase.getAllCards(null, id, model, page, keyword, p, new Action1<QcDataResponse<Cards>>() {
            @Override public void call(QcDataResponse<Cards> qcResponseAllRealCards) {
                if (ResponseConstant.checkSuccess(qcResponseAllRealCards)) {
                    totalpage = qcResponseAllRealCards.data.pages;
                    view.onSuccees(qcResponseAllRealCards.data.total_count, qcResponseAllRealCards.data.current_page,
                        qcResponseAllRealCards.data.cards);

                    page++;
                }
            }
        });
    }

    public void filterBalanceCard(String keyword, String cardtpl, int cardtplType, int cardstatus, String shopid) {

        if (balance_page > balance_totalpage) {
            view.onFailed("没有更多数据");
            return;
        }
        HashMap<String, Object> p = new HashMap<>();

        if (cardtplType != 0) {
            p.put("card_tpl_type", cardtplType);
        }
        if (!TextUtils.isEmpty(cardtpl)) {
            p.put("card_tpl_id", cardtpl);
        }
        if (cardstatus == 1) {//无请假 无停卡
            p.put("is_active", "1");
            p.put("is_locked", "0");
            p.put("is_expired", "0");
        } else if (cardstatus == 2) {//请假
            p.put("is_locked", "1");
            p.put("is_active", "0");
            p.put("is_expired", "0");
        } else if (cardstatus == 3) {//停卡
            p.put("is_active", "0");
            p.put("is_locked", "0");
            p.put("is_expired", "0");
        } else if (cardstatus == 4) {
            p.put("is_expired", "1");
        }
        String id = gymWrapper.id();
        String model = gymWrapper.model();
        String brandid = gymWrapper.brand_id();
        if (!TextUtils.isEmpty(shopid)) {
            CoachService gym = GymBaseInfoAction.getGymByShopIdNow(gymWrapper.brand_id(), shopid);
            id = gym.getId();
            model = gym.getModel();
            brandid = null;
        }
        RxRegiste(usecase.getBalanceCard(null, id, model, balance_page, keyword, p, new Action1<QcDataResponse<Cards>>() {
            @Override public void call(QcDataResponse<Cards> qcResponseAllRealCards) {
                if (ResponseConstant.checkSuccess(qcResponseAllRealCards)) {
                    balance_totalpage = qcResponseAllRealCards.data.pages;
                    view.onSuccees(qcResponseAllRealCards.data.total_count, qcResponseAllRealCards.data.current_page,
                        qcResponseAllRealCards.data.cards);
                    balance_page++;
                }
            }
        }));
    }

    public void putBalanceRemindCondition(String staffid, List<CardBalanceNotifyBody.ConfigsBean> configs) {
        CardBalanceNotifyBody cardBalanceNotifyBody = new CardBalanceNotifyBody();
        cardBalanceNotifyBody.setConfigs(configs);
        RxRegiste(restRepository.getPost_api()
            .qcPostBalanceCondition(staffid, gymWrapper.getParams(), cardBalanceNotifyBody)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (ResponseConstant.checkSuccess(qcResponse)) {
                        onSettingBalanceListener.onSettingSuccess();
                    } else {
                        onSettingBalanceListener.onSettingFailed();
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onFailed(throwable.getMessage());
                }
            }));
    }

    public void queryBalanceCondition(String staffId) {

        HashMap<String, Object> params = gymWrapper.getParams();
        RxRegiste(restRepository.getGet_api()
            .qcGetBalanceCondition(staffId, params, QUERY_BANALCE_KEYS)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<BalanceConfigs>>() {
                @Override public void call(QcDataResponse<BalanceConfigs> balanceDetailQcResponseData) {
                    if (ResponseConstant.checkSuccess(balanceDetailQcResponseData)) {
                        if (onSettingBalanceListener != null) {
                            onSettingBalanceListener.onGetBalance(balanceDetailQcResponseData.data.balances);
                        }
                    } else {
                        onSettingBalanceListener.onSettingFailed();
                    }
                }
            }));
    }

    public void queryData() {
    }

    public void queryBalanceCount() {

        RxRegiste(restRepository.getGet_api()
            .qcGetCardCount(loginStatus.staff_id(), gymWrapper.getParams())
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<cn.qingchengfit.network.response.QcDataResponse<BalanceCount>>() {
                @Override public void call(
                    cn.qingchengfit.network.response.QcDataResponse<BalanceCount> balanceCountQcResponseData) {
                    if (onSettingBalanceListener != null) {
                        onSettingBalanceListener.onGetCardCount(balanceCountQcResponseData.data.count);
                    }
                }
            }));
    }

    public interface OnSettingBalanceListener {
        void onSettingSuccess();

        void onSettingFailed();

        void onGetBalance(List<BalanceDetail> balanceDetailList);

        void onGetCardCount(int count);
    }
}
