package cn.qingchengfit.saasbase.cards.views;

import android.content.Context;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;



import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.cards.event.EventCustomOption;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.ExpandedLayout;
import com.anbillon.flabellum.annotations.Need;

/**
 * Created by fb on 2017/12/15.
 */

public abstract class CustomCardOptionFragment extends SaasBaseFragment {

	CommonInputView civChargeMoney;
	CommonInputView civRealMoney;
	CommonInputView civCustomValidity;
	ExpandedLayout elNeedValid;
  @Need CardTplOption cardOptionCustom;
	Toolbar toolbar;
	TextView toolbarTitle;
	LinearLayout layout;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_custom_card_option, container, false);
    civChargeMoney = (CommonInputView) view.findViewById(R.id.civ_charge_money);
    civRealMoney = (CommonInputView) view.findViewById(R.id.civ_real_money);
    civCustomValidity = (CommonInputView) view.findViewById(R.id.civ_custom_validity);
    elNeedValid = (ExpandedLayout) view.findViewById(R.id.el_need_valid);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    layout = (LinearLayout) view.findViewById(R.id.layout);
    view.findViewById(R.id.tv_custom_card_option_confirm)
        .setOnClickListener(new View.OnClickListener() {
          @Override public void onClick(View v) {
            onConfirm();
          }
        });

    layout.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (getActivity() != null) {
          AppUtils.hideKeyboard(getActivity());
        }
      }
    });
    setToolbar();
    return view;
  }

  @Override public boolean isBlockTouch() {
    return false;
  }

  private void hideKeyboard(IBinder token) {
    if (token != null) {
      InputMethodManager im =
          (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
      if (im != null) im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
    }
  }

  private void setToolbar() {
    initToolbar(toolbar);
    toolbarTitle.setText(getToolbarTitle());
  }

 public void onConfirm() {
    if (elNeedValid.isExpanded() && TextUtils.isEmpty(civCustomValidity.getContent())) {
      DialogUtils.showAlert(getContext(), "请填写有效期");
      return;
    }
    if (civChargeMoney.isEmpty() || civRealMoney.isEmpty()){
      showAlert("请正确填写规格");
      return;
    }
    initData();
    if (cardOptionCustom != null) RxBus.getBus().post(new EventCustomOption(cardOptionCustom));
    getActivity().onBackPressed();
  }

  abstract void initData();

  abstract String getToolbarTitle();

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
