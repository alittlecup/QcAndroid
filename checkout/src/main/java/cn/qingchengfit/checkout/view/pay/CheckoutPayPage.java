package cn.qingchengfit.checkout.view.pay;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.checkout.R;
import cn.qingchengfit.checkout.databinding.CkPageCheckoutPayBinding;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.mvvm_student.StudentBaseFragment;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;

@Leaf(module = "checkout", path = "/checkout/pay") public class CheckoutPayPage
    extends StudentBaseFragment<CkPageCheckoutPayBinding, CheckoutPayViewModel> {
  @Need String type = "";
  @Need Float money = 0f;

  @Override protected void subscribeUI() {

  }

  @Override
  public CkPageCheckoutPayBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = CkPageCheckoutPayBinding.inflate(inflater, container, false);
    initToolbar();
    initUI();
    initListener();
    return mBinding;
  }

  private void initListener() {
    mBinding.flScan.setOnClickListener(v -> {

    });
  }

  private void initUI() {
    mBinding.tvDec.setText(type + "扫描上方二维码完成支付");
    mBinding.tvQrTitle.setText(type + "收款码");
    mBinding.tvCheckoutMoney.setText("￥" + String.valueOf(money));
    if (type.equals("支付宝")) {
      mBinding.root.setBackgroundResource(R.drawable.ck_bg_ali);
    } else if (type.equals("微信")) {
      mBinding.root.setBackgroundResource(R.drawable.ck_bg_wechat);
    }
  }

  private void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel(type + "收款"));
    initToolbar(mBinding.includeToolbar.toolbar);
    mBinding.includeToolbar.toolbarLayout.setBackgroundColor(Color.TRANSPARENT);
  }
}
