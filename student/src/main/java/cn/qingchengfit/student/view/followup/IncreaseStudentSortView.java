package cn.qingchengfit.student.view.followup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.saascommon.item.StudentItem;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.StViewIncreaseStudentSortBinding;
import cn.qingchengfit.student.inter.DrawerListener;
import cn.qingchengfit.student.inter.LoadDataListener;
import cn.qingchengfit.student.view.home.StudentFilterView;
import cn.qingchengfit.student.view.home.StudentListView;
import java.util.List;

public class IncreaseStudentSortView
    extends StudentBaseFragment<StViewIncreaseStudentSortBinding, IncreaseStudentSortViewModel> {
  StudentListView listView;
  StudentFilterView filterView;

  public void setListener(DrawerListener listener) {
    this.listener = listener;
  }

  DrawerListener listener;

  public void setLoadDataListener(LoadDataListener loadDataListener) {
    this.loadDataListener = loadDataListener;
  }

  LoadDataListener loadDataListener;

  @Override protected void subscribeUI() {

  }

  @Override public StViewIncreaseStudentSortBinding initDataBinding(LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    mBinding = StViewIncreaseStudentSortBinding.inflate(inflater, container, false);
    mBinding.setViewModel(mViewModel);
    initFragment();
    return mBinding;
  }

  private void initFragment() {
    listView=new StudentListView();
    stuff(R.id.fragment_list_container,listView);

  }
  public void selectAll(boolean selectedAll) {
    if (listView != null) {
      listView.selectAll(selectedAll);
    }
  }


  public static IncreaseStudentSortView newInstanceWithType(
      @StudentListView.AllotType String type) {
    IncreaseStudentSortView listView = new IncreaseStudentSortView();
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
    setData(items);
  }
  private void setData(List<? extends StudentItem> studentItems) {
    listView.setItems(studentItems);
  }
}
