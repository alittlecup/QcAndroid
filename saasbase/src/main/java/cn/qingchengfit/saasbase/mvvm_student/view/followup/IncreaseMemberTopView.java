package cn.qingchengfit.saasbase.mvvm_student.view.followup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.databinding.ViewIncreaseMemberTopBinding;
import cn.qingchengfit.saasbase.mvvm_student.StudentBaseFragment;

public class IncreaseMemberTopView
    extends StudentBaseFragment<ViewIncreaseMemberTopBinding, IncreaseMemberTopViewModel> {
  String type = "";

  @Override protected void subscribeUI() {

  }

  @Override
  public ViewIncreaseMemberTopBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = ViewIncreaseMemberTopBinding.inflate(inflater, container, false);
    initListener();
    if (type.equals(IncreaseStudentPage.IncreaseType.INCREASE_STUDENT)) {
      mBinding.radioGroup.setBackgroundResource(R.drawable.radiogroup_green_bg);
      mBinding.tvTypeContent.setText("新用户跟进");
      mBinding.tvCount.setTextColor(getResources().getColor(R.color.success_green));
    } else if (type.equals(IncreaseStudentPage.IncreaseType.INCREASE_FOLLOWUP)) {
      mBinding.radioGroup.setBackgroundResource(R.drawable.radiogroup_orange_bg);
      mBinding.tvTypeContent.setText("会员维护");
      mBinding.tvCount.setTextColor(getResources().getColor(R.color.orange));
    }
    return mBinding;
  }

  private void initListener() {
    mBinding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.radio_left) {
        } else if (checkedId == R.id.radio_mid) {
        } else if (checkedId == R.id.radio_right) {
        }
      }
    });
  }

  public void setType(@IncreaseStudentPage.IncreaseType String type) {
    this.type = type;
  }
}
