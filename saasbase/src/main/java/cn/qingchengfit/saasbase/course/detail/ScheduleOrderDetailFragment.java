package cn.qingchengfit.saasbase.course.detail;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.FragmentScheduleOrderDetailBinding;
import cn.qingchengfit.saasbase.routers.SaasbaseParamsInjector;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.utils.BundleBuilder;
import cn.qingchengfit.views.fragments.TitleFragment;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

@Leaf(module = "course", path = "/schedule/order/detail") public class ScheduleOrderDetailFragment
    extends SaasCommonFragment {
  List<Fragment> fragmentList = new ArrayList<>();
  FragmentScheduleOrderDetailBinding mBinding;
  @Need ScheduleDetail schedule;
  @Need ScheduleOrders orders;
  @Inject ViewModelProvider.Factory factory;
  ScheduleDetailVM mViewModel;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding = FragmentScheduleOrderDetailBinding.inflate(inflater, container, false);
    mViewModel = ViewModelProviders.of(this, factory).get(ScheduleDetailVM.class);
    initViewPager();
    if (schedule.isTrainerClass()) {
      mBinding.tabBar.setVisibility(View.GONE);
    } else {
      initTab();
      mViewModel.orderDetailFirstTab.observe(this, first -> {
        mBinding.tabBar.getTabAt(0).setText(first);
      });
      mViewModel.orderDetailSecondTab.observe(this, second -> {
        mBinding.tabBar.getTabAt(1).setText(second);
      });
    }
    initToolbar();

    return mBinding.getRoot();
  }

  private void initViewPager() {
    if (fragmentList.isEmpty()) {
      ScheduleOrdersFragment scheduleOrdersFragment = new ScheduleOrdersFragment();
      scheduleOrdersFragment.setArguments(
          new BundleBuilder().withString("scheduleID", String.valueOf(schedule.getId()))
              .withParcelable("orders", orders)
              .build());
      fragmentList.add(scheduleOrdersFragment);
      if (!schedule.isTrainerClass()) {
        ScheduleCandidateFragment scheduleCandidateFragment = new ScheduleCandidateFragment();
        scheduleCandidateFragment.setArguments(
            new BundleBuilder().withString("scheduleID", String.valueOf(schedule.getId())).build());
        fragmentList.add(scheduleCandidateFragment);
      }
    }
    mBinding.viewpager.setAdapter(new StateViewPager(getChildFragmentManager()));
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SaasbaseParamsInjector.inject(this);
  }

  private void initTab() {
    mBinding.tabBar.setupWithViewPager(mBinding.viewpager);
    mBinding.tabBar.setTabTextColors(getResources().getColor(R.color.text_grey),
        getResources().getColor(R.color.colorPrimary));
  }

  private void initToolbar() {
    ToolbarModel toolbarModel = new ToolbarModel("预约详情");
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

    @Nullable @Override public CharSequence getPageTitle(int position) {
      if (position == 0) {
        return "约课人数";
      } else if (position == 1) {
        return "空位订阅";
      }
      return super.getPageTitle(position);
    }

    @Override public int getItemPosition(Object object) {
      return POSITION_NONE;
    }

    @Override public int getCount() {
      return fragmentList.size();
    }
  }
}
