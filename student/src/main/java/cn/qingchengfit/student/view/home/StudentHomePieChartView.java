package cn.qingchengfit.student.view.home;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.StViewStudentHomePiechartBinding;
import com.github.mikephil.charting.data.PieData;

public class StudentHomePieChartView
    extends StudentBaseFragment<StViewStudentHomePiechartBinding, StudentHomePieChartViewModel> {

  @Override protected void subscribeUI() {
    mViewModel.pieData.observe(this, pieData -> {
      mBinding.pieChart.setData(pieData);
      mBinding.pieChart.highlightValue(null);
      mBinding.pieChart.invalidate();
    });
  }

  @Override public StViewStudentHomePiechartBinding initDataBinding(LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    mBinding = StViewStudentHomePiechartBinding.inflate(inflater, container, false);
    mBinding.setViewModel(mViewModel);
    mBinding.setLifecycleOwner(this);
    mViewModel.showDivider.setValue(showItemDividers);
    mViewModel.backgroundColor.setValue(getResources().getColor(color));
    mViewModel.pieData.setValue(pieData);
    if (listener != null) {
      mBinding.getRoot().setOnClickListener(listener);
    }
    return mBinding;
  }

  private boolean showItemDividers = false;

  public void setBackgroundColor(@ColorRes int color) {
    if (mViewModel != null) {
      mViewModel.backgroundColor.setValue(getResources().getColor(color));
    }
    this.color = color;
  }

  private View.OnClickListener listener;

  public void setOnClickListener(View.OnClickListener listener) {
    if (mBinding != null) {
      mBinding.getRoot().setOnClickListener(listener);
    } else {
      this.listener = listener;
    }
  }

  private PieData pieData;

  public void setPieChart(PieData pieChartData) {
    if (mViewModel != null) {
      mViewModel.pieData.setValue(pieChartData);
    }
    this.pieData = pieChartData;
  }

  private int color = R.color.st_new_student_color;

  public void showItemDividers(boolean show) {
    showItemDividers = show;
    if (mViewModel != null) {
      mViewModel.showDivider.setValue(show);
    }
  }
}
