package cn.qingchengfit.shop.ui.items.product;

/**
 * Created by huangbaole on 2018/1/15.
 */

public interface IProductItemData {
  String getProductName();

  String[] getProductImages();

  String getProductAddTime();

  String getProductPrices();

  int getProductSales();

  int getProductInventory();

  int getProductPriority();
}
