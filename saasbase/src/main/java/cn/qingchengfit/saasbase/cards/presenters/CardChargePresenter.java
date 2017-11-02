package cn.qingchengfit.saasbase.cards.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.events.EventTxT;
import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.cards.network.body.ChargeBody;
import cn.qingchengfit.saasbase.cards.network.response.CardTplOptionListWrap;
import cn.qingchengfit.saasbase.cards.network.response.PayBusinessResponse;
import cn.qingchengfit.saasbase.cards.network.response.PayBusinessResponseWrap;
import cn.qingchengfit.saasbase.events.EventSelectedStudent;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.subscribes.NetSubscribe;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CardChargePresenter extends BasePresenter {
  private MVPView view;

  @Inject ICardModel cardModel;
  private Card mCard;
  public ChargeBody chargeBody = new ChargeBody();
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
    RxBusAdd(Staff.class).onBackpressureLatest()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new BusSubscribe<Staff>() {
        @Override public void onNext(Staff staff) {
          view.bindSaler(staff.getUsername());
          chargeBody.setSeller_id(staff.id);
        }
      });

    //备注信息
    RxBusAdd(EventTxT.class).onBackpressureLatest()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new BusSubscribe<EventTxT>() {
        @Override public void onNext(EventTxT eventTxT) {
          view.remark(!eventTxT.txt.isEmpty());
          chargeBody.setRemarks(eventTxT.txt);
        }
      });

    //学员 选择某个学员
    RxBusAdd(EventSelectedStudent.class).onBackpressureLatest()
      .subscribe(new BusSubscribe<EventSelectedStudent>() {
        @Override public void onNext(EventSelectedStudent eventSelectedStudent) {
          view.bindStudent(eventSelectedStudent.getNameStr());
          chargeBody.setSeller_id(eventSelectedStudent.getIdStr());
        }
      });
  }

  public void selectOption(int pos) {

    if (pos < mOptions.size()) {
      //选择了规格
      mChosenOption = mOptions.get(pos);
      view.setPayMoney(mChosenOption.price);
      view.showInputMoney(false, mCard.getType(), mChosenOption.limit_days);
    } else {
      //选择其他
      mChosenOption = null;
      view.showInputMoney(true, mCard.getType(), false);
    }
  }

  /**
   * 下单
   */
  public void chargeCard() {
    chargeBody.setCharge_type(4);
    chargeBody.setCard_id(mCard.getId());
    chargeBody.setType(mCard.getType());
    //chargeBody.setUser_ids(CmStringUtils.List2Str(mCard.getUserIds()));
    if (mChosenOption == null) {
      chargeBody.setPrice(view.realMoney());
      chargeBody.setBuyAccount(view.chargeMoney(), view.startDay(), view.endDay(), null);
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
    } else {
      chargeBody.setPrice(mChosenOption.price);
      chargeBody.setBuyAccount(mChosenOption.charge, view.startDay(), view.endDay(), mChosenOption);
    }

    if (chargeBody.checkData() > 0) {
      view.showAlert(chargeBody.checkData());
      return;
    }
    RxRegiste(cardModel.qcChargeCard(chargeBody)
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<PayBusinessResponseWrap>>() {
        @Override public void onNext(QcDataResponse<PayBusinessResponseWrap> qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            view.onBusinessOrder(qcResponse.data.order);
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
            for (CardTplOption option : qcResponse.data.options) {
              if (option.can_charge)
                mOptions.add(option);
            }
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

  public void setSellerId(String sellerId) {
    this.chargeBody.setSeller_id(sellerId);
  }

  public interface MVPView extends CView {
    void onGetOptions(List<CardTplOption> options);

    void showInputMoney(boolean other, int cardCate, boolean hasValid);

    void bindStudent(String student);

    void bindSaler(String saler);

    void remark(boolean remark);

    void setPayMoney(String x);

    /**
     * 下单完成后返回的数据
     */
    void onBusinessOrder(PayBusinessResponse payBusinessResponse);

    String chargeMoney();

    String realMoney();

    boolean openValidDay();

    String startDay();

    String endDay();
  }
}
