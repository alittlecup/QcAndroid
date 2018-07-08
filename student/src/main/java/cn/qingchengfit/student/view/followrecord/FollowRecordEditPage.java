package cn.qingchengfit.student.view.followrecord;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.databinding.StPageFollowRecordEditBinding;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.bigkoo.pickerview.lib.SimpleScrollPicker;
import java.util.ArrayList;

@Leaf(module = "student", path = "/student/follow_record_edit") public class FollowRecordEditPage
    extends StudentBaseFragment<StPageFollowRecordEditBinding, FollowRecordEditViewModel> {
  @Override protected void subscribeUI() {

  }

  @Override
  public StPageFollowRecordEditBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = StPageFollowRecordEditBinding.inflate(inflater, container, false);
    initToolbar();
    initListener();
    return mBinding;
  }

  private void initListener() {
    mBinding.cmChooseType.setOnClickListener(view -> {
      showFollowTypeDialog();
    });
    mBinding.cmChooseTime.setOnClickListener(view -> {
      showFollowTimeDialog();
    });
    mBinding.cmNotifyOther.setOnClickListener(view -> {

    });
    mBinding.tvEditStatus.setOnClickListener(view -> {
      routeTo("/student/follow_record_status", null);
    });
    mBinding.llChooseStatus.setOnClickListener(view->{
      ToastUtils.show("click follow status");
    });
  }

  private void initToolbar() {
    ToolbarModel toolbarModel = new ToolbarModel("写跟进");
    toolbarModel.setMenu(R.menu.menu_compelete);
    toolbarModel.setListener(item -> {
      onComplete();
      return false;
    });
    // TODO: 2018/7/5  设置Action 颜色
    mBinding.setToolbarModel(toolbarModel);
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  private void onComplete() {

  }

  private void showFollowTimeDialog() {
    SimpleScrollPicker picker = new SimpleScrollPicker(getContext());
    ArrayList<String> dates = new ArrayList<>();
    dates.add("不设定时间");
    for (int i = 0; i < 180; i++) {
      dates.add(DateUtils.addDay(DateUtils.getStringToday(), i));
    }
    picker.show(dates, 0);
    picker.setListener(pos -> {
      mBinding.cmChooseTime.setContent(dates.get(pos));
    });
  }

  private void showFollowTypeDialog() {
    SimpleScrollPicker picker = new SimpleScrollPicker(getContext());
    ArrayList<String> dataList = new ArrayList<>();
    dataList.add("电话");
    dataList.add("微信");
    dataList.add("客户到馆");
    dataList.add("上门拜访");
    picker.show(dataList, 0);
    picker.setListener(pos -> {
      mBinding.cmChooseType.setContent(dataList.get(pos));
    });
  }
}
