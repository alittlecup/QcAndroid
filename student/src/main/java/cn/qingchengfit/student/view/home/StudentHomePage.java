package cn.qingchengfit.student.view.home;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.saascommon.utils.SpanUtils;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.bean.StatData;
import cn.qingchengfit.student.databinding.StPageStudentHomeBinding;
import cn.qingchengfit.student.listener.IncreaseType;
import cn.qingchengfit.student.view.followup.IncreaseMemberPageParams;
import cn.qingchengfit.student.view.followup.IncreaseStudentPageParams;
import cn.qingchengfit.student.view.state.StudentStateInfoPageParams;
import cn.qingchengfit.utils.DialogUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/5.
 */
@Leaf(module = "student", path = "/student/home") public class StudentHomePage
    extends StudentBaseFragment<StPageStudentHomeBinding, StudentHomeViewModel> {
  @Inject IPermissionModel permissionModel;

  @Override protected void subscribeUI() {
    mViewModel.increaseFollowers.observe(this, msg -> mBinding.tvFollowMemberCount.setText(msg));
    mViewModel.increaseStudents.observe(this, msg -> mBinding.tvNewStudentCount.setText(msg));
    mViewModel.increaseMembers.observe(this, msg -> mBinding.tvNewMemberCount.setText(msg));
    mViewModel.followStudents.observe(this, msg -> mBinding.tvFollowStudentCount.setText(msg));

    mViewModel.totalMembers.observe(this, msg -> {
      SpannableStringBuilder text = new SpanUtils().append(StringUtils.formatePrice(msg))
          .append("人")
          .setFontSize(11, true)
          .create();
      mBinding.tvAllStudent.setText(text);
    });
    mViewModel.showLoading.observe(this, aBoolean -> {
      if (aBoolean) {
        showLoading();
      } else {
        hideLoading();
      }
    });
    mViewModel.glanceLiveData.observe(this,info->{
      StatData inactive_registered = info.getInactive_registered();
      if(inactive_registered!=null){
        chartViews.get(0).setStatData(inactive_registered);
      }
      StatData inactive_following = info.getInactive_following();
      if(inactive_following!=null){
        chartViews.get(1).setStatData(inactive_following);
      }
      StatData inactive_member = info.getInactive_member();
      if(inactive_member!=null){
        chartViews.get(2).setStatData(inactive_member);
      }
    });
  }


  @Override
  public StPageStudentHomeBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = StPageStudentHomeBinding.inflate(inflater, container, false);
    mBinding.setViewModel(mViewModel);
    mBinding.setLifecycleOwner(this);
    initToolbar();
    initListener();
    initTabLayout();
    mViewModel.loadSource();
    return mBinding;
  }

  private List<StudentHomePieChartView> chartViews = new ArrayList<>();

  private void initTabLayout() {
    if (chartViews.isEmpty()) {
      StudentHomePieChartView memberView = new StudentHomePieChartView();
      memberView.setBackgroundColor(R.color.st_new_member_color);

      memberView.setOnClickListener(view -> {
        routeTo("saler/student",
            new StudentStateInfoPageParams().curType(IncreaseType.INCREASE_MEMBER).build());
      });

      StudentHomePieChartView followView = new StudentHomePieChartView();
      followView.setBackgroundColor(R.color.st_follow_ing_color);

      followView.setOnClickListener(view -> {
        routeTo("saler/student",
            new StudentStateInfoPageParams().curType(IncreaseType.INCREASE_FOLLOWUP).build());
      });

      StudentHomePieChartView studentView = new StudentHomePieChartView();
      studentView.setBackgroundColor(R.color.st_new_student_color);

      studentView.setOnClickListener(view -> {
        routeTo("saler/student",
            new StudentStateInfoPageParams().curType(IncreaseType.INCREASE_STUDENT).build());
      });

      chartViews.add(memberView);
      chartViews.add(followView);
      chartViews.add(studentView);
    }
    mBinding.viewpager.setAdapter(new StateViewPager(getChildFragmentManager()));
    mBinding.viewpager.setOffscreenPageLimit(3);
    mBinding.tabLayout.setupWithViewPager(mBinding.viewpager);

    mBinding.tabLayout.setSelectedTabIndicatorColor(
        getResources().getColor(R.color.st_new_member_color));
    mBinding.tabLayout.getTabAt(0)
        .setText(new SpanUtils().append("新注册")
            .setForegroundColor(getResources().getColor(R.color.st_new_member_color))
            .create());
    mBinding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override public void onTabSelected(TabLayout.Tab tab) {
        String text = tab.getText().toString();
        int color = -1;
        switch (text) {
          case "新注册":
            color = getResources().getColor(R.color.st_new_member_color);
            break;
          case "已接洽":
            color = getResources().getColor(R.color.st_follow_ing_color);
            break;
          case "会员":
            color = getResources().getColor(R.color.st_new_student_color);
            break;
        }
        tab.setText(new SpanUtils().append(text).setForegroundColor(color).create());
        mBinding.tabLayout.setSelectedTabIndicatorColor(color);
      }

      @Override public void onTabUnselected(TabLayout.Tab tab) {
        tab.setText(new SpanUtils().append(tab.getText().toString())
            .setForegroundColor(getResources().getColor(R.color.text_grey))
            .create());
      }

      @Override public void onTabReselected(TabLayout.Tab tab) {

      }
    });

    mBinding.viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
      @Override
      public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        Log.d("TAG", "onPageScrolled: "
            + "position: "
            + position
            + "  positionOffset:  "
            + positionOffset
            + "   positionOffsetPixels: "
            + positionOffsetPixels);
      }

      @Override public void onPageSelected(int position) {
      }

      @Override public void onPageScrollStateChanged(int state) {

      }
    });
  }

  //private void initChart() {
  //  colors.add(Color.rgb(110, 184, 241));
  //  colors.add(Color.rgb(249, 148, 78));
  //  colors.add(Color.rgb(88, 184, 122));
  //  mBinding.pieChart.setOnChartValueSelectedListener(this);
  //}
  //
  //ArrayList<Integer> colors = new ArrayList<>();
  //
  //private void setData(int register, int follow, int member) {
  //  ArrayList<PieEntry> entries = new ArrayList<>();
  //  entries.add(new PieEntry(register, 0));
  //  entries.add(new PieEntry(follow, 1));
  //  entries.add(new PieEntry(member, 2));
  //  PieDataSet dataSet = new PieDataSet(entries, "");
  //  dataSet.setDrawValues(false);
  //  dataSet.setSelectionShift(3f);
  //  dataSet.setSliceSpace(0f);
  //
  //  dataSet.setColors(colors);
  //  PieData data = new PieData(dataSet);
  //  mBinding.pieChart.setData(data);
  //  mBinding.pieChart.highlightValue(null);
  //  mBinding.pieChart.invalidate();
  //}

  private void initListener() {
    mBinding.tvIncreaseMember.setOnClickListener(view -> {
      routeTo("student/increase",
          new IncreaseStudentPageParams().curType(IncreaseType.INCREASE_MEMBER).build());
    });
    mBinding.tvIncreaseStudent.setOnClickListener(view -> {
      routeTo("student/increase",
          new IncreaseStudentPageParams().curType(IncreaseType.INCREASE_STUDENT).build());
    });
    mBinding.tvIncreaseFollow.setOnClickListener(view -> {
      routeTo("increase/member",
          new IncreaseMemberPageParams().curType(IncreaseType.INCREASE_FOLLOWUP).build());
    });
    mBinding.tvIncreaseStudentFollow.setOnClickListener(view -> {
      routeTo("increase/member",
          new IncreaseMemberPageParams().curType(IncreaseType.INCREASE_STUDENT).build());
    });
    //mBinding.llMember.setOnClickListener(view -> {
    //  routeTo("saler/student",
    //      new StudentStateInfoPageParams().curType(IncreaseType.INCREASE_MEMBER).build());
    //});
    //mBinding.llFollowup.setOnClickListener(view -> {
    //  routeTo("saler/student",
    //      new StudentStateInfoPageParams().curType(IncreaseType.INCREASE_FOLLOWUP).build());
    //});
    //mBinding.llStudent.setOnClickListener(view -> {
    //  routeTo("saler/student",
    //      new StudentStateInfoPageParams().curType(IncreaseType.INCREASE_STUDENT).build());
    //});
    mBinding.commAllotStudent.setOnClickListener(view -> {

      if (permissionModel.check(PermissionServerUtils.MANAGE_MEMBERS_IS_ALL)) {
        routeTo("/student/allotstaff", null);
      } else {
        showAlert(R.string.sorry_for_no_permission);
      }
    });
    mBinding.commAttendStudent.setOnClickListener(view -> {
      routeTo("/attendance/page", null);
    });
    mBinding.commTransferStudent.setOnClickListener(view -> {
      routeTo("/transfer/student", null);
      //routeTo("/select_member/", null);
    });
    mBinding.commSendMsg.setOnClickListener(view -> {
      routeTo("/student/sendmsg/", null);
    });
    mBinding.commExportImport.setOnClickListener(view -> {
      routeTo("/student/export", null);
      //routeTo("/student/follow_record", null);
    });
    mBinding.tvAllStudent.setOnClickListener(view -> {
      routeTo("/student/all", null);
    });

    mBinding.commDayStudent.setOnClickListener(view -> {
      routeTo("student/birthday", null);
    });
  }

  private void initToolbar() {
    ToolbarModel toolbarModel = new ToolbarModel("会员");
    mBinding.setToolbarModel(toolbarModel);
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  class StateViewPager extends FragmentStatePagerAdapter {

    public StateViewPager(FragmentManager fm) {
      super(fm);
    }

    @Override public Fragment getItem(int position) {
      if (position < chartViews.size()) {
        return chartViews.get(position);
      } else {
        return new Fragment();
      }
    }

    @Nullable @Override public CharSequence getPageTitle(int position) {
      switch (position) {
        case 0:
          return "新注册";
        case 1:
          return "已接洽";
        case 2:
          return "会员";
      }
      return "";
    }

    @Override public int getItemPosition(Object object) {
      return POSITION_NONE;
    }

    @Override public int getCount() {
      return chartViews.size();
    }
  }
}
