package cn.qingchengfit.shop.ui.product;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.anbillon.flabellum.annotations.Leaf;
import cn.qingchengfit.shop.base.ShopBaseFragment;
import cn.qingchengfit.shop.databinding.PageShopProductBinding;

/**
 * Created by huangbaole on 2017/12/18.
 */
@Leaf(module = "shop",path = "/shop/product")
public class ShopProductPage extends ShopBaseFragment<PageShopProductBinding,ShopProductViewModel>{
  @Override protected void subscribeUI() {

  }

  @Override
  public PageShopProductBinding initDataBinding(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    mBinding=PageShopProductBinding.inflate(inflater,container,false);
    initViewPager();
    return mBinding;
  }

  private void initViewPager() {
    
  }
}
