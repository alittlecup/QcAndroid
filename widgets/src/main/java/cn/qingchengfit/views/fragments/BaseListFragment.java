package cn.qingchengfit.views.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

  public int left = 15, right = 15;
  public CommonNoDataItem commonNoDataItem;
  @BindView(R2.id.rv) protected RecyclerView rv;
  protected CommonFlexAdapter commonFlexAdapter;
    @BindView(R2.id.srl) SwipeRefreshLayout srl;
    List<AbstractFlexibleItem> datas = new ArrayList<>();
    private Object listeners;
  private ProgressItem progressItem;


    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commonFlexAdapter = new CommonFlexAdapter(datas);
      progressItem = new ProgressItem(getContext());
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_list, container, false);
      super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, view);
        rv.setLayoutManager(new SmoothScrollLinearLayoutManager(getContext()));
        rv.addItemDecoration(new QcLeftRightDivider(getContext(), 1, 0, left, right));
        if (datas.size() == 0 && commonNoDataItem != null) datas.add(commonNoDataItem);
        rv.setAdapter(commonFlexAdapter);
        if (listeners != null) commonFlexAdapter.addListener(listeners);
        if (listeners instanceof SwipeRefreshLayout.OnRefreshListener)
            srl.setOnRefreshListener((SwipeRefreshLayout.OnRefreshListener) listeners);
        return view;
    }

  public void initLoadMore() {
    if (commonFlexAdapter != null) {
      commonFlexAdapter.setEndlessProgressItem(progressItem);
    }
    }

    public void initListener(Object o) {
        this.listeners = o;
    }

  public void setDatas(List<AbstractFlexibleItem> ds, int page) {
        stopRefresh();
        if (rv != null && commonFlexAdapter != null) {
          if (page == 1) commonFlexAdapter.clear();
            for (AbstractFlexibleItem item : ds) {
                commonFlexAdapter.addItem(item);
            }
          if (commonFlexAdapter.getItemCount() == 0 && commonNoDataItem != null) {
            datas.add(commonNoDataItem);
          }
        }
    }

    public void localFilter(String s){
        if (commonFlexAdapter != null){
            commonFlexAdapter.setSearchText(s);
            commonFlexAdapter.filterItems(datas);
        }

    }

    public void stopRefresh(){
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
