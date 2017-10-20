package cn.qingchengfit.pos.exchange;

import android.content.Intent;
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
import butterknife.OnClick;
import cn.qingchengfit.pos.R;
import cn.qingchengfit.pos.login.LoginActivity;
import cn.qingchengfit.saasbase.constant.Configs;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.views.fragments.TipTextDialogFragment;
import com.anbillon.flabellum.annotations.Leaf;

/**
 * Created by fb on 2017/10/17.
 */
@Leaf(module = "exchange",path = "/home/")
public class FragmentExchange extends BaseFragment {

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitle;
  @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R.id.img_exchange_head) ImageView imgExchangeHead;
  @BindView(R.id.tv_exchange_name) TextView tvExchangeName;
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

  @OnClick(R.id.btn_exchange)
  public void onExchange(){
    TipTextDialogFragment dialogFragment = TipTextDialogFragment.newInstance(getResources().getString(R.string.tips_exchange),
        getResources().getString(R.string.confirm),
        getResources().getString(R.string.exchange_confirm));
    dialogFragment.setOnConfirmListener(new TipTextDialogFragment.OnConfirmListener() {
      @Override public void onConfirm() {
        PreferenceUtils.setPrefString(getContext(), Configs.PREFER_SESSION, "");
        PreferenceUtils.setPrefString(getContext(), Configs.PREFER_SESSION_ID, "");
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        getContext().startActivity(intent);
        getActivity().finish();
      }
    });
    dialogFragment.show(getFragmentManager(), null );
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
