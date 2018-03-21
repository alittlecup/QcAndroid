package cn.qingchengfit.shop.base;

/**
 * Created by huangbaole on 2018/3/20.
 */

public final class ShopPermissionUtils {
  //商品代购：commodity_market
  //订单管理： commodity_orders
  //商品管理：commodity_list
  //库存管理： commodity_inventory
  //分类管理： commodity_category

  public final static String COMMODITY_MARKET="commodity_market";

  public final static String COMMODITY_MARKET_CAN_WRITE="commodity_market_can_write";
  public final static String COMMODITY_MARKET_CAN_CHANGE="commodity_market_can_change";
  public final static String COMMODITY_MARKET_CAN_DELETE="commodity_market_can_delete";

  public final static String COMMODITY_ORDERS="commodity_orders";
  public final static String COMMODITY_ORDERS_CAN_CHANGE="commodity_orders_can_change";
  public final static String COMMODITY_ORDERS_CAN_DELETE="commodity_orders_can_delete";

  public final static String COMMODITY_LIST="commodity_list";
  public final static String COMMODITY_LIST_CAN_WRITE="commodity_list_can_write";
  public final static String COMMODITY_LIST_CAN_CHANGE="commodity_list_can_change";
  public final static String COMMODITY_LIST_CAN_DELETE="commodity_list_can_delete";

  public final static String COMMODITY_INVENTORY="commodity_inventory";
  public final static String COMMODITY_INVENTORY_CAN_CHANGE="commodity_inventory_can_change";

  public final static String COMMODITY_CATEGORY="commodity_category";
  public final static String COMMODITY_CATEGORY_CAN_WRITE="commodity_category_can_write";
  public final static String COMMODITY_CATEGORY_CAN_CHANGE="commodity_category_can_change";
  public final static String COMMODITY_CATEGORY_CAN_DELETE="commodity_category_can_delete";


}
