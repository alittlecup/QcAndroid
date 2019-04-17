package cn.qingchengfit.staffkit.views.signin.config;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.staffkit.databinding.FragmentSigninTimeSettingBinding;
import cn.qingchengfit.staffkit.views.ChooseActivity;
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

  @Override protected void subscribeUI() {

  }

  @Override public FragmentSigninTimeSettingBinding initDataBinding(LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    mBinding = FragmentSigninTimeSettingBinding.inflate(inflater, container, false);
    initListener();
    mBinding.setToolbarModel(new ToolbarModel("入场时间"));
    initToolbar(mBinding.includeToolbar.toolbar);
    return mBinding;
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
      datas.add(String.valueOf(i) + "个小时");
    }
    if (simpleScrollPicker == null) {
      simpleScrollPicker = new SimpleScrollPicker(getContext());
      simpleScrollPicker.setTvLeft("修改扣费方式");
      simpleScrollPicker.setLabel("有效时间");
    }
    simpleScrollPicker.setListener(pos1 -> {
      String s = datas.get(pos1);
      if (pos1 == 0) {
        mBinding.tvDurationContent.setText("每次入场都扣费");
      } else {
        mBinding.tvDurationContent.setText(s + "内入场不重复扣费");
      }
    });
    simpleScrollPicker.show(datas, pos);
  }
}
