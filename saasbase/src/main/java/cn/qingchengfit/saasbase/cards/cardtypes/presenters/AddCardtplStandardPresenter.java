package cn.qingchengfit.saasbase.cards.cardtypes.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.saasbase.cards.cardtypes.network.body.OptionBody;
import javax.inject.Inject;

public class AddCardtplStandardPresenter extends BasePresenter {
  @Inject GymWrapper gymWrapper;
  private MVPView view;
  private String cardtplId;
  private int cardtplType = 1;
  private OptionBody ob = new OptionBody();

  @Inject public AddCardtplStandardPresenter() {
  }

  public String getCardtplId() {
    return cardtplId;
  }

  public void setCardtplId(String cardtplId) {
    this.cardtplId = cardtplId;
  }

  public int getCardtplType() {
    return cardtplType;
  }

  public void setCardtplType(int cardtplType) {
    this.cardtplType = cardtplType;
  }

  public void setLimit(boolean limit){
    this.ob.limit_days = limit;
  }

  public void setLimitDay(String days){
    try{
      this.ob.days = Integer.parseInt(days);
    }catch (Exception e){
      this.ob.days = -1;
    }
  }

  /**
   * @param price  实收金额
   * @param charge 充值金额
   */
  public void setChargeAndReal(String price,String charge){
    this.ob.charge = charge;
    this.ob.price = price;
  }

  public void setCanCharge(boolean charge){
    this.ob.can_charge = charge;
  }

  public void setCanCreated(boolean b){
    this.ob.can_create = b;
  }

  public void canOnlyStaff(boolean o){
    this.ob.for_staff = o;
  }

  public String getDesc(){
    if (ob.description == null)
      return "";
    else return ob.description;
  }

  public void setDesc(String desc){
    ob.description = desc;
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public void addOption() {

  }

  public interface MVPView extends CView {

  }
}
