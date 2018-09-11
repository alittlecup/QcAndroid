package cn.qingchengfit.student.view.allot;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import cn.qingchengfit.constant.DirtySender;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.StSalerStudentsPageBinding;
import cn.qingchengfit.student.listener.DrawerListener;
import cn.qingchengfit.student.listener.LoadDataListener;
import cn.qingchengfit.student.view.home.StudentFilterView;
import cn.qingchengfit.student.view.home.StudentListView;
import cn.qingchengfit.student.view.home.StudentListViewModel;
import cn.qingchengfit.student.view.home.StudentRecyclerSortView;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.MeasureUtils;
import com.afollestad.materialdialogs.DialogAction;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

@Leaf(module = "student", path = "/student/seller/student") public class SalerStudentsPage
    extends StudentBaseFragment<StSalerStudentsPageBinding, SalerStudentsViewModel>
    implements FlexibleAdapter.OnItemClickListener, DrawerListener, LoadDataListener {
  @Need Staff staff;
  @Need Integer type;
  StudentRecyclerSortView listView;
  StudentFilterView filterView;
  @Inject IPermissionModel permissionModel;

  @Override protected void subscribeUI() {
    mViewModel.getLiveItems().observe(this, items -> {
      if(TextUtils.isEmpty(staff.getId())){
        listView.getListView().setAdapterTag("choose",-1);
      }else {
        listView.getListView().setAdapterTag("choose",type);
      }
      listView.setDatas(items);
    });
  }

  @Override public StSalerStudentsPageBinding initDataBinding(LayoutInflater layoutInflater,
      ViewGroup viewGroup, Bundle bundle) {
    if(mBinding!=null){
      loadData(new HashMap<>());
      toggleToolbar(false,"");
      return mBinding;
    }
    mBinding = StSalerStudentsPageBinding.inflate(layoutInflater, viewGroup, false);
    initFragment();
    initListener();
    toggleToolbar(false,"");

    mViewModel.type = type;
    mViewModel.setSalerId(staff.getId());
    return mBinding;
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    listView.getListView().setListener(
        () -> DialogUtils.shwoConfirm(getContext(), "确定将选中的会员从" + staff.getUsername() + "的名下移除？",
            (materialDialog, dialogAction) -> {
              materialDialog.dismiss();
              if (dialogAction == DialogAction.POSITIVE) {
                listView.getListView().removeStaffStudents();
              }
            }));
  }
  public void onRemoveResult(boolean isSuccess){
    if(isSuccess){
      loadData(new HashMap<>());
    }
    toggleToolbar(false,"");
  }
  private void initListener() {
    mBinding.includeAllot.allotCoach.setOnClickListener(v -> {
      if (permissionModel.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE)) {
        toggleToolbar(true, StudentListView.TRAINER_TYPE);
      } else {
        showAlert(R.string.sorry_for_no_permission);
      }
    });
    mBinding.includeAllot.allotSale.setOnClickListener(v -> {
      if (permissionModel.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE)) {
        toggleToolbar(true, StudentListView.SELLER_TYPE);
      } else {
        showAlert(R.string.sorry_for_no_permission);
      }
    });
    mBinding.includeAllot.allotMsg.setOnClickListener(view->{
      toggleToolbar(true, StudentListView.MSG_TYPE);
    });
  }

  private void initFragment() {
    listView = new StudentRecyclerSortView();
    filterView = new StudentFilterView();
    stuff(R.id.frame_student_filter, filterView);
    stuff(R.id.list_container, listView);
    listView.setListener(this);
    listView.setFilterView(filterView);
    listView.setLoadDataListener(this);


    filterView.setListener(params -> {
      mViewModel.loadSource(params);
      mBinding.drawer.closeDrawer(GravityCompat.END);
    });
  }

  private void initToolbar() {
    String title;
    if (TextUtils.isEmpty(staff.getId())) {
      title = "未分配";
    }else{
      title=(type==0?"销售":"教练")+staff.getUsername();
    }
    mBinding.setToolbarModel(new ToolbarModel(title));
    initToolbar(mBinding.includeToolbar.toolbar);
  }
  private void toggleToolbar(boolean showCheckbox,String type){
    if(showCheckbox){
      if(!permissionModel.check(PermissionServerUtils.MANAGE_MEMBERS_CAN_CHANGE)){
        showAlert(R.string.sorry_for_no_permission);
        return;
      }
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
      mBinding.includeAllot.getRoot().setVisibility(View.GONE);
      //收缩布局
      listView.getListView().setCurType(type);
      if(!TextUtils.isEmpty(staff.getId())){
        listView.getListView().setCurId(staff.getId());
      }
    }else{
      initToolbar();
      mBinding.rbSelectAll.setVisibility(View.GONE);
      mBinding.includeAllot.getRoot().setVisibility(View.VISIBLE);
      if(listView.getListView()!=null){
        listView.getListView().removeSelected();
        listView.getListView().setCurId("");
      }
    }
  }

  @Override public void openDrawer() {
    mBinding.drawer.openDrawer(GravityCompat.END);
  }

  @Override public void closeDrawer() {
    mBinding.drawer.closeDrawer(GravityCompat.END);
  }

  @Override public void loadData(Map<String, Object> params) {
    mViewModel.loadSource(params);
  }

  @Override public boolean onItemClick(int position) {
    return false;
  }
}
