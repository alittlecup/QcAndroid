package cn.qingchengfit.staffkit.views.signin.config;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.InputType;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.model.responese.SignInCardCostBean;
import cn.qingchengfit.saascommon.mvvm.SaasBindingFragment;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.databinding.FragmentSigninTimeSettingBinding;
import cn.qingchengfit.staffkit.views.ChooseActivity;
import cn.qingchengfit.staffkit.views.signin.bean.CardCost;
import cn.qingchengfit.staffkit.views.signin.bean.SignChooseWeekEvent;
import cn.qingchengfit.staffkit.views.signin.bean.SignInTimeFrameBean;
import cn.qingchengfit.staffkit.views.signin.bean.SignInTimePayOnceEvent;
import cn.qingchengfit.staffkit.views.signin.bean.SignInTimeUploadBean;
import cn.qingchengfit.staffkit.views.signin.bean.SignTimeFrame;
import cn.qingchengfit.utils.BundleBuilder;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.IntentUtils;
import cn.qingchengfit.utils.ListUtils;
import cn.qingchengfit.utils.ToastUtils;
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
  private int durationPos = -1;

  @Override protected void subscribeUI() {
    mViewModel.delResult.observe(this, aBoolean -> {
      hideLoading();
      if (aBoolean != null && aBoolean) {
        getActivity().onBackPressed();
      }
    });
    mViewModel.uploadResult.observe(this, bean -> {
      hideLoading();
      if (bean != null) {
        getActivity().onBackPressed();
      }
    });
    mViewModel.civChooseWeek.observe(this, event -> {
      List<Integer> weeks = event.getWeeks();
      StringBuilder stringBuilder = new StringBuilder("每周");
      for (Integer integer : weeks) {
        stringBuilder.append(DateUtils.weeksSimple[integer]).append("/");
      }
      String substring = stringBuilder.substring(0, stringBuilder.length() - 1);
      mBinding.civTimeWeek.setContent(substring);
    });
    mViewModel.civPayOnceEvent.observe(this, event -> {
      mBinding.civPayOnce.setContent(event.isOpen() ? "已设置" : "未设置");
    });
    mViewModel.cardCosts.observe(this, cardCosts1 -> {
      if (cardCosts1 != null && !cardCosts1.isEmpty()) {
        for (int i = 0; i < cardCosts1.size(); i++) {
          SignInCardCostBean.CardCost cardCost = cardCosts1.get(i);
          cardCost.setSelected(false);
          cardCost.setCost(0f);
        }
        allCardCosts = cardCosts1;
      }
    });
  }

  private List<SignInCardCostBean.CardCost> allCardCosts = new ArrayList<>();

  public static SignInTimeSettingFragment getInstance(SignInTimeFrameBean bean) {
    SignInTimeSettingFragment fragment = new SignInTimeSettingFragment();
    fragment.setArguments(new BundleBuilder().withParcelable("bean", bean).build());
    return fragment;
  }

  @Override public FragmentSigninTimeSettingBinding initDataBinding(LayoutInflater inflater,
      ViewGroup container, Bundle savedInstanceState) {
    mBinding = FragmentSigninTimeSettingBinding.inflate(inflater, container, false);
    if (getArguments() != null) {
      bean = getArguments().getParcelable("bean");
    }
    if (durationPos == -1) {
      updateView(bean);
    }

    initListener();
    initToolbar();
    mBinding.civTrainCount.getEditText().setInputType(InputType.TYPE_CLASS_NUMBER);
    mViewModel.loadCardCosts();
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
      upDateTimeWeek(bean);
      mBinding.btnDel.setOnClickListener(v -> {
        delTimeFrame();
      });
      cardCosts = new ArrayList<>(bean.getCardCosts());
    } else {
      mBinding.btnDel.setVisibility(View.GONE);
      mBinding.civPayOnce.setContent("未设置");
      mBinding.civPayCard.setContent("未设置");
      setDurationPos(0);
    }
  }

  private void upDateTimeWeek(SignInTimeFrameBean bean) {
    mBinding.civTimeWeek.setContent("每周" + bean.getTimeFrameWeekWithSplit("/"));
  }

  private void delTimeFrame() {
    showLoading();
    mViewModel.delTimeFrame(bean.getId());
  }

  private void initToolbar() {
    ToolbarModel toolbarModel = new ToolbarModel("入场时段");
    toolbarModel.setMenu(R.menu.menu_save);
    toolbarModel.setListener(menuItem -> {
      makeUploadBody();
      return false;
    });
    mBinding.setToolbarModel(toolbarModel);
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  private void makeUploadBody() {
    String startTime = mBinding.civTimeStart.getContent();
    String endTime = mBinding.civTimeEnd.getContent();
    String weekContent = mBinding.civTimeWeek.getContent();
    String countContent = mBinding.civTrainCount.getContent();
    if (TextUtils.isEmpty(startTime)) {
      ToastUtils.show("请选择开始时间");
      return;
    }
    if (TextUtils.isEmpty(endTime)) {
      ToastUtils.show("请选择结束时间");
      return;
    }
    if (TextUtils.isEmpty(weekContent)) {
      ToastUtils.show("请选择周期");
      return;
    }
    if (TextUtils.isEmpty(countContent)) {
      ToastUtils.show("请输入自主训练人数");
      return;
    }
    if (!DateUtils.AlessTimeB(startTime, endTime)) {
      ToastUtils.show("开始时间应小于结束时间");
      return;
    }
    SignChooseWeekEvent value = mViewModel.civChooseWeek.getValue();
    SignInTimeUploadBean bean;
    if (this.bean == null) {
      bean = new SignInTimeUploadBean();
    } else {
      bean = this.bean.copyUpload();
      bean.setCardCosts(convert(this.bean.getCardCosts()));
    }

    List<SignTimeFrame> timeFrames = bean.getTimeRepeats();
    if (value != null) {
      timeFrames = new ArrayList<>();
      for (Integer integer : value.getWeeks()) {
        SignTimeFrame timeFrame = new SignTimeFrame();
        timeFrame.setStart(startTime);
        timeFrame.setEnd(endTime);
        timeFrame.setWeekday(integer + 1);
        timeFrames.add(timeFrame);
      }
    } else {
      for (int i = 0; i < timeFrames.size(); i++) {
        timeFrames.get(i).setStart(startTime);
        timeFrames.get(i).setEnd(endTime);
      }
    }

    bean.setTimeRepeats(timeFrames);
    bean.setMaxUsers(Integer.valueOf(countContent));
    bean.setExpireTime(durationPos * 60);
    SignInTimePayOnceEvent payOnceEvent = mViewModel.civPayOnceEvent.getValue();
    if (payOnceEvent != null) {
      bean.setSupportOnlinePay(payOnceEvent.isOpen());
      bean.setOnlinePayPrice(payOnceEvent.getPrice() * 100);
    }
    bean.setCardCosts(convert(cardCosts));
    if (!bean.isSupportCardPay() && !bean.isSupportOnlinePay()) {
      ToastUtils.show("请至少选择一种支付方式");
      return;
    }
    mViewModel.postTimeFrame(bean, this.bean == null ? "" : this.bean.getId());
  }

  public List<CardCost> convert(List<SignInCardCostBean.CardCost> costs) {
    List<CardCost> cardCosts = new ArrayList<>();
    if (!ListUtils.isEmpty(costs)) {
      for (SignInCardCostBean.CardCost cost : costs) {
        cardCosts.add(new CardCost(cost.getCost(), cost.getCardTplId()));
      }
    }
    return cardCosts;
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
          if (signWeekFragment == null) {
            signWeekFragment = new SignTimeWeekChooseFragment();
          }
          if (bean != null) {
            signWeekFragment.setChoosedWeek(bean.getTimeFrameWeek());
          }
          SignChooseWeekEvent value = mViewModel.civChooseWeek.getValue();
          if (value != null) {
            signWeekFragment.setChoosedWeek(value.getWeeks());
          }
          routeTo(signWeekFragment);
        });
    RxView.clicks(mBinding.tvChangeDuration)
        .compose(RxHelper.schedulersClickTransformer())
        .subscribe(v -> showChooseDuration(durationPos));

    RxView.clicks(mBinding.civPayOnce)
        .compose(RxHelper.schedulersClickTransformer())
        .subscribe(v -> {
          if (payOnceFragment == null) {
            payOnceFragment = new SignInTimePayOnceFragment();
          }
          if (bean != null) {
            payOnceFragment.setOncePay(bean.isSupportOnlinePay(), bean.getOnlinePayPrice() / 100f);
          }
          SignInTimePayOnceEvent value = mViewModel.civPayOnceEvent.getValue();
          if (value != null) {
            payOnceFragment.setOncePay(value.isOpen(), value.getPrice());
          }
          routeTo(payOnceFragment);
        });
    RxView.clicks(mBinding.civPayCard)
        .compose(RxHelper.schedulersClickTransformer())
        .subscribe(v -> {
          routeToCardConfig();
        });
  }

  SignInTimePayOnceFragment payOnceFragment;
  SignTimeWeekChooseFragment signWeekFragment;
  ArrayList<SignInCardCostBean.CardCost> cardCosts = new ArrayList<>();

  private void routeToCardConfig() {
    if (allCardCosts.isEmpty()) {
      mViewModel.loadCardCosts();
      return;
    }
    Intent x = IntentUtils.getChooseIntent(getContext(), gymWrapper.getCoachService(),
        gymWrapper.getBrand(), ChooseActivity.SIGN_IN_CARDS);
    x.putExtra("costs", getAllWithSelectedCardCosts());
    startActivityForResult(x, ChooseActivity.SIGN_IN_CARDS);
  }

  private ArrayList<Parcelable> getAllWithSelectedCardCosts() {
    ArrayList<SignInCardCostBean.CardCost> costs = new ArrayList<>(allCardCosts);
    if (cardCosts != null && !cardCosts.isEmpty()) {
      for (SignInCardCostBean.CardCost cardCost : costs) {
        cardCost.setSelected(false);
        cardCost.setCost(0);
        for (SignInCardCostBean.CardCost beanCardCoast : cardCosts) {
          if (cardCost.getId().equals(beanCardCoast.getCardTplId())) {
            cardCost.setCost(beanCardCoast.getCost());
            cardCost.setSelected(true);
          }
        }
      }
    }
    return new ArrayList<>(costs);
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == ChooseActivity.SIGN_IN_CARDS) {
        mBinding.civPayCard.setContent(getResources().getString(R.string.common_have_setting));
        ArrayList<Parcelable> costsList = IntentUtils.getListParcelable(data);
        if (costsList != null && costsList.size() > 0) {
          cardCosts.clear();
          for (int j = 0; j < costsList.size(); j++) {
            SignInCardCostBean.CardCost cc = (SignInCardCostBean.CardCost) costsList.get(j);
            cc.setCardTplId(cc.getId());
            cardCosts.add(cc);
          }
        }
      }
    }
  }

  private TimeDialogWindow timeDialogWindow;

  private void showChooseTimeDialog(Date date, boolean isStart) {
    if (timeDialogWindow == null) {
      timeDialogWindow = new TimeDialogWindow(getContext(), TimePopupWindow.Type.HOURS_MINS, 5);
    }
    timeDialogWindow.setOnTimeSelectListener(date1 -> {
      updateTime(date1, isStart);
    });
    timeDialogWindow.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, date);
  }

  private void updateTime(Date date, boolean isStart) {
    String timeHHMM = DateUtils.getTimeHHMM(date);
    if (isStart) {
      mBinding.civTimeStart.setContent(timeHHMM);
    } else {
      mBinding.civTimeEnd.setContent(timeHHMM);
    }
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
    durationPos = pos;
  }
}
