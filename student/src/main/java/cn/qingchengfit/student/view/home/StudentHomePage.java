package cn.qingchengfit.student.view.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.StPageStudentHomeBinding;
import cn.qingchengfit.student.listener.IncreaseType;
import cn.qingchengfit.student.view.followup.IncreaseMemberPageParams;
import cn.qingchengfit.student.view.followup.IncreaseStudentPageParams;
import cn.qingchengfit.student.view.state.StudentStateInfoPageParams;
import com.anbillon.flabellum.annotations.Leaf;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import java.util.ArrayList;

/**
 * Created by huangbaole on 2017/12/5.
 */
@Leaf(module = "student", path = "/student/home") public class StudentHomePage
    extends StudentBaseFragment<StPageStudentHomeBinding, StudentHomeViewModel>
    implements OnChartValueSelectedListener {
  @Override protected void subscribeUI() {
    mViewModel.increaseFollowers.observe(this,msg->mBinding.tvIncreaseFollow.setCount(msg));
    mViewModel.increaseStudents.observe(this,msg->mBinding.tvIncreaseStudent.setCount(msg));
    mViewModel.increaseMembers.observe(this,msg->mBinding.tvIncreaseMember.setCount(msg));
    mViewModel.followStudents.observe(this,msg->mBinding.tvIncreaseStudentFollow.setCount(msg));
  }

  @Override
  public StPageStudentHomeBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = StPageStudentHomeBinding.inflate(inflater, container, false);
    mBinding.setViewModel(mViewModel);
    mBinding.setLifecycleOwner(this);
    initToolbar();
    initListener();
    initChart();
    mViewModel.loadSource();
    return mBinding;
  }

  private void initChart() {
    mBinding.pieChart.setOnChartValueSelectedListener(this);
    setData();
  }

  private void setData() {
    ArrayList<PieEntry> entries = new ArrayList<>();
    entries.add(new PieEntry(33));
    entries.add(new PieEntry(33));
    entries.add(new PieEntry(33));
    PieDataSet dataSet = new PieDataSet(entries, "");
    dataSet.setDrawValues(false);
    dataSet.setSelectionShift(3f);
    dataSet.setSliceSpace(0f);
    ArrayList<Integer> colors = new ArrayList<>();
    colors.add(Color.rgb(110, 184, 241));
    colors.add(Color.rgb(249, 148, 78));
    colors.add(Color.rgb(88, 184, 122));
    dataSet.setColors(colors);
    PieData data = new PieData(dataSet);
    mBinding.pieChart.setData(data);
    mBinding.pieChart.highlightValue(null);
    mBinding.pieChart.invalidate();
  }

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
    mBinding.llMember.setOnClickListener(view -> {
      routeTo("saler/student",
          new StudentStateInfoPageParams().curType(IncreaseType.INCREASE_MEMBER).build());
    });
    mBinding.llFollowup.setOnClickListener(view -> {
      routeTo("saler/student",
          new StudentStateInfoPageParams().curType(IncreaseType.INCREASE_FOLLOWUP).build());
    });
    mBinding.llStudent.setOnClickListener(view -> {
      routeTo("saler/student",
          new StudentStateInfoPageParams().curType(IncreaseType.INCREASE_STUDENT).build());
    });
    mBinding.commAllotStudent.setOnClickListener(view -> {
      routeTo("/student/allotstaff", null);
    });
    mBinding.commAttendStudent.setOnClickListener(view -> {
      routeTo("attendance/page", null);
    });
    mBinding.commTransferStudent.setOnClickListener(view -> {
      routeTo("/student/follow_record", null);
    });
    mBinding.commSendMsg.setOnClickListener(view -> {
      // TODO: 2018/6/19 这里位于staff
      //if (!serPermisAction.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE)) {
      //  showAlert(R.string.alert_permission_forbid);
      //  return false;
      //}
      routeTo("/student/sendmsg/", null);
    });
    mBinding.commExportImport.setOnClickListener(view -> {
      routeTo("/student/export", null);
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

  @Override public void onValueSelected(Entry e, Highlight h) {

  }

  @Override public void onNothingSelected() {

  }
}
