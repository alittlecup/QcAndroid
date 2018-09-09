package cn.qingchengfit.student.view.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.ViewStudentFilterBinding;
import cn.qingchengfit.student.listener.onRightFilterCloseListener;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * ;
 * Created by huangbaole on 2017/12/6.
 */

public class StudentFilterView
    extends StudentBaseFragment<ViewStudentFilterBinding, StudentFilterViewModel>
{
  CommonFlexAdapter adapter;

  @Override protected void subscribeUI() {
    mViewModel.setFilterTimeVisible(filterTimeVisible);
    mViewModel.setFilterStatusVisible(filterStatusIds);
    mViewModel.showAll(showAll);
    mViewModel.getRemoteFilters().observe(this, filterModels -> {
      mViewModel.items.set(mViewModel.getItems(filterModels));
      mBinding.recyclerBillFilter.post(new Runnable() {
        @Override public void run() {
          mBinding.recyclerBillFilter.scrollToPosition(0);
          mBinding.recyclerBillFilter.smoothScrollToPosition(0);
        }
      });
    });
    mViewModel.getmResetEvent().observe(this, aVoid -> {
      showDialog();
    });
    mViewModel.getmFilterMap().observe(this, params -> {
      if (listener != null) {
        listener.onConfirm(new HashMap<>(params));
      }
    });
    mViewModel.loadfilterModel();
  }

  private void showDialog() {
    new MaterialDialog.Builder(getContext()).content("确认重置所有筛选项么？")
        .positiveText(R.string.common_comfirm)
        .negativeText(R.string.common_cancel)
        .autoDismiss(true)
        .onPositive(new MaterialDialog.SingleButtonCallback() {
          @Override
          public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
            doReset();
            mViewModel.filterMap.clear();
            mViewModel.getmFilterMap().setValue(new HashMap<>());
          }
        })
        .show();
  }

  private boolean filterTimeVisible = true;

  public void setFilterTimeVisible(boolean visible) {
    filterTimeVisible = visible;
    if (mViewModel != null) {
      mViewModel.setFilterTimeVisible(visible);
    }
  }

  private boolean showAll = false;

  public void setFilterShowAll(boolean showAll) {
    this.showAll = showAll;
    if (mViewModel != null) {
      mViewModel.showAll(showAll);
    }
  }

  private boolean filterStatusIds = true;

  public void setFilterStatusIds(boolean visible) {
    filterStatusIds = visible;
    if (mViewModel != null) {
      mViewModel.setFilterStatusVisible(filterStatusIds);
    }
  }

  public void setListener(onRightFilterCloseListener listener) {
    this.listener = listener;
  }

  private onRightFilterCloseListener listener;

  private void doReset() {
    for (int i = 0; i < adapter.getItemCount(); i++) {
      adapter.addSelection(i);
    }
    adapter.notifyDataSetChanged();
  }

  @Override
  public ViewStudentFilterBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = ViewStudentFilterBinding.inflate(inflater, container, false);
    initRecyclerView();
    mBinding.setViewModel(mViewModel);
    mBinding.setLifecycleOwner(this);
    return mBinding;
  }

  private void initRecyclerView() {
    mBinding.recyclerBillFilter.setAdapter(adapter = new CommonFlexAdapter(new ArrayList(),this));
    mBinding.recyclerBillFilter.setLayoutManager(new LinearLayoutManager(getContext()));
    mBinding.recyclerBillFilter.addItemDecoration(
        new FlexibleItemDecoration(getContext()).withDivider(R.drawable.divider_grey_left_margin)
            .withBottomEdge(true));
    mBinding.recyclerBillFilter.setFocusable(false);
  }

}
