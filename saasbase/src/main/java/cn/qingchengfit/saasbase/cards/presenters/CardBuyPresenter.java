package cn.qingchengfit.saasbase.cards.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventTxT;
import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.network.body.CardBuyBody;
import cn.qingchengfit.saasbase.cards.network.body.ChargeBody;
import cn.qingchengfit.saasbase.cards.network.response.CardTplOptionListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplWrapper;
import cn.qingchengfit.saasbase.events.EventSelectedStudent;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.subscribes.NetSubscribe;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CardBuyPresenter extends BasePresenter {

  private MVPView view;
  @Inject GymWrapper gymWrapper;
  @Inject ICardModel cardModel;
  @Inject SerPermisAction serPermisAction;
  @Inject LoginStatus loginStatus;
  private Card mCard;
  private String mCardTplId;
  private int cardCate;
  private List<CardTplOption> mOptions = new ArrayList<>();
  private ArrayList<String> choseStuIds = new ArrayList<>();
  /**
   * 选中的支付价格
   */
  private CardTplOption mChosenOption;
  /**
   * 购卡数据
   */
  private CardBuyBody cardBuyBody = new CardBuyBody();

  public String getRemarks() {
    return cardBuyBody.remarks;
  }

  public String getmCardTplId() {
    return mCardTplId;
  }

  public void setmCard(Card mCard) {
    this.mCard = mCard;
  }

  public Card getmCard() {
    return mCard;
  }

  public void setCardCate(int cate) {
    cardBuyBody.setType(cate);
    cardCate = cate;
  }

  public ArrayList<String>  getChoseStuIds() {
    return choseStuIds;
  }

  public void setmCardTplId(String mCardTplId) {
    this.mCardTplId = mCardTplId;
  }

  @Inject public CardBuyPresenter() {

  }

  public boolean hasEditPermission() {
    return serPermisAction.check(PermissionServerUtils.CARDSETTING_CAN_CHANGE);
  }

  /**
   * 选择规格时的逻辑
   */
  public void selectOption(int pos) {
    if (pos < mOptions.size()) {
      //已有规格 展示价格
      mChosenOption = mOptions.get(pos);
      view.showInputMoney(false, mChosenOption, mChosenOption.limit_days);
      view.setPayMoney(mChosenOption.price);
    } else {
      //其他规格
      mChosenOption = null;
      view.showInputMoney(true, mChosenOption, false);
    }
  }

  public void setSaler(String id){
    cardBuyBody.setSeller_id(id);
  }

  /**
   * 初始化 监听选择
   */
  public void initBus() {
    RxBusAdd(Staff.class).onBackpressureLatest()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new BusSubscribe<Staff>() {
        @Override public void onNext(Staff staff) {
          view.bindSaler(staff.getUsername());
            cardBuyBody.setSeller_id(staff.id);
        }
      });

    //备注信息
    RxBusAdd(EventTxT.class).onBackpressureLatest()
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new BusSubscribe<EventTxT>() {
        @Override public void onNext(EventTxT eventTxT) {
          view.remark(!eventTxT.txt.isEmpty());
          cardBuyBody.remarks = eventTxT.txt;
        }
      });

    //学员 选择某个学员
    RxBusAdd(EventSelectedStudent.class).onBackpressureLatest()
      .subscribe(new BusSubscribe<EventSelectedStudent>() {
        @Override public void onNext(EventSelectedStudent eventSelectedStudent) {
          view.bindStudent(eventSelectedStudent.getNameStr());
          cardBuyBody.user_ids = eventSelectedStudent.getIdStr();
          choseStuIds = eventSelectedStudent.getIDlist();
        }
      });
  }

  /**
   * 获取卡种类详情
   */
  public void getCardTplDetail() {
    RxRegiste(cardModel.qcGetCardTplsDetail(mCardTplId)
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<CardTplWrapper>>() {
        @Override public void onNext(QcDataResponse<CardTplWrapper> qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            view.onGetCardTpl(qcResponse.data.card_tpl);
          } else {
            view.onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  /**
   * 获取可选的购卡规格
   */
  public void queryOption() {
    RxRegiste(cardModel.qcGetOptions(mCardTplId)
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<CardTplOptionListWrap>>() {
        @Override public void onNext(QcDataResponse<CardTplOptionListWrap> qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            mOptions.clear();
            for (CardTplOption option : qcResponse.data.options) {
              if (option.can_create)
                mOptions.add(option);
            }
            view.onGetOptions(mOptions);
          } else {
            view.onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  //续卡操作
  public void chargeCard() {
    cardBuyBody.setCharge_type(view.payMethod());
    //cardBuyBody.setCard_id(mCard.getId());
    cardBuyBody.setType(mCard.getType());
    cardBuyBody.setCard_tpl_id(mCardTplId);
    //chargeBody.setUser_ids(CmStringUtils.List2Str(mCard.getUserIds()));
    if (mChosenOption == null) {
      cardBuyBody.setPrice(view.realMoney());
      cardBuyBody.setBuyAccount(view.chargeMoney(), view.startDay(), view.endDay(), null);
          /*
     * 非期限卡可以设置有效期
     */
      if (mCard.getType() != 3) {
        cardBuyBody.setCheck_valid(view.openValidDay());
        if (cardBuyBody.isCheck_valid()) {
          cardBuyBody.setValid_from(view.startDay());
          cardBuyBody.setValid_to(view.endDay());
        }
      }
    } else {
      cardBuyBody.setPrice(mChosenOption.price);
      cardBuyBody.setBuyAccount(mChosenOption.charge, view.startDay(), view.endDay(), mChosenOption);
    }

    //if (cardBuyBody.checkData() > 0) {
    //  view.showAlert(cardBuyBody.checkData());
    //  return;
    //}
    cardBuyBody.is_auto_start = view.autoOpen();
    cardBuyBody.origin = 2;
    if (cardBuyBody.getSeller_id() != null && cardBuyBody.getSeller_id()
        .equals(loginStatus.staff_id())) {
      cardBuyBody.setSeller_id(null);
      cardBuyBody.staff_id = loginStatus.staff_id();
    }
    RxRegiste(cardModel.qcChargeCard(mCard.getId(), cardBuyBody)
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new NetSubscribe<QcDataResponse<JsonObject>>() {
          @Override public void onNext(QcDataResponse<JsonObject> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onBusinessOrder(qcResponse.data);
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }));
  }

  //扣费操作
  public void proactiveDeduction(String cardId, ChargeBody body) {
    RxRegiste(cardModel.qcChargeRefund(String.valueOf(cardCate), body)
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new NetSubscribe<QcDataResponse<JsonObject>>() {
          @Override public void onNext(QcDataResponse<JsonObject> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onBusinessOrder(qcResponse.data);
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }));
  }

  /**
   * 真 购卡操作，先检查数据，交给后端处理
   */
  public void buyCard() {
    cardBuyBody.setCard_tpl_id(mCardTplId);
    if (mChosenOption == null) {
      cardBuyBody.setPrice(view.realMoney());
      cardBuyBody.setBuyAccount(view.chargeMoney(), view.startDay(), view.endDay(), null);
      /*
     * 非期限卡可以设置有效期
     */
      if (cardBuyBody.getType() != 3) {
        cardBuyBody.setCheck_valid(view.openValidDay());
        if (cardBuyBody.isCheck_valid()) {
          cardBuyBody.setValid_from(view.startDay());
          cardBuyBody.setValid_to(view.endDay());
        }
      }
    } else {
      cardBuyBody.setPrice(mChosenOption.price);
      cardBuyBody.setBuyAccount(mChosenOption.charge, view.startDay(), view.endDay(), mChosenOption);
    }

    if (cardBuyBody.checkData() > 0) {
      view.showAlert(cardBuyBody.checkData());
      return;
    }
    cardBuyBody.setCharge_type(view.payMethod());
    cardBuyBody.is_auto_start = view.autoOpen();
    buyCardRequest();
  }
  public void buyCardRequest(){
    RxRegiste(cardModel.buyCard(cardBuyBody)
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new NetSubscribe<QcDataResponse<JsonObject>>() {
          @Override public void onNext(
              QcDataResponse<JsonObject> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onBusinessOrder(qcResponse.data);
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

    void onGetCardTpl(CardTpl cardTpl);

    /**
     * @param other 是否选中其他
     * @param cardTplOption 是否是期限卡
     */
    void showInputMoney(boolean other, CardTplOption cardTplOption, boolean hasValid);

    void bindStudent(String student);

    void bindSaler(String saler);

    void remark(boolean remark);

    void setPayMoney(String x);

    /**
     * 下单完成后返回的数据
     */
    void onBusinessOrder(JsonObject response);

    /**
     * @return 实体卡号
     */
    String realCardNum();

    String realMoney();

    String chargeMoney();

    int payMethod();

    /**
     * 是否设置有效期
     */
    boolean openValidDay();

    String startDay(); //开始日期

    String endDay();   //结束日期

    boolean autoOpen();//自动开卡

  }
}
