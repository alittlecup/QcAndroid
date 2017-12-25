package cn.qingchengfit.shop.base;

import android.content.Intent;
import android.support.v4.app.Fragment;
import cn.qingchengfit.saasbase.SaasContainerActivity;
import cn.qingchengfit.shop.routers.ShopRouterCenter;
import cn.qingchengfit.shop.routers.shopImpl;
import com.anbillon.flabellum.annotations.Trunk;
import cn.qingchengfit.shop.ui.category.ShopCategoryPage;
import cn.qingchengfit.shop.ui.home.ShopHomePage;
import cn.qingchengfit.shop.ui.home.categorylist.ShopCategoryListPage;
import cn.qingchengfit.shop.ui.home.inventorylist.ShopInventoryListPage;
import cn.qingchengfit.shop.ui.home.productlist.ShopProductsListPage;
import cn.qingchengfit.shop.ui.inventory.ShopInventoryPage;
import cn.qingchengfit.shop.ui.inventory.product.ProductInventoryPage;
import cn.qingchengfit.shop.ui.inventory.product.UpdateInventoryPage;
import cn.qingchengfit.shop.ui.product.ShopProductPage;
import cn.qingchengfit.shop.ui.product.addsuccess.ProductAddSuccessPage;
import cn.qingchengfit.shop.ui.product.deliverchannel.ProductDeliverPage;
import cn.qingchengfit.shop.ui.product.paychannel.ProductPayPage;
import cn.qingchengfit.shop.ui.product.prices.ProductPricesPage;

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
