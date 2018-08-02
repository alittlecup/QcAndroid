package cn.qingchengfit.checkout.view.home;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.checkout.CheckoutCounterFragment;
import cn.qingchengfit.checkout.R;
import cn.qingchengfit.checkout.databinding.CkPageCheckoutHomeBinding;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.router.qc.IQcRouteCallback;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.utils.AppUtils;
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
      QcRouteUtil.setRouteOptions(new RouteOptions("card").setActionName("/cardtpl/nonew"))
          .callAsync(callback);
    });
    mBinding.flAppendCard.setOnClickListener(view -> {
      QcRouteUtil.setRouteOptions(new RouteOptions("card").setActionName("/list/nobalance"))
          .callAsync(callback);
    });
    mBinding.flCheckout.setOnClickListener(view -> {
      QcRouteUtil.setRouteOptions(new RouteOptions("checkout").setActionName("/checkout/money"))
          .call();
    });
  }

  private IQcRouteCallback callback = qcResult -> {
    mViewModel.loadHomePageInfo();
  };

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
