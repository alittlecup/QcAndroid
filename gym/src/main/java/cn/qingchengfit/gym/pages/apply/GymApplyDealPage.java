package cn.qingchengfit.gym.pages.apply;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.gym.GymBaseFragment;
import cn.qingchengfit.gym.bean.GymPosition;
import cn.qingchengfit.gym.R;
import cn.qingchengfit.gym.databinding.GyGymApplyDealPageBinding;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.common.BottomChooseData;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.network.Status;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DialogUtils;
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
    showLoading();
    mViewModel.loagAplyOrderInfo(gymId, applyId).observe(this, gymApplyOrder -> {
      hideLoading();
      if (gymApplyOrder != null) {
        if (gymApplyOrder.status != 1) {
          routeTo("/gym/deal/finish", null);
          return;
        }
        if (TextUtils.isEmpty(gymApplyOrder.position.name)) {
          mBinding.civPosition.setContent("教练");
          mBinding.civPosition.setClickable(false);
          mBinding.tvApplyFrom.setText("通过教练助手App申请");
        } else {
          mBinding.civPosition.setContent(gymApplyOrder.position.name);
          mBinding.tvApplyFrom.setText("通过健身管理App申请");
        }
        positionID = gymApplyOrder.position.id;
        staff = gymApplyOrder.user;
        mBinding.tvNamePhone.setText(
            gymApplyOrder.user.getUsername() + "(" + gymApplyOrder.user.getPhone() + ")");
        loadPhoto(gymApplyOrder.user);
      }
    });
  }

  private Staff staff;

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

    mBinding.civPosition.setOnClickListener((View.OnClickListener) v -> {
      if (positionChooseDialog != null) {
        positionChooseDialog.show();
      } else {
        mViewModel.loadGymPositions(gymId);
      }
    });
    return mBinding;
  }

  private void showCancelDialog() {
    if (staff != null) {
      DialogUtils.showConfirm(getContext(), "确定要拒绝" + staff.getUsername() + "的加入申请吗？",
          (materialDialog, dialogAction) -> {
            materialDialog.dismiss();
            if (dialogAction == DialogAction.POSITIVE) {
              dealApply(gymId, applyId, 3);
            }
          });
    }
  }

  private void dealApply(String gymid, String orderID, int status) {
    mViewModel.dealApply(gymid, orderID, status, positionID).observe(this, resource -> {
      if (resource.status == Status.SUCCESS) {
        routeTo("/gym/deal/finish", null);
      }
    });
  }
}
