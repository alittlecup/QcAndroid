package cn.qingchengfit.saasbase.cards.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.cards.BindCardModel;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.network.response.CardTplWrapper;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.saasbase.repository.ICardModel;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class CardTplDetailPresenter extends BasePresenter {
  @Inject ICardModel cardModel;
  @Inject BindCardModel.SelectedData selectedData;
  SerPermisAction serPermisAction;
  CardTpl cardTpl;
  private MVPView view;

  @Inject CardTplDetailPresenter() {
  }

  public void setCardTpl(CardTpl cardTpl) {
    this.cardTpl = cardTpl;
  }

  public String getCardtplId(){
    if (cardTpl != null){
      return cardTpl.getId();
    }else return "";
  }
  public int getCardCate(){
    if (cardTpl != null)
      return cardTpl.getCardTypeInt();
    else return 0;
  }
  public boolean isCardTplEnable() {
    if (cardTpl != null) {
      return cardTpl.is_enable;
    } else {
      return false;
    }
  }

  public void queryCardtpl() {
    RxRegiste(cardModel.qcGetCardTplsDetail(selectedData.cardtplId)
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<CardTplWrapper>>() {
          @Override public void call(QcDataResponse<CardTplWrapper> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              cardTpl = qcResponse.data.card_tpl;
              view.onGetCardTypeInfo(qcResponse.data.card_tpl);
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public void queryCardtplOption(){

  }

  /**
   * 编辑卡模板，判断是否在品牌下，是否有权限
   */
  public void editCardTpl() {
    if (!cardTpl.is_enable) {
      view.onShowError(R.string.alert_edit_disable_cardtpl);
      return;
    }
    if (hasPermission(PermissionServerUtils.CARDSETTING_CAN_CHANGE)) {

    }
  }

  /**
   * 停用会员卡种类
   */
  public void disable() {
    cardModel.qcDelCardtpl(cardTpl.id)
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse>() {
          @Override public void call(QcDataResponse qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onDelSucceess();
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable());


  }

  /**
   * 开始适用会员卡种类
   */
  public void enable() {
    cardModel.qcResumeCardtpl(cardTpl.id)
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse>() {
          @Override public void call(QcDataResponse qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onResumeOk();
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable());


  }

  /**
   * 不仅判断是否有权限，还会禁止单场馆下 操作多场馆的卡
   */
  public boolean hasPermission(String p) {
    //if (!gymWrapper.inBrand() && cardTpl.getShopIds().size() > 1) {
    //  view.onShowError(R.string.alert_edit_cardtype_link_manage);
    //  return false;
    //}
    //boolean ret = serPermisAction.check(p);
    //if (!ret) view.onShowError(R.string.alert_permission_forbid);
    return true;
  }



  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public interface MVPView extends CView {

    void onGetCardTypeInfo(CardTpl card_tpl);
    void onGetStandards(List<CardTplOption> options);


    void onDelSucceess();
    void onResumeOk();

  }
}
