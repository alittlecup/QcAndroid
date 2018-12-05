package cn.qingchengfit.checkout.view.qrcode;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.checkout.CheckoutCounterFragment;
import cn.qingchengfit.checkout.databinding.CkCheckoutQrcodeBinding;
import cn.qingchengfit.model.others.ToolbarModel;
import com.anbillon.flabellum.annotations.Leaf;

@Leaf(module = "checkout", path = "/checkout/qrcode") public class CheckoutQrCodePage
    extends CheckoutCounterFragment<CkCheckoutQrcodeBinding, CheckoutQrCodeVM> {
  @Override protected void subscribeUI() {

  }

  @Override
  public CkCheckoutQrcodeBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return CkCheckoutQrcodeBinding.inflate(inflater, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initToolbar();
    initListener();
  }

  private void initListener() {
    mBinding.flPayOrders.setOnClickListener(v -> routeTo("/order/list", null));
  }

  private void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel("场馆收款二维码"));
    initToolbar(mBinding.includeToolbar.toolbar);
    mBinding.includeToolbar.toolbarLayout.setBackgroundColor(Color.TRANSPARENT);
  }
}
