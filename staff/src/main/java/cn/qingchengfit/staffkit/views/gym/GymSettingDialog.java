package cn.qingchengfit.staffkit.views.gym;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.saascommon.utils.RouteUtil;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.databinding.DialogGymSettingBinding;
import cn.qingchengfit.utils.BundleBuilder;
import java.util.ArrayList;

public class GymSettingDialog extends AppCompatDialog {
  DialogGymSettingBinding mBinding;

  public GymSettingDialog(@NonNull Context context, String gymType) {
    super(context);
    View inflate = LayoutInflater.from(context).inflate(R.layout.dialog_gym_setting, null);
    setContentView(inflate);
    mBinding = DialogGymSettingBinding.bind(inflate);
    mBinding.flGroupSetting.setOnClickListener(v -> checkedView(!v.isActivated(), (FrameLayout) v));
    mBinding.flPrivateSetting.setOnClickListener(
        v -> checkedView(!v.isActivated(), (FrameLayout) v));
    mBinding.flShopSetting.setOnClickListener(v -> checkedView(!v.isActivated(), (FrameLayout) v));
    mBinding.flTrainSetting.setOnClickListener(v -> checkedView(!v.isActivated(), (FrameLayout) v));
    mBinding.tvGymType.setText("您的" + gymType + "主要提供哪些服务？");

    mBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        ArrayList<Integer> types = new ArrayList<>();
        if (mBinding.flGroupSetting.isActivated()) {
          types.add(1);
        }
        if (mBinding.flPrivateSetting.isActivated()) {
          types.add(2);
        }
        if (mBinding.flTrainSetting.isActivated()) {
          types.add(3);
        }
        if (mBinding.flShopSetting.isActivated()) {
          types.add(4);
        }

        RouteUtil.routeTo(getContext(), "staff", "/gym/setting",
            new BundleBuilder().withIntegerArrayList("types", types).build());
        dismiss();
      }
    });
  }

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Window window = getWindow();
    window.setWindowAnimations(cn.qingchengfit.saascommon.R.style.QcStyleDialog);
    window.setBackgroundDrawableResource(cn.qingchengfit.saascommon.R.color.transparent);
    WindowManager.LayoutParams wl = window.getAttributes();
    wl.gravity = Gravity.CENTER;
    window.setAttributes(wl);
  }

  private void checkedView(boolean isChecked, FrameLayout frameLayout) {
    frameLayout.setActivated(isChecked);
    int childCount = frameLayout.getChildCount();
    for (int i = 0; i < childCount; i++) {
      View childAt = frameLayout.getChildAt(i);
      if (childAt instanceof TextView) {
        ((TextView) childAt).setTextColor(
            getContext().getResources().getColor(isChecked ? R.color.primary : R.color.text_grey));
      } else if (childAt instanceof ImageView) {
        ((ImageView) childAt).setImageResource(
            isChecked ? R.drawable.vd_success_tick_primary : R.drawable.vd_success_tick_grey);
      }
    }
  }
}
