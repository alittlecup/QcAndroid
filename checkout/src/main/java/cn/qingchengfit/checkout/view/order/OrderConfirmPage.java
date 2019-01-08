package cn.qingchengfit.checkout.view.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import cn.qingchengfit.checkout.CheckoutCounterFragment;
import cn.qingchengfit.checkout.R;
import cn.qingchengfit.checkout.bean.CheckoutBill;
import cn.qingchengfit.checkout.bean.OrderListItemData;
import cn.qingchengfit.checkout.bean.PayChannel;
import cn.qingchengfit.checkout.databinding.ChOrderConfirmPageBinding;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.utils.AppUtils;
import cn.qingchengfit.utils.ToastUtils;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;

@Leaf(module = "checkout", path = "/order/confirm") public class OrderConfirmPage
    extends CheckoutCounterFragment<ChOrderConfirmPageBinding, OrderConfirmVM> {
  @Need String orderId;

  @Override protected void subscribeUI() {
    mViewModel.getData().observe(this, data -> {
      if (data != null) {
        initView(data);
      }
    });
    mViewModel.getPutResult().observe(this, data -> {
      if (data != null) {
        ToastUtils.show("操作成功");
        getActivity().onBackPressed();
      }
    });
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
    mViewModel.loadOrderDetail(orderId);
  }

  private void initView(OrderListItemData order) {
    if (order == null) return;
    mBinding.civMoney.setContent(order.getOrderMoney() + "元");
    mBinding.civTime.setContent(order.getOrderCreateDate());
    if (order.getType() == PayChannel.ALIPAY_QRCODE) {
      mBinding.civPayType.setContent("支付宝");
    } else if (order.getType() == PayChannel.WEIXIN_QRCODE) {
      mBinding.civPayType.setContent("微信");
    }
    mBinding.editInput.requestFocus();
    AppUtils.showKeyboard(getContext(), mBinding.editInput);
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
    toolbarModel.setListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        String s = mBinding.editInput.getText().toString();
        if (!TextUtils.isEmpty(s)) {
          OrderListItemData value = mViewModel.getData().getValue();
          if (value instanceof CheckoutBill) {
            ((CheckoutBill) value).setRemarks(s);
            mViewModel.putOrderDetail((CheckoutBill) value);
          }
        } else {
          ToastUtils.show("请填写备注");
        }
        return false;
      }
    });
    mBinding.setToolbarModel(toolbarModel);
    initToolbar(mBinding.includeToolbar.toolbar);
  }
}
