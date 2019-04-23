package cn.qingchengfit.staffkit.views.signin;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.TextView;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.SignInCostBody;
import cn.qingchengfit.model.responese.SignInCardCostBean;
import cn.qingchengfit.model.responese.SignInConfig;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import cn.qingchengfit.items.SignInConfigItem;
import cn.qingchengfit.staffkit.views.adapter.SignInFlexibleAdapter;
import cn.qingchengfit.staffkit.views.custom.SpaceItemDecoration;
import cn.qingchengfit.staffkit.views.custom.SwitcherLayout;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * 签到配置
 * Created by yangming on 16/8/30.
 */
public class SignInConfigFragment extends BaseFragment
    implements SignInConfigPresenter.SignInConfigView {

  SwitcherLayout swtSigninConfigLocker;
  SwitcherLayout swtSigninConfigLockerBack;
  TextView tvSignInConfigNotice;
  RecyclerView recyclerViewSignInConfig;

  @Inject SignInConfigPresenter presenter;
  @Inject SerPermisAction serPermisAction;
  /**
   * 获得的初始化费用配置信息
   */
  List<SignInCardCostBean.CardCost> cardCosts = new ArrayList<>();
  /**
   * GET -- GSON
   * 签到是否分配更衣柜
   */
  List<SignInConfig.Config> signInConfig = new ArrayList<>();
  /**
   * GET -- GSON
   * 签出是否自动归还更衣柜
   */
  List<SignInConfig.Config> signOutConfig = new ArrayList<>();
  View noPermission;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  private LinearLayoutManager mLinearLayoutManager;
  private List<AbstractFlexibleItem> items;
  private SignInFlexibleAdapter flexibleAdapter;
  private boolean isGranted;
  /**
   * 提交费用配置的body
   */
  private List<SignInCostBody.CardCost> card_costs = new ArrayList<>();

  public SignInConfigFragment() {
  }

  public static SignInConfigFragment newInstance() {
    SignInConfigFragment fragment = new SignInConfigFragment();
    return fragment;
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_signin_config, container, false);
    swtSigninConfigLocker = (SwitcherLayout) view.findViewById(R.id.swt_signin_config_locker);
    swtSigninConfigLockerBack =
        (SwitcherLayout) view.findViewById(R.id.swt_signin_config_locker_back);
    tvSignInConfigNotice = (TextView) view.findViewById(R.id.tv_sign_in_config_notice);
    recyclerViewSignInConfig = (RecyclerView) view.findViewById(R.id.recyclerView_SignIn_config);
    noPermission = (View) view.findViewById(R.id.no_permission);
    view.findViewById(R.id.no_permission).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        noPermission();
      }
    });

    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    delegatePresenter(presenter, this);
    isGranted = serPermisAction.check(gymWrapper.id(), gymWrapper.model(),
        PermissionServerUtils.CHECKIN_SETTING_CAN_CHANGE);
    noPermission.setVisibility(isGranted ? View.GONE : View.VISIBLE);
    if (!PreferenceUtils.getPrefBoolean(getContext(), "showNotice" + App.staffId, false)) {
      tvSignInConfigNotice.setVisibility(View.VISIBLE);
    }

    Drawable drawable = getResources().getDrawable(R.drawable.ic_sign_in_notice);
    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
    tvSignInConfigNotice.setCompoundDrawables(drawable, null, null, null);

    mLinearLayoutManager = new LinearLayoutManager(getContext());
    recyclerViewSignInConfig.setLayoutManager(mLinearLayoutManager);
    recyclerViewSignInConfig.addItemDecoration(new SpaceItemDecoration(10, getContext()));

    swtSigninConfigLocker.setOnCheckListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        swtSigninConfigLocker.setOpen(b);
        swtSigninConfigLockerBack.setVisibility(b ? View.VISIBLE : View.GONE);
        swtSigninConfigLockerBack.setOpen(b);
      }
    });

    presenter.getSignInConfig();
    presenter.getSignOutConfig();
    presenter.getCardCostList();

    return view;
  }

  @Override public String getFragmentName() {
    return SignInConfigFragment.class.getName();
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);

    mCallbackActivity.setToolbar(getString(R.string.sign_in_config_title), false, null,
        R.menu.menu_comfirm, new Toolbar.OnMenuItemClickListener() {
          @Override public boolean onMenuItemClick(MenuItem item) {
            if (!isGranted) {
              showAlert(R.string.alert_permission_forbid);
              return true;
            }
            showLoading();
            if (signInConfig != null && !signInConfig.isEmpty()) {
              presenter.putCheckinWithLocker(signInConfig.get(0).getId() + "",
                  swtSigninConfigLocker.isOpen());
            } else if (signOutConfig != null && !signOutConfig.isEmpty()) {
              presenter.putCheckoutWithReturnLocker(signOutConfig.get(0).getId() + "",
                  swtSigninConfigLockerBack.isOpen());
            } else {
              onCheckOutConfigComplete();
            }

            return true;
          }
        });
  }

  public void noPermission() {
    showAlert(R.string.alert_permission_forbid);
  }

  @Override public void onDestroyView() {
    AppUtils.hideKeyboard(getActivity());
    super.onDestroyView();

    presenter.unattachView();
  }

  @Override public void onGetSignInConfig(List<SignInConfig.Config> signInConfig) {
    this.signInConfig = signInConfig;
    if (signInConfig == null || signInConfig.isEmpty()) {
      ToastUtils.show("没有签到配置信息");
      return;
    }
    if (signInConfig.get(0).getValue() instanceof Integer
        && signInConfig.get(0).getValueInt() == 1) {
      swtSigninConfigLocker.setOpen(true);
    } else {
      swtSigninConfigLocker.setOpen(false);
      swtSigninConfigLocker.setShowDivier(false);
    }
  }

  @Override public void onGetSignOutConfig(List<SignInConfig.Config> signInConfig) {
    this.signOutConfig = signInConfig;
    if (signInConfig == null || signInConfig.isEmpty()) {
      ToastUtils.show("没有签出配置信息");
      return;
    }
    if (signInConfig.get(0).getValue() instanceof Integer
        && signInConfig.get(0).getValueInt() == 1) {
      swtSigninConfigLockerBack.setOpen(true);
    } else {
      swtSigninConfigLockerBack.setOpen(false);
    }
  }

  @Override public void onGetCostList(List<SignInCardCostBean.CardCost> signInConfigs) {
    cardCosts = signInConfigs;//.data.card_costs;
    items = new ArrayList<>();
    for (SignInCardCostBean.CardCost o : cardCosts) {
      items.add(new SignInConfigItem(o));
    }

    flexibleAdapter = new SignInFlexibleAdapter(items);
    recyclerViewSignInConfig.setAdapter(flexibleAdapter);
  }

  @Override public void onCheckInConfigComplete() {
    SignInActivity.checkinWithLocker = swtSigninConfigLocker.isOpen() ? 1 : 0;
    presenter.putCheckoutWithReturnLocker(signOutConfig.get(0).getId() + "",
        swtSigninConfigLockerBack.isOpen());
  }

  @Override public void onCheckOutConfigComplete() {
    card_costs.clear();
    for (int i = 0; i < flexibleAdapter.getItemCount(); i++) {
      SignInConfigItem item = (SignInConfigItem) flexibleAdapter.getItem(i);
      if (item.bean.isSelected()) {
        card_costs.add(new SignInCostBody.CardCost(item.bean.getId() + "", item.bean.getCost()));
      }
    }
    presenter.confirm(card_costs);
  }

  @Override public void onCostConfigSuccess() {
    ToastUtils.show("设置成功");
    PreferenceUtils.setPrefBoolean(getContext(), "showNotice" + App.staffId, isSetted());
    hideLoading();
    getActivity().onBackPressed();
  }

  @Override public void onFail() {
    ToastUtils.show("请检查网络设置");
  }

  public boolean isSetted() {
    //if (swtSigninConfigLocker.isOpen() || swtSigninConfigLockerBack.isOpen()) {
    //    return true;
    //} else {
    for (int i = 0; i < flexibleAdapter.getItemCount(); i++) {
      SignInConfigItem item = (SignInConfigItem) flexibleAdapter.getItem(i);
      if (item.bean.isSelected()) {
        return true;
      }
    }
    return false;
    //}
  }
}
