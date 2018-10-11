package cn.qingchengfit.saasbase.cards.presenters;

import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.events.EventTxT;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.cards.network.response.CardWrap;
import cn.qingchengfit.saascommon.events.EventSaasFresh;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.subscribes.NetSubscribe;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CardDetailPresenter extends BasePresenter {
  private MVPView view;

  @Inject GymWrapper gymWrapper;
  @Inject ICardModel cardModel;

  private String cardId;
  private Card mCard;

  public String getCardId() {
    return cardId;
  }

  public Card getmCard() {
    return mCard;
  }

  public void setCardId(String cardId) {
    this.cardId = cardId;
  }

  @Inject public CardDetailPresenter() {

  }

  @Override public void onNewSps() {
    super.onNewSps();
    /*
     *修改卡信息
     */
    RxBusAdd(EventTxT.class)
      .onBackpressureBuffer()
      .throttleLast(500, TimeUnit.MILLISECONDS)
      .subscribe(new BusSubscribe<EventTxT>() {
        @Override public void onNext(EventTxT eventTxT) {
          editCardNo(eventTxT.txt);
        }
      });
    /*
     * 修改绑定会员
     */



  }

  public void queryCardDetail() {
    RxRegiste(cardModel.qcGetCardDetail(cardId)
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<CardWrap>>() {
        @Override public void onNext(QcDataResponse<CardWrap> qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            mCard = qcResponse.data.card;
            view.onCardDetail(qcResponse.data.card);
          } else {
            view.onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  public void editCardStudents(String studentIdStr) {
    HashMap<String, Object> params = new HashMap<>();
    params.put("user_ids", studentIdStr);
    editInfo(params);
  }

  public void editCardNo(String cardNo) {
    HashMap<String, Object> params = new HashMap<>();
    params.put("card_no", cardNo);
    editInfo(params);
  }

  public void unRegeister(){
    RxRegiste(cardModel.qcStopCard(cardId)
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new NetSubscribe<QcDataResponse>() {
          @Override public void onNext(QcDataResponse qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              queryCardDetail();
              RxBus.getBus().post(new EventSaasFresh.CardList());
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }));
  }

  public void resumeCard(){
    RxRegiste(cardModel.qcResumeCard(cardId)
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new NetSubscribe<QcDataResponse>() {
          @Override public void onNext(QcDataResponse qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              queryCardDetail();
              RxBus.getBus().post(new EventSaasFresh.CardList());
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }));
  }

  private void editInfo(HashMap<String, Object> params) {
    RxRegiste(cardModel.editCardInfo(cardId, params)
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse>() {
        @Override public void onNext(QcDataResponse qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            queryCardDetail();
            RxBus.getBus().post(new EventSaasFresh.CardList());
          } else {
            view.onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public interface MVPView extends CView {
    void onCardDetail(Card card);
  }
}
