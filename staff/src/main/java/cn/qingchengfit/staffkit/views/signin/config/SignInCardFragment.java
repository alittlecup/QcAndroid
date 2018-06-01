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




import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.views.activity.WebActivity;
import cn.qingchengfit.views.fragments.BaseFragment;

/**
 * Created by fb on 2017/9/27.
 */

public class SignInCardFragment extends BaseFragment {

  public static final String CARD_URL = "https://mp.weixin.qq.com/s/6NWvN_Pis0emC5q6_gjmbA";

	LinearLayout inputSignInCardJudge;
	LinearLayout inputSignInCardBind;

	Toolbar toolbar;
	TextView toolbarTitle;
	FrameLayout toolbarLayout;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_card_sign, container, false);
    inputSignInCardJudge = (LinearLayout) view.findViewById(R.id.input_sign_in_card_judge);
    inputSignInCardBind = (LinearLayout) view.findViewById(R.id.input_sign_in_card_bind);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    toolbarLayout = (FrameLayout) view.findViewById(R.id.toolbar_layout);
    view.findViewById(R.id.input_sign_in_card_judge).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onJudge();
      }
    });
    view.findViewById(R.id.input_sign_in_card_bind).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onJudge();
      }
    });

    initToolbar(toolbar);
    toolbarTitle.setText("刷卡签到");
    return view;
  }

 public void onJudge() {
    WebActivity.startWeb(CARD_URL, getContext());
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }
}
