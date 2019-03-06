package cn.qingcheng.gym.pages.apply;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingcheng.gym.GymBaseFragment;
import cn.qingcheng.gym.bean.GymPosition;
import cn.qingchengfit.gym.databinding.GyGymApplyDealPageBinding;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.model.common.BottomChooseData;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.widgets.BottomChooseDialog;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.util.ArrayList;
import java.util.List;

@Leaf(module = "gym", path = "/gym/deal//apply") public class GymApplyDealPage
    extends GymBaseFragment<GyGymApplyDealPageBinding, GymApplyDealViewModel> {
  @Need Gym gym;
  private BottomChooseDialog positionChooseDialog;
  private String positionID;

  @Override protected void subscribeUI() {
    if (AppUtils.getCurApp(getContext()) != 0) {
      mViewModel.loadGymPositions(gym.id);
      mViewModel.positions.observe(this, gymPositions -> {
        hideLoading();
        if (gymPositions != null && !gymPositions.isEmpty()) {
          List<BottomChooseData> items = new ArrayList<>();
          for (GymPosition position : gymPositions) {
            items.add(new BottomChooseData(position.name));
          }
          positionChooseDialog = new BottomChooseDialog(getContext(), "TA的职位", items);
          positionChooseDialog.setOnItemClickListener(new BottomChooseDialog.onItemClickListener() {
            @Override public boolean onItemClick(int position) {
              List<GymPosition> value = mViewModel.positions.getValue();
              GymPosition gymPosition = value.get(position);
              mBinding.civPosition.setContent(gymPosition.name);
              positionID = gymPosition.id;
              return true;
            }
          });
        }
      });
    }
  }

  @Override
  public GyGymApplyDealPageBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    if (mBinding != null) return mBinding;
    mBinding = GyGymApplyDealPageBinding.inflate(inflater, container, false);
    initToolbar(mBinding.includeToolbar.toolbar);
    mBinding.setToolbarModel(new ToolbarModel("审批加入申请"));
    mBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        showCancelDialog();
      }
    });
    mBinding.btnConfirm.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        //dealApply();
      }
    });
    if (AppUtils.getCurApp(getContext()) == 0) {
      mBinding.civPosition.setContent("教练");
    } else {

    }
    return mBinding;
  }

  private void showCancelDialog() {
    DialogUtils.showConfirm(getContext(), "确定要拒绝XXX的加入申请吗？",
        new MaterialDialog.SingleButtonCallback() {
          @Override public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
            materialDialog.dismiss();
            if (dialogAction == DialogAction.POSITIVE) {
              //dealApply();
            }
          }
        });
  }

  private void dealApply(String gymid, String orderID, int status) {
    mViewModel.dealApply(gym.id, orderID, AppUtils.getCurApp(getContext()), positionID);
  }
}
