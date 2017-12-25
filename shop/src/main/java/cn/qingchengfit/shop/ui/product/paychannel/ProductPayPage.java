package cn.qingchengfit.shop.ui.product.paychannel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.databinding.PageProductPayBinding;

/**
 * Created by huangbaole on 2017/12/20.
 */

public class ProductPayPage extends ShopBaseFragment<PageProductPayBinding,ProductPayViewModel> {
  @Override protected void subscribeUI() {

  }

  @Override
  public PageProductPayBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding=PageProductPayBinding.inflate(inflater,container,false);
    return mBinding;
  }
}
