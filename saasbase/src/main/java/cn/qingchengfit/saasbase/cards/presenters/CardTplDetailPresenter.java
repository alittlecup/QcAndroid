package cn.qingchengfit.saasbase.cards.presenters;

import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.events.EventTxT;
import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.network.body.CardtplBody;
import cn.qingchengfit.saasbase.cards.network.response.CardTplOptionListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplWrapper;
import cn.qingchengfit.saasbase.events.EventSaasFresh;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.saasbase.repository.IPermissionModel;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.subscribes.NetSubscribe;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class CardTplDetailPresenter extends BasePresenter {
  @Inject ICardModel cardModel;
  @Inject IPermissionModel permissionModel;
  CardTpl cardTpl; //卡种类详情
  private CardtplBody body = new CardtplBody();
  private int cardCate; //新增卡种类是记录卡类型

  private MVPView view;

  @Inject CardTplDetailPresenter() {
  }

  public void setCardTpl(CardTpl cardTpl) {
    this.cardTpl = cardTpl;
  }

  public String getCardtplId() {
    if (cardTpl != null) {
      return cardTpl.getId();
    } else {
      return "";
    }
  }

  public int getCardCate() {
    if (cardTpl != null) {
      return cardTpl.getCardTypeInt();
    } else {
      return cardCate;
    }
  }

  public String getCardName() {
    if (cardTpl != null) {
      return cardTpl.getName();
    } else {
      return "";
    }
  }

  public boolean isCardTplEnable() {
    if (cardTpl != null) {
      return cardTpl.is_enable;
    } else {
      return false;
    }
  }

  public void setCardCate(int cardCate) {
    this.cardCate = cardCate;
  }

  public void queryCardtpl() {
    RxRegiste(cardModel.qcGetCardTplsDetail(cardTpl.getId())
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

  public void queryCardtplOption() {
    RxRegiste(cardModel.qcGetOptions(cardTpl.id)
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<CardTplOptionListWrap>>() {
        @Override public void onNext(QcDataResponse<CardTplOptionListWrap> qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            view.onGetStandards(qcResponse.data.options);
          } else {
            view.onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  /**
   * 编辑卡模板，判断是否在品牌下，是否有权限
   */
  public void editCardTpl() {
    if (!cardTpl.is_enable) {
      view.onShowError(R.string.alert_edit_disable_cardtpl);
      return;
    }
    if (permissionModel.check(PermissionServerUtils.CARDSETTING_CAN_CHANGE)) {
      RxRegiste(cardModel.qcUpdateCardtpl(cardTpl.getId(), body)
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new NetSubscribe<QcDataResponse>() {
          @Override public void onNext(QcDataResponse qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              RxBus.getBus().post(new EventSaasFresh.CardTplList());
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }));
    }
  }

  /**
   * 新建会员卡种类
   */
  public void createCardTpl() {
    if (permissionModel.check(PermissionServerUtils.CARDSETTING_CAN_WRITE)) {
      CardtplBody body = new CardtplBody.Builder().type(cardCate)
        .name(view.getCardName())
        .options(view.getCardTplOptions())
        .build();
      cardModel.qcCreateCardtpl(body)
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new NetSubscribe<QcDataResponse>() {
          @Override public void onNext(QcDataResponse qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              RxBus.getBus().post(new EventSaasFresh.CardTplList());
              view.onShowError("创建成功");
              view.popBack();
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        });
    } else {
      view.showAlert(R.string.sorry_for_no_permission);
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

  @Override public void attachView(final PView v) {
    view = (MVPView) v;
    RxBusAdd(EventTxT.class).observeOn(AndroidSchedulers.mainThread())
      .subscribe(new BusSubscribe<EventTxT>() {
        @Override public void onNext(EventTxT eventTxT) {
          body.name = eventTxT.txt;
          editCardTpl();
          cardTpl.setName(eventTxT.txt);
          view.onGetCardTypeInfo(cardTpl);
        }
      });
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

    /**
     * 新增会员卡种类时获取 会员卡种的名字和规格
     */
    String getCardName();

    List<CardTplOption> getCardTplOptions();
  }
}
