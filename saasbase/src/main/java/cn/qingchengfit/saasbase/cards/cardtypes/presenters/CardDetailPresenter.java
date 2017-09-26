package cn.qingchengfit.saasbase.cards.cardtypes.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.cards.cardtypes.bean.Card;
import cn.qingchengfit.saasbase.cards.cardtypes.network.response.CardWrap;
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
