package cn.qingchengfit.student.view.detail;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toolbar;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.utils.StudentBusinessUtils;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.StStudentDetailCardPageBinding;
import cn.qingchengfit.utils.PhotoUtils;
import com.anbillon.flabellum.annotations.Need;

public class StudentDetailWithCardPage
    extends StudentBaseFragment<StStudentDetailCardPageBinding, StudentDetailWithCardViewModel> {
  @Need String studentId;

  @Override protected void subscribeUI() {
    mViewModel.student.observe(this, this::updateStudentInfo);
  }

  private void updateStudentInfo(QcStudentBean student) {
    if (student != null) {
      mBinding.tvName.setText(student.getUsername());
      mBinding.tvPhone.setText(student.getPhone());
      mBinding.imgGender.setImageResource(
          student.getGender() == 0 ? R.drawable.vd_gender_male : R.drawable.vd_gender_female);
      PhotoUtils.smallCircle(mBinding.imgAvatar, student.getAvatar(),
          student.getGender() == 0 ? R.drawable.default_manage_male
              : R.drawable.default_manager_female,
          student.getGender() == 0 ? R.drawable.default_manage_male
              : R.drawable.default_manager_female);

      mBinding.tvStatus.setText(getContext().getResources()
          .getStringArray(cn.qingchengfit.saascommon.R.array.student_status)[student.getStatus()
          % 3]);
      mBinding.tvStatus.setCompoundDrawablePadding(10);
      mBinding.tvStatus.setCompoundDrawables(
          StudentBusinessUtils.getStudentStatusDrawable(getContext(), student.getStatus() % 3),
          null, null, null);
    }
  }

  @Override public StStudentDetailCardPageBinding initDataBinding(LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    mBinding = StStudentDetailCardPageBinding.inflate(inflater, container, false);
    initToolbar();
    initFragment();

    return mBinding;
  }

  private void initFragment() {

  }

  private void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel("选择会员卡"));
    initToolbar(mBinding.includeToolbar.toolbar);
  }
}
