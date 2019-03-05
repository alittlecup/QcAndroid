package cn.qingcheng.gym.pages.gym;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import cn.qingchengfit.gym.R;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.SpanUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;
import javax.inject.Inject;

@Leaf(module = "gym", path = "/gym/edit") public class GymEditPage extends GymInfoPage {
  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    mBinding.setToolbarModel(new ToolbarModel("编辑场馆信息"));
    initView();
  }

  private void initView() {
    mBinding.civGymType.setShowRight(true);
    mBinding.civGymAddress.setShowRight(true);

    mBinding.civGymMark.setShowRight(true);
    mBinding.tvGymAction.setVisibility(View.VISIBLE);
    mBinding.tvGymAction.setOnClickListener(v -> showDeleteDialog());
    mBinding.imgPhotoArrow.setVisibility(View.VISIBLE);
    mBinding.viewShadow.setVisibility(View.GONE);
  }

  @Override public void initToolbar() {
    ToolbarModel toolbarModel = new ToolbarModel("编辑场馆信息");
    toolbarModel.setMenu(R.menu.menu_save);
    toolbarModel.setListener(item -> {
      if(checkShop(shop)){
        if(brand!=null){
          shop.brand_id = brand.id;
        }
        mViewModel.editShop(shop).observe(GymEditPage.this,aBoolean -> {
          if(aBoolean){
            ToastUtils.show("修改场馆成功");
            getActivity().onBackPressed();
          }else{
            ToastUtils.show("修改场馆失败");

          }
        });
      }
      return false;
    });
    mBinding.setToolbarModel(toolbarModel);
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  @Override public void initToolbar(@NonNull Toolbar toolbar) {
    super.initToolbar(toolbar);
  }

  private void showDeleteDialog() {
    DialogUtils.showConfirm(getContext(), "确认删除该场馆?", new MaterialDialog.SingleButtonCallback() {
      @Override public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
        materialDialog.dismiss();
        if (dialogAction == DialogAction.POSITIVE) {
          mViewModel.deleteShop(shop.id);
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
