package cn.qingchengfit.saasbase.cards.presenters;

import android.text.TextUtils;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.events.EventTxT;
import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.bean.Coupon;
import cn.qingchengfit.saasbase.cards.network.body.CardBuyBody;
import cn.qingchengfit.saasbase.cards.network.body.ChargeBody;
import cn.qingchengfit.saasbase.cards.network.response.CardTplOptionListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplWrapper;
import cn.qingchengfit.saasbase.cards.views.CardBuyFragment;
import cn.qingchengfit.saascommon.events.EventSelectedStudent;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.saasbase.staff.model.IStaffModel;
import cn.qingchengfit.saasbase.staff.network.response.SalerListWrap;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.subscribes.NetSubscribe;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CardBuyPresenter extends BasePresenter {

  private MVPView view;
  @Inject GymWrapper gymWrapper;
  @Inject IStaffModel staffModel;
  @Inject ICardModel cardModel;
  @Inject SerPermisAction serPermisAction;
  @Inject LoginStatus loginStatus;
  private Card mCard;
  private String mCardTplId;
  private int cardCate;
  private List<CardTplOption> mOptions = new ArrayList<>();
  private ArrayList<String> choseStuIds = new ArrayList<>();

  public CardTplOption getmChosenOption() {
    return mChosenOption;
  }

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
    cardBuyBody.card_no = mCard.getCard_no();
    cardBuyBody.remarks = mCard.getRemarks();
  }

  public Card getmCard() {
    return mCard;
  }

  public String getRealCardNo() {
    return cardBuyBody.card_no;
  }

  public void setCardCate(int cate) {
    cardBuyBody.setType(cate);
    cardCate = cate;
  }

  public void setSignaturePath(String path) {
    cardBuyBody.signature = path;
  }

  public String getSignaturePath() {
    return cardBuyBody.signature;
  }

  public void setOtherOption(boolean isOther) {
    cardBuyBody.setCustomize_option(isOther);
  }

  public void setChoseStuIds(ArrayList<String> studentList) {
    if (studentList.size() > 0) {
      choseStuIds.clear();
    }
    this.choseStuIds.addAll(studentList);
  }

  public void setUserIds(String userIds) {
    cardBuyBody.user_ids = userIds;
  }

  public ArrayList<String> getChoseStuIds() {
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

  public void setmChosenOption(CardTplOption mChosenOption) {
    this.mChosenOption = mChosenOption;
  }

  /**
   * 选择规格时的逻辑
   */
  //public void selectOption(int pos) {
  //  if (pos < mOptions.size()) {
  //    //已有规格 展示价格
  //    mChosenOption = mOptions.get(pos);
  //    view.showInputMoney(false, mChosenOption, mChosenOption.limit_days);
  //    view.setPayMoney(mChosenOption.price + "元");
  //  } else {
  //    //其他规格
  //    mChosenOption = null;
  //    view.showInputMoney(true, mChosenOption, false);
  //  }
  //}
  public void setSaler(String id) {
    cardBuyBody.setSeller_id(id);
    cardBuyBody.staff_id = null;
  }

  public void setStaffId(String id) {
    cardBuyBody.staff_id = id;
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
            if (eventTxT.type == CardBuyFragment.TYPE_CARD_BUY_REMARK) {
              view.remark(!eventTxT.txt.isEmpty());
              cardBuyBody.remarks = eventTxT.txt;
            } else {
              view.realCardNum(!eventTxT.txt.isEmpty());
              cardBuyBody.card_no = eventTxT.txt;
            }
          }
        });

    //学员 选择某个学员
    RxBusAdd(EventSelectedStudent.class).onBackpressureLatest()
        .subscribe(new BusSubscribe<EventSelectedStudent>() {
          @Override public void onNext(EventSelectedStudent eventSelectedStudent) {
            qcStudentBeans = eventSelectedStudent.getStudents();
            view.bindStudent(eventSelectedStudent.getNameStr());
            cardBuyBody.user_ids = eventSelectedStudent.getIdStr();
            choseStuIds = eventSelectedStudent.getIDlist();
          }
        });
  }

  public List<QcStudentBean> getQcStudentBeans() {
    return qcStudentBeans;
  }

  public void setQcStudentBeans(List<QcStudentBean> qcStudentBeans) {
    this.qcStudentBeans = qcStudentBeans;
  }

  private List<QcStudentBean> qcStudentBeans;

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

  private String id = "";

  /**
   * 获取默认填充的销售信息
   */
  public void getDefineSeller(String sellerId) {
    if (TextUtils.isEmpty(sellerId)) return;
    Observable<QcDataResponse<SalerListWrap>> salers = staffModel.getSalers();
    RxRegiste(
        salers.filter(response -> ResponseConstant.checkSuccess(response) && response.data != null)
            .onBackpressureLatest()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new NetSubscribe<QcDataResponse<SalerListWrap>>() {
              @Override public void onNext(QcDataResponse<SalerListWrap> response) {
                for (Staff seller : response.data.sellers) {
                  if (seller.getId().equals(sellerId)) {
                    view.setDefineSeller(seller);
                  }
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
                if (option.can_create) mOptions.add(option);
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
      cardBuyBody.setPrice(String.valueOf(mChosenOption.price));
      cardBuyBody.setBuyAccount(mChosenOption.charge, view.startDay(), view.endDay(),
          mChosenOption);
    }

    cardBuyBody.is_auto_start = view.autoOpen();
    cardBuyBody.coupon_id = view.getCouponId();
    cardBuyBody.origin = 2;
    if ((cardBuyBody.getSeller_id() != null && cardBuyBody.getSeller_id()
        .equals(loginStatus.staff_id())) || TextUtils.isEmpty(cardBuyBody.getSeller_id())) {
      cardBuyBody.setSeller_id(null);
      cardBuyBody.staff_id = loginStatus.staff_id();
    }
    balanceInfo = new HashMap<>();
    balanceInfo.put("cardId", mCard.getId());
    balanceInfo.put("chargeBody", new Gson().toJson(cardBuyBody));
    RxRegiste((isFromCheckout ? cardModel.qcChargeCardFromCheckout(mCard.getId(), cardBuyBody)
        : cardModel.qcChargeCard(mCard.getId(), cardBuyBody)).onBackpressureLatest()
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

  private boolean isFromCheckout = false;

  public void setFromCheckout(boolean isFromCheckout) {
    this.isFromCheckout = isFromCheckout;
  }

  public Map<String, String> getBalanceInfo() {
    return balanceInfo;
  }

  private Map<String, String> balanceInfo;

  //扣费操作
  public void proactiveDeduction(String cardId, ChargeBody body) {
    RxRegiste(cardModel.qcChargeRefund(cardId, body)
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
      cardBuyBody.setPrice(String.valueOf(mChosenOption.price));
      cardBuyBody.setBuyAccount(mChosenOption.charge, view.startDay(), view.endDay(),
          mChosenOption);
    }

    if (view.checkCardBuyBody(cardBuyBody)) {
      return;
    }
    cardBuyBody.coupon_id = view.getCouponId();
    cardBuyBody.setCharge_type(view.payMethod());
    cardBuyBody.is_auto_start = view.autoOpen();
    buyCardRequest();
  }

  public String getRePayJson() {
    return rePayJson;
  }

  private String rePayJson;

  public void buyCardRequest() {
    rePayJson = new Gson().toJson(cardBuyBody);
    RxRegiste((isFromCheckout ? cardModel.buyCardFromCheckout(cardBuyBody)
        : cardModel.buyCard(cardBuyBody)).onBackpressureLatest()
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

    void updateCoupons(Coupon coupon);

    /**
     * @param other 是否选中其他
     * @param cardTplOption 是否是期限卡
     */
    void showInputMoney(boolean other, CardTplOption cardTplOption, boolean hasValid);

    void bindStudent(String student);

    void bindSaler(String saler);

    void remark(boolean remark);

    void setPayMoney(float x);

    void setDefineSeller(Staff seller);

    /**
     * 下单完成后返回的数据
     */
    void onBusinessOrder(JsonObject response);

    /**
     * @return 实体卡号
     */
    void realCardNum(boolean isRealCard);

    String realMoney();

    String chargeMoney();

    int payMethod();

    boolean checkCardBuyBody(CardBuyBody cardBuyBody);

    String getCouponId();

    /**
     * 是否设置有效期
     */
    boolean openValidDay();

    String startDay(); //开始日期

    String endDay();   //结束日期

    boolean autoOpen();//自动开卡
  }
}
