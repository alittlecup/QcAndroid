package cn.qingchengfit.staffkit.card.presenter;

import cn.qingchengfit.staffkit.usecase.BuyCardUsecase;
import cn.qingchengfit.staffkit.views.card.buy.CompletedBuyPresenter;
import javax.inject.Inject;

/**
 * Created by fb on 2017/12/20.
 */

public class StaffCardBuyPresenter extends CompletedBuyPresenter{

  @Inject
  public StaffCardBuyPresenter(BuyCardUsecase usecase) {
    super(usecase);
  }
}
