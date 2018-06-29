package cn.qingchengfit.checkout.view;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import cn.qingchengfit.checkout.R;
import cn.qingchengfit.checkout.databinding.PageCheckoutHomeBinding;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.saasbase.mvvm_student.StudentBaseFragment;
import cn.qingchengfit.utils.ToastUtils;
import com.anbillon.flabellum.annotations.Leaf;

@Leaf(module = "checkout",path = "/checkout/home")
public class CheckoutHomePage
    extends StudentBaseFragment<PageCheckoutHomeBinding, CheckoutHomeViewModel> {
  @Override protected void subscribeUI() {

  }

  @Override
  public PageCheckoutHomeBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageCheckoutHomeBinding.inflate(inflater, container, false);
    initToolbar();
    initListener();
    return mBinding;
  }

  private void initListener() {
    mBinding.flNewCard.setOnClickListener(view->{

    });
    mBinding.flAppendCard.setOnClickListener(view->{

    });
    mBinding.flCheckout.setOnClickListener(view->{

    });
  }

  private void initToolbar() {
    mBinding.includeToolbar.toolbarLayout.setBackgroundColor(Color.TRANSPARENT);
    ToolbarModel toolbarModel = new ToolbarModel("收银台");
    toolbarModel.setMenu(R.menu.menu_checkout_help);
    toolbarModel.setListener(new Toolbar.OnMenuItemClickListener() {
      @Override public boolean onMenuItemClick(MenuItem item) {
        ToastUtils.show("menu click");
        return false;
      }
    });
    mBinding.setToolbarModel(toolbarModel);
    initToolbar(mBinding.includeToolbar.toolbar);
  }
}
