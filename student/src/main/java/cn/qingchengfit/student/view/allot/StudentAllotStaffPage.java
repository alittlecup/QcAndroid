package cn.qingchengfit.student.view.allot;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.databinding.PageStudentAllotStaffBinding;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.item.AllotStaffItem;
import cn.qingchengfit.student.view.state.SalerStudentListView;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.List;

@Leaf(module = "student", path = "/student/allotstaff") public class StudentAllotStaffPage
    extends StudentBaseFragment<PageStudentAllotStaffBinding, AllotListViewModel>
    implements FlexibleAdapter.OnItemClickListener {
  List<SalerStudentListView> fragmentList = new ArrayList<>();
  private List<AllotStaffItem> items;

  @Override protected void subscribeUI() {
    mViewModel.getLiveItems().observe(this, items -> {
      mViewModel.isLoading.set(false);
      this.items = items;
      fragmentList.get(mViewModel.type).setItems(items);
    });
    mViewModel.loading.observe(this,showLoading->{
      if(showLoading){
        showLoading();
      }else{
        hideLoading();
      }
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
    if (fragmentList.isEmpty()) {
      fragmentList.add(new SalerStudentListView());
      fragmentList.add(new SalerStudentListView());
      fragmentList.get(0).setOnItemClickListener(this);
      fragmentList.get(1).setOnItemClickListener(this);
    }
    mBinding.viewpager.setAdapter(new StateViewPager(getChildFragmentManager()));

    mBinding.tabBar.setupWithViewPager(mBinding.viewpager);

    mBinding.tabBar.setTabTextColors(getResources().getColor(R.color.text_grey),
        getResources().getColor(R.color.colorPrimary));

    mBinding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

      }

      @Override public void onPageSelected(int position) {
        if (fragmentList.get(position).isEmpty()) {
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

  @Override public boolean onItemClick(int position) {
    routeTo("student/seller/student",
        new SalerStudentsPageParams().staff(items.get(position).data.getSeller())
            .type(mBinding.viewpager.getCurrentItem())
            .build());
    return false;
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

    @Nullable @Override public CharSequence getPageTitle(int position) {
      switch (position) {
        case 0:
          return "销售";
        case 1:
          return "教练";
      }
      return "";
    }

    @Override public int getItemPosition(Object object) {
      return POSITION_NONE;
    }

    @Override public int getCount() {
      return fragmentList.size();
    }
  }
}
