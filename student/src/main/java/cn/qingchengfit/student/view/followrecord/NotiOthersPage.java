package cn.qingchengfit.student.view.followrecord;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.events.EventCommonUserList;
import cn.qingchengfit.saascommon.item.CommonUserItem;
import cn.qingchengfit.saascommon.views.commonuserselect.CommonUserSelectView;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;

import cn.qingchengfit.student.databinding.StPageNotiOthersBinding;
import cn.qingchengfit.student.view.home.StudentListView;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.MeasureUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import java.util.ArrayList;
import java.util.List;

@Leaf(module = "student", path = "/followrecord/notiothers/") public class NotiOthersPage
    extends StudentBaseFragment<StPageNotiOthersBinding, NotiOthersVM> {
  CommonUserSelectView studentListView = new CommonUserSelectView();
  @Need public ArrayList<Staff> staffs;

  @Override protected void subscribeUI() {
    mViewModel.getEditAfterTextChange().observe(this, filter -> {
      studentListView.filter(filter);
    });
  }

  @Override
  public StPageNotiOthersBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    getChildFragmentManager().registerFragmentLifecycleCallbacks(childrenCB, false);
    mBinding = StPageNotiOthersBinding.inflate(inflater);
    mBinding.setViewModel(mViewModel);
    mBinding.setLifecycleOwner(this);
    initToolbar();
    return mBinding;
  }

  @Override protected void onChildViewCreated(FragmentManager fm, Fragment f, View v,
      Bundle savedInstanceState) {
    if (f instanceof CommonUserSelectView) {
      if (staffs != null) {
        List<CommonUserItem> list = new ArrayList<>();
        for (Staff staff : staffs) {
          list.add(new CommonUserItem(staff));
        }
        studentListView.setDatas(list);
      }
      studentListView.setBtnRightListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          RxBus.getBus().post(new EventCommonUserList(studentListView.getSelectUser()));
          getActivity().onBackPressed();
        }
      });
    }
  }

  @Override protected void onFinishAnimation() {
    super.onFinishAnimation();
    stuff(R.id.frag_noti_ohter, studentListView);
  }

  private void initToolbar() {

    ToolbarModel toolbarModel = new ToolbarModel("选择通知对象");
    toolbarModel.setMenu(R.menu.menu_cancel);
    toolbarModel.setListener(item -> {
      popBack();
      return true;
    });
    // TODO: 2018/7/5  设置Action 颜色
    mBinding.setToolbarModel(toolbarModel);
    initToolbar(mBinding.includeToolbar.toolbar);
    if (!CompatUtils.less21()
        && mBinding.includeToolbar.toolbar.getParent() instanceof ViewGroup
        && this.isfitSystemPadding()) {
      RelativeLayout.LayoutParams layoutParams =
          (RelativeLayout.LayoutParams) mBinding.rbSelectAll.getLayoutParams();
      layoutParams.setMargins(0, MeasureUtils.getStatusBarHeight(this.getContext()), 0, 0);
      mBinding.rbSelectAll.setLayoutParams(layoutParams);
    }
    mBinding.rbSelectAll.setOnCheckedChangeListener((buttonView, isChecked) -> {
      if (studentListView == null || !studentListView.isAdded()) {
        return;
      }
      if (isChecked) {
        studentListView.selectAll();
      } else {
        studentListView.clearSelect();
      }
    });
  }
}
