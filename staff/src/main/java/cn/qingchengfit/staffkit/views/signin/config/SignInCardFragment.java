package cn.qingchengfit.staffkit.views.signin.config;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;

/**
 * Created by fb on 2017/9/27.
 */

public class SignInCardFragment extends BaseFragment {

  public static final String CARD_URL = "https://mp.weixin.qq.com/s/6NWvN_Pis0emC5q6_gjmbA";

  @BindView(R.id.input_sign_in_card_judge) LinearLayout inputSignInCardJudge;
  @BindView(R.id.input_sign_in_card_bind) LinearLayout inputSignInCardBind;
  Unbinder unbinder;
  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitle;
  @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_card_sign, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    toolbarTitle.setText("刷卡签到");
    return view;
  }

  @OnClick({ R.id.input_sign_in_card_judge, R.id.input_sign_in_card_bind }) public void onJudge() {
    WebActivity.startWeb(CARD_URL, getContext());
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
