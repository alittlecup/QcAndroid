package cn.qingchengfit.saasbase.cards.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.network.body.CardBuyBody;
import cn.qingchengfit.saasbase.cards.network.response.CardTplOptionListWrap;
import cn.qingchengfit.saasbase.cards.network.response.CardTplWrapper;
import cn.qingchengfit.saasbase.cards.network.response.PayBusinessResponse;
import cn.qingchengfit.saasbase.events.EventSelectedStudent;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.utils.DateUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CardBuyPresenter extends BasePresenter {

  private MVPView view;
  @Inject GymWrapper gymWrapper;
  @Inject ICardModel cardModel;

  private String mCardTplId;
  private List<CardTplOption> mOptions  = new ArrayList<>();
  /**
   * 选中的支付价格
   */
  private CardTplOption mChosenOption;
  /**
   * 购卡数据
   */
  private CardBuyBody cardBuyBody = new CardBuyBody();


  public String getmCardTplId() {
    return mCardTplId;
  }

  public void setmCardTplId(String mCardTplId) {
    this.mCardTplId = mCardTplId;
  }

  @Inject public CardBuyPresenter() {

  }




  public void selectOption(int pos){
    if ( pos < mOptions.size()){
      //已有规格
       mChosenOption = mOptions.get(pos);
      view.showInputMoney(false);
    }else {
      //其他规格
      view.showInputMoney(true);
    }
  }

  /**
   * 初始化 监听选择
   */
  public void initBus(){
    RxBusAdd(Staff.class)
        .onBackpressureLatest()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BusSubscribe<Staff>() {
          @Override public void onNext(Staff staff) {
            view.bindSaler(staff.getUsername());
            cardBuyBody.setSeller_id(staff.id);
          }
        });

    //备注信息

    //学员 选择某个学员
    RxBusAdd(EventSelectedStudent.class)
        .onBackpressureLatest()
        .subscribe(new BusSubscribe<EventSelectedStudent>() {
          @Override public void onNext(EventSelectedStudent eventSelectedStudent) {
            view.bindStudent(eventSelectedStudent.getNameStr());
            cardBuyBody.user_ids = eventSelectedStudent.getIdStr();
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
              mOptions.addAll(qcResponse.data.options);
              view.onGetOptions(qcResponse.data.options);
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }));
  }

  /**
   * 真 购卡操作，先检查数据，交给后端处理
   */
  public void buyCard(){
    if (mChosenOption == null){
      cardBuyBody.setPrice(view.realMoney());
      cardBuyBody.setBuyAccount(view.chargeMoney(),view.startDay(),view.endDay());
    }else {
      cardBuyBody.setPrice(mChosenOption.price);
      cardBuyBody.setBuyAccount(mChosenOption.charge, view.startDay(), DateUtils.addDay(view.startDay(),mChosenOption.days));
    }

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

    if (cardBuyBody.checkData() > 0) {
      view.showAlert(cardBuyBody.checkData());
      return;
    }
    cardBuyBody.is_auto_start = view.autoOpen();
    
    //RxRegiste(cardModel.buyCard(cardBuyBody)
    //    .onBackpressureLatest()
    //    .subscribeOn(Schedulers.io())
    //    .observeOn(AndroidSchedulers.mainThread())
    //    .subscribe(new NetSubscribe<QcDataResponse<PayBusinessResponse>>() {
    //      @Override public void onNext(QcDataResponse<PayBusinessResponse> qcResponse) {
    //        if (ResponseConstant.checkSuccess(qcResponse)) {
    //          view.onBusinessOrder(qcResponse.data);
    //        } else {
    //          view.onShowError(qcResponse.getMsg());
    //        }
    //      }
    //    }));
    view.onBusinessOrder(new PayBusinessResponse());
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
    void showInputMoney(boolean show);
    void bindStudent(String student);
    void bindSaler(String saler);
    /**
     * 下单完成后返回的数据
     */
    void onBusinessOrder(PayBusinessResponse payBusinessResponse);
    /**
     * @return 实体卡号
     */
    String realCardNum();
    String realMoney();
    String chargeMoney();
    /**
     * 是否设置有效期
     */
    boolean openValidDay();
    String startDay(); //开始日期
    String endDay();   //结束日期
    boolean autoOpen();//自动开卡



  }
}
