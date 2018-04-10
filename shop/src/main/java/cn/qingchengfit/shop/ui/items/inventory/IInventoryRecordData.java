package cn.qingchengfit.shop.ui.items.inventory;

/**
 * Created by huangbaole on 2018/1/16.
 */

public interface IInventoryRecordData {

  String getProductName();

  String getGoodName();

  String getCreateName();

  String getCreateTime();

  int getOperatorType();

  int getOffset();

  long getInventorCount();
}
