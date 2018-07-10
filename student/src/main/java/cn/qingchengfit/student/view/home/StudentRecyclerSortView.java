package cn.qingchengfit.student.view.home;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.saascommon.item.StudentItem;
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
  private StudentFilterViewModel filterViewModel;

  @Override protected void subscribeUI() {
    initSortViewModel();
    sortViewModel.getFilterEvent().observe(this, aVoid -> {
      if (filterView == null) return;
      openDrawer();
      filterViewModel =
          ViewModelProviders.of(filterView, factory).get(StudentFilterViewModel.class);
      filterViewModel.getmFilterMap().observe(filterView, map -> {
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
    mViewModel.getStudentBeans().observe(this, items -> {
      setData(sortViewModel.sortItems(items));
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
    mViewModel.getStudentBeans().setValue(items);
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
    mBinding.includeFilter.setItems(new ArrayList<AbstractFlexibleItem>(studentItems));
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
