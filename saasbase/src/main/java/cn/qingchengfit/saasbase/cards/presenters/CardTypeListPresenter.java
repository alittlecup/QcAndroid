package cn.qingchengfit.saasbase.cards.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.cards.BindCardModel;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.network.response.CardTplListWrap;
import cn.qingchengfit.saasbase.repository.ICardModel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class CardTypeListPresenter extends BasePresenter {

  @Inject ICardModel cardModel;
  @Inject BindCardModel.SelectedData selectedData;
  private MVPView view;
  protected List<CardTpl> cardTpls = new ArrayList<>();
  private boolean enable = true;

  @Inject public CardTypeListPresenter() {
  }

  /**
   * 选择某个卡种类
   *
   * @param cardTpl 选择某个卡种类
   */
  public void chooseOneCardTpl(CardTpl cardTpl) {
    selectedData.cardcategory = cardTpl.type;
    selectedData.cardtplId = cardTpl.id;
  }

  public void createCardCate(int cate) {
    selectedData.cardcategory = cate;
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

  public void queryCardTypeNoNeedPermission(boolean isPrivate, boolean isAdd) {
    HashMap<String, Object> params = new HashMap<>();
    params.put("show_all", 1);
    params.put("method", isAdd ? "post" : "put");//add -post ，edit= put
    params.put("key", isPrivate ? PermissionServerUtils.PRIARRANGE_CALENDAR
        : PermissionServerUtils.TEAMARRANGE_CALENDAR); //isprivate PRIARRANGE_CALENDAR， false  =TEAMARRANGE_CALENDAR
    RxRegiste(cardModel.qcGetCardTplsPermission(params)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(qcResponse -> {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            cardTpls.clear();
            cardTpls.addAll(qcResponse.data.card_tpls);
            view.onDoneCardtplList();
          } else {
            view.onShowError(qcResponse.getMsg());
          }
        }, new NetWorkThrowable()));
  }

  public interface MVPView extends CView {
    void onDoneCardtplList();
  }
}
