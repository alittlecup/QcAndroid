package cn.qingchengfit.student.view.home;

import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.utils.SpanUtils;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.Utils;
import cn.qingchengfit.student.bean.InactiveBean;
import cn.qingchengfit.student.bean.StatData;
import cn.qingchengfit.student.databinding.StViewStudentHomePiechartBinding;
import cn.qingchengfit.student.widget.CountDateView;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import java.util.ArrayList;
import java.util.List;

public class StudentHomePieChartView
    extends StudentBaseFragment<StViewStudentHomePiechartBinding, StudentHomePieChartViewModel> {

  @Override protected void subscribeUI() {
    mViewModel.pieData.observe(this, statData -> {
      if (statData == null) return;
      boolean isNoData;
      if(statData.getTotal_count()==0){
        isNoData=true;
      }else{
        isNoData=false;
      }
      List<InactiveBean> stat_data = statData.getStat_data();
      ArrayList<PieEntry> entries = new ArrayList<>();
      for (int i = 0; i < stat_data.size(); i++) {
        InactiveBean inactiveBean = stat_data.get(i);
        View childAt = mBinding.llCountDate.getChildAt(i * 2);
        if (childAt instanceof CountDateView) {
          ((CountDateView) childAt).setContent(inactiveBean.getPeriod());
          ((CountDateView) childAt).setCount(inactiveBean.getCount());
        }
        entries.add(new PieEntry(isNoData?1:inactiveBean.getCount(), i));
      }
      PieDataSet dataSet = new PieDataSet(entries, "");
      dataSet.setDrawValues(false);
      dataSet.setSelectionShift(3f);
      dataSet.setSliceSpace(0f);
      dataSet.setColors(colors);
      PieData data = new PieData(dataSet);
      SpannableStringBuilder text = new SpanUtils().append(statData.getTotal_count() + " ")
          .setFontSize(72, true)
          .append("人")
          .setFontSize(30, true)
          .append("\n" + commonText)
          .setFontSize(36, true)
          .setForegroundColor(getResources().getColor(R.color.text_grey))
          .create();
      mBinding.pieChart.setCenterText(text);
      mBinding.pieChart.setData(data);
      mBinding.pieChart.highlightValue(null);
      mBinding.pieChart.invalidate();
    });
    mViewModel.backgroundColor.observe(this, color -> {
      initColors(color);
    });
  }

  @Override public StViewStudentHomePiechartBinding initDataBinding(LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    mBinding = StViewStudentHomePiechartBinding.inflate(inflater, container, false);
    mBinding.setViewModel(mViewModel);
    mBinding.setLifecycleOwner(this);
    initPieChart();
    mViewModel.showDivider.setValue(showItemDividers);
    mViewModel.backgroundColor.setValue(getResources().getColor(color));
    mBinding.pieChart.setCenterTextColor(getResources().getColor(R.color.text_black));
    mViewModel.pieData.setValue(statData);
    mBinding.pieChart.setHighlightPerTapEnabled(false);
    mBinding.pieChart.setClickable(false);
    initListener();

    commonText = "未跟进";
    mBinding.tvDesc.setText("未跟进天数");
    return mBinding;
  }

  private void initListener() {
    for (int i = 0; i < mBinding.llCountDate.getChildCount(); i++) {
      View childAt = mBinding.llCountDate.getChildAt(i);
      if (childAt instanceof CountDateView) {
        childAt.setClickable(false);
      }
    }
    mBinding.shadowClick.setOnClickListener(v -> {
      if (listener != null) {
        listener.onClick(v);
      }
    });
  }

  private void initPieChart() {
    mBinding.pieChart.setHoleRadius(64f);
  }

  private boolean showItemDividers = false;
  private String commonText = "未跟进";

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

  List<Integer> colors = new ArrayList<>();

  private void initColors(int baseColor) {
    colors.add(Utils.getColorWithAlpha(0.2f, baseColor));
    colors.add(Utils.getColorWithAlpha(0.4f, baseColor));
    colors.add(Utils.getColorWithAlpha(0.6f, baseColor));
    colors.add(Utils.getColorWithAlpha(1f, baseColor));
  }

  private StatData statData;

  public void setStatData(StatData statData) {
    if (mViewModel != null) {
      mViewModel.pieData.setValue(statData);
    }
    this.statData = statData;
  }

  private int color = R.color.st_new_student_color;

  public void showItemDividers(boolean show) {
    showItemDividers = show;
    if (mViewModel != null) {
      mViewModel.showDivider.setValue(show);
    }
  }
}
