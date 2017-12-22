package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.base.CardTplOption;
import com.anbillon.flabellum.annotations.Leaf;

/**
 * Created by fb on 2017/12/15.
 */

@Leaf(module = "card", path = "/custom/option")
public class TimeCardOptionFragment extends CustomCardOptionFragment {

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    civChargeMoney.setLabel("面额(天)");
    elNeedValid.setVisibility(View.GONE);
    if (cardOptionCustom != null){
      civChargeMoney.setContent(String.valueOf(cardOptionCustom.getCharge()));
      civRealMoney.setContent(cardOptionCustom.getPrice());
    }
    return view;
  }

  @Override public boolean onFragmentBackPress() {
    return getFragmentManager().popBackStackImmediate();
  }

  @Override void initData() {
    if (cardOptionCustom == null){
      cardOptionCustom = new CardTplOption();
    }
    cardOptionCustom.setDays(Integer.valueOf(civChargeMoney.getContent()));
    cardOptionCustom.setPrice(String.valueOf(civRealMoney.getContent()));
  }

  @Override String getToolbarTitle() {
    return "自定义";
  }
}
