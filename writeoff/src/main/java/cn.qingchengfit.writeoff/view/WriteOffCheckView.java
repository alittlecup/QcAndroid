package cn.qingchengfit.writeoff.view;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.base.Course;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.common.BottomChooseData;
import cn.qingchengfit.saascommon.SaasCommonFragment;
import cn.qingchengfit.saascommon.events.EventCourse;
import cn.qingchengfit.saascommon.events.EventStaffWrap;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.LogUtil;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.BottomChooseDialog;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.writeoff.databinding.WrViewWriteOffCheckBinding;
import cn.qingchengfit.writeoff.view.verify.WriteOffCheckPage;
import cn.qingchengfit.writeoff.view.verify.WriteOffCheckViewModel;
import cn.qingchengfit.writeoff.vo.ITicketDetailData;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.trello.rxlifecycle.android.FragmentEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import rx.android.schedulers.AndroidSchedulers;

public class WriteOffCheckView extends SaasCommonFragment {
  WrViewWriteOffCheckBinding mBinding;
  private BottomChooseDialog chooseBatchTypeDialog;
  WriteOffCheckViewModel mViewModel;
  TimeDialogWindow chooseOpenTimeDialog;

  @Nullable @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
      @Nullable Bundle savedInstanceState) {
    super.onCreateView(inflater, container, savedInstanceState);
    mBinding = WrViewWriteOffCheckBinding.inflate(inflater, container, false);
    if (getParentFragment() instanceof WriteOffCheckPage) {
      mViewModel = ViewModelProviders.of(getParentFragment()).get(WriteOffCheckViewModel.class);
      initListener();
      initRxbus();
    } else {
      disableView();
    }
    if (data != null) {
      mBinding.setTicketData(data);
    }
    return mBinding.getRoot();
  }

  private void disableView() {
    int childCount = mBinding.llContainer.getChildCount();
    for (int i = 0; i < childCount; i++) {
      View childAt = mBinding.llContainer.getChildAt(i);
      if (childAt instanceof CommonInputView) {
        ((CommonInputView) childAt).setEnable(false);
        ((CommonInputView) childAt).setShowRight(false);
      }
    }
  }

  private ITicketDetailData data;

  public void setTicket(ITicketDetailData data) {
    if (mBinding != null) {
      mBinding.setTicketData(data);
    } else {
      this.data = data;
    }
  }

  private void initRxbus() {
   RxBusAdd(EventCourse.class)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BusSubscribe<EventCourse>() {
          @Override public void onNext(EventCourse course) {
            setCourse(course.getCourse());
          }
        });
    RxBusAdd(EventStaffWrap.class).onBackpressureDrop()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BusSubscribe<EventStaffWrap>() {
          @Override public void onNext(EventStaffWrap eventStaffWrap) {
            setTrainer(eventStaffWrap.getStaff());
          }
        });
  }

  private void setCourse(Course course) {
    mBinding.cmChooseBatch.setContent(course.getName());
    mViewModel.ticketPostBody.getValue().setCourse_id(course.getId());
  }

  private void setTrainer(Staff staff) {
    mBinding.cmChooseTrainer.setContent(staff.getUsername());
    mViewModel.ticketPostBody.getValue().setTeacher_id(staff.getId());
  }

  private boolean isPrivate() {
    return mViewModel.ticketPostBody.getValue().isPrivate();
  }

  private void initListener() {
    mBinding.cmChooseBatchType.setOnClickListener(v -> showChooseBatchType());
    mBinding.cmChooseBatch.setOnClickListener(v -> {
      int usedType = mViewModel.ticketPostBody.getValue().getUsedType();
      if (usedType != 1 && usedType != 2) {
        ToastUtils.show("请选择课程类型");
        return;
      }
      Bundle bundle = new Bundle();
      bundle.putString("courseId", "");
      bundle.putBoolean("mIsPrivate", isPrivate());
      routeTo("course", "/choose/", bundle);
    });
    mBinding.cmChooseTrainer.setOnClickListener(v -> {
      Bundle bundle = new Bundle();
      bundle.putString("selectedId", "");
      routeTo("staff", "/trainer/choose/", bundle);
    });
    mBinding.cmChooseTime.setOnClickListener(v -> chooseOpenTime());
    mBinding.cmInputCount.addTextWatcher(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override public void afterTextChanged(Editable s) {
        String s1 = s.toString();
        if (!TextUtils.isEmpty(s1)) {
          try {
            mViewModel.ticketPostBody.getValue().setPeople_num(Integer.valueOf(s1));
          } catch (Exception e) {
            ToastUtils.show("请输入正确的数字");
          }
        } else {
          mViewModel.ticketPostBody.getValue().setPeople_num(0);
        }
      }
    });
  }

  public void chooseOpenTime() {
    if (chooseOpenTimeDialog == null) {
      chooseOpenTimeDialog = new TimeDialogWindow(getContext(), TimePopupWindow.Type.ALL);
      chooseOpenTimeDialog.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
        @Override public void onTimeSelect(Date date) {
          String s = DateUtils.Date2YYYYMMDDHHmm(date);
          mBinding.cmChooseTime.setContent(s);
          mViewModel.ticketPostBody.getValue().setUsed_at(s);
        }
      });
    }
    chooseOpenTimeDialog.setRange(DateUtils.getYear(new Date()) - 1,
        DateUtils.getYear(new Date()) + 1);
    Date d = new Date();
    if (!TextUtils.isEmpty(mBinding.cmChooseTime.getContent())) {
      try {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-DD HH:mm", Locale.CHINA);
        d = formatter.parse(mBinding.cmChooseTime.getContent());
      } catch (Exception e) {
        LogUtil.e(e.getMessage());
      }
    }

    chooseOpenTimeDialog.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, d);
  }

  private void showChooseBatchType() {
    if (chooseBatchTypeDialog == null) {
      List<BottomChooseData> batchType = new ArrayList<>();
      batchType.add(new BottomChooseData("团课"));
      batchType.add(new BottomChooseData("私教"));
      chooseBatchTypeDialog = new BottomChooseDialog(getContext(), "课程类型", batchType);
      chooseBatchTypeDialog.setOnItemClickListener(position -> {
        mBinding.cmChooseBatchType.setContent(batchType.get(position).getContent().toString());
        mViewModel.ticketPostBody.getValue().setBatchType(position != 0);
        mBinding.cmChooseBatch.setContent("");
        mViewModel.ticketPostBody.getValue().setCourse_id("");
        return true;
      });
    }
    chooseBatchTypeDialog.show();
  }
}
