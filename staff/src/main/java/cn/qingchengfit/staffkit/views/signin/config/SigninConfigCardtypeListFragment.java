package cn.qingchengfit.staffkit.views.signin.config;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.model.responese.SignInCardCostBean;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.items.SignInConfigItem;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.custom.SpaceItemDecoration;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BaseFragment;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/2/13.
 */
@FragmentWithArgs public class SigninConfigCardtypeListFragment extends BaseFragment
    implements SigninConfigCardtypePresenter.MVPView {

  @Arg(required = false) ArrayList<SignInCardCostBean.CardCost> costList;

  RecyclerView recycleview;
  @Inject SigninConfigCardtypePresenter presenter;
  /**
   * 获得的初始化费用配置信息
   */
  private List<AbstractFlexibleItem> items = new ArrayList<>();
  private CommonFlexAdapter flexibleAdapter;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    FragmentArgs.inject(this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_signin_cardtype_lsit, container, false);
    recycleview = (RecyclerView) view.findViewById(R.id.recycleview);

    delegatePresenter(presenter, this);
    mCallbackActivity.setToolbar("会员卡签到", false, null, R.menu.menu_compelete,
        new Toolbar.OnMenuItemClickListener() {
          @Override public boolean onMenuItemClick(MenuItem item) {
            if (isSetted()) {
              ArrayList<Parcelable> card_costs = new ArrayList<>();
              for (int i = 0; i < flexibleAdapter.getItemCount(); i++) {
                if (flexibleAdapter.getItem(i) instanceof SignInConfigItem) {
                  SignInConfigItem signInConfigItem = (SignInConfigItem) flexibleAdapter.getItem(i);
                  if (signInConfigItem.bean.isSelected()) {
                    card_costs.add(signInConfigItem.bean);
                  }
                }
              }
              Intent ret = IntentUtils.instanceListParcelable(card_costs);
              getActivity().setResult(Activity.RESULT_OK, ret);
              getActivity().finish();
            } else {
              Intent ret = IntentUtils.instanceListParcelable(new ArrayList<>());
              getActivity().setResult(Activity.RESULT_OK, ret);
              getActivity().finish();
            }
            return true;
          }
        });
    recycleview.setLayoutManager(new LinearLayoutManager(getContext()));
    recycleview.addItemDecoration(new SpaceItemDecoration(10, getContext()));
    recycleview.getViewTreeObserver()
        .addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
          @Override public void onGlobalLayout() {
            CompatUtils.removeGlobalLayout(recycleview.getViewTreeObserver(), this);
            if (costList != null) {
              items.clear();
              for (SignInCardCostBean.CardCost o : costList) {
                items.add(new SignInConfigItem(o));
              }
              if (items.size() == 0) {
                items.add(new CommonNoDataItem(R.drawable.no_cardtype, "您没有可用卡种类"));
              }
            }
            flexibleAdapter = new CommonFlexAdapter(items);
            recycleview.setAdapter(flexibleAdapter);
          }
        });
    return view;
  }

  public boolean isSetted() {
    if (flexibleAdapter.getItemCount() == 0) {
      return false;
    }
    for (int i = 0; i < flexibleAdapter.getItemCount(); i++) {
      if (flexibleAdapter.getItem(i) instanceof SignInConfigItem) {
        SignInConfigItem item = (SignInConfigItem) flexibleAdapter.getItem(i);
        if (item.bean.isSelected()) {
          return true;
        }
      }
    }
    return false;
  }

  @Override public void onGetCostList(List<SignInCardCostBean.CardCost> signInConfigs) {
  }


  @Override public void onCostConfigSuccess() {
    getActivity().onBackPressed();
  }

  @Override public String getFragmentName() {
    return SigninConfigCardtypeListFragment.class.getName();
  }

  @Override public void onShowError(String e) {
    hideLoading();
    ToastUtils.show(e);
  }

  @Override public void onShowError(@StringRes int e) {
    onShowError(getString(e));
  }
}
