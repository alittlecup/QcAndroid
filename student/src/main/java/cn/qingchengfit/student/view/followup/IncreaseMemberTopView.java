package cn.qingchengfit.student.view.followup;

import android.arch.lifecycle.Transformations;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import cn.qingchengfit.saascommon.utils.SpanUtils;
import cn.qingchengfit.student.databinding.ViewIncreaseMemberTopBinding;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.listener.IncreaseType;
import cn.qingchengfit.utils.DateUtils;
import java.util.HashMap;

public class IncreaseMemberTopView
    extends StudentBaseFragment<ViewIncreaseMemberTopBinding, IncreaseMemberTopViewModel> {
  String type = "";

  @Override protected void subscribeUI() {
    mViewModel.count.observe(this, msg -> {
      mBinding.tvCount.setText(
          new SpanUtils().append(msg).append("人").setFontSize(16, true).create());
    });
  }

  @Override
  public ViewIncreaseMemberTopBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = ViewIncreaseMemberTopBinding.inflate(inflater, container, false);
    initListener();
    mBinding.setViewModel(mViewModel);
    mBinding.setLifecycleOwner(this);
    updateUI();
    return mBinding;
  }

  private void initListener() {
    mBinding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
        int pos = -1;
        String end = DateUtils.getStringToday();
        String start = "";
        if (checkedId == R.id.radio_left) {
          pos = 0;
          start = DateUtils.addDay(end, -30);
        } else if (checkedId == R.id.radio_mid) {
          start = DateUtils.addDay(end, -7);
          pos = 2;
        } else if (checkedId == R.id.radio_right) {
          start = end;
          pos = 4;
        }
        mViewModel.selectPos.setValue(pos);
        HashMap<String, Object> dates = new HashMap<>();
        dates.put("start", start);
        dates.put("end", end);
        mViewModel.loadSource(dates);
      }
    });
  }

  @Override public void onResume() {
    super.onResume();
    if (mViewModel.selectPos.getValue() != null) {
      ((RadioButton) mBinding.radioGroup.getChildAt(mViewModel.selectPos.getValue())).setChecked(
          true);
    } else {
      mBinding.radioRight.setChecked(true);
    }
  }

  public void setType(@IncreaseType String type) {
    this.type = type;
    if (mBinding != null) {
      updateUI();
    }
  }

  private void updateUI() {
    if (type.equals(IncreaseType.INCREASE_STUDENT)) {
      mBinding.radioGroup.setBackgroundResource(R.drawable.radiogroup_green_bg);
      mBinding.tvTypeContent.setText("会员维护");
      mBinding.tvCount.setTextColor(getResources().getColor(R.color.success_green));

      mViewModel.status = 0;
    } else if (type.equals(IncreaseType.INCREASE_FOLLOWUP)) {
      mBinding.radioGroup.setBackgroundResource(R.drawable.radiogroup_orange_bg);
      mBinding.tvTypeContent.setText("跟进新用户");
      mBinding.tvCount.setTextColor(getResources().getColor(R.color.orange));
      mViewModel.status = 1;
    }
  }
}
