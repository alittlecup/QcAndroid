package cn.qingchengfit.shop.ui.product.deliverchannel;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.databinding.PageProductDeliverBinding;

/**
 * Created by huangbaole on 2017/12/20.
 */

public class ProductDeliverPage extends ShopBaseFragment<PageProductDeliverBinding,ProductDeliverViewModel> {
  @Override protected void subscribeUI() {

  }

  @Override
  public PageProductDeliverBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding=PageProductDeliverBinding.inflate(inflater,container,false);
    return mBinding;
  }
}
