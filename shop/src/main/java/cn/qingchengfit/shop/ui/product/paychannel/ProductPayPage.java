package cn.qingchengfit.shop.ui.product.paychannel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.model.others.ToolbarModel;
import cn.qingchengfit.shop.R;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.databinding.PageProductPayBinding;
import com.anbillon.flabellum.annotations.Leaf;

/**
 * Created by huangbaole on 2017/12/20.
 */
@Leaf(module = "shop",path = "/product/paychannel")
public class ProductPayPage extends ShopBaseFragment<PageProductPayBinding,ProductPayViewModel> {
  @Override protected void subscribeUI() {

  }

  @Override
  public PageProductPayBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding=PageProductPayBinding.inflate(inflater,container,false);
    initToolbar();
    return mBinding;
  }

  private void initToolbar() {
    mBinding.setToolbarModel(new ToolbarModel(getString(R.string.choose_product_pay_channel)));
  }
}
