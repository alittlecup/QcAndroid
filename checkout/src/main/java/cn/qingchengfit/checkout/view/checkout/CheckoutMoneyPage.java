package cn.qingchengfit.checkout.view.checkout;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import cn.qingchengfit.checkout.CheckoutCounterFragment;
import cn.qingchengfit.checkout.R;
import cn.qingchengfit.checkout.databinding.CkPageCheckoutMoneyBinding;
import cn.qingchengfit.checkout.view.pay.CheckoutPayPageParams;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.utils.DialogUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.anbillon.flabellum.annotations.Leaf;

@Leaf(module = "checkout", path = "/checkout/money") public class CheckoutMoneyPage
    extends CheckoutCounterFragment<CkPageCheckoutMoneyBinding, CheckoutMoneyViewModel>
    implements View.OnClickListener {
  @Override protected void subscribeUI() {

  }

  @Override
  public CkPageCheckoutMoneyBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = CkPageCheckoutMoneyBinding.inflate(inflater, container, false);
    initToolbar();
    initListener();
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
    String s = mBinding.edContent.getText().toString();

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
        mBinding.edContent.append(((TextView) v).getText());
      }
    } else if (i == R.id.tv_c) {
      mBinding.edContent.setText("");
    } else if (i == R.id.img_delete) {
      if (!TextUtils.isEmpty(s)) {
        if (s.length() == 1) {
          mBinding.edContent.setText("");
        } else {
          mBinding.edContent.setText(s.subSequence(0, s.length() - 1));
        }
      }
    } else if (i == R.id.tv_point) {
      if (!TextUtils.isEmpty(s)) {
        if (!s.contains(".")) {
          mBinding.edContent.append(".");
        }
      }
    } else if (i == R.id.fl_alipay) {
      if (checkMoney(s)) {
        routeTo("checkout/pay",
            new CheckoutPayPageParams().type("支付宝").money(Float.valueOf(s)).build());
      }
    } else if (i == R.id.fl_wxpay) {
      if (checkMoney(s)) {
        routeTo("checkout/pay",
            new CheckoutPayPageParams().type("微信").money(Float.valueOf(s)).build());
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
