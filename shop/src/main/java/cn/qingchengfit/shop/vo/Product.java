package cn.qingchengfit.shop.vo;

import cn.qingchengfit.shop.ui.items.product.IProductItemData;

/**
 * Created by huangbaole on 2018/1/15.
 */

public class Product implements IProductItemData {


  @Override public String getProductName() {
    return null;
  }

  @Override public String[] getProductImages() {
    return new String[0];
  }

  @Override public String getProductAddTime() {
    return null;
  }

  @Override public String getProductPrices() {
    return null;
  }

  @Override public int getProductSales() {
    return 0;
  }

  @Override public int getProductInventory() {
    return 0;
  }

  @Override public int getProductPriority() {
    return 0;
  }
}
