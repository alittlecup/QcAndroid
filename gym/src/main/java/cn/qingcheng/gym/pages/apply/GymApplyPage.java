package cn.qingcheng.gym.pages.apply;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingcheng.gym.GymBaseFragment;
import cn.qingchengfit.gym.R;
import cn.qingchengfit.gym.databinding.GyGymApplyPageBinding;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.utils.DialogUtils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;

@Leaf(module = "gym", path = "/gym/apply") public class GymApplyPage
    extends GymBaseFragment<GyGymApplyPageBinding, GymApplyViewModel> {
  @Override protected void subscribeUI() {

  }

  @Override
  public GyGymApplyPageBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = GyGymApplyPageBinding.inflate(inflater, container, false);
    return mBinding;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mBinding.setToolbarModel(new ToolbarModel("申请加入场馆"));
    initToolbar(mBinding.includeToolbar.toolbar);
    mBinding.btnApply.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        showSendDialog();
      }
    });
  }

  private void showSendDialog() {
    DialogUtils.showIconDialog(getContext(), R.drawable.ic_vd_success_dialog_icon, "已发送申请",
        "场馆超级管理员通过申请后，系统会进行通知，请及时留意", "知道了", new MaterialDialog.SingleButtonCallback() {
          @Override public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
            materialDialog.dismiss();
          }
        });
  }

  private void showAlertDialog() {
    DialogUtils.showIconDialog(getContext(), R.drawable.ic_vd_alert_dialog_icon, "当日申请次数用光了",
        "请线下联系场馆同事在系统中邀请您加入场馆", "知道了", new MaterialDialog.SingleButtonCallback() {
          @Override public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
            materialDialog.dismiss();
          }
        });
  }
}
