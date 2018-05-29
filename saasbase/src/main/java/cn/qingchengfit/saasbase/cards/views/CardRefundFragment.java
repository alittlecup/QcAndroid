package cn.qingchengfit.saasbase.cards.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.items.BottomPayExpandItem;
import cn.qingchengfit.items.BottomPayItem;
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
import cn.qingchengfit.saasbase.utils.CardBusinessUtils;
import cn.qingchengfit.saasbase.utils.IntentUtils;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.ToastUtils;
import cn.qingchengfit.views.fragments.BottomPayDialog;
import cn.qingchengfit.widgets.CommonInputView;
import cn.qingchengfit.widgets.SwitcherLayout;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import com.google.gson.JsonObject;
import java.text.DecimalFormat;
import java.util.ArrayList;
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

@Leaf(module = "card", path = "/deduction/") public class CardRefundFragment
    extends SaasBaseFragment implements CardBuyPresenter.MVPView {

  @BindView(R2.id.deduction_money) CommonInputView deductionMoney;
  @BindView(R2.id.refund_money) CommonInputView refundMoney;
  @BindView(R2.id.balance) TextView balance;
  @BindView(R2.id.switch_deduction) SwitcherLayout swDeduction;
  @BindView(R2.id.switch_time) SwitcherLayout swTime;
  @BindView(R2.id.ll_deduction) LinearLayout llDeduction;
  @BindView(R2.id.start_time) CommonInputView startTime;
  @BindView(R2.id.end_time) CommonInputView endTime;
  @BindView(R2.id.extra_period) LinearLayout extraPeriod;
  @BindView(R2.id.deduction_way) CommonInputView deductionWay;

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
    //先全判断通过再赋值，避免应该反复开关导致数据错乱
    if (TextUtils.isEmpty(deductionMoney.getContent())) {
      ToastUtils.show("请填写" + deductionMoney.getLable());
      return;
    }
    if (swDeduction.isOpen()) {
      if (TextUtils.isEmpty(refundMoney.getContent())) {
        ToastUtils.show("请填写退款金额");
        return;
      }
      if (TextUtils.isEmpty(deductionWay.getContent())) {
        ToastUtils.show("请选择退款方式");
        return;
      }
      if (TextUtils.isEmpty(sale.getContent())) {
        ToastUtils.show("请选择销售");
        return;
      }
    }
    if (swTime.isOpen()) {
      if (TextUtils.isEmpty(startTime.getContent())) {
        ToastUtils.show("请填写开始日期");
        return;
      }
      if (TextUtils.isEmpty(endTime.getContent())) {
        ToastUtils.show("请填写结束日期");
        return;
      }
    }

    chargeBody.setRemarks(remarkString);
    chargeBody.setShop_id(shopid);

    switch (card.getType()) {
      case Configs.CATEGORY_VALUE:
        chargeBody.setAccount("-" + deductionMoney.getContent());
        break;
      case Configs.CATEGORY_TIMES:
        chargeBody.setTimes("-" + deductionMoney.getContent());
        break;
      case Configs.CATEGORY_DATE:
        try {
          chargeBody.setStart(
              DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(card.getStart())));
          chargeBody.setEnd(DateUtils.minusDay(DateUtils.formatDateFromServer(card.getEnd()),
              Integer.parseInt(deductionMoney.getContent())));
        } catch (Exception e) {

        }
        break;
    }
    float price = Float.valueOf(deductionMoney.getContent()) * (card.getRatio());
    if (!(card.getType() == Configs.CATEGORY_DATE)) {
      chargeBody.setPrice("-" + formatePrice(price));
      chargeBody.setCharge_type(1);
    }
    if (swDeduction.isOpen()) {
      chargeBody.setPrice("-" + refundMoney.getContent());
      chargeBody.setSeller_id(sellerId);
      chargeBody.setType(Configs.TRADE_DEDUCTION);
      chargeBody.setCharge_type(chargeType);
    } else {
      chargeBody.setType(Configs.TRADE_REFUND);
    }

    if (swTime.isOpen()) {
      chargeBody.setCheck_valid(true);
      chargeBody.setValid_from(startTime.getContent());
      chargeBody.setValid_to(endTime.getContent());
    } else {
      chargeBody.setCheck_valid(false);
    }
    presenter.proactiveDeduction(card.getId(), chargeBody);
  }

  @Nullable @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    final View view = inflater.inflate(R.layout.fragment_modify_card_refund, container, false);
    unbinder = ButterKnife.bind(this, view);
    delegatePresenter(presenter, this);
    initToolbar(toolbar);
    toolbarTitle.setText("会员卡扣费");
    pwTime = new TimeDialogWindow(getContext(), TimePopupWindow.Type.YEAR_MONTH_DAY);
    initView();
    return view;
  }

  private float formatePrice(float value) {
    DecimalFormat format = new DecimalFormat("#.00");
    String format1 = format.format(value);
    return Float.valueOf(format1);
  }

  private void initView() {
    switch (card.getType()) {
      case Configs.CATEGORY_VALUE:
        break;
      case Configs.CATEGORY_TIMES:
        deductionMoney.setLabel("扣费次数");
        deductionMoney.setUnit("次");
        break;
      case Configs.CATEGORY_DATE:
        deductionMoney.setLabel("扣费天数");
        deductionMoney.setUnit("天");
        extraPeriod.setVisibility(View.GONE);
        swTime.setVisibility(View.GONE);
        break;
    }
    balance.setText("当前卡余额：" + CardBusinessUtils.getCardBlance(card));
    deductionMoney.addTextWatcher(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override public void afterTextChanged(Editable s) {
        try {
          if (s == null || TextUtils.isEmpty(s.toString())) {
            if (swDeduction.isOpen()) {
              refundMoney.setContent("");
            }
            return;
          }
          Float b = Float.parseFloat(s.toString());
          Float a = card.getBalance();
          balance.setText("当前卡余额："
              + CardBusinessUtils.getCardBlance(card)
              + "  扣费后卡余额："
              + (a - b)
              + CardBusinessUtils.getCardTypeCategoryUnit(card.getType(), getContext()));
          if (swDeduction.isOpen()) {
            refundMoney.setContent(formatePrice(b * card.getRatio()) + "");
          }
        } catch (Exception e) {
          //balance.setText("扣费后余额:" + card.getBalance());
        }
      }
    });
    swTime.setOpen(false);
    swTime.setOnCheckListener((buttonView, isChecked) -> {
      if (isChecked) {
        extraPeriod.setVisibility(View.VISIBLE);
      } else {
        extraPeriod.setVisibility(View.GONE);
      }
    });
    swDeduction.setOpen(false);
    swDeduction.setOnCheckListener((buttonView, isChecked) -> {
      if (isChecked) {
        llDeduction.setVisibility(View.VISIBLE);
        setRefundMoney(deductionMoney.getContent());
        if (seller == null) {
          presenter.getDefineSeller(card.getSeller().getId());
        }
        if (TextUtils.isEmpty(deductionWay.getContent())) {
          deductionWay.setContent("其他");
          chargeType = 4;
        }
      } else {
        llDeduction.setVisibility(View.GONE);
      }
    });
  }

  private void setRefundMoney(String s) {
    try {
      if (!TextUtils.isEmpty(s)) {
        Float v = Float.parseFloat(s);
        refundMoney.setContent(formatePrice(v * card.getRatio()) + "");
      } else {
        refundMoney.setContent("");
      }
    } catch (Exception e) {

    }
  }

  private String sellerId;

  @Override public void onDestroyView() {
    super.onDestroyView();
  }

  private void initBus() {
    RxBusAdd(Staff.class).onBackpressureLatest()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new BusSubscribe<Staff>() {
          @Override public void onNext(Staff staff) {
            sale.setContent(staff.getUsername());
            if (staff.id.equals("0")) {
              sellerId = null;
            } else {
              sellerId = staff.id;
            }
          }
        });
  }

  private BottomPayDialog dialog;

  @OnClick({ R2.id.start_time, R2.id.end_time, R2.id.sale, R2.id.mark, R2.id.deduction_way })
  public void onClick(View view) {
    int i = view.getId();
    if (i == R.id.start_time) {
      pwTime.setRange(2000, 2100);
      pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
        @Override public void onTimeSelect(Date date) {
          startTime.setContent(DateUtils.Date2YYYYMMDD(date));
        }
      });
      pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
    } else if (i == R.id.end_time) {
      pwTime.setRange(2000, 2100);
      pwTime.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
        @Override public void onTimeSelect(Date date) {
          endTime.setContent(DateUtils.Date2YYYYMMDD(date));
        }
      });
      pwTime.showAtLocation(getView(), Gravity.BOTTOM, 0, 0, new Date());
    } else if (i
        == R.id.sale) {//                ChooseSaleFragment.start(this, 1, shopid, Configs.INIT_TYPE_ADD);
      routeTo(AppUtils.getRouterUri(getContext(), "/staff/choose/saler/"), null);
    } else if (i == R.id.mark) {
      WriteDescFragment.start(this, 5, "备注", "填写");
    } else if (i == R.id.deduction_way) {
      showBottomPayDialog();
    }
  }

  private int chargeType = -1;

  private void showBottomPayDialog() {
    if (dialog == null) {
      List<BottomPayExpandItem> items = new ArrayList<>();
      BottomPayExpandItem item1 = new BottomPayExpandItem("线下支付");
      item1.addSubItem(
          new BottomPayItem(cn.qingchengfit.views.fragments.BottomPayDialog.PayType.CASH_PAY));
      item1.addSubItem(
          new BottomPayItem(cn.qingchengfit.views.fragments.BottomPayDialog.PayType.CREDIT_PAY));
      item1.addSubItem(
          new BottomPayItem(cn.qingchengfit.views.fragments.BottomPayDialog.PayType.TRANSIT_PAY));
      item1.addSubItem(
          new BottomPayItem(cn.qingchengfit.views.fragments.BottomPayDialog.PayType.OTHER_PAY));
      items.add(item1);
      dialog = new BottomPayDialog(getContext(), "选择退款方式", items);
      dialog.setOnItemClickListener(i -> {
        switch (i) {
          case 1:
            deductionWay.setContent("现金");
            chargeType = 1;
            break;
          case 2:
            deductionWay.setContent("刷卡");
            chargeType = 2;
            break;
          case 3:
            deductionWay.setContent("转账");
            chargeType = 3;
            break;
          case 4:
            deductionWay.setContent("其他");
            chargeType = 4;
            break;
        }
        return false;
      });
    }
    dialog.show();
  }

  private String remarkString;

  @Override public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (resultCode == Activity.RESULT_OK) {
      if (requestCode == 5) {
        mark.setContent("已填写");
        remarkString = IntentUtils.getIntentString(data);
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

  @Override public void setPayMoney(float x) {

  }

  private Staff seller;

  @Override public void setDefineSeller(Staff seller) {
    this.seller = seller;
    if (swDeduction.isOpen()) {
      sale.setContent(seller.getUsername());
      sellerId = seller.getId();
    }
  }

  @Override public void onBusinessOrder(JsonObject response) {
    ToastUtils.showS("扣费成功");
    getActivity().onBackPressed();
  }

  @Override public void realCardNum(boolean isReal) {

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
