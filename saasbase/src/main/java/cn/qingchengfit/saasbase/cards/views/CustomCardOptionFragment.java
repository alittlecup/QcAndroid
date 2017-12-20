package cn.qingchengfit.saasbase.cards.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.cards.event.EventCustomOption;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.ExpandedLayout;
import com.anbillon.flabellum.annotations.Need;

/**
 * Created by fb on 2017/12/15.
 */

public abstract class CustomCardOptionFragment extends SaasBaseFragment {

  @BindView(R2.id.civ_charge_money) CommonInputView civChargeMoney;
  @BindView(R2.id.civ_real_money) CommonInputView civRealMoney;
  @BindView(R2.id.civ_custom_validity) CommonInputView civCustomValidity;
  @BindView(R2.id.el_need_valid) ExpandedLayout elNeedValid;
  @Need CardTplOption cardOptionCustom;
  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_custom_card_option, container, false);
    unbinder = ButterKnife.bind(this, view);
    setToolbar();
    return view;
  }

  private void setToolbar(){
    initToolbar(toolbar);
    toolbarTitle.setText(getToolbarTitle());
  }

  @OnClick(R2.id.tv_custom_card_option_confirm) public void onConfirm() {
    initData();
    if (cardOptionCustom != null) RxBus.getBus().post(new EventCustomOption(cardOptionCustom));
    getActivity().onBackPressed();
  }

  abstract void initData();
  abstract String getToolbarTitle();
}
