package cn.qingchengfit.student.view.state;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.student.bean.InactiveBean;
import cn.qingchengfit.student.bean.QcStudentBeanWithFollow;
import cn.qingchengfit.student.databinding.PageSalerStudentStateBinding;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.listener.IncreaseType;
import cn.qingchengfit.student.listener.onSecondBottomButtonListener;
import cn.qingchengfit.student.view.home.StudentListView;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.MeasureUtils;
import com.afollestad.materialdialogs.DialogAction;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.util.ArrayList;
import javax.inject.Inject;

@Leaf(module = "student", path = "/student/seller_state") public class SalerStudentStatePage
    extends StudentBaseFragment<PageSalerStudentStateBinding, SalerStudentStateViewModel> {
  @Need @IncreaseType String type = IncreaseType.INCREASE_MEMBER;
  @Need Staff staff;
  @Inject IPermissionModel permissionModel;
  @Need ArrayList<InactiveBean> beans;
  StudentListView listView;
  SalerStateFilterView filterView;

  @Override protected void subscribeUI() {
    mViewModel.seller_id = staff.getId();
    mViewModel.filterContent.observe(this, content -> mBinding.qcFilterToggle.setText(content));
    mViewModel.items.observe(this, items -> listView.setItems(items));
    mViewModel.filterVisible.observe(this, ab -> {
      if (ab) {
        filterView.showPage(0);
      } else {
        mBinding.qcFilterToggle.setChecked(false);
      }
    });
  }

  @Override
  public PageSalerStudentStateBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageSalerStudentStateBinding.inflate(inflater, container, false);
    mBinding.setViewModel(mViewModel);
    mBinding.setLifecycleOwner(this);
    initFragment();
    initListener();
    toggleToolbar(false, "");
    mViewModel.setCurAttack(beans.get(0));
    return mBinding;
  }

  private void initFragment() {
    listView = new StudentListView();
    filterView = new SalerStateFilterView();
    filterView.setInactiveBeans(beans);
    listView.setListener(
        () -> DialogUtils.shwoConfirm(getContext(), "确定将选中的会员从" + staff.getUsername() + "的名下移除？",
            (materialDialog, dialogAction) -> {
              materialDialog.dismiss();
              if (dialogAction == DialogAction.POSITIVE) {
                listView.removeStaffStudents();
              }
            }));

    stuff(R.id.fragment_list_container, listView);
    stuff(R.id.fragment_filter, filterView);
  }

  private void initToolbar() {
    ToolbarModel toolbarModel = null;
    switch (type) {
      case IncreaseType.INCREASE_FOLLOWUP:
        toolbarModel = new ToolbarModel(staff.getUsername() + "的已接洽用户");
        mViewModel.type = 1;
        break;
      case IncreaseType.INCREASE_STUDENT:
        toolbarModel = new ToolbarModel(staff.getUsername() + "的会员用户");
        mViewModel.type = 2;

        break;
      case IncreaseType.INCREASE_MEMBER:
        toolbarModel = new ToolbarModel(staff.getUsername() + "的新注册用户");
        mViewModel.type = 0;
        break;
    }
    if (toolbarModel != null) {
      mBinding.setToolbarModel(toolbarModel);
    }
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  private void initListener() {
    mBinding.bottomAllot.allotCoach.setOnClickListener(v -> {
      if (permissionModel.check(PermissionServerUtils.MANAGE_MEMBERS_IS_ALL)) {
        toggleToolbar(true, StudentListView.TRAINER_TYPE);
      } else {
        showAlert(R.string.sorry_for_no_permission);
      }
    });
    mBinding.bottomAllot.allotSale.setOnClickListener(v -> {
      if (permissionModel.check(PermissionServerUtils.MANAGE_MEMBERS_IS_ALL)) {
        toggleToolbar(true, StudentListView.SELLER_TYPE);
      } else {
        showAlert(R.string.sorry_for_no_permission);
      }
    });
    mBinding.bottomAllot.allotMsg.setOnClickListener(v -> {
      toggleToolbar(true, StudentListView.MSG_TYPE);
    });
  }

  private void toggleToolbar(boolean showCheckBox, String type) {
    if (showCheckBox) {
      if (!permissionModel.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE)) {
        showAlert(R.string.sorry_for_no_permission);
        return;
      }
      //修改toolBar
      mBinding.rbSelectAll.setVisibility(View.VISIBLE);
      ToolbarModel toolbarModel = new ToolbarModel(StudentListView.getStringByType(type));
      toolbarModel.setMenu(R.menu.menu_cancel);
      toolbarModel.setListener(item -> {
        toggleToolbar(false, "");
        return false;
      });
      mBinding.setToolbarModel(toolbarModel);
      if (!CompatUtils.less21()
          && mBinding.includeToolbar.toolbar.getParent() instanceof ViewGroup
          && this.isfitSystemPadding()) {
        RelativeLayout.LayoutParams layoutParams =
            (RelativeLayout.LayoutParams) mBinding.rbSelectAll.getLayoutParams();
        layoutParams.setMargins(0, MeasureUtils.getStatusBarHeight(this.getContext()), 0, 0);
        mBinding.rbSelectAll.setLayoutParams(layoutParams);
      }
      //底部分配布局
      mBinding.bottomAllot.getRoot().setVisibility(View.GONE);
      //修改列表内容
      listView.setCurType(type);
      if (type.equals(StudentListView.SELLER_TYPE)) {
        listView.setCurId(staff.getId());
      }
    } else {
      //修改toolBar
      mBinding.rbSelectAll.setVisibility(View.GONE);
      initToolbar();
      //底部分配布局
      mBinding.bottomAllot.getRoot().setVisibility(View.VISIBLE);
      listView.reset();
      listView.setCurId("");
    }
  }
}
