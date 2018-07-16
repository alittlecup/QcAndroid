package cn.qingchengfit.student.view.followup;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import cn.qingchengfit.saascommon.widget.LineCharDate;
import cn.qingchengfit.student.bean.StatDate;
import cn.qingchengfit.student.databinding.ViewIncreaseStudentTopBinding;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.listener.DateGroupDimension;
import cn.qingchengfit.student.listener.IncreaseType;

public class IncreaseStudentTopView
    extends StudentBaseFragment<ViewIncreaseStudentTopBinding, IncreaseStudentTopViewModel> {
  @IncreaseType String type = IncreaseType.INCREASE_MEMBER;

  @Override protected void subscribeUI() {
    mViewModel.showLoading.observe(this,aBoolean -> {
      if(aBoolean){
        showLoading();
      }else{
        hideLoading();
      }
    });
  }

  @Override
  public ViewIncreaseStudentTopBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = ViewIncreaseStudentTopBinding.inflate(inflater, container, false);
    mBinding.setViewModel(mViewModel);
    mBinding.setLifecycleOwner(this);
    initListener();
    if (type.equals(IncreaseType.INCREASE_MEMBER)) {
      mBinding.radioGroup.setBackgroundResource(R.drawable.radiogroup_blue_bg);
    } else if (type.equals(IncreaseType.INCREASE_STUDENT)) {
      mBinding.radioGroup.setBackgroundResource(R.drawable.radiogroup_green_bg);
    }
    mViewModel.setType(type);
    mViewModel.setDateDimension(DateGroupDimension.DAY);
    mBinding.radioRight.setChecked(true);
    initLineChart();
    return mBinding;
  }

  private void initLineChart() {
    mBinding.lineChart.hideBottomDate();
    mBinding.lineChart.hideMarkDate();
  }

  private void initListener() {
    mBinding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.radio_left) {
          mViewModel.setDateDimension(DateGroupDimension.MONTH);
        } else if (checkedId == R.id.radio_mid) {
          mViewModel.setDateDimension(DateGroupDimension.WEEK);
        } else if (checkedId == R.id.radio_right) {
          mViewModel.setDateDimension(DateGroupDimension.DAY);
        }
      }
    });
    mBinding.lineChart.setLineCharListener(new LineCharDate.onLineCharListener() {
      @Override public void onChartTranslate(int valuePos,int count) {
        int curpos = valuePos - 3 >= 0 ? valuePos - 3 : 0;
        Log.d("TAG", "onChartTranslate: "+curpos);
        if(curpos==0){
          mViewModel.loadMore();
        }
        mViewModel.upDateTvContent(curpos,count);
      }

      @Override public void onChartClick(int clickPos, int count) {
        int curpos = clickPos - 3 >= 0 ? clickPos - 3 : 0;
        Log.d("TAG", "onChartClick: "+curpos);
        mViewModel.updateSelectPos(curpos,count);
      }
    });
  }

  public void setType(@IncreaseType String type) {
    this.type = type;
  }
}
