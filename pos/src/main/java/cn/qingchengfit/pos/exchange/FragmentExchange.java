package cn.qingchengfit.pos.exchange;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.pos.R;
import cn.qingchengfit.views.fragments.BaseFragment;

/**
 * Created by fb on 2017/10/17.
 */

public class FragmentExchange extends BaseFragment {

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitle;
  @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R.id.img_exchange_head) ImageView imgExchangeHead;
  @BindView(R.id.tv_exchange_name) TextView tvExchangeName;
  @BindView(R.id.tv_exchange_position) TextView tvExchangePosition;
  @BindView(R.id.tv_exchange_now_position) TextView tvExchangeNowPosition;
  @BindView(R.id.tv_exchange_start) TextView tvExchangeStart;
  @BindView(R.id.tv_exchange_end) TextView tvExchangeEnd;
  @BindView(R.id.tv_exchange_business) TextView tvExchangeBusiness;
  @BindView(R.id.tv_exchange_money) TextView tvExchangeMoney;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_exchange, container, false);
    unbinder = ButterKnife.bind(this, view);
    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
