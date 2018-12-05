package cn.qingchengfit.checkout.view.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.checkout.CheckoutCounterFragment;
import cn.qingchengfit.checkout.R;
import cn.qingchengfit.checkout.bean.OrderListItemData;
import cn.qingchengfit.checkout.bean.PayChannel;
import cn.qingchengfit.checkout.databinding.ChOrderConfirmPageBinding;
import cn.qingchengfit.model.others.ToolbarModel;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;

@Leaf(module = "checkout", path = "/order/confirm") public class OrderConfirmPage
    extends CheckoutCounterFragment<ChOrderConfirmPageBinding, OrderConfirmVM> {
  @Need OrderListItemData order;

  @Override protected void subscribeUI() {

  }

  @Override
  public ChOrderConfirmPageBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    return ChOrderConfirmPageBinding.inflate(inflater, container, false);
  }

  @Override public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
    super.onViewCreated(view, savedInstanceState);
    initToolbar();
    initListener();
    initView();
  }

  private void initView() {
    if (order == null) return;
    mBinding.civMoney.setContent(order.getOrderMoney() + "元");
    mBinding.civTime.setContent(order.getOrderCreateDate());
    if (order.getType() == PayChannel.ALIPAY_QRCODE) {
      mBinding.civPayType.setContent("支付宝");
    } else if (order.getType() == PayChannel.WEIXIN_QRCODE) {
      mBinding.civPayType.setContent("微信");
    }
  }

  private void initListener() {
    mBinding.editInput.addTextChangedListener(new TextWatcher() {
      @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

      }

      @Override public void onTextChanged(CharSequence s, int start, int before, int count) {

      }

      @Override public void afterTextChanged(Editable s) {
        String s1 = s.toString();
        mBinding.tvEditCount.setText(
            String.format(getResources().getString(R.string.ck_order_input_count), s1.length() + "",
                "20"));
      }
    });
  }

  private void initToolbar() {
    ToolbarModel toolbarModel = new ToolbarModel("确认收款");
    toolbarModel.setMenu(R.menu.menu_comfirm);
    mBinding.setToolbarModel(toolbarModel);
    initToolbar(mBinding.includeToolbar.toolbar);
  }
}
