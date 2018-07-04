package cn.qingchengfit.saasbase.mvvm_student.view.home;

import android.arch.lifecycle.ViewModelProviders;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.items.StickerDateItem;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.PageStudentHomeBinding;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.saasbase.mvvm_student.viewmodel.home.StudentFilterViewModel;
import cn.qingchengfit.saasbase.mvvm_student.viewmodel.home.StudentHomeViewModel;
import cn.qingchengfit.saasbase.student.items.StudentItem;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by huangbaole on 2017/12/5.
 */
@Leaf(module = "student", path = "/home/student") public class StudentHomePage
    extends SaasBindingFragment<PageStudentHomeBinding, StudentHomeViewModel>
    implements FlexibleAdapter.OnItemClickListener {

  CommonFlexAdapter adapter;
  StudentFilterViewModel filterViewModel;
  StudentFilterView filterView;

  @Override protected void subscribeUI() {
    mViewModel.getLiveItems().observe(this, studentItems -> {
      mViewModel.isLoading.set(false);
      if (studentItems == null || studentItems.isEmpty()) return;
      mViewModel.items.set(mViewModel.getSortViewModel().sortItems(studentItems));
      mBinding.includeFilter.setItems(new ArrayList<>(studentItems));
    });
    mViewModel.getSortViewModel().getFilterEvent().observe(this, aVoid -> {
      openDrawer();
      filterViewModel =
          ViewModelProviders.of(filterView, factory).get(StudentFilterViewModel.class);
      filterViewModel.getmFilterMap().observe(this, map -> {
        // REFACTOR: 2017/12/6 Map与Studentfilter的对决
        if (map != null) {
          closeDrawer();
          mViewModel.loadSource(map);
          filterViewModel.getmFilterMap().setValue(null);
        }
      });
    });
  }

  @Override
  public PageStudentHomeBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageStudentHomeBinding.inflate(inflater, container, false);
    initToolbar();
    initFragment();
    initRecyclerView();

    mBinding.setViewModel(mViewModel);
    mBinding.includeFilter.setFilter(mViewModel.getSortViewModel());

    adapter.setFastScroller(mBinding.fastScroller);
    mViewModel.loadSource(new HashMap<>());
    return mBinding;
  }

  private void initRecyclerView() {
    mBinding.recyclerview.setAdapter(adapter = new CommonFlexAdapter(new ArrayList(),this));
    initFastScroller();
    mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
  }

  private void initFastScroller() {
    mBinding.fastScroller.setBarClickListener(letter -> {
      List<StudentItem> itemList = mViewModel.items.get();
      int position = 0;
      for (int i = 0; i < itemList.size(); i++) {
        if (itemList.get(i).getHeader() != null) {
          if (itemList.get(i).getHeader() instanceof StickerDateItem) {
            if (((StickerDateItem) itemList.get(i).getHeader()).getDate()
                .equalsIgnoreCase(letter)) {
              position = i;
            }
          }
        }
      }
      return position;
    });
  }

  private void openDrawer() {
    mBinding.drawer.openDrawer(GravityCompat.END);
  }

  private void closeDrawer() {
    mBinding.drawer.closeDrawer(GravityCompat.END);
  }

  private void initFragment() {
    stuff(R.id.frame_student_operation, new StudentOperationView());
    filterView = new StudentFilterView();
    stuff(R.id.frame_student_filter, filterView);
  }

  private void initToolbar() {
    ToolbarModel toolbarModel = new ToolbarModel("会员");
    toolbarModel.setMenu(cn.qingchengfit.saasbase.R.menu.menu_search);
    toolbarModel.setListener(item -> {
      ToastUtils.show("click");
      return true;
    });
    mBinding.setToolbarModel(toolbarModel);
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  @Override public boolean onItemClick(int position) {
    routeTo(Uri.parse("qcstaff://student/common/select_member"),null);
    return false;
  }
}
