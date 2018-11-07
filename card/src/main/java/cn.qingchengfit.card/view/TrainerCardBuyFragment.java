package cn.qingchengfit.card.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.card.R;
import cn.qingchengfit.saasbase.cards.network.body.CardBuyBody;

public class TrainerCardBuyFragment extends StaffCardBuyFragment {
  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    civSaler.setContent(loginStatus.getLoginUser().getUsername());
    //presenter.setSaler(loginStatus.getLoginUser().getId());
    return view;
  }

  @Override public boolean checkCardBuyBody(CardBuyBody cardBuyBody) {
    int i = cardBuyBody.checkWithOutSeller();
    if (i > 0 ) {
      showAlert(cardBuyBody.checkWithOutSeller());
      return true;
    }
    return false;
  }
}
