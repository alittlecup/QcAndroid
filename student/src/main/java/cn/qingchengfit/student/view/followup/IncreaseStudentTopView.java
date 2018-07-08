package cn.qingchengfit.student.view.followup;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import cn.qingchengfit.student.databinding.ViewIncreaseStudentTopBinding;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.listener.IncreaseType;

public class IncreaseStudentTopView
    extends StudentBaseFragment<ViewIncreaseStudentTopBinding, IncreaseStudentTopViewModel> {
  String type = "";

  @Override protected void subscribeUI() {

  }

  @Override
  public ViewIncreaseStudentTopBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = ViewIncreaseStudentTopBinding.inflate(inflater, container, false);
    initListener();
    if (type.equals(IncreaseType.INCREASE_MEMBER)) {
      mBinding.radioGroup.setBackgroundResource(R.drawable.radiogroup_blue_bg);
    } else if (type.equals(IncreaseType.INCREASE_STUDENT)) {
      mBinding.radioGroup.setBackgroundResource(R.drawable.radiogroup_green_bg);
    }
    mBinding.radioRight.setChecked(true);
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

  public void setType(@IncreaseType String type) {
    this.type = type;
  }
}
