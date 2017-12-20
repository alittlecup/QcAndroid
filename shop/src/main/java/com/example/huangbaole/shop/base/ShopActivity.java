package com.example.huangbaole.shop.base;

import android.content.Intent;
import android.support.v4.app.Fragment;
import cn.qingchengfit.saasbase.SaasContainerActivity;
import cn.qingchengfit.shop.routers.ShopRouterCenter;
import cn.qingchengfit.shop.routers.shopImpl;
import com.anbillon.flabellum.annotations.Trunk;
import com.example.huangbaole.shop.ui.category.ShopCategoryPage;
import com.example.huangbaole.shop.ui.home.ShopHomePage;
import com.example.huangbaole.shop.ui.home.categorylist.ShopCategoryListPage;
import com.example.huangbaole.shop.ui.home.inventorylist.ShopInventoryListPage;
import com.example.huangbaole.shop.ui.home.productlist.ShopProductsListPage;
import com.example.huangbaole.shop.ui.inventory.ShopInventoryPage;
import com.example.huangbaole.shop.ui.inventory.product.ProductInventoryPage;
import com.example.huangbaole.shop.ui.inventory.product.UpdateInventoryPage;
import com.example.huangbaole.shop.ui.product.ShopProductPage;
import com.example.huangbaole.shop.ui.product.addsuccess.ProductAddSuccessPage;
import com.example.huangbaole.shop.ui.product.deliverchannel.ProductDeliverPage;
import com.example.huangbaole.shop.ui.product.paychannel.ProductPayPage;
import com.example.huangbaole.shop.ui.product.prices.ProductPricesPage;

@Trunk(fragments = {
    ShopHomePage.class, ShopProductsListPage.class, ShopCategoryListPage.class,
    ShopInventoryListPage.class, ShopCategoryPage.class, ShopInventoryPage.class,
    ShopProductPage.class, ProductInventoryPage.class, UpdateInventoryPage.class,
    ProductAddSuccessPage.class, ProductPayPage.class, ProductDeliverPage.class,
    ProductPricesPage.class
}) public class ShopActivity extends SaasContainerActivity {
  @Override public String getModuleName() {
    return "shop";
  }

  @Override protected Fragment getRouterFragment(Intent intent) {
    return new ShopRouterCenter(new shopImpl()).getFragment(intent.getData(), intent.getExtras());
  }
}
