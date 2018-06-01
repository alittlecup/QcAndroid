package cn.qingchengfit.notisetting.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.items.ProgressItem;
import cn.qingchengfit.notisetting.bean.ShopOrder;
import cn.qingchengfit.notisetting.item.NotiFooterItem;
import cn.qingchengfit.notisetting.item.SmsChargeItem;
import cn.qingchengfit.notisetting.presenter.NotiSettingChargeHistoryPresenter;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.views.fragments.BaseFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
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
 * Created by Paper on 2017/8/3.
 */
public class NotiSettingChargeHistoryFragment extends BaseFragment
    implements NotiSettingChargeHistoryPresenter.MVPView, FlexibleAdapter.EndlessScrollListener {

	Toolbar toolbar;
	TextView toolbarTitle;
	RecyclerView recycleview;

  @Inject NotiSettingChargeHistoryPresenter presenter;

  private CommonFlexAdapter adapter;
  private SmoothScrollLinearLayoutManager linearLayoutManager;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    adapter = new CommonFlexAdapter(new ArrayList(), this);
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.recyleview_toolbar, container, false);
    toolbar = (Toolbar) view.findViewById(R.id.toolbar);
    toolbarTitle = (TextView) view.findViewById(R.id.toolbar_title);
    recycleview = (RecyclerView) view.findViewById(R.id.recycleview);

    delegatePresenter(presenter, this);
    initToolbar(toolbar);
    linearLayoutManager = new SmoothScrollLinearLayoutManager(getContext());
    if (savedInstanceState != null) {
      linearLayoutManager.scrollToPosition(savedInstanceState.getInt("pos"));
    }
    recycleview.setLayoutManager(linearLayoutManager);
    recycleview.addItemDecoration(
        new FlexibleItemDecoration(getContext()).withOffset(15).withBottomEdge(true));
    recycleview.setAdapter(adapter);

    return view;
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbarTitle.setText("短信充值记录");
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    presenter.queryList();
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    try {
      outState.putInt("pos", linearLayoutManager.findFirstVisibleItemPosition());
    } catch (Exception e) {

    }
    super.onSaveInstanceState(outState);
  }

  @Override public String getFragmentName() {
    return NotiSettingChargeHistoryFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  @Override public void onList(List<ShopOrder> list, int total, int curPage) {
    if (list != null) {
      if (curPage == 1) {
        adapter.clear();
        adapter.addScrollableFooter(new NotiFooterItem("如有疑问，请联系青橙客服\n400-900-7986"));
        if (total > list.size()) {
          adapter.setEndlessTargetCount(total);
          adapter.setEndlessScrollListener(this, new ProgressItem(getContext()));
        }
        if (list.size() == 0) {
          adapter.addItem(new CommonNoDataItem(R.drawable.no_renewal_history, "暂无充值记录"));
        }
      }
      List<AbstractFlexibleItem> items = new ArrayList<>();
      for (ShopOrder renewalHistory : list) {
        items.add(generateItem(renewalHistory));
      }
      adapter.onLoadMoreComplete(items, 500);
    } else {
      adapter.onLoadMoreComplete(null);
    }
  }

  public SmsChargeItem generateItem(ShopOrder renewalHistory) {
    return new SmsChargeItem(renewalHistory, getContext());
  }

  @Override public void noMoreLoad(int i) {
  }

  @Override public void onLoadMore(int i, int i1) {
    presenter.queryList();
  }
}
