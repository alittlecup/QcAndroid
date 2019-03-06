package cn.qingcheng.gym.pages.apply;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingcheng.gym.GymBaseFragment;
import cn.qingcheng.gym.bean.GymApplyOrder;
import cn.qingcheng.gym.bean.GymPosition;
import cn.qingcheng.gym.bean.GymWithSuperUser;
import cn.qingcheng.gym.bean.SuperUser;
import cn.qingchengfit.gym.R;
import cn.qingchengfit.gym.databinding.GyGymApplyPageBinding;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.model.common.BottomChooseData;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.network.Status;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.PhotoUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.BottomChooseDialog;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Leaf(module = "gym", path = "/gym/apply") public class GymApplyPage
    extends GymBaseFragment<GyGymApplyPageBinding, GymApplyViewModel> {
  @Need Gym gym;
  @Need String brandName;
  BottomChooseDialog positionChooseDialog;
  private Map<String, Object> params = new HashMap<>();

  @Override protected void subscribeUI() {
    mViewModel.loadGymPositions(gym.id);
    mViewModel.positions.observe(this, gymPositions -> {
      hideLoading();
      if (gymPositions != null && !gymPositions.isEmpty()) {
        List<BottomChooseData> items = new ArrayList<>();
        for (GymPosition position : gymPositions) {
          items.add(new BottomChooseData(position.name));
        }
        positionChooseDialog = new BottomChooseDialog(getContext(), "场馆职位", items);
        positionChooseDialog.setOnItemClickListener(new BottomChooseDialog.onItemClickListener() {
          @Override public boolean onItemClick(int position) {
            List<GymPosition> value = mViewModel.positions.getValue();
            GymPosition gymPosition = value.get(position);
            mBinding.tvMyPosition.setText(gymPosition.name);
            mBinding.btnApply.setEnabled(true);
            params.put("position_id", gymPosition.id);
            return true;
          }
        });
      }
    });
    mViewModel.gymApplyOrder.observe(this, resource -> {
      if (resource.status == Status.SUCCESS) {
        showSendDialog();
      } else {
        int errorCode = resource.errorCode;
        switch (errorCode) {
          case 400100:
            ToastUtils.show("已经是场馆工作人员");
            break;
          case 400101:
            ToastUtils.show("已经是场馆教练");
            break;
          case 400102:
            ToastUtils.show("已经申请过了，重复申请");
            break;
          case 400103:
            showAlertDialog();
            break;
        }
      }
      mBinding.btnApply.setEnabled(false);
    });
  }

  @Override
  public GyGymApplyPageBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    if (mBinding != null) return mBinding;
    mBinding = GyGymApplyPageBinding.inflate(inflater, container, false);
    mBinding.setToolbarModel(new ToolbarModel("申请加入场馆"));
    initToolbar(mBinding.includeToolbar.toolbar);
    mBinding.btnApply.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        mViewModel.postGymApplyOrder(params);
      }
    });
    updateView(gym);
    mBinding.tvMyPosition.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (positionChooseDialog != null) {
          positionChooseDialog.show();
        } else {
          showLoading();
          mViewModel.loadGymPositions(gym.id);
        }
      }
    });
    mBinding.btnApply.setEnabled(false);
    if (AppUtils.getCurApp(getContext()) == 0) {
      params.put("position_type", 0);
      mBinding.btnApply.setEnabled(true);
      mBinding.tvMyPosition.setText("教练");
      mBinding.tvMyPosition.setClickable(false);
    } else {
      params.put("position_type", 1);
    }
    params.put("gym_id", gym.id);

    Glide.with(getContext())
        .load(PhotoUtils.getSmall(gym.getPhoto()))
        .asBitmap()
        .placeholder(R.drawable.ic_default_header)
        .into(new CircleImgWrapper(mBinding.imgGmyPhoto, getContext()));
    findPositionInGym(gym.id);
    return mBinding;
  }

  private void findPositionInGym(String gymID) {
    mViewModel.findPositionInGym(gymID, AppUtils.getCurApp(getContext()) + "")
        .observe(this, gymPosition -> {
          if (gymPosition == null) {
            loadUserApplyOrders();
          } else {
            mBinding.tvMyPosition.setText(gymPosition.name);
            updateBtnView();
          }
        });
  }

  private void updateView(Gym gym) {
    mBinding.tvGymAddress.setText(gym.address);
    mBinding.tvGymName.setText(brandName + "-" + gym.name);
    if (gym instanceof GymWithSuperUser) {
      SuperUser superuser = ((GymWithSuperUser) gym).superuser;
      if (superuser != null && superuser.user != null) {
        mBinding.tvCreateName.setText(superuser.user.username);
      }
    }
  }

  private void loadUserApplyOrders() {
    mViewModel.loadGymOrder(params).observe(this, gymApplyOrderResponse -> {
      if (gymApplyOrderResponse != null) {
        GymApplyOrder gymApplyOrder = gymApplyOrderResponse.gymApplyOrder;
        if (gymApplyOrder != null) {
          if (AppUtils.getCurApp(getContext()) != 0) {
            mBinding.tvMyPosition.setText(gymApplyOrder.position.name);
          }
          if (gymApplyOrder.status == 1) {
            mBinding.btnApply.setText("已发送申请");
          }
          switch (gymApplyOrder.status) {
            case 1:
              mBinding.btnApply.setText("已发送申请");
              mBinding.btnApply.setEnabled(false);
              mBinding.tvPoint.setVisibility(View.GONE);
              break;
            case 2:
              updateBtnView();
              break;
            case 3:
              break;
          }
        }
      }
    });
  }

  private void updateBtnView() {
    mBinding.btnApply.setText("返回场馆主页");
    mBinding.btnApply.setOnClickListener(v -> {
      //Todo 返回场馆主页
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
