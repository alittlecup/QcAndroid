package cn.qingcheng.gym.pages.apply;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingcheng.gym.GymBaseFragment;
import cn.qingchengfit.gym.databinding.GyGymApplyDealPageBinding;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.utils.DialogUtils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;

public class GymApplyDealPage
    extends GymBaseFragment<GyGymApplyDealPageBinding, GymApplyDealViewModel> {
  @Override protected void subscribeUI() {

  }

  @Override
  public GyGymApplyDealPageBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = GyGymApplyDealPageBinding.inflate(inflater, container, false);
    return mBinding;
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initToolbar(mBinding.includeToolbar.toolbar);
    mBinding.setToolbarModel(new ToolbarModel("审批加入申请"));
    mBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {

      }
    });
  }

  private void showCancelDialog() {
    DialogUtils.showConfirm(getContext(), "确定要拒绝段光英的加入申请吗？",
        new MaterialDialog.SingleButtonCallback() {
          @Override public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
            materialDialog.dismiss();
            if (dialogAction == DialogAction.POSITIVE) {

            }
          }
        });
  }

  private void cancelApply() {

  }
}
