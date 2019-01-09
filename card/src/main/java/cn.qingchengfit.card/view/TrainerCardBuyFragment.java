package cn.qingchengfit.card.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.saasbase.cards.network.body.CardBuyBody;

public class TrainerCardBuyFragment extends StaffCardBuyFragment {
  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    civSaler.setContent(loginStatus.getLoginUser().getUsername());
    //presenter.setSaler(loginStatus.getLoginUser().getId());
    presenter.setFromCheckout(true);
    return view;
  }

  @Override public void onCivBindMenbersClicked() {
    QcRouteUtil.setRouteOptions(new RouteOptions("student").setActionName("/search/student/")
        .addParam("addAble", true)
        .addParam("selectedStudent", presenter.getQcStudentBeans())
        .addParam("studentIdList", presenter.getChoseStuIds())).call();
  }

  @Override public boolean checkCardBuyBody(CardBuyBody cardBuyBody) {
    int i = cardBuyBody.checkWithOutSeller();
    if (i > 0) {
      showAlert(cardBuyBody.checkWithOutSeller());
      return true;
    }
    return false;
  }
}
