package cn.qingchengfit.student.view.followrecord;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.StPageFollowRecordStatusBinding;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.CommonInputView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;
import java.util.ArrayList;
@Leaf(module = "student",path = "/student/follow_record_status")
public class FollowRecordStatusPage
    extends StudentBaseFragment<StPageFollowRecordStatusBinding, FollowRecordStatusViewModel> {
  CommonFlexAdapter adapter;

  @Override protected void subscribeUI() {

  }

  @Override public StPageFollowRecordStatusBinding initDataBinding(LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    mBinding = StPageFollowRecordStatusBinding.inflate(inflater, container, false);
    initToolbar();
    initRecyclerView();
    return mBinding;
  }

  private void initRecyclerView() {
    mBinding.refreshLayout.setEnabled(false);
    mBinding.recyclerView.setAdapter(adapter = new CommonFlexAdapter(new ArrayList(), this));
    mBinding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
  }

  private void initToolbar() {
    ToolbarModel toolbarModel = new ToolbarModel("编辑跟进状态");
    toolbarModel.setMenu(R.menu.menu_add);
    toolbarModel.setListener(item -> {
      addFollowStatus();
      return false;
    });
    mBinding.setToolbarModel(toolbarModel);
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  private void addFollowStatus() {

  }

  private void deleteFollorStatus() {
    DialogUtils.showConfirm(getContext(), "确定要删除该跟进状态吗？", "删除后，该状态下会员的跟进状态将变为空",
        (materialDialog, dialogAction) -> {
          materialDialog.dismiss();
          if (dialogAction == DialogAction.POSITIVE) {
            mViewModel.deleteFollowStatus();
          }
        });
  }
}
