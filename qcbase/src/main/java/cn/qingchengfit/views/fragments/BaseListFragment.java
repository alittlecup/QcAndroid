package cn.qingchengfit.views.fragments;

import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import cn.qingchengfit.items.CommonNoDataItem;
import cn.qingchengfit.items.ProgressItem;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.R;
import com.github.clans.fab.FloatingActionMenu;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
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
public abstract class BaseListFragment extends BaseFragment{

  public boolean isChild = false;
  public int left = 15, right = 15;
  public CommonNoDataItem commonNoDataItem;
  protected CommonFlexAdapter commonFlexAdapter;
  protected ProgressItem progressItem;
  protected SmoothScrollLinearLayoutManager linearLayoutManager;
  protected RecyclerView rv;
  protected SwipeRefreshLayout srl;
  protected FloatingActionMenu fab;
  protected int fabDrawable;
  protected List<AbstractFlexibleItem> datas = new ArrayList<>();
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
      srl = view.findViewById(R.id.srl);
    }
    view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickFab();
      }
    });
    rv = view.findViewById(R.id.rv);
    fab = view.findViewById(R.id.fab);
    fab.setClosedOnTouchOutside(true);

    fab.setIconAnimated(false);
    fab.setOnMenuButtonClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        onClickFab();
      }
    });
    super.onCreateView(inflater, container, savedInstanceState);

    initView(savedInstanceState);
    return view;
  }

  protected void initView(Bundle savedInstanceState) {
    if (getFbIcon() != 0){
      fab.setVisibility(View.VISIBLE);
      fab.getMenuIconView().setImageResource(getFbIcon());
    }

    linearLayoutManager = new SmoothScrollLinearLayoutManager(getContext());
    if (savedInstanceState != null && savedInstanceState.containsKey("p")) {
      linearLayoutManager.scrollToPosition(savedInstanceState.getInt("p", 0));
    }
    rv.setLayoutManager(linearLayoutManager);
    setAnimation();
    addDivider();
    rv.setAdapter(commonFlexAdapter);
    rv.setItemViewCacheSize(0);
    if (listeners != null) commonFlexAdapter.addListener(listeners);
    if (srl != null && listeners instanceof SwipeRefreshLayout.OnRefreshListener) {
      srl.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) listeners);
    }
  }

  protected void setAnimation() {
    commonFlexAdapter.setAnimationEntryStep(false)
      .setAnimationOnReverseScrolling(true)
      .setAnimationOnScrolling(true)
      .setAnimationDuration(300L)
      .setAnimationInterpolator(new LinearInterpolator());
  }

  protected void addDivider() {
    rv.addItemDecoration(
        new FlexibleItemDecoration(getContext()).withDivider(R.drawable.divider_grey_left_margin)
            .withBottomEdge(true));
  }

  public void initLoadMore() {
    if (commonFlexAdapter != null) {
      commonFlexAdapter.setEndlessProgressItem(progressItem);
    }
  }

  public void initLoadMore(int count, FlexibleAdapter.EndlessScrollListener l) {
    if (commonFlexAdapter != null) {
      commonFlexAdapter.setEndlessScrollListener(l, progressItem);
      commonFlexAdapter.setEndlessTargetCount(count);
      commonFlexAdapter.setEndlessPageSize(30);
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

  public void setDatas(List<? extends IFlexible> ds, @IntRange(from = 1) int page) {
    stopRefresh();
    if (rv != null && commonFlexAdapter != null) {
      if (page == 1) clearItems();
      commonFlexAdapter.onLoadMoreComplete(ds, 500);
      if ((ds == null || ds.size() == 0) && commonNoDataItem != null) {
        addEmptyPage();
      }

      commonFlexAdapter.notifyDataSetChanged();
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

 public void onClickFab(){};

  @Override public String getFragmentName() {
    return BaseListFragment.class.getName();
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  public abstract int getNoDataIconRes();

  public int getFbIcon(){
    return fabDrawable;
  }

  public abstract String getNoDataStr();
}
