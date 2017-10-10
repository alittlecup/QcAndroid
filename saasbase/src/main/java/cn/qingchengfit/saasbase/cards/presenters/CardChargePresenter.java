package cn.qingchengfit.saasbase.cards.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.cards.network.body.ChargeBody;
import cn.qingchengfit.saasbase.cards.network.response.CardTplOptionListWrap;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CardChargePresenter extends BasePresenter {
  private MVPView view;

  @Inject ICardModel cardModel;
  private Card mCard;
  private ChargeBody chargeBody = new ChargeBody();
  private CardTplOption mChosenOption;
  //private
  private List<CardTplOption> mOptions = new ArrayList<>();



  @Inject public CardChargePresenter() {
  }

  public Card getmCard() {
    return mCard;
  }

  public void setmCard(Card mCard) {
    this.mCard = mCard;
  }

  private void initBus() {
    //销售
    //RxBusAdd()
    //备注
    //RxBusAdd();
  }

  public void selectOption(int pos) {
    view.showInputMoney(pos >= mOptions.size());
    if (pos < mOptions.size()) {
      //选择了规格
      mChosenOption = mOptions.get(pos);
    } else {
      //选择其他
      mChosenOption = null;
    }
  }

  /**
   * 下单
   */
  public void chargeCard() {
    if (mChosenOption == null){
      chargeBody.setPrice(view.realMoney());
      chargeBody.setChargeAccount(mCard.getType(), view.chargeMoney(),view.startDay(),view.endDay());
    }else {
      chargeBody.setPrice(mChosenOption.price);
      chargeBody.setChargeAccount(mCard.getType(),mChosenOption.charge,view.startDay(), DateUtils.addDay(view.startDay(),mChosenOption.days));
    }

    /*
     * 非期限卡可以设置有效期
     */
    if (mCard.getType() != 3) {
      chargeBody.setCheck_valid(view.openValidDay());
      if (chargeBody.isCheck_valid()) {
        chargeBody.setValid_from(view.startDay());
        chargeBody.setValid_to(view.endDay());
      }
    }

    if (chargeBody.checkData() > 0) {
      view.showAlert(chargeBody.checkData());
      return;
    }
    RxRegiste(cardModel.qcChargeCard(chargeBody)
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new NetSubscribe<QcResponse>() {
          @Override public void onNext(QcResponse qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              //下单之后做什么？// TODO: 2017/9/25
              //view.
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }));
  }

  /**
   * 续卡规格
   */
  public void queryOption() {
    RxRegiste(cardModel.qcGetOptions(mCard.getCard_tpl_id())
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new NetSubscribe<QcDataResponse<CardTplOptionListWrap>>() {
          @Override public void onNext(QcDataResponse<CardTplOptionListWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              mOptions.clear();
              mOptions.addAll(qcResponse.data.options);
              view.onGetOptions(mOptions);
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }));
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
    initBus();
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public interface MVPView extends CView {
    void onGetOptions(List<CardTplOption> options);

    void showInputMoney(boolean show);


    String chargeMoney();
    String realMoney();
    boolean openValidDay();
    String startDay();
    String endDay();

  }
}
