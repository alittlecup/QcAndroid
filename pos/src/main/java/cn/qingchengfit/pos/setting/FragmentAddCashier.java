package cn.qingchengfit.pos.setting;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.pos.R;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonInputView;

/**
 * Created by fb on 2017/10/18.
 */

public class FragmentAddCashier extends BaseFragment {

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitle;
  @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R.id.input_add_cashier_name) CommonInputView inputAddCashierName;
  @BindView(R.id.input_add_cashier_gender) CommonInputView inputAddCashierGender;
  @BindView(R.id.input_add_cashier_phone) CommonInputView inputAddCashierPhone;
  @BindView(R.id.btn_exchange) TextView btnExchange;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_add_crashier, container, false);
    unbinder = ButterKnife.bind(this, view);
    return view;
  }

  @OnClick(R.id.btn_exchange)
  public void onConfirmExchange(){
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
