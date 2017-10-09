package cn.qingchengfit.saasbase.cards.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.cards.network.response.CardWrap;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.subscribes.NetSubscribe;
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
