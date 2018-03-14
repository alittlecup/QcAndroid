package cn.qingchengfit.shop.ui.product.addsuccess;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.databinding.PageProductAddSuccessBinding;
import com.anbillon.flabellum.annotations.Leaf;
import com.anbillon.flabellum.annotations.Need;

/**
 * Created by huangbaole on 2017/12/20.
 */
@Leaf(module = "shop", path = "/add/success") public class ProductAddSuccessPage
    extends ShopBaseFragment<PageProductAddSuccessBinding, ProductAddSuccessViewModel> {

  @Need Boolean status;

  @Override protected void subscribeUI() {
    mViewModel.onAddMoreClick.observe(this, aVoid -> {
      routeTo("/product/add", null);
      popBack();
    });
    mViewModel.onBackHomeClick.observe(this, aVoid -> {
      getActivity().onBackPressed();
    });
  }

  @Override
  public PageProductAddSuccessBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding = PageProductAddSuccessBinding.inflate(inflater, container, false);
    mBinding.setViewModel(mViewModel);
    mBinding.titleContent.setText(
        getString(status ? R.string.product_onsave_status : R.string.product_offsale_status));
    initToolbar();
    return mBinding;
  }

  private void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel(getString(R.string.add_success)));
    initToolbar(mBinding.includeToolbar.toolbar);
  }
}
