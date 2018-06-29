package cn.qingchengfit.saasbase.mvvm_student.view.allot;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.databinding.PageStudentAllotStaffBinding;
import cn.qingchengfit.saasbase.mvvm_student.StudentBaseFragment;
import cn.qingchengfit.saasbase.mvvm_student.view.state.SalerStudentListView;
import cn.qingchengfit.saasbase.mvvm_student.viewmodel.allot.AllotListViewModel;
import com.anbillon.flabellum.annotations.Leaf;
import java.util.ArrayList;
import java.util.List;

@Leaf(module = "student",path = "/student/allotstaff")
public class StudentAllotStaffPage
    extends StudentBaseFragment<PageStudentAllotStaffBinding, AllotListViewModel> {
  List<SalerStudentListView> fragmentList = new ArrayList<>();

  @Override protected void subscribeUI() {
    mViewModel.getLiveItems().observe(this, items -> {
      mViewModel.isLoading.set(false);
      fragmentList.get(mViewModel.type).setItems(items);
    });
  }

  @Override
  public PageStudentAllotStaffBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageStudentAllotStaffBinding.inflate(inflater, container, false);
    initToolbar();
    initTab();
    mViewModel.loadSource(0);
    return mBinding;
  }

  private void initTab() {
    TabLayout.Tab tab = mBinding.tabBar.newTab();
    tab.setText("销售");
    mBinding.tabBar.addTab(tab);
    TabLayout.Tab tab2 = mBinding.tabBar.newTab();
    tab2.setText("教练");
    mBinding.tabBar.addTab(tab2);

    fragmentList.add(new SalerStudentListView());
    fragmentList.add(new SalerStudentListView());

    mBinding.viewpager.setAdapter(new StateViewPager(getChildFragmentManager()));
    mBinding.tabBar.setupWithViewPager(mBinding.viewpager);


    mBinding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override public void onPageSelected(int position) {
        if(fragmentList.get(position).isEmpty()){
          mViewModel.loadSource(position);
        }
      }

      @Override public void onPageScrollStateChanged(int state) {

      }
    });

  }

  private void initToolbar() {
    ToolbarModel toolbarModel = new ToolbarModel("会员分配");
    mBinding.setToolbarModel(toolbarModel);
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  class StateViewPager extends FragmentStatePagerAdapter {

    public StateViewPager(FragmentManager fm) {
      super(fm);
    }

    @Override public Fragment getItem(int position) {
      if (position < fragmentList.size()) {
        return fragmentList.get(position);
      } else {
        return new Fragment();
      }
    }

    @Override public int getItemPosition(Object object) {
      return POSITION_NONE;
    }

    @Override public int getCount() {
      return fragmentList.size();
    }
  }
}
