package cn.qingchengfit.checkout.view.home;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.checkout.CheckoutCounterFragment;
import cn.qingchengfit.checkout.R;
import cn.qingchengfit.checkout.databinding.CkPageCheckoutHomeBinding;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.utils.ToastUtils;
import com.anbillon.flabellum.annotations.Leaf;

@Leaf(module = "checkout", path = "/checkout/home") public class CheckoutHomePage
    extends CheckoutCounterFragment<CkPageCheckoutHomeBinding, CheckoutHomeViewModel> {
  @Override protected void subscribeUI() {

  }

  @Override
  public CkPageCheckoutHomeBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = CkPageCheckoutHomeBinding.inflate(inflater, container, false);
    mBinding.setViewModel(mViewModel);
    mBinding.setLifecycleOwner(this);
    initToolbar();
    initListener();
    mViewModel.loadHomePageInfo();
    return mBinding;
  }

  private void initListener() {
    mBinding.flNewCard.setOnClickListener(view -> {
      routeTo("card", "/cardtpl/nonew/", null);
    });
    mBinding.flAppendCard.setOnClickListener(view -> {
      routeTo("card", "/balance/list/", null);

    });
    mBinding.flCheckout.setOnClickListener(view -> {
      routeTo("/checkout/money", null);
    });
  }

  private void initToolbar() {
    mBinding.includeToolbar.toolbarLayout.setBackgroundColor(Color.TRANSPARENT);
    ToolbarModel toolbarModel = new ToolbarModel("收银台");
    toolbarModel.setMenu(R.menu.ck_menu_checkout_help);
    toolbarModel.setListener(item -> {
      ToastUtils.show("menu click");
      return false;
    });
    mBinding.setToolbarModel(toolbarModel);
    initToolbar(mBinding.includeToolbar.toolbar);
  }
}
