package cn.qingchengfit.staffkit.views.signin.config;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import cn.qingchengfit.model.responese.SignInConfig;
import cn.qingchengfit.model.responese.SignInUrl;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.adapter.FragmentAdapter;
import cn.qingchengfit.staffkit.views.signin.SignInPresenter;
import cn.qingchengfit.views.fragments.BaseFragment;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by fb on 2017/9/27.
 */

public class SignInCodeFragment extends BaseFragment implements SignInPresenter.SignInView {

  @BindView(R.id.toolbar) Toolbar toolbar;
  @BindView(R.id.toolbar_title) TextView toolbarTitle;
  @BindView(R.id.toolbar_layout) FrameLayout toolbarLayout;
  @BindView(R.id.tab_code_sign) TabLayout tabCodeSign;
  Unbinder unbinder;
  @BindView(R.id.vp_sign_in_code) ViewPager vpSignInCode;
  @Inject SignInPresenter presenter;
  private ArrayList<Fragment> fragments = new ArrayList<>();
  private FragmentAdapter adapter;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_code_sign, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    initToolbar(toolbar);
    presenter.getSigninUrl();
    toolbarTitle.setText("扫码签到");
    adapter = new FragmentAdapter(getChildFragmentManager(), fragments);
    vpSignInCode.setAdapter(adapter);
    tabCodeSign.setupWithViewPager(vpSignInCode);
    return view;
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override public void onGetSignInConfig(List<SignInConfig.Config> signInConfig) {

  }

  @Override public void onGetSignInUrl(SignInUrl signInUrl) {
    String checkin_url = signInUrl.data.getCheckin_url();
    String checkout_url = signInUrl.data.getCheckout_url();
    fragments.add(SignInChildCodeFragment.newInstance(checkin_url, true));
    fragments.add(SignInChildCodeFragment.newInstance(checkout_url, false));
    adapter.notifyDataSetChanged();
  }

  @Override public void onFail() {

  }
}
