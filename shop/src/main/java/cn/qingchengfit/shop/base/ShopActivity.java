package cn.qingchengfit.shop.base;

import android.content.Intent;
import android.support.v4.app.Fragment;
import cn.qingchengfit.saascommon.SaasCommonActivity;
import cn.qingchengfit.shop.di.ShopViewModel;
import cn.qingchengfit.shop.routers.ShopRouterCenter;
import cn.qingchengfit.shop.routers.shopImpl;
import cn.qingchengfit.shop.ui.category.ShopCategoryPage;
import cn.qingchengfit.shop.ui.home.ShopHomePage;
import cn.qingchengfit.shop.ui.home.categorylist.ShopCategoryListPage;
import cn.qingchengfit.shop.ui.home.inventorylist.ShopInventoryListPage;
import cn.qingchengfit.shop.ui.home.productlist.ShopProductsListPage;
import cn.qingchengfit.shop.ui.inventory.ShopInventoryPage;
import cn.qingchengfit.shop.ui.inventory.product.ProductInventoryPage;
import cn.qingchengfit.shop.ui.inventory.product.UpdateInventoryPage;
import cn.qingchengfit.shop.ui.product.ShopProductAddPage;
import cn.qingchengfit.shop.ui.product.ShopProductModifyPage;
import cn.qingchengfit.shop.ui.product.ShopProductPage;
import cn.qingchengfit.shop.ui.product.addsuccess.ProductAddSuccessPage;
import cn.qingchengfit.shop.ui.product.bottom.ShopBottomCategoryFragment;
import cn.qingchengfit.shop.ui.product.deliverchannel.ProductDeliverPage;
import cn.qingchengfit.shop.ui.product.paycardchannel.ProductPayPage;
import cn.qingchengfit.shop.ui.product.prices.ProductPricesPage;
import cn.qingchengfit.shop.ui.product.productdetail.ShopProductDetailPage;
import cn.qingchengfit.shop.ui.product.productdetail.ShopProductModifyDetailPage;
import com.anbillon.flabellum.annotations.Trunk;
import javax.inject.Inject;

@Trunk(fragments = {
    ShopHomePage.class, ShopProductsListPage.class, ShopCategoryListPage.class,
    ShopInventoryListPage.class, ShopCategoryPage.class, ShopInventoryPage.class,
    ShopProductPage.class, ProductInventoryPage.class, UpdateInventoryPage.class,
    ProductAddSuccessPage.class, ProductPayPage.class, ProductDeliverPage.class,
    ProductPricesPage.class, ShopBottomCategoryFragment.class, ShopProductAddPage.class,
    ShopProductModifyPage.class, ShopViewModel.class, ShopProductDetailPage.class,
    ShopProductModifyDetailPage.class
}) public class ShopActivity extends SaasCommonActivity {
  @Inject ShopRouterCenter routerCenter;
  @Override public String getModuleName() {
    return "shop";
  }

  @Override protected Fragment getRouterFragment(Intent intent) {
    return routerCenter.getFragment(intent.getData(), intent.getExtras());
  }
}
