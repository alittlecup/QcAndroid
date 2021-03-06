package cn.qingchengfit.student.view.followrecord;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventChooseImage;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.base.StudentBean;
import cn.qingchengfit.model.base.User;
import cn.qingchengfit.model.common.ICommonUser;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.events.EventCommonUserList;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.StudentBaseFragment;
import cn.qingchengfit.student.bean.FollowRecordStatus;
import cn.qingchengfit.student.bean.SalerUserListWrap;
import cn.qingchengfit.student.bean.StudentTransferBean;
import cn.qingchengfit.student.bean.StudentWrap;
import cn.qingchengfit.student.databinding.StPageFollowRecordEditBinding;
import cn.qingchengfit.student.item.ItemGridImage;
import cn.qingchengfit.student.item.ItemGridImageAdd;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.ChoosePictureFragmentNewDialog;
import cn.qingchengfit.views.fragments.MultiChoosePicFragment;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.bigkoo.pickerview.lib.SimpleScrollPicker;
import com.trello.rxlifecycle.android.FragmentEvent;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.inject.Inject;

@Leaf(module = "student", path = "/student/follow_record_edit") public class FollowRecordEditPage
    extends StudentBaseFragment<StPageFollowRecordEditBinding, FollowRecordEditViewModel>
    implements FlexibleAdapter.OnItemClickListener {
  private MultiChoosePicFragment picDialog;
  CommonFlexAdapter adapter = new CommonFlexAdapter(new ArrayList(), this);
  @Inject StudentWrap studentWrap;
  @Inject IPermissionModel permissionModel;

  @Override protected void subscribeUI() {
    mViewModel.showLoading.observe(this, aBoolean -> {
      if (aBoolean) {
        showLoading();
      } else {
        hideLoading();
      }
    });
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
            if (eventCommonUserList.getCommonUsers() != null) {
              List<User> users = new ArrayList<>();
              List<String> ids = new ArrayList<>();
              selected.clear();
              for (ICommonUser iCommonUser : eventCommonUserList.getCommonUsers()) {
                User u = new User();
                u.setId(iCommonUser.getId());
                u.setUsername(iCommonUser.getTitle());
                u.setAvatar(iCommonUser.getAvatar());
                users.add(u);
                ids.add(iCommonUser.getId());
                if(iCommonUser instanceof Staff){
                  selected.add((Staff) iCommonUser);
                }
              }
              mViewModel.notiOthers.setValue(users);
              mViewModel.userIds.setValue(ids);
            }
          }
        });
  }

  private List<Staff> selected=new ArrayList<>();

  @Override protected void handleHttpSuccess(String s) {
    if (s.equalsIgnoreCase("add")) {
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
    mViewModel.loadSellers();
    initToolbar();
    initListener();
    initRv();
    return mBinding;
  }

  private void initRv() {
    mBinding.rvPics.setLayoutManager(new GridLayoutManager(getContext(), 4));
    mBinding.rvPics.setAdapter(adapter);

    if (permissionModel.check(PermissionServerUtils.MANAGE_MEMBERS_FOLLOW_STATUS_CAN_EDIT)) {
      mBinding.tvEditStatus.setVisibility(View.VISIBLE);
    } else {
      mBinding.tvEditStatus.setVisibility(View.GONE);
    }
  }

  private void initListener() {
    mBinding.cmChooseType.setOnClickListener(view -> {
      showFollowTypeDialog();
    });
    mBinding.cmChooseTime.setOnClickListener(view -> {
      showFollowTimeDialog();
    });
    mBinding.cmNotifyOther.setOnClickListener(view -> {
      List<Staff> value = mViewModel.getSalerStaffs().getValue();
      if (value == null) {
        mViewModel.loadSellers();
        showLoading();
        return;
      }
      routeTo("/followrecord/notiothers/", NotiOthersPageParams.builder()
          .staffs(new ArrayList<>(value))
          .selecteds(new ArrayList<>(selected))
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
    mBinding.editContent.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override public void afterTextChanged(Editable s) {
        if (s != null && s.toString().length() >= 140) {
          ToastUtils.show("最多输入140个字符");
        }
      }
    });
  }

  void addImage() {
    picDialog = MultiChoosePicFragment.newInstance(mViewModel.followRecordUrl);
    picDialog.setUpLoadImageCallback(uris -> {
      mViewModel.followRecordUrl.clear();
      mViewModel.followRecordUrl.addAll(uris);
      adapter.clear();
      for (String s : uris) {
        adapter.addItem(new ItemGridImage(s));
      }
      if (uris.size() < 5) adapter.addItem(new ItemGridImageAdd());
      mBinding.chooseImg.setVisibility(uris.size() > 0 ? View.GONE : View.VISIBLE);
    });
    if (!picDialog.isVisible()) picDialog.show(getChildFragmentManager(), "");
  }

  private void initToolbar() {
    ToolbarModel toolbarModel = new ToolbarModel("写跟进");
    toolbarModel.setMenu(R.menu.menu_compelete);
    toolbarModel.setListener(item -> {
      if (TextUtils.isEmpty(mViewModel.content.getValue())) {
        DialogUtils.showAlert(getContext(), "您尚未填写跟进详情");
      } else {
        mViewModel.addFollowRecord();
      }
      return false;
    });
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
      mViewModel.nextFollowTime.setValue(dates.get(pos));
    });
  }

  private void showFollowTypeDialog() {
    SimpleScrollPicker picker = new SimpleScrollPicker(getContext());
    ArrayList<String> dataList = new ArrayList<>();
    for (String s : getResources().getStringArray(R.array.st_follow_record_method)) {
      dataList.add(s);
    }
    int position = 0;
    if (mViewModel.followMethod.getValue() != null) {
      position = mViewModel.followMethod.getValue() - 1;
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
    } else {
      return;
    }
    picker.show(dataList, position);
    picker.setListener(pos -> {
      mViewModel.followStatus.setValue(mViewModel.followRecordStatus.getValue().get(pos));
    });
  }

  @Override public boolean onItemClick(int position) {
    IFlexible item = adapter.getItem(position);
    if (item == null) return false;
    addImage();
    return true;
  }
}
