package cn.qingchengfit.shop.ui.items.product;

/**
 * Created by huangbaole on 2018/1/15.
 */

public interface IProductItemData {
  String getProductName();

  String getProductImage();

  String getProductAddTime();

  String getProductPrices();

  int getProductSales();

  int getProductInventory();

  int getProductPriority();

  boolean getProductStatus();
}
