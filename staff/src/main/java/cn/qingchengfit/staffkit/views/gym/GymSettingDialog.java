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
import cn.qingchengfit.saascommon.qrcode.views.QRActivity;
import cn.qingchengfit.saascommon.utils.RouteUtil;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.databinding.DialogGymSettingBinding;
import cn.qingchengfit.utils.BundleBuilder;
import cn.qingchengfit.utils.PreferenceUtils;
import cn.qingchengfit.utils.ToastUtils;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;

public class GymSettingDialog extends AppCompatDialog {
  DialogGymSettingBinding mBinding;

  public Set<String> getModules() {
    return modules;
  }

  public  Set<String> modules = new LinkedHashSet<>();

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
        modules.clear();
        modules.add(QRActivity.MODULE_STUDENT_CARDS);
        modules.add(QRActivity.MODULE_STUDENT);
        int i = 0;
        if (mBinding.flGroupSetting.isActivated()) {
          types.add(1);
          i += 1;
          modules.add(QRActivity.MODULE_SERVICE_GROUP);
          modules.add(QRActivity.MODULE_MANAGE_COACH);
          modules.add(QRActivity.MODULE_GYM_SITE);
          modules.add(QRActivity.MODULE_WORKSPACE_ORDER_LIST);
          modules.add(QRActivity.MODULE_WORKSPACE_GROUP);
        }
        if (mBinding.flPrivateSetting.isActivated()) {
          types.add(2);
          i += 1;
          modules.add(QRActivity.MODULE_SERVICE_PRIVATE);
          modules.add(QRActivity.MODULE_MANAGE_COACH);
          modules.add(QRActivity.MODULE_GYM_SITE);
          modules.add(QRActivity.MODULE_WORKSPACE_ORDER_LIST);
          modules.add(QRActivity.MODULE_WORKSPACE_PRIVATE);
        }
        if (mBinding.flTrainSetting.isActivated()) {
          types.add(3);
          i += 1;
          modules.add(QRActivity.MODULE_SERVICE_FREE);
          modules.add(QRActivity.MODULE_WORKSPACE_ORDER_SIGNIN);
          modules.add(QRActivity.MODULE_WARDROBE);
        }
        if (mBinding.flShopSetting.isActivated()) {
          types.add(4);
          i += 1;
          modules.add(QRActivity.MODULE_SERVICE_SHOP);
          modules.add(QRActivity.MODULE_WORKSPACE_COMMODITY_LIST);
        }
        if (i == 0) {
          ToastUtils.show("请至少选择一项服务");
          return;
        }

        PreferenceUtils.setPrefInt(getContext(), "GymSettingCount", i);
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
