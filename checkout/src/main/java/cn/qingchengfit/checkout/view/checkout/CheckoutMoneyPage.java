package cn.qingchengfit.checkout.view.checkout;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.qingchengfit.checkout.CheckoutCounterFragment;
import cn.qingchengfit.checkout.R;
import cn.qingchengfit.router.IComponentCallback;
import cn.qingchengfit.router.QC;
import cn.qingchengfit.router.QCResult;
import cn.qingchengfit.router.qc.IQcRouteCallback;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.saascommon.bean.CashierBean;
import cn.qingchengfit.saascommon.bean.CashierBeanWrapper;
import cn.qingchengfit.checkout.bean.PayChannel;
import cn.qingchengfit.checkout.databinding.CkPageCheckoutMoneyBinding;
import cn.qingchengfit.checkout.view.pay.CheckoutPayPageParams;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saascommon.bean.ScanRepayInfo;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.anbillon.flabellum.annotations.Leaf;
import java.util.HashMap;
import java.util.Map;

@Leaf(module = "checkout", path = "/checkout/money") public class CheckoutMoneyPage
    extends CheckoutCounterFragment<CkPageCheckoutMoneyBinding, CheckoutMoneyViewModel>
    implements View.OnClickListener {
  @Override protected void subscribeUI() {
    mViewModel.count.observe(this, prices -> {
      if (TextUtils.isEmpty(prices)) {
        mViewModel.enable.setValue(false);
        return;
      }
      double v = Double.parseDouble(prices);
      if (v > 20000 | v == 0) {
        mViewModel.enable.setValue(false);
      } else {
        mViewModel.enable.setValue(true);
      }
    });
    mViewModel.cashierBean.observe(this, cashierBean -> {
      hideLoading();
      if (cashierBean != null && !cashierBean.equals(preCashierBean)) {
        preCashierBean = cashierBean;
        dealCashierBean(cashierBean);
      }
    });
  }

  private CashierBean preCashierBean;

  private void dealCashierBean(CashierBean cashierBean) {
    CashierBeanWrapper wrapper = new CashierBeanWrapper(cashierBean);
    wrapper.setPrices( String.valueOf(Double.parseDouble(mViewModel.count.getValue())));
    ScanRepayInfo info = new ScanRepayInfo();
    info.setModuleName("checkout");
    info.setActionName("reOrder");
    Map<String, String> params = new HashMap<>();
    params.put("price",mViewModel.count.getValue());

    switch (mViewModel.getType()) {
      case PayChannel.ALIPAY_QRCODE:
        params.put("channel", "ALIPAY_QRCODE");
        info.setParams(params);
        wrapper.setInfo(info);
        QcRouteUtil.setRouteOptions(new RouteOptions("checkout").setActionName("/checkout/pay")
            .setContext(getContext())
            .addParam("type", "支付宝")
            .addParam("orderData", wrapper)).callAsync(callback);
        break;
      case PayChannel.WEIXIN_QRCODE:
        params.put("channel", "WEIXIN_QRCODE");
        info.setParams(params);
        wrapper.setInfo(info);
        QcRouteUtil.setRouteOptions(new RouteOptions("checkout").setActionName("/checkout/pay")
            .setContext(getContext())
            .addParam("type", "微信")
            .addParam("orderData", wrapper)).callAsync(callback);
        break;
    }
  }

  private IQcRouteCallback callback = qcResult -> QcRouteUtil.setRouteOptions(new RouteOptions("checkout").setActionName("/checkout/home"))
      .call();

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
        mViewModel.getOrderInfo(PayChannel.ALIPAY_QRCODE, s);
      }
    } else if (i == R.id.fl_wxpay) {
      if (checkMoney(s)) {
        showLoading();
        mViewModel.getOrderInfo(PayChannel.WEIXIN_QRCODE, s);
      }
    }
  }

  private boolean checkMoney(String s) {
    try {
      if (TextUtils.isEmpty(s)) return false;
      float money = Float.valueOf(s);
      if (money > 20000) {
        DialogUtils.showAlert(getContext(), "单笔收款最多支持20000");
        return false;
      }
    } catch (Exception e) {
      ToastUtils.show("请输入正确的数字");
      return false;
    }

    return true;
  }
}
