package cn.qingchengfit.saasbase.cards.cardtypes.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.SaasRouter;
import cn.qingchengfit.saasbase.cards.cardtypes.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.cardtypes.bean.CardTplStandard;
import cn.qingchengfit.saasbase.cards.cardtypes.network.response.CardTplWrapper;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.saasbase.repository.SaasModel;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class CardTplDetailPresenter extends BasePresenter {
  @Inject GymWrapper gymWrapper;
  @Inject SaasModel saasModel;
  @Inject SerPermisAction serPermisAction;
  @Inject SaasRouter saasRouter;
  CardTpl cardTpl;
  private MVPView view;

  @Inject public CardTplDetailPresenter() {
  }

  public void setCardTpl(CardTpl cardTpl) {
    this.cardTpl = cardTpl;
  }

  public String getCardtplId(){
    if (cardTpl != null){
      return cardTpl.getId();
    }else return "";
  }

  public boolean isCardTplEnable() {
    if (cardTpl != null) {
      return cardTpl.is_enable;
    } else {
      return false;
    }
  }

  public void queryCardtpl() {
    RxRegiste(saasModel.qcGetCardTplsDetail(cardTpl.getId())
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<CardTplWrapper>>() {
          @Override public void call(QcDataResponse<CardTplWrapper> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
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
      saasRouter.routerTo("/cardtpl/edit/?id=" + cardTpl.getId());
    }
  }

  /**
   * 停用会员卡种类
   */
  public void disable() {
    saasModel.qcDelCardtpl(cardTpl.id)
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
    saasModel.qcResumeCardtpl(cardTpl.id)
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
    if (!gymWrapper.inBrand() && cardTpl.getShopIds().size() > 1) {
      view.onShowError(R.string.alert_edit_cardtype_link_manage);
      return false;
    }
    boolean ret = serPermisAction.check(p);
    if (!ret) view.onShowError(R.string.alert_permission_forbid);
    return ret;
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

    void onGetStandards(List<CardTplStandard> cardStandards);

    void onDelSucceess();

    void onResumeOk();

  }
}
