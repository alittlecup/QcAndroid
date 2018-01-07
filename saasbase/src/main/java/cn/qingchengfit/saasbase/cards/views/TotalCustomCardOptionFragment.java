package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.saasbase.routers.SaasbaseParamsInjector;
import cn.qingchengfit.utils.CmStringUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;

/**
 * Created by fb on 2017/12/20.
 */

@Leaf(module = "card", path = "/custom/all/option")
public class TotalCustomCardOptionFragment extends CustomCardOptionFragment {

  @Need Integer unuse;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SaasbaseParamsInjector.inject(this);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = super.onCreateView(inflater, container, savedInstanceState);
    civChargeMoney.setLabel("面额(元)");
    if (cardOptionCustom != null){
      elNeedValid.setExpanded(cardOptionCustom.isLimit_days());
      civCustomValidity.setContent(String.valueOf(cardOptionCustom.getDays()));
      civChargeMoney.setContent(cardOptionCustom.getCharge());
      civRealMoney.setContent(CmStringUtils.getMoneyStr(cardOptionCustom.getPrice()));
    }
    return view;
  }

  @Override void initData() {
    if (cardOptionCustom == null){
      cardOptionCustom = new CardTplOption();
    }
    if (elNeedValid.isExpanded() && TextUtils.isEmpty(civCustomValidity.getContent())){
      showAlert("请填写有效期");
      return;
    }
    cardOptionCustom.setLimit_days(elNeedValid.isExpanded());
    if (elNeedValid.isExpanded()) {
      cardOptionCustom.setDays(Integer.valueOf(civCustomValidity.getContent()));
    }
    cardOptionCustom.setCharge(civChargeMoney.getContent());
    cardOptionCustom.setPrice(Float.parseFloat(civRealMoney.getContent()));
  }

  @Override String getToolbarTitle() {
    return null;
  }
}
