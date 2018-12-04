package cn.qingchengfit.card.presenter;

import cn.qingchengfit.card.BuyCardUsecase;
import cn.qingchengfit.card.buy.CompletedBuyPresenter;
import javax.inject.Inject;

/**
 * Created by fb on 2017/12/20.
 */

public class StaffCardBuyPresenter extends CompletedBuyPresenter {


  @Inject
  public StaffCardBuyPresenter(BuyCardUsecase usecase) {
    super(usecase);
  }
}
