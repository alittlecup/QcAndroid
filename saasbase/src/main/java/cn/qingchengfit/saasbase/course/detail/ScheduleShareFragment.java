package cn.qingchengfit.saasbase.course.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.common.BottomChooseData;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.databinding.ScheduleSpellFragmentBinding;
import cn.qingchengfit.saasbase.routers.SaasbaseParamsInjector;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.widgets.BottomChooseDialog;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.bigkoo.pickerview.lib.SimpleScrollPicker;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
@Leaf(module = "course",path = "/schedule/share")
public class ScheduleShareFragment
    extends SaasBindingFragment<ScheduleSpellFragmentBinding, ScheduleDetailVM> {
  @Need String scheduleID;

  @Override protected void subscribeUI() {
    mViewModel.scheduleShareDetail.observe(this, this::updateView);
  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    SaasbaseParamsInjector.inject(this);
  }

  private void updateView(ScheduleShareDetail detail) {
    if (detail == null) return;
    mBinding.switchClass.setChecked(detail.isShared());
    mBinding.edRemarks.setText(detail.getSharedText());
    mBinding.civSpellCount.setContent(String.valueOf(detail.getShareUser()));
    mBinding.civSpellType.setContent(detail.isPublicShared() ? "公开" : "私密");
  }

  @Override
  public ScheduleSpellFragmentBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = ScheduleSpellFragmentBinding.inflate(inflater, container, false);
    initToolbar();
    mViewModel.loadScheduleShare(scheduleID);
    return mBinding;
  }

  SimpleScrollPicker chooseCountPicker;
  int position = 0;

  public void showChooseCountDialog(View v) {
    ScheduleShareDetail value = mViewModel.scheduleShareDetail.getValue();
    if (value == null) return;

    if (chooseCountPicker == null) {
      chooseCountPicker = new SimpleScrollPicker(getContext());
    }
    chooseCountPicker.show(1, value.getMaxUsers() - value.getFirstCount(), position);
    chooseCountPicker.setListener(new SimpleScrollPicker.SelectItemListener() {
      @Override public void onSelectItem(int pos) {
        position = pos;
        mBinding.civSpellCount.setContent(String.valueOf(pos + 1));
      }
    });
  }

  BottomChooseDialog bottomChooseDialog;

  public void showChooseShareTypeDialog(View view) {
    if (bottomChooseDialog == null) {
      List<BottomChooseData> types = new ArrayList<>();
      types.add(new BottomChooseData("公开","所有会员可在私教列表中进行拼课"));
      types.add(new BottomChooseData("私密","会员仅可通过分享链接进行拼课"));
      bottomChooseDialog = new BottomChooseDialog(getContext(), "请选择拼课类型", types);
      bottomChooseDialog.setOnItemClickListener(new BottomChooseDialog.onItemClickListener() {
        @Override public boolean onItemClick(int position) {
          mBinding.civSpellType.setContent(types.get(position).getContent().toString());
          return true;
        }
      });
    }

    bottomChooseDialog.show();
  }

  public void save(View view) {
    Map<String, Object> body = new HashMap<>();
    body.put("is_shared", mBinding.switchClass.isChecked());
    body.put("is_public_shared", "公开".equals(mBinding.civSpellType.getContent()));
    body.put("is_public_shared", mBinding.edRemarks.getText());
    body.put("can_shared_max_user", mBinding.civSpellCount.getContent());
    mViewModel.putScheduleShare(scheduleID, body);
  }

  private void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel("拼课设置"));
    initToolbar(mBinding.includeToolbar.toolbar);
  }
}
