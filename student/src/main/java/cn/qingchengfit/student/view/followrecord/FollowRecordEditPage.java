package cn.qingchengfit.student.view.followrecord;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventChooseImage;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.base.User;
import cn.qingchengfit.model.common.ICommonUser;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.events.EventCommonUserList;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.bean.FollowRecordStatus;
import cn.qingchengfit.student.bean.SalerUserListWrap;
import cn.qingchengfit.student.databinding.StPageFollowRecordEditBinding;
import cn.qingchengfit.student.item.ItemGridImage;
import cn.qingchengfit.student.item.ItemGridImageAdd;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentNewDialog;
import cn.qingchengfit.views.fragments.MultiChoosePicFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.bigkoo.pickerview.lib.SimpleScrollPicker;
import com.trello.rxlifecycle.android.FragmentEvent;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Leaf(module = "student", path = "/student/follow_record_edit") public class FollowRecordEditPage
  extends StudentBaseFragment<StPageFollowRecordEditBinding, FollowRecordEditViewModel>
  implements FlexibleAdapter.OnItemClickListener {
  private MultiChoosePicFragment picDialog;
  CommonFlexAdapter adapter = new CommonFlexAdapter(new ArrayList(), this);

  @Override protected void subscribeUI() {

  }

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    RxBus.getBus()
      .register(EventCommonUserList.class)
      .compose(bindToLifecycle())
      .compose(doWhen(FragmentEvent.CREATE_VIEW))
      .compose(this.bindToLifecycle())
      .subscribe(new BusSubscribe<EventCommonUserList>() {
        @Override public void onNext(EventCommonUserList eventCommonUserList) {
          if (eventCommonUserList.getCommonUsers() != null){
            List<User> users = new ArrayList<>();
            for (ICommonUser iCommonUser : eventCommonUserList.getCommonUsers()) {
              User u = new User();
              u.setId(iCommonUser.getId());
              u.setUsername(iCommonUser.getTitle());
              u.setAvatar(iCommonUser.getAvatar());
              users.add(u);
            }
            mViewModel.notiOthers.setValue(users);
          }
        }
      });
  }

  @Override protected void handleHttpSuccess(String s) {
    if (s.equalsIgnoreCase("add")){
      ToastUtils.show("添加成功");
      popBack();
    }
  }

  @Override
  public StPageFollowRecordEditBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
    Bundle savedInstanceState) {
    mBinding = StPageFollowRecordEditBinding.inflate(inflater, container, false);
    mBinding.setLifecycleOwner(this);
    mBinding.setVm(mViewModel);
    mViewModel.loadFollowRecordStatus();
    initToolbar();
    initListener();
    initRv();
    return mBinding;
  }

  private void initRv() {
    mBinding.rvPics.setLayoutManager(new GridLayoutManager(getContext(),4));
    mBinding.rvPics.setAdapter(adapter);
  }

  private void initListener() {
    mBinding.cmChooseType.setOnClickListener(view -> {
      showFollowTypeDialog();
    });
    mBinding.cmChooseTime.setOnClickListener(view -> {
      showFollowTimeDialog();
    });
    mBinding.cmNotifyOther.setOnClickListener(view -> {
      ArrayList<Staff> staffs = new ArrayList<>();
      staffs.add(new Staff(new User("志恒","123123","123123",1),"25"));
      staffs.add(new Staff(new User("志恒1","123123","123123",1),"26"));
      staffs.add(new Staff(new User("志恒2","123123","123123",1),"27"));
      routeTo("/followrecord/notiothers/",NotiOthersPageParams.builder()
        .staffs(staffs)
        .build());
    });
    mBinding.tvEditStatus.setOnClickListener(view -> {
      routeTo("/student/follow_record_status", null);
    });
    mBinding.llChooseStatus.setOnClickListener(view -> {
      showFollowStatusDialog();
    });
    mBinding.chooseImg.setOnClickListener(v -> {
      addImage();
    });
  }

  void addImage() {
    if (mViewModel.followRecordUrl.size() >= 5) {
      ToastUtils.show("最多只能添加5张");
      return;
    }
    if (picDialog == null) {
      picDialog = MultiChoosePicFragment.newInstance(mViewModel.followRecordUrl);
      picDialog.setUpLoadImageCallback(uris -> {
        mViewModel.followRecordUrl.clear();
        mViewModel.followRecordUrl.addAll(uris);
        adapter.clear();
        for (String s : uris) {
          adapter.addItem(new ItemGridImage(s));
        }
        if (uris.size() < 5)
          adapter.addItem(new ItemGridImageAdd());
      });
    }
    if (!picDialog.isVisible()) picDialog.show(getChildFragmentManager(), "");
  }

  private void initToolbar() {
    ToolbarModel toolbarModel = new ToolbarModel("写跟进");
    toolbarModel.setMenu(R.menu.menu_compelete);
    toolbarModel.setListener(item -> {
      mViewModel.addFollowRecord();
      return false;
    });
    // TODO: 2018/7/5  设置Action 颜色
    mBinding.setToolbarModel(toolbarModel);
    initToolbar(mBinding.includeToolbar.toolbar);
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
      mViewModel.nextFollowTime.setValue(
        DateUtils.DateToServer(DateUtils.addDay(new Date(DateUtils.getToadayMidnight()), pos)));
    });
  }

  private void showFollowTypeDialog() {
    SimpleScrollPicker picker = new SimpleScrollPicker(getContext());
    ArrayList<String> dataList = new ArrayList<>();
    for (String s : getResources().getStringArray(R.array.st_follow_record_method)) {
      dataList.add(s);
    }
    int position = 0;
    if (mViewModel.followMethod.getValue() != null){
      position = mViewModel.followMethod.getValue() -1;
    }
    picker.show(dataList, position);
    picker.setListener(pos -> {
      mViewModel.followMethod.setValue(pos + 1);
    });
  }

  /**
   * 展示跟进状态
   */
  private void showFollowStatusDialog() {
    SimpleScrollPicker picker = new SimpleScrollPicker(getContext());
    ArrayList<String> dataList = new ArrayList<>();
    int position = 0;
    if (mViewModel.followRecordStatus.getValue() != null) {
      for (int i = 0; i < mViewModel.followRecordStatus.getValue().size(); i++) {
        FollowRecordStatus followRecordStatus = mViewModel.followRecordStatus.getValue().get(i);
        if (TextUtils.equals(Objects.requireNonNull(followRecordStatus.getTrack_status()),
          Objects.requireNonNull(mBinding.tvFollowStatus.getText().toString()))) {
          position = i;
        }
        dataList.add(followRecordStatus.getTrack_status());
      }
    }else return;
    picker.show(dataList, position);
    picker.setListener(pos -> {
      mViewModel.followStatus.setValue(mViewModel.followRecordStatus.getValue().get(pos));
    });
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = adapter.getItem(position);
    if (item == null) return false;
    if (item instanceof ItemGridImageAdd) {
      addImage();
    }
    return true;
  }


}
