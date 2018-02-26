package cn.qingchengfit.shop.di;

import dagger.Subcomponent;

/**
 * Created by huangbaole on 2017/12/19.
 */
 @Subcomponent(modules = { BindShopActivity.class, ShopViewModel.class })
public interface ShopModelComponent {
  @Subcomponent.Builder interface Builder {
    ShopModelComponent build();
  }
}
