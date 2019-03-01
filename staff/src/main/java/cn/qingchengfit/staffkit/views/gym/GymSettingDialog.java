package cn.qingchengfit.staffkit.views.gym;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.databinding.DialogGymSettingBinding;
import cn.qingchengfit.utils.DrawableUtils;
import cn.qingchengfit.views.fragments.BaseDialogFragment;

public class GymSettingDialog extends BaseDialogFragment {
  DialogGymSettingBinding mBinding;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setStyle(DialogFragment.STYLE_NORMAL, cn.qingchengfit.saasbase.R.style.AppTheme);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
  }

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding = DialogGymSettingBinding.inflate(inflater, container, false);
    mBinding.flGroupSetting.setOnClickListener(v -> checkedView(!v.isActivated(), (FrameLayout) v));
    mBinding.flPrivateSetting.setOnClickListener(
        v -> checkedView(!v.isActivated(), (FrameLayout) v));
    mBinding.flShopSetting.setOnClickListener(v -> checkedView(!v.isActivated(), (FrameLayout) v));
    mBinding.flTrainSetting.setOnClickListener(v -> checkedView(!v.isActivated(), (FrameLayout) v));
    return mBinding.getRoot();
  }

  private void checkedView(boolean isChecked, FrameLayout frameLayout) {
    frameLayout.setActivated(isChecked);
    int childCount = frameLayout.getChildCount();
    for (int i = 0; i < childCount; i++) {
      View childAt = frameLayout.getChildAt(i);
      if (childAt instanceof TextView) {
        ((TextView) childAt).setTextColor(
            getResources().getColor(isChecked ? R.color.primary : R.color.text_grey));
      } else if (childAt instanceof ImageView) {
        ((ImageView) childAt).setImageDrawable(
            DrawableUtils.tintDrawable(getContext(), R.drawable.vd_success_tick_white,
                isChecked ? R.color.primary : R.color.divider_dark));
      }
    }
  }
}
