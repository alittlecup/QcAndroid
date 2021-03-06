package cn.qingchengfit.student.view.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Checkable;
import cn.qingchengfit.saascommon.item.IItemData;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.ViewStudentRecyclerSortBinding;
import cn.qingchengfit.student.listener.DrawerListener;
import cn.qingchengfit.student.listener.LoadDataListener;
import cn.qingchengfit.student.viewmodel.SortViewModel;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StudentRecyclerSortView
    extends StudentBaseFragment<ViewStudentRecyclerSortBinding, StudentRecyclerSortViewModel> {
  private StudentFilterView filterView;
  private StudentListView listView;
  private SortViewModel sortViewModel;

  @Override protected void subscribeUI() {
    initSortViewModel();
    sortViewModel.getFilterEvent().observe(this, aVoid -> {
      if (filterView == null) return;
      openDrawer();
    });
    mViewModel.getStudentBeans().observe(this, items -> {
      if (items == null || items.isEmpty()) {
        listView.setItems(new ArrayList<>());
        return;
      }
      AbstractFlexibleItem abstractFlexibleItem = items.get(0);
      if (abstractFlexibleItem instanceof IItemData) {
        List data = new ArrayList<>(items);
        setData(new ArrayList<>(sortViewModel.sortItems(data)));
      }
    });
  }

  public void selectAll(boolean selectedAll,Checkable checkable) {
    if (listView != null) {
      listView.selectAll(selectedAll,checkable);
    }
  }

  boolean sortFilterVisible = true;

  public void setSortFilterVisible(boolean visible) {
    sortFilterVisible = visible;
    if (mBinding != null) {
      mBinding.includeFilter.getRoot().setVisibility(visible ? View.VISIBLE : View.GONE);
    }
  }

  public static StudentRecyclerSortView newInstanceWithType(
      @StudentListView.AllotType String type) {
    StudentRecyclerSortView listView = new StudentRecyclerSortView();
    Bundle bundle = new Bundle();
    bundle.putString("type", type);
    listView.setArguments(bundle);
    return listView;
  }

  public StudentListView getListView() {
    return listView;
  }

  public void setFilterView(StudentFilterView filterView) {
    this.filterView = filterView;
  }

  private void openDrawer() {
    if (listener != null) {
      listener.openDrawer();
    }
  }

  private void closeDrawer() {
    if (listener != null) {
      listener.closeDrawer();
    }
  }

  public void filter(String text) {
    listView.filter(text);
  }

  private String curID;

  public void setCurId(String curID) {
    this.curID = curID;
    if (listView != null) {
      listView.setCurId(curID);
    }
  }

  private List<? extends AbstractFlexibleItem> items;

  public void setDatas(List<? extends AbstractFlexibleItem> items) {
    if (mViewModel != null) {
      mViewModel.getStudentBeans().setValue(items);
    } else {
      this.items = items;
    }
  }

  @Override public ViewStudentRecyclerSortBinding initDataBinding(LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    mBinding = ViewStudentRecyclerSortBinding.inflate(inflater, container, false);
    initFragment();
    mBinding.includeFilter.setFilter(sortViewModel);
    mBinding.includeFilter.getRoot().setVisibility(sortFilterVisible ? View.VISIBLE : View.GONE);
    if (mViewModel != null && items != null) {
      mViewModel.getStudentBeans().setValue(items);
    }
    return mBinding;
  }

  //筛选项变化
  public void notifyFilterDot(boolean isChecked){
    mBinding.includeFilter.tvSortFilter.setChecked(isChecked);
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    if (loadDataListener != null) {
      loadDataListener.loadData(new HashMap<>());
      listView.setOnRefreshListenr(() -> loadDataListener.loadData(new HashMap<>()));
    }
  }

  private void initSortViewModel() {
    sortViewModel = new SortViewModel();
    sortViewModel.setListener(itemss -> {
      setData(new ArrayList(itemss));
    });
  }

  private void setData(List<? extends AbstractFlexibleItem> studentItems) {
    if (sortViewModel.letterChecked.get()) {
      listView.showFastScroller();
    } else {
      listView.hideFastScroller();
    }
    listView.setItems(studentItems);
    mBinding.includeFilter.setItems(new ArrayList<AbstractFlexibleItem>(studentItems));
  }

  private void initFragment() {
    if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("type"))) {
      listView = StudentListView.newInstanceWithType(getArguments().getString("type"));
    } else {
      listView = new StudentListView();
    }
    listView.setCurId(curID);
    stuff(R.id.fragment_recycler_container, listView);
  }

  public void setListener(DrawerListener listener) {
    this.listener = listener;
  }

  DrawerListener listener;

  public void setLoadDataListener(LoadDataListener loadDataListener) {
    this.loadDataListener = loadDataListener;
  }

  LoadDataListener loadDataListener;
}
