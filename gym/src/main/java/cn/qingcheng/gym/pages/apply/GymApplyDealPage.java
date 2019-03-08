package cn.qingcheng.gym.pages.apply;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingcheng.gym.GymBaseFragment;
import cn.qingcheng.gym.bean.GymPosition;
import cn.qingchengfit.gym.R;
import cn.qingchengfit.gym.databinding.GyGymApplyDealPageBinding;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.common.BottomChooseData;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.widgets.BottomChooseDialog;
import com.afollestad.materialdialogs.DialogAction;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import java.util.ArrayList;
import java.util.List;

@Leaf(module = "gym", path = "/gym/deal//apply") public class GymApplyDealPage
    extends GymBaseFragment<GyGymApplyDealPageBinding, GymApplyDealViewModel> {
  @Need String applyId;
  @Need String gymId;
  private BottomChooseDialog positionChooseDialog;
  private String positionID;

  @Override protected void subscribeUI() {
    if (AppUtils.getCurApp(getContext()) != 0) {
      mViewModel.loadGymPositions(gymId);
      mViewModel.positions.observe(this, gymPositions -> {
        hideLoading();
        if (gymPositions != null && !gymPositions.isEmpty()) {
          List<BottomChooseData> items = new ArrayList<>();
          for (GymPosition position : gymPositions) {
            items.add(new BottomChooseData(position.name));
          }
          positionChooseDialog = new BottomChooseDialog(getContext(), "TA的职位", items);
          positionChooseDialog.setOnItemClickListener(position -> {
            List<GymPosition> value = mViewModel.positions.getValue();
            GymPosition gymPosition = value.get(position);
            mBinding.civPosition.setContent(gymPosition.name);
            positionID = gymPosition.id;
            return true;
          });
        }
      });
      mViewModel.loagAplyOrderInfo(gymId, applyId).observe(this, gymApplyOrder -> {
        if (gymApplyOrder != null) {
          mBinding.civPosition.setContent(gymApplyOrder.position.name);
          positionID = gymApplyOrder.position.id;
          mBinding.btnConfirm.setText(
              gymApplyOrder.user.getUsername() + "(" + gymApplyOrder.user.getPhone() + ")");
          loadPhoto(gymApplyOrder.user);
        }
      });
    }
  }

  private void loadPhoto(Staff staff) {
    Glide.with(getContext())
        .load(com.tencent.qcloud.timchat.widget.PhotoUtils.getSmall(staff.getAvatar()))
        .asBitmap()
        .placeholder(staff.getGender() == 0 ? R.drawable.default_student_male
            : R.drawable.default_student_female)
        .error(staff.getGender() == 0 ? R.drawable.default_student_male
            : R.drawable.default_student_female)
        .into(new CircleImgWrapper(mBinding.imgPhoto, getContext()));
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
        dealApply(gymId, applyId, 2);
      }
    });
    if (AppUtils.getCurApp(getContext()) == 0) {
      mBinding.civPosition.setContent("教练");
    } else {
      mBinding.civPosition.setOnClickListener((View.OnClickListener) v -> {
        if (positionChooseDialog != null) {
          positionChooseDialog.show();
        } else {
          mViewModel.loadGymPositions(gymId);
        }
      });
    }
    return mBinding;
  }

  private void showCancelDialog() {
    DialogUtils.showConfirm(getContext(), "确定要拒绝XXX的加入申请吗？", (materialDialog, dialogAction) -> {
      materialDialog.dismiss();
      if (dialogAction == DialogAction.POSITIVE) {
        dealApply(gymId, applyId, 3);
      }
    });
  }

  private void dealApply(String gymid, String orderID, int status) {
    mViewModel.dealApply(gymid, orderID, status, positionID);
  }
}
