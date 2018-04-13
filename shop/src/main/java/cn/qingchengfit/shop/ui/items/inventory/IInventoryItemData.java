package cn.qingchengfit.shop.ui.items.inventory;

/**
 * Created by huangbaole on 2018/1/16.
 */

public interface IInventoryItemData {

  String getName();

  String getImageUri();

  boolean getProductStatus();

  String getInventoryCount();
  String getProductUnit();

  CharSequence getCategoryDetail();
}
