package cn.qingchengfit.staffkit.views.signin.config;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.databinding.FragmentSigninTimeSettingBinding;
import cn.qingchengfit.staffkit.views.ChooseActivity;
import cn.qingchengfit.staffkit.views.signin.bean.SignInTimeFrameBean;
import cn.qingchengfit.utils.BundleBuilder;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.IntentUtils;
import com.bigkoo.pickerview.SimpleScrollPicker;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.jakewharton.rxbinding.view.RxView;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;

/**
 * @author huangbaole
 */
public class SignInTimeSettingFragment
    extends SaasBindingFragment<FragmentSigninTimeSettingBinding, SignInTimeSettingVM> {
  @Inject GymWrapper gymWrapper;
  private SignInTimeFrameBean bean;

  @Override protected void subscribeUI() {

  }

  public static SignInTimeSettingFragment getInstance(SignInTimeFrameBean bean) {
    SignInTimeSettingFragment fragment = new SignInTimeSettingFragment();
    fragment.setArguments(new BundleBuilder().withParcelable("bean", bean).build());
    return fragment;
  }

  @Override public FragmentSigninTimeSettingBinding initDataBinding(LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    mBinding = FragmentSigninTimeSettingBinding.inflate(inflater, container, false);

    initListener();
    initToolbar();
    mBinding.civTrainCount.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
    if (getArguments() != null) {
      bean = getArguments().getParcelable("bean");
      updateView(bean);
    }
    return mBinding;
  }

  private void updateView(SignInTimeFrameBean bean) {
    if (bean != null) {
      mBinding.civTimeStart.setContent(bean.getStartTime());
      mBinding.civTimeEnd.setContent(bean.getEndTime());
      mBinding.btnDel.setVisibility(View.VISIBLE);
      setDurationPos(bean.getExpireTime() / 60);
      mBinding.civTrainCount.setContent(String.valueOf(bean.getMaxUsers()));
      mBinding.civPayOnce.setContent(bean.isSupportOnlinePay() ? "已设置" : "未设置");
      mBinding.civPayCard.setContent(bean.isSupportCardPay() ? "已设置" : "未设置");
      mBinding.civTimeWeek.setContent("每周" + bean.getTimeFrameWeekWithSplit("/"));
    } else {
      mBinding.btnDel.setVisibility(View.GONE);
    }
  }

  private void initToolbar() {
    ToolbarModel toolbarModel = new ToolbarModel("入场时段");
    toolbarModel.setMenu(R.menu.menu_save);
    toolbarModel.setListener(menuItem -> {
      return false;
    });
    mBinding.setToolbarModel(toolbarModel);
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  private void initListener() {
    RxView.clicks(mBinding.civTimeStart)
        .compose(RxHelper.schedulersClickTransformer())
        .subscribe(v -> {
          showChooseTimeDialog(DateUtils.HHMM2date(mBinding.civTimeStart.getContent()), true);
        });
    RxView.clicks(mBinding.civTimeEnd)
        .compose(RxHelper.schedulersClickTransformer())
        .subscribe(v -> {
          showChooseTimeDialog(DateUtils.HHMM2date(mBinding.civTimeEnd.getContent()), false);
        });
    RxView.clicks(mBinding.civTimeWeek)
        .compose(RxHelper.schedulersClickTransformer())
        .subscribe(v -> {
          routeTo(new SignTimeWeekChooseFragment());
        });
    RxView.clicks(mBinding.tvChangeDuration)
        .compose(RxHelper.schedulersClickTransformer())
        .subscribe(v -> {
          String text = mBinding.tvDurationContent.getText().toString();
          int pos = 0;
          if (!TextUtils.isEmpty(text)) {
            pos = Integer.parseInt(text.substring(0, 1));
          }
          showChooseDuration(pos > 24 ? 0 : pos);
        });

    RxView.clicks(mBinding.civPayOnce)
        .compose(RxHelper.schedulersClickTransformer())
        .subscribe(v -> {
          routeTo(new SignInTimePayOnceFragment());
        });
    RxView.clicks(mBinding.civPayCard)
        .compose(RxHelper.schedulersClickTransformer())
        .subscribe(v -> {
          routeToCardConfig();
        });
  }

  private void routeToCardConfig() {
    Intent x = IntentUtils.getChooseIntent(getContext(), gymWrapper.getCoachService(),
        gymWrapper.getBrand(), ChooseActivity.SIGN_IN_CARDS);
    //x.putExtra("costs", cardCosts);
    startActivityForResult(x, ChooseActivity.SIGN_IN_CARDS);
  }

  private TimeDialogWindow timeDialogWindow;

  private void showChooseTimeDialog(Date date, boolean isStart) {
    if (timeDialogWindow == null) {
      timeDialogWindow = new TimeDialogWindow(getContext(), TimePopupWindow.Type.HOURS_MINS, 5);
    }
    timeDialogWindow.setOnTimeSelectListener(date1 -> {
      String timeHHMM = DateUtils.getTimeHHMM(date1);
      if (isStart) {
        mBinding.civTimeStart.setContent(timeHHMM);
      } else {
        mBinding.civTimeEnd.setContent(timeHHMM);
      }
    });
    timeDialogWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, date);
  }

  private SimpleScrollPicker simpleScrollPicker;

  private void showChooseDuration(int pos) {
    List<String> datas = new ArrayList<>();
    int hourLength = 24;
    for (int i = 0; i <= hourLength; i++) {
      datas.add(i + "个小时");
    }
    if (simpleScrollPicker == null) {
      simpleScrollPicker = new SimpleScrollPicker(getContext());
      simpleScrollPicker.setTvLeft("修改扣费方式");
      simpleScrollPicker.setLabel("有效时间");
    }
    simpleScrollPicker.setListener(this::setDurationPos);
    simpleScrollPicker.show(datas, pos);
  }

  private void setDurationPos(int pos) {
    if (pos == 0) {
      mBinding.tvDurationContent.setText("每次入场都扣费");
    } else {
      mBinding.tvDurationContent.setText(pos + "个小时内入场不重复扣费");
    }
  }
}
