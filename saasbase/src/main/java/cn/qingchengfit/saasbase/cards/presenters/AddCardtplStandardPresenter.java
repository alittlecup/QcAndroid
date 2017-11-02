package cn.qingchengfit.saasbase.cards.presenters;

import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.cards.event.EventCardTplOption;
import cn.qingchengfit.saasbase.cards.network.body.OptionBody;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.utils.CmStringUtils;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AddCardtplStandardPresenter extends BasePresenter {

  @Inject GymWrapper gymWrapper;
  @Inject ICardModel cardModel;
  private MVPView view;
  private OptionBody ob = new OptionBody();
  private String optionId;
  private String tplId;

  @Inject public AddCardtplStandardPresenter() {
  }

  public String getOptionId() {
    return optionId;
  }

  public void setOptionId(String optionId) {
    this.optionId = optionId;
  }

  public String getCardtplId() {
    return tplId;
  }

  public void setTplId(String tplId) {
    this.tplId = tplId;
  }

  public void setLimit(boolean limit) {
    this.ob.limit_days = limit;
  }

  public void setLimitDay(String days) {
    try {
      this.ob.days = Integer.parseInt(days);
    } catch (Exception e) {
      this.ob.days = 0;
    }
  }

  /**
   * @param price 实收金额
   * @param charge 充值金额
   */
  public void setChargeAndReal(String price, String charge) {
    this.ob.charge = charge;
    this.ob.price = price;
  }

  public void setCanCharge(boolean charge) {
    this.ob.can_charge = charge;
  }

  public void setCanCreated(boolean b) {
    this.ob.can_create = b;
  }

  public void canOnlyStaff(boolean o) {
    this.ob.for_staff = o;
  }

  public String getDesc() {
    if (ob.description == null) {
      return "";
    } else {
      return ob.description;
    }
  }

  public void setDesc(String desc) {
    ob.description = desc;
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  /**
   * 如果是新开卡，保存在本地
   */
  public void addOption() {

    if (CmStringUtils.isEmpty(tplId)) {
      if (!CmStringUtils.checkMoney(ob.charge) || !CmStringUtils.checkMoney(ob.price)) {
        view.showAlert("请填写正确的充值和实收");
        return;
      }
      RxBus.getBus().post(new EventCardTplOption(ob, 0));
      view.onSaveOk();
    } else {
      ob.card_tpl_id = tplId;
      if (ob.checkStaff() != 0) {
        view.showAlert(ob.checkPos());
        return;
      }
      RxRegiste(cardModel.qcCreateStandard(tplId, ob)
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new NetSubscribe<QcDataResponse>() {
          @Override public void onNext(QcDataResponse qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onSaveOk();
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }));
    }
  }

  public void delOption() {
    if (CmStringUtils.isEmpty(tplId)) {
      ob.id = optionId;
      RxBus.getBus().post(new EventCardTplOption(ob, -1));
      view.onDelOk();
    } else {
      RxRegiste(cardModel.qcDelCardStandard(optionId)
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new NetSubscribe<QcDataResponse>() {
          @Override public void onNext(QcDataResponse qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onDelOk();
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }));
    }
  }

  public void saveOption() {
    if (CmStringUtils.isEmpty(tplId)) {
      ob.id = optionId;
      RxBus.getBus().post(new EventCardTplOption(ob, 0));
      view.onSaveOk();
    } else {
      ob.id = null;
    }
    RxRegiste(cardModel.qcUpdateCardStandard(optionId, ob)
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse>() {
        @Override public void onNext(QcDataResponse qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            view.onSaveOk();
          } else {
            view.onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  public interface MVPView extends CView {
    /**
     * 保存或者新增完成
     */
    void onSaveOk();

    /**
     * 删除成功
     */
    void onDelOk();
  }
}
