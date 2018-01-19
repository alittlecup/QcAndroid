package cn.qingchengfit.saasbase.cards.presenters;

import android.text.TextUtils;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.cards.bean.BalanceDetail;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.cards.network.body.CardBalanceNotifyBody;
import cn.qingchengfit.saasbase.cards.network.response.BalanceConfigs;
import cn.qingchengfit.saasbase.cards.network.response.CardListWrap;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.utils.ListUtils;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class CardBalancePresenter extends BasePresenter<CardBalancePresenter.MVPView> {

  @Inject GymWrapper gymWrapper;
  @Inject ICardModel cardModel;
  public static final String QUERY_STORE_BALANCE = "card_balance_remind_value";
  public static final String QUERY_SECOND_BALANCE = "card_balance_remind_times";
  public static final String QUERY_DAYS_BALANCE = "card_balance_remind_days";
  private HashMap<String,Object> p = new HashMap<>();
  private static final String QUERY_BANALCE_KEYS = "card_balance_remind_days,card_balance_remind_value,card_balance_remind_times";

  @Inject public CardBalancePresenter() {
  }

  public void setFilter(int cardtplType ,String cardtpl,int cardstatus){

    initpage();
    p.clear();
    if (cardtplType != 0) {
      p.put("card_tpl_type", cardtplType);
    }
    if (!TextUtils.isEmpty(cardtpl)) {
      p.put("card_tpl_id", cardtpl);
      p.remove("card_tpl_type");
    }
    if (cardstatus == 1) {//无请假 无停卡
      p.put("is_active", "1");
      p.put("is_locked", "0");
      p.put("is_expired", "0");
    } else if (cardstatus == 2) {//请假
      p.put("is_locked", "1");
    } else if (cardstatus == 4) {//停卡
      p.put("is_active", "0");
      p.put("is_locked", "0");
      p.put("is_expired", "0");
    } else if (cardstatus == 3) {//过期
      p.put("is_expired", "1");
    }else {
      p.put("is_active", null);
      p.put("is_locked", null);
      p.put("is_expired", null);
    }

    queryAllCards();
  }
  int curPage =1, totalPage =1;

  public void queryAllCards() {
    if (curPage <= totalPage) {
      p.put("page", curPage);
      RxRegiste(cardModel.qcGetBalanceCard(ListUtils.mapRemoveNull(p))
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new NetSubscribe<QcDataResponse<CardListWrap>>() {
          @Override public void onNext(QcDataResponse<CardListWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              mvpView.onCardList(qcResponse.data.cards, qcResponse.data.current_page);
              curPage++;
              totalPage = qcResponse.data.pages;

              mvpView.onCardCount(qcResponse.data.total_count);
            } else {
              mvpView.onShowError(qcResponse.getMsg());
            }
          }
        }));
    }
    else mvpView.onCardList(null,curPage);
  }



  public void initpage() {
    curPage=1;totalPage =1;
  }

  public void queryKeyworkd(String query) {
    initpage();
    p.put("q",query);
    queryAllCards();
  }


  public void queryBalanceCondition() {
    HashMap<String, Object> params = gymWrapper.getParams();
    RxRegiste(cardModel
      .qcGetBalanceCondition(p , QUERY_BANALCE_KEYS)
      .onBackpressureBuffer()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<QcDataResponse<BalanceConfigs>>() {
        @Override public void call(QcDataResponse<BalanceConfigs> balanceDetailQcResponseData) {
          if (ResponseConstant.checkSuccess(balanceDetailQcResponseData)) {
              mvpView.onGetBalance(balanceDetailQcResponseData.data.balances);
          } else {
              mvpView.onShowError(balanceDetailQcResponseData.getMsg());
          }
        }
      }));
  }

  public void putBalanceRemindCondition(List<CardBalanceNotifyBody.ConfigsBean> configs) {
    CardBalanceNotifyBody cardBalanceNotifyBody = new CardBalanceNotifyBody();
    cardBalanceNotifyBody.setConfigs(configs);
    RxRegiste(cardModel
      .qcPostBalanceCondition(cardBalanceNotifyBody)
      .onBackpressureBuffer()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new Action1<QcResponse>() {
        @Override public void call(QcResponse qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            queryBalanceCondition();
          } else {
            mvpView.onShowError(qcResponse.getMsg());
          }
        }
      }, new Action1<Throwable>() {
        @Override public void call(Throwable throwable) {
          mvpView.onShowError(throwable.getMessage());
        }
      }));
  }


  public interface MVPView extends CView {
    void onCardList(List<Card> cards,int page);
    void onCardCount(int count);
    void onGetBalance(List<BalanceDetail> balanceDetails);
  }
}
