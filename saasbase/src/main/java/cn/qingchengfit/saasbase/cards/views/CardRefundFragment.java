package cn.qingchengfit.saasbase.cards.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import cn.qingchengfit.saasbase.cards.network.body.ChargeBody;
import cn.qingchengfit.saasbase.cards.presenters.CardBuyPresenter;
import cn.qingchengfit.saasbase.constant.Configs;
import cn.qingchengfit.saasbase.utils.IntentUtils;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.widgets.CommonInputView;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.google.gson.JsonObject;
import java.util.Date;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/3/25 2016.
 */

@Leaf(module = "card", path = "/deduction/")
public class CardRefundFragment extends SaasBaseFragment implements CardBuyPresenter.MVPView {

  @BindView(R2.id.deduction_money) CommonInputView deductionMoney;
  @BindView(R2.id.refund_money) CommonInputView refundMoney;
  @BindView(R2.id.balance) TextView balance;
  @BindView(R2.id.switch_name) TextView name;
  @BindView(R2.id.switcher) SwitchCompat switcher;
  @BindView(R2.id.switcher_layout) RelativeLayout switcherLayout;
  @BindView(R2.id.starttime) CommonInputView starttime;
  @BindView(R2.id.endtime) CommonInputView endtime;
  @BindView(R2.id.extra_period) LinearLayout extraPeriod;
  @BindView(R2.id.sale) CommonInputView sale;
  @BindView(R2.id.mark) CommonInputView mark;
  @Inject CardBuyPresenter presenter;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Need Card card;
  @BindView(R2.id.toolbar) Toolbar toolbar;
  @BindView(R2.id.toolbar_title) TextView toolbarTitle;
  private TimeDialogWindow pwTime;
  private String shopid;

  private ChargeBody chargeBody = new ChargeBody();
  private List<Staff> mSalers;

  @Override public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    shopid = gymWrapper.shop_id();
    initBus();
  }

  @OnClick(R2.id.comfirm) public void onComfirm() {

    if (TextUtils.isEmpty(sale.getContent())) {
      ToastUtils.show("请选择销售");
      return;
    }
    chargeBody.setShop_id(shopid);
    if (TextUtils.isEmpty(deductionMoney.getContent())) {
      ToastUtils.show("请填写" + deductionMoney.getLable());
      return;
    }

    if (TextUtils.isEmpty(refundMoney.getContent())) {
      ToastUtils.show("请填写退款金额");
      return;
    }
    chargeBody.setPrice("-" + refundMoney.getContent());
    switch (card.getType()) {
      case Configs.CATEGORY_VALUE:
        chargeBody.setAccount("-" + deductionMoney.getContent());
        break;
      case Configs.CATEGORY_TIMES:
        chargeBody.setTimes("-" + deductionMoney.getContent());
        break;
      case Configs.CATEGORY_DATE:
        //                deductionMoney.setLabel("扣费天数(天)");
        //                extraPeriod.setVisibility(View.GONE);
        try {
          chargeBody.setStart(
              DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(card.getStart())));
          chargeBody.setEnd(DateUtils.minusDay(DateUtils.formatDateFromServer(card.getEnd()),
              Integer.parseInt(deductionMoney.getContent())));
        } catch (Exception e) {

        }
        break;
    }

    if (switcher.isChecked()) {
      if (TextUtils.isEmpty(starttime.getContent())) {
        ToastUtils.show("请填写开始日期");
        return;
      }
      if (TextUtils.isEmpty(endtime.getContent())) {
        ToastUtils.show("请填写结束日期");
        return;
      }
      chargeBody.setCheck_valid(true);
      chargeBody.setValid_from(starttime.getContent());
      chargeBody.setValid_to(endtime.getContent());
    } else {
      chargeBody.setCheck_valid(false);
    }
    chargeBody.setCharge_type(1);
    chargeBody.setType(Configs.TRADE_REFUND);
    presenter.proactiveDeduction(card.getId(), chargeBody);
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_modify_card_refund, container, false);
    unbinder = ButterKnife.bind(this, view);
    initToolbar(toolbar);
    toolbarTitle.setText("会员卡扣费");
    presenter.attachView(this);
    pwTime = new TimeDialogWindow(getContext(), TimePopupWindow.Type.YEAR_MONTH_DAY);
    initView();
    return view;
  }

  private void initView() {
    switch (card.getType()) {
      case Configs.CATEGORY_VALUE:
        break;
      case Configs.CATEGORY_TIMES:
        deductionMoney.setLabel("扣费次数(次)");
        break;
      case Configs.CATEGORY_DATE:
        deductionMoney.setLabel("扣费天数(天)");
        extraPeriod.setVisibility(View.GONE);
        break;
    }
    name.setText("设置有效期");
    deductionMoney.addTextWatcher(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override public void afterTextChanged(Editable s) {
        try {
          Float b = Float.parseFloat(s.toString());
          Float a = card.getBalance();
          balance.setText("扣费后余额:" + (a - b));
        } catch (Exception e) {
          balance.setText("扣费后余额:" + card.getBalance());
        }
      }
    });
    switcher.setChecked(false);
    switcher.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
      @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
          starttime.setVisibility(View.VISIBLE);
          endtime.setVisibility(View.VISIBLE);
        } else {
          starttime.setVisibility(View.GONE);
          endtime.setVisibility(View.GONE);
        }
      }
    });
  }

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  private void initBus() {
    RxBusAdd(Staff.class).onBackpressureLatest()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BusSubscribe<Staff>() {
          @Override public void onNext(Staff staff) {
            sale.setContent(staff.getUsername());
            chargeBody.setSeller_id(staff.id);
          }
        });
  }

  @OnClick({ R2.id.switcher_layout, R2.id.starttime, R2.id.endtime, R2.id.sale, R2.id.mark })
  public void onClick(View view) {
    int i = view.getId();
    if (i == R.id.switcher_layout) {
      switcher.toggle();
    } else if (i == R.id.starttime) {
      pwTime.setRange(2000, 2100);
      pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
        @Override public void onTimeSelect(Date date) {
          starttime.setContent(DateUtils.Date2YYYYMMDD(date));
        }
      });
      pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
    } else if (i == R.id.endtime) {
      pwTime.setRange(2000, 2100);
      pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
        @Override public void onTimeSelect(Date date) {
          endtime.setContent(DateUtils.Date2YYYYMMDD(date));
        }
      });
      pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
    } else if (i
        == R.id.sale) {//                ChooseSaleFragment.start(this, 1, shopid, Configs.INIT_TYPE_ADD);
      routeTo(AppUtils.getRouterUri(getContext(), "/staff/choose/saler/"), null);
    } else if (i == R.id.mark) {
      WriteDescFragment.start(this, 5, "备注", "填写");
    }
  }

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == 1) {
        String name = IntentUtils.getIntentString(data, 0);
        String id = IntentUtils.getIntentString(data, 1);
        chargeBody.setSeller_id(id);
        sale.setContent(name);
      } else if (requestCode == 5) {
        mark.setContent("已填写");
        chargeBody.setRemarks(IntentUtils.getIntentString(data));
      } else if (requestCode == 2) {
        if (mSalers != null) {
          int pos = Integer.parseInt(IntentUtils.getIntentString(data));
          if (pos > 0) {
            Staff mChosenSaler = mSalers.get(pos - 1);
            sale.setContent(mChosenSaler.username);
            chargeBody.setSeller_id(mChosenSaler.id);
          } else {
            sale.setContent("无销售");
            chargeBody.setSeller_id(null);
          }
        }
      }
    }
  }

  @Override public String getFragmentName() {
    return CardRefundFragment.class.getName();
  }

  @Override public void onGetOptions(List<CardTplOption> options) {

  }

  @Override public void onGetCardTpl(CardTpl cardTpl) {

  }

  @Override
  public void showInputMoney(boolean other, CardTplOption cardTplOption, boolean hasValid) {

  }

  @Override public void bindStudent(String student) {

  }

  @Override public void bindSaler(String saler) {

  }

  @Override public void remark(boolean remark) {

  }

  @Override public void setPayMoney(String x) {

  }

  @Override public void onBusinessOrder(JsonObject response) {
    ToastUtils.showS("扣费成功");
    //for (int i = 0; i < getFragmentManager().getFragments().size() - 1; i++) {
    getActivity().onBackPressed();
  }

  @Override public String realCardNum() {
    return null;
  }

  @Override public String realMoney() {
    return null;
  }

  @Override public String chargeMoney() {
    return null;
  }

  @Override public int payMethod() {
    return 0;
  }

  @Override public boolean openValidDay() {
    return false;
  }

  @Override public String startDay() {
    return null;
  }

  @Override public String endDay() {
    return null;
  }

  @Override public boolean autoOpen() {
    return false;
  }
}
