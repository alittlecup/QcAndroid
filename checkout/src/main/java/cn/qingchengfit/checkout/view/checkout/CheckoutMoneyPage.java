package cn.qingchengfit.checkout.view.checkout;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.qingchengfit.checkout.CheckoutCounterFragment;
import cn.qingchengfit.checkout.R;
import cn.qingchengfit.router.QCResult;
import cn.qingchengfit.router.qc.IQcRouteCallback;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.checkout.bean.CashierBean;
import cn.qingchengfit.checkout.bean.CashierBeanWrapper;
import cn.qingchengfit.checkout.bean.PayChannel;
import cn.qingchengfit.checkout.databinding.CkPageCheckoutMoneyBinding;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.checkout.bean.ScanRepayInfo;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import java.math.BigDecimal;

@Leaf(module = "checkout", path = "/checkout/money") public class CheckoutMoneyPage
    extends CheckoutCounterFragment<CkPageCheckoutMoneyBinding, CheckoutMoneyViewModel>
    implements View.OnClickListener {
  @Override protected void subscribeUI() {
    mViewModel.count.observe(this, prices -> {
      if (TextUtils.isEmpty(prices)) {
        mViewModel.enable.setValue(false);
        return;
      }
      BigDecimal money = new BigDecimal(prices);
      if (money.floatValue() > 100000 || money.floatValue() == 0) {
        mViewModel.enable.setValue(false);
      } else {
        mViewModel.enable.setValue(true);
      }
      if (money.floatValue() > 3000) {
        mBinding.tvAliHint.setText("超过3000元\n大额支付");
      } else {
        mBinding.tvAliHint.setText("花呗支付");
      }
    });
    mViewModel.cashierBean.observe(this, cashierBean -> {
      hideLoading();
      if (cashierBean != null && "10FC".equals(cashierBean.getResult_code())) {
        ToastUtils.show("收款金额超过您的额度上限，请您重新输入");
        return;
      }
      if (cashierBean != null && !cashierBean.equals(preCashierBean)) {
        preCashierBean = cashierBean;
        dealCashierBean(cashierBean);
      }
    });
  }

  private CashierBean preCashierBean;

  private void dealCashierBean(CashierBean cashierBean) {
    CashierBeanWrapper wrapper = new CashierBeanWrapper(cashierBean);
    wrapper.setPrice(mViewModel.count.getValue());
    ScanRepayInfo info = new ScanRepayInfo();
    info.setModuleName("checkout");
    info.setActionName("reOrder");
    JsonObject params = new JsonObject();
    params.addProperty("price", mViewModel.count.getValue());
    switch (mViewModel.getType()) {
      case PayChannel.ALIPAY_QRCODE:
        params.addProperty("channel", "ALIPAY_QRCODE");
        info.setParams(params.toString());
        wrapper.setType("ALIPAY_QRCODE");
        wrapper.setInfo(info);
        break;
      case PayChannel.WEIXIN_QRCODE:
        params.addProperty("channel", "WEIXIN_QRCODE");
        wrapper.setType("WEIXIN_QRCODE");
        info.setParams(params.toString());
        wrapper.setInfo(info);
        break;
    }
    String json = new Gson().toJson(wrapper);
    QcRouteUtil.setRouteOptions(new RouteOptions("checkout").setActionName("/checkout/pay")
        .setContext(getContext())
        .addParam("data", json)).callAsync(callback);
  }

  private IQcRouteCallback callback = qcResult -> getActivity().onBackPressed();

  @Override
  public CkPageCheckoutMoneyBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = CkPageCheckoutMoneyBinding.inflate(inflater, container, false);
    initToolbar();
    initListener();
    mBinding.setViewModel(mViewModel);
    mBinding.setLifecycleOwner(this);
    return mBinding;
  }

  private void initListener() {
    mBinding.tv0.setOnClickListener(this);
    mBinding.tv1.setOnClickListener(this);
    mBinding.tv2.setOnClickListener(this);
    mBinding.tv3.setOnClickListener(this);
    mBinding.tv4.setOnClickListener(this);
    mBinding.tv5.setOnClickListener(this);
    mBinding.tv6.setOnClickListener(this);
    mBinding.tv7.setOnClickListener(this);
    mBinding.tv8.setOnClickListener(this);
    mBinding.tv9.setOnClickListener(this);
    mBinding.tvC.setOnClickListener(this);
    mBinding.tvPoint.setOnClickListener(this);
    mBinding.imgDelete.setOnClickListener(this);
    mBinding.flAlipay.setOnClickListener(this);
    mBinding.flWxpay.setOnClickListener(this);
  }

  private void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel("直接收银"));
    initToolbar(mBinding.includeToolbar.toolbar);
  }

  @Override public void onClick(View v) {
    String s = mViewModel.count.getValue();

    int i = v.getId();
    if (i == R.id.tv_0
        || i == R.id.tv_1
        || i == R.id.tv_2
        || i == R.id.tv_3
        || i == R.id.tv_4
        || i == R.id.tv_5
        || i == R.id.tv_6
        || i == R.id.tv_7
        || i == R.id.tv_8
        || i == R.id.tv_9) {
      if (v instanceof TextView) {
        if (!TextUtils.isEmpty(s)) {
          if (s.contains(".") && s.indexOf(".") < s.length() - 2) {
            return;
          }
          if (s.length() >= 9) {
            return;
          }
          if (i == R.id.tv_0 && s.equals("0")) {
            return;
          }
        }
        mViewModel.count.setValue(s + "" + ((TextView) v).getText());
      }
    } else if (i == R.id.tv_c) {
      mViewModel.count.setValue("");
    } else if (i == R.id.img_delete) {
      if (!TextUtils.isEmpty(s)) {
        if (s.length() == 1) {
          mViewModel.count.setValue("");
        } else {
          mViewModel.count.setValue(s.subSequence(0, s.length() - 1).toString());
        }
      }
    } else if (i == R.id.tv_point) {
      if (!TextUtils.isEmpty(s)) {
        if (!s.contains(".")) {
          mViewModel.count.setValue(s + ".");
        }
      }
    } else if (i == R.id.fl_alipay) {
      if (checkMoney(s)) {
        showLoading();
        mViewModel.getOrderInfo(PayChannel.ALIPAY_QRCODE, s,
            mBinding.edRemarks.getText().toString());
      }
    } else if (i == R.id.fl_wxpay) {
      if (checkMoney(s)) {
        showLoading();
        mViewModel.getOrderInfo(PayChannel.WEIXIN_QRCODE, s,
            mBinding.edRemarks.getText().toString());
      }
    }
  }

  private boolean checkMoney(String s) {
    if (TextUtils.isEmpty(mBinding.edRemarks.getText().toString())) {
      mBinding.edRemarks.requestFocus();
      AppUtils.showKeyboard(getContext(), mBinding.edRemarks);
      ToastUtils.show("请填写款项说明");
      return false;
    }
    try {
      if (TextUtils.isEmpty(s)) return false;
      BigDecimal money = new BigDecimal(s);
      if (money.floatValue() > 100000) {
        DialogUtils.showAlert(getContext(), "单笔收款最多支持100000");
        return false;
      }
    } catch (Exception e) {
      ToastUtils.show("请输入正确的数字");
      return false;
    }

    return true;
  }
}
