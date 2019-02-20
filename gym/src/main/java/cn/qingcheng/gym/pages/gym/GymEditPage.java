package cn.qingcheng.gym.pages.gym;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import cn.qingchengfit.gym.R;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.utils.SpanUtils;
import cn.qingchengfit.utils.DialogUtils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;

@Leaf(module = "gym", path = "/gym/edit") public class GymEditPage extends GymInfoPage {
  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mBinding.setToolbarModel(new ToolbarModel("编辑场馆信息"));
    initView();
  }

  private void initView() {
    mBinding.civGymName.setCanBeNull(false);
    mBinding.civGymName.setEditable(true);

    mBinding.civGymType.setEditable(false);
    mBinding.civGymType.setCanClick(true);
    mBinding.civGymAddress.setShowRight(true);
    mBinding.civGymType.setCanBeNull(false);

    mBinding.civGymPhone.setEditable(true);
    mBinding.civGymPhone.setIsNum(true);
    mBinding.civGymPhone.setCanBeNull(false);

    mBinding.civGymAddress.setEditable(false);
    mBinding.civGymAddress.setCanClick(true);
    mBinding.civGymAddress.setShowRight(true);
    mBinding.civGymAddress.setCanBeNull(false);

    mBinding.civGymSquare.setEditable(true);
    mBinding.civGymSquare.setCanClick(false);
    mBinding.civGymSquare.setShowRight(true);
    mBinding.civGymSquare.setCanBeNull(false);

    mBinding.civGymMark.setEditable(false);
    mBinding.civGymMark.setCanClick(true);
    mBinding.civGymMark.setShowRight(true);
    mBinding.civGymMark.setCanBeNull(false);
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
    toolbar.inflateMenu(R.menu.menu_save);
    toolbar.getMenu()
        .getItem(0)
        .setTitle(new SpanUtils().append("保存")
            .setForegroundColor(getResources().getColor(R.color.primary))
            .create());
    toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        return false;
      }
    });
  }

  private void showDeleteDialog() {
    DialogUtils.showConfirm(getContext(), "确认删除该场馆?", new MaterialDialog.SingleButtonCallback() {
      @Override public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
        materialDialog.dismiss();
        if (dialogAction == DialogAction.POSITIVE) {
          // TODO: 2019/2/20  删除
        }
      }
    });
  }

  private void showFireGymDialog() {
    DialogUtils.showIconDialog(getContext(), -1, "确认退出", "退出后将无法恢复，\n需由该健身房工作人员添加", "取消", "确定",
        new MaterialDialog.SingleButtonCallback() {
          @Override public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
            materialDialog.dismiss();
            if (dialogAction == DialogAction.POSITIVE) {
              // TODO: 2019/2/20 离职
            }
          }
        });
  }
}
