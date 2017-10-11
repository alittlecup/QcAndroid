package cn.qingchengfit.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.items.ProgressItem;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.QcLeftRightDivider;
import cn.qingchengfit.widgets.R;
import cn.qingchengfit.widgets.R2;
import eu.davidea.flexibleadapter.common.SmoothScrollLinearLayoutManager;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.List;

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
 * Created by Paper on 2017/5/26.
 */
public abstract class BaseListFragment extends BaseFragment {

  public boolean isChild = false;
  public int left = 15, right = 15;
  public CommonNoDataItem commonNoDataItem;
  @BindView(R2.id.rv) protected RecyclerView rv;
  protected CommonFlexAdapter commonFlexAdapter;
  protected ProgressItem progressItem;
  protected SmoothScrollLinearLayoutManager linearLayoutManager;
  @Nullable @BindView(R2.id.srl) protected SwipeRefreshLayout srl;
  List<AbstractFlexibleItem> datas = new ArrayList<>();
  private Object listeners;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    commonFlexAdapter = new CommonFlexAdapter(datas);
    progressItem = new ProgressItem(getContext());
    commonNoDataItem = new CommonNoDataItem(getNoDataIconRes(), getNoDataStr());
  }

  @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    View view = null;
    if (isChild) {
      view = inflater.inflate(R.layout.fragment_base_list_nosrl, container, false);
    } else {
      view = inflater.inflate(R.layout.fragment_base_list, container, false);
    }
    super.onCreateView(inflater, container, savedInstanceState);
    unbinder = ButterKnife.bind(this, view);
    initView(savedInstanceState);
    return view;
  }

  protected void initView(Bundle savedInstanceState) {
    linearLayoutManager = new SmoothScrollLinearLayoutManager(getContext());
    if (savedInstanceState != null && savedInstanceState.containsKey("p")){
      linearLayoutManager.scrollToPosition(savedInstanceState.getInt("p",0));
    }
    rv.setLayoutManager(linearLayoutManager);
    setAnimation();
    addDivider();
    rv.setAdapter(commonFlexAdapter);
    if (listeners != null) commonFlexAdapter.addListener(listeners);
    if (srl != null && listeners instanceof SwipeRefreshLayout.OnRefreshListener) {
      srl.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) listeners);
    }

  }

  protected void setAnimation() {
    commonFlexAdapter.setAnimationEntryStep(true)
        .setAnimationOnReverseScrolling(true)
    .setAnimationOnScrolling(true)
    .setAnimationDuration(300)
    .setAnimationInterpolator(new DecelerateInterpolator());
  }

  protected void addDivider() {
    rv.addItemDecoration(new QcLeftRightDivider(getContext(), 1, 0, left, right));
  }

  public void initLoadMore() {
    if (commonFlexAdapter != null) {
      commonFlexAdapter.setEndlessProgressItem(progressItem);
    }
  }

  @Override public void onSaveInstanceState(Bundle outState) {
    outState.putInt("p", linearLayoutManager.findFirstVisibleItemPosition());
    super.onSaveInstanceState(outState);
  }

  public void initListener(Object o) {
    this.listeners = o;
    if (commonFlexAdapter != null) commonFlexAdapter.addListener(listeners);
    if (srl != null && o instanceof SwipeRefreshLayout.OnRefreshListener) {
      srl.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) o);
    }
  }

  protected void clearItems() {
    commonFlexAdapter.clear();
  }
  public void setDatas(List<? extends AbstractFlexibleItem> ds, int page) {
    stopRefresh();
    if (rv != null && commonFlexAdapter != null) {
      if (page == 1) clearItems();
      for (AbstractFlexibleItem item : ds) {
        commonFlexAdapter.addItem(item);
      }
      if (commonFlexAdapter.getItemCount() == 0 && commonNoDataItem != null) {
        addEmptyPage();
      }
    }
  }

  public void addEmptyPage() {
    commonFlexAdapter.addItem(commonNoDataItem);
  }

  public void localFilter(String s) {
    if (commonFlexAdapter != null) {
      commonFlexAdapter.setSearchText(s);
      commonFlexAdapter.filterItems(datas);
    }
  }

  public void stopRefresh() {
    if (srl != null) srl.setRefreshing(false);
  }

  public IFlexible getItem(int position) {
    if (commonFlexAdapter != null && commonFlexAdapter.getItemCount() > position) {
      return commonFlexAdapter.getItem(position);
    } else {
      return null;
    }
  }

  public void stopLoadMore() {
    if (commonFlexAdapter == null) return;
    commonFlexAdapter.removeItem(commonFlexAdapter.getGlobalPositionOf(progressItem));
  }

  @Override public String getFragmentName() {
    return BaseListFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  public abstract int getNoDataIconRes();

  public abstract String getNoDataStr();
}
