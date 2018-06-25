package cn.qingchengfit.saasbase.mvvm_student.view.home;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.common.mvvm.SortViewModel;
import cn.qingchengfit.saasbase.databinding.ViewStudentRecyclerSortBinding;
import cn.qingchengfit.saasbase.mvvm_student.StudentBaseFragment;
import cn.qingchengfit.saasbase.mvvm_student.inter.DrawerListener;
import cn.qingchengfit.saasbase.mvvm_student.inter.LoadDataListener;
import cn.qingchengfit.saasbase.mvvm_student.viewmodel.home.StudentFilterViewModel;
import cn.qingchengfit.saasbase.student.items.StudentItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StudentRecyclerSortView
    extends StudentBaseFragment<ViewStudentRecyclerSortBinding, StudentRecyclerSortViewModel> {
  private StudentFilterView filterView;
  private StudentListView listView;
  private SortViewModel sortViewModel;
  private StudentFilterViewModel filterViewModel;

  @Override protected void subscribeUI() {
    initSortViewModel();
    sortViewModel.getFilterEvent().observe(this, aVoid -> {
      if (filterView == null) return;
      openDrawer();
      filterViewModel =
          ViewModelProviders.of(filterView, factory).get(StudentFilterViewModel.class);
      filterViewModel.getmFilterMap().observe(this, map -> {
        // REFACTOR: 2017/12/6 Map与Studentfilter的对决
        if (map != null) {
          closeDrawer();
          if (loadDataListener != null) {
            loadDataListener.loadData(map);
          }
          filterViewModel.getmFilterMap().setValue(null);
        }
      });
    });
  }

  public void selectAll(boolean selectedAll) {
    if (listView != null) {
      listView.selectAll(selectedAll);
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

  public void setDatas(List<? extends StudentItem> items) {
    setData(sortViewModel.sortItems(items));
  }

  @Override public ViewStudentRecyclerSortBinding initDataBinding(LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    mBinding = ViewStudentRecyclerSortBinding.inflate(inflater, container, false);
    initFragment();
    mBinding.includeFilter.setFilter(sortViewModel);
    if (loadDataListener != null) {
      loadDataListener.loadData(new HashMap<>());
    }
    return mBinding;
  }

  private void initSortViewModel() {
    sortViewModel = new SortViewModel();
    sortViewModel.setListener(itemss -> {
      setData(itemss);
    });
  }

  private void setData(List<? extends StudentItem> studentItems) {
    if (sortViewModel.letterChecked.get()) {
      listView.showFastScroller();
    } else {
      listView.hideFastScroller();
    }
    listView.setItems(studentItems);
    mBinding.includeFilter.setItems(new ArrayList<>(studentItems));
    // TODO: 2018/6/20 选中项
  }

  private void initFragment() {
    if (getArguments() != null && !TextUtils.isEmpty(getArguments().getString("type"))) {
      listView = StudentListView.newInstanceWithType(getArguments().getString("type"));
    } else {
      listView = new StudentListView();
    }
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
