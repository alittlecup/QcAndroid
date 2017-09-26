package cn.qingchengfit.saasbase.cards.cardtypes.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.cards.cardtypes.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.cardtypes.network.response.CardTplOptionListWrap;
import cn.qingchengfit.saasbase.cards.cardtypes.network.response.CardTplWrapper;
import cn.qingchengfit.saasbase.repository.ICardModel;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.subscribes.NetSubscribe;
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

  @Inject public CardBuyPresenter() {

  }




  public void selectOption(int pos){
    if ( pos < mOptions.size()){
      //已有规格

    }else {
      //其他规格
      view.showInputMoney();
    }
  }

  /**
   * 初始化 监听选择
   */
  public void initBus(){
    //RxBusAdd()销售

    //备注信息

    //学员 选择某个学员
    RxBusAdd(QcStudentBean.class)
        .onBackpressureLatest()
        .subscribe(new BusSubscribe<QcStudentBean>() {
          @Override public void onNext(QcStudentBean qcStudentBean) {

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
    void showInputMoney();
    void bindStudent(String student);
    void bindSaler(String saler);

    /**
     * @return 实体卡号
     */
    String realCardNum();

    /**
     * 是否设置有效期
     */
    boolean needValidDay();
    String stardDay(); //开始日期
    String endDay();   //结束日期
    boolean autoOpen();//自动开卡

  }
}
