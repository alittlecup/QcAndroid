package cn.qingchengfit.saasbase.cards.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.network.response.CardTplListWrap;
import cn.qingchengfit.saasbase.repository.ICardModel;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class CardTypeListPresenter extends BasePresenter {
  @Inject GymWrapper gymWrapper;
  @Inject QcRestRepository qcRestRepository;
  @Inject LoginStatus loginStatus;
  @Inject ICardModel cardModel;

  private MVPView view;
  private List<CardTpl> cardTpls = new ArrayList<>();
  private boolean enable = true;

  @Inject public CardTypeListPresenter() {
  }

  /**
   * 按类型获取已经 拉去的卡模板
   */
  public List<CardTpl> getCardTplByType(int type) {
    List<CardTpl> ret = new ArrayList<>();
    for (CardTpl cardTpl : cardTpls) {
      if (type == 0 || type == cardTpl.type) {
        ret.add(cardTpl);
      }
    }
    return ret;
  }

  public boolean isEnable() {
    return enable;
  }

  public void setEnable(boolean enable) {
    this.enable = enable;
    queryCardtypeList();
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  /**
   * 获取所有卡种类
   */
  public void queryCardtypeList() {
    RxRegiste(cardModel.qcGetCardTpls(null, enable ? "1" : "0")
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<CardTplListWrap>>() {
          @Override public void call(QcDataResponse<CardTplListWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              cardTpls.clear();
              cardTpls.addAll(qcResponse.data.card_tpls);
              view.onDoneCardtplList();
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public interface MVPView extends CView {
    void onDoneCardtplList();
  }
}
