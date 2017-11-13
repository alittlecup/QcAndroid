package cn.qingchengfit.saasbase.cards.presenters;

import android.text.TextUtils;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.cards.network.response.CardListWrap;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.utils.ListUtils;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CardListPresenter extends BasePresenter {
  private MVPView view;
  @Inject ICardModel cardModel;

  private HashMap<String,Object> p = new HashMap<>();

  @Inject public CardListPresenter() {
  }

  public void setFilter(int cardtplType ,String cardtpl,int cardstatus){

    initpage();
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
      RxRegiste(cardModel.qcGetAllCard(ListUtils.mapRemoveNull(p))
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new NetSubscribe<QcDataResponse<CardListWrap>>() {
          @Override public void onNext(QcDataResponse<CardListWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onCardList(qcResponse.data.cards, qcResponse.data.current_page);
              curPage++;
              totalPage = qcResponse.data.pages;

              view.onCardCount(qcResponse.data.total_count);
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }));
    }
    else view.onCardList(null,curPage);
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public void initpage() {
    curPage=1;totalPage =1;
    p.clear();
  }

  public void queryKeyworkd(String query) {
    initpage();
    p.put("q",query);
    queryAllCards();
  }

  public interface MVPView extends CView {
    void onCardList(List<Card> cards,int page);
    void onCardCount(int count);
  }
}
