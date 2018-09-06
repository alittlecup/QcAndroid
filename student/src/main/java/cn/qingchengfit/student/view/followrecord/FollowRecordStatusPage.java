package cn.qingchengfit.student.view.followrecord;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventClickViewPosition;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.StPageFollowRecordStatusBinding;

import cn.qingchengfit.student.item.FollowRecordStatusItem;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.DividerItemDecoration;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import cn.qingchengfit.widgets.CommonInputView;
import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.anbillon.flabellum.annotations.Leaf;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.common.FlexibleItemDecoration;
import eu.davidea.flexibleadapter.helpers.ItemTouchHelperCallback;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

@Leaf(module = "student", path = "/student/follow_record_status")
public class FollowRecordStatusPage
  extends StudentBaseFragment<StPageFollowRecordStatusBinding, FollowRecordStatusViewModel>
  implements FlexibleAdapter.OnItemClickListener{
  CommonFlexAdapter adapter;

  @Override protected void subscribeUI() {

  }

  @Override public StPageFollowRecordStatusBinding initDataBinding(LayoutInflater inflater,
    ViewGroup container, Bundle savedInstanceState) {
    mBinding = StPageFollowRecordStatusBinding.inflate(inflater, container, false);
    mBinding.setLifecycleOwner(this);
    mBinding.setVm(mViewModel);
    initToolbar();
    initRecyclerView();
    HashMap<String,Object> map = new HashMap<>();
    mViewModel.loadSource(map);
    RxBusAdd(EventClickViewPosition.class)
      .subscribe(new BusSubscribe<EventClickViewPosition>() {
        @Override public void onNext(EventClickViewPosition eventClickViewPosition) {
          FollowRecordStatusItem item =  mViewModel.getLiveItems().getValue().get(eventClickViewPosition.getPosition());
          String statusId = item.getStatus().getId();
          if (eventClickViewPosition.getId() == R.id.btn_del){
            //删除
            deleteFollorStatus(statusId);
          }else if (eventClickViewPosition.getId() == R.id.btn_edit){
            //编辑
            editFollowStatus(statusId,item.getStatus().getTrack_status());
          }
        }
      });
    return mBinding;
  }

  private void initRecyclerView() {
    mBinding.refreshLayout.setEnabled(false);
    mBinding.recyclerView.addItemDecoration(new FlexibleItemDecoration(getContext())
      .withOffset(1)
      .withBottomEdge(true));
    adapter = new CommonFlexAdapter(new ArrayList(), this);
    mBinding.recyclerView.setAdapter(adapter);

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
    DialogUtils.showInputDialog(getContext(), "", "请输入会员分类名称(仅限输入10字)", "","取消", "确定", new MaterialDialog.SingleButtonCallback() {
      @Override public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
        if (materialDialog.getInputEditText() != null && materialDialog.getInputEditText().getText()!=null)
          mViewModel.addFollowStatus(
            materialDialog.getInputEditText() != null ? Objects.requireNonNull(
              materialDialog.getInputEditText()).getText().toString().trim() : null);
      }
    });
  }

  private void editFollowStatus(String statusId,String conent) {
    DialogUtils.showInputDialog(getContext(), "", "请输入会员分类名称(仅限输入10字)",conent, "取消", "确定", new MaterialDialog.SingleButtonCallback() {
      @Override public void onClick(MaterialDialog materialDialog, DialogAction dialogAction) {
        if (materialDialog.getInputEditText() != null && materialDialog.getInputEditText().getText()!=null)
          mViewModel.editFollowStatus(statusId,
            materialDialog.getInputEditText() != null ? Objects.requireNonNull(
              materialDialog.getInputEditText()).getText().toString().trim() : null);
      }
    });
  }


  private void deleteFollorStatus(String id) {
    DialogUtils.showConfirm(getContext(), "确定要删除该跟进状态吗？", "删除后，该状态下会员的跟进状态将变为空",
      (materialDialog, dialogAction) -> {
        materialDialog.dismiss();
        if (dialogAction == DialogAction.POSITIVE) {
          mViewModel.deleteFollowStatus(id);
        }
      });
  }

  @Override protected void handleHttpSuccess(String s) {

    mViewModel.loadSource(new HashMap<>());
  }

  @Override public boolean onItemClick(int position) {

    return true;
  }
}
