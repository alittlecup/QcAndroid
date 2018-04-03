package cn.qingchengfit.shop.vo;

/**
 * Created by huangbaole on 2018/3/30.
 */

public final class ShopSensorsConstants {
  //QcSaasCommodityAddVisit	健身房SaaS添加商品页访问
  //QcSaasCommodityAddLeave	健身房SaaS添加商品页跳出
  //QcSaasCommodityDetailVisit	健身房SaaS商品详情页访问
  //QcSaasCommodityDetailLeave	健身房SaaS商品详情页跳出
  //QcSaasCommodityInventoryVisit	健身房SaaS商品库存管理页访问
  //QcSaasCommodityInventoryLeave	健身房SaaS商品库存管理页跳出
  //QcSaasCommodityInventoryRecordsVisit	健身房SaaS商品库存记录页访问
  //QcSaasCommodityInventoryRecordsLeave	健身房SaaS商品库存记录页跳出
  //QcSaasCommodityCategoryVisit	健身房SaaS商品分类管理页访问
  //QcSaasCommodityCategoryLeave	健身房SaaS商品分类管理页跳出

  //QcSaasActivedCommodityListVisit	健身房SaaS出售中商品列表页访问
  //QcSaasAcctivedCommodityListVisit	健身房SaaS出售中商品列表页跳出
  //QcSaasInactivedCommodityListVisit	健身房SaaS已下架商品列表页访问
  //QcSaasInactivedCommodityListLeave	健身房SaaS已下架商品列表页跳出
  //QcSaasPreviewMallBtnClick	健身房SaaS商店会员端预览点击

  //QcSaasAddCommodityConfirmBtnClick	健身房SaaS添加商品保存按钮点击
  //QcSaasAddCommodityCancelBtnClick	健身房SaaS添加商品取消按钮点击
  //QcSaasAddAndActivateCommodityBtnClick	健身房SaaS保存并上架商品按钮点击

  //QcSaasEditCommodityConfirmBtnClick	健身房SaaS编辑商品确认按钮点击
  //QcSaasEditCommodityCancelBtnClick	健身房SaaS编辑商品取消按钮点击

  //has_image		布尔	商品是否有图片
  //images_count		数字	商品图片数量
  //commodity_unit		字符串	商品的单位
  //commodity_name		字符串	商品名称
  //commodity_category		字符串	商品分类名称 不要ID
  //commodity_priority		数字	商品的优先级
  //is_in_house_purchase_enabled		布尔	是否支持馆内购买
  //is_self_purchase_enabled		布尔	是否支持自助购买
  //is_delivery_enabled		布尔	是否支持配送
  //is_card_pay_supported		布尔	是否支持会员卡支付
  //support_cards_count		数字	支持的会员卡数量
  //commodity_options_count		数字	商品价格规格数量
  //commodity_price_${n}		数字	商品价格 (处理多规格n从1开始)
  //card_pay_price_${n}		数字	会员卡支付价格 (处理多规格n从1开始)
  //commodity_inventory_${n}		数字	商品库存（处理多规格n从1开始）
  //commodity_has_desc		布尔	商品是否有描述

  //QcSaasAddCommodityCategoryConfirmBtnClick	健身房SaaS添加商品分类确认按钮点击
  //QcSaasAddCommodityCategoryCancelBtnClick	健身房SaaS添加商品分类取消按钮点击

  public static final String SHOP_COMMODITY_ADD_VISIT = "QcSaasCommodityAddVisit";
  public static final String SHOP_COMMODITY_ADD_LEAVE = "QcSaasCommodityAddLeave";
  public static final String SHOP_COMMODITY_DETAIL_VISIT = "QcSaasCommodityDetailVisit";
  public static final String SHOP_COMMODITY_DETAIL_LEAVE = "QcSaasCommodityDetailLeave";
  public static final String SHOP_COMMODITY_INVENTORY_VISIT = "QcSaasCommodityInventoryVisit";
  public static final String SHOP_COMMODITY_INVENTORY_LEAVE = "QcSaasCommodityInventoryLeave";

  public static final String SHOP_ACTIVED_COMMODITY_LIST_VISIT = "QcSaasActivedCommodityListVisit";
  public static final String SHOP_ACTIVED_COMMODITY_LIST_LEAVE = "QcSaasAcctivedCommodityListLeave";

  public static final String SHOP_INACTIVED_COMMODITY_LIST_VISIT =
      "QcSaasInactivedCommodityListVisit";
  public static final String SHOP_INACTIVED_COMMODITY_LIST_LEAVE =
      "QcSaasInactivedCommodityListLeave";

  public static final String SHOP_PREVIEW_MALL_BTN_CLICK = "QcSaasPreviewMallBtnClick";

  public static final String QC_PAGE_STAY_TIME = "qc_page_stay_time";

  public static final String SHOP_COMMODITY_INVENTORT_RECORDS_VISIT =
      "QcSaasCommodityInventoryRecordsVisit";
  public static final String SHOP_COMMODITY_INVENTORY_RECORDS_LEAVE =
      "QcSaasCommodityInventoryRecordsLeave";
  public static final String SHOP_COMMODITY_CATEGORY_VISIT = "QcSaasCommodityCategoryVisit";
  public static final String SHOP_COMMODITY_CATEGORY_LEAVE = "QcSaasCommodityCategoryLeave";

  public static final String SHOP_ADD_COMMODITY_CONFIRM_BTN_CLICK =
      "QcSaasAddCommodityConfirmBtnClick";
  public static final String SHOP_ADD_COMMODITY_CANCEL_BTN_CLICK =
      "QcSaasAddCommodityCancelBtnClick";
  public static final String SHOP_ADD_AND_ACTIVATE_COMMODITY_CANCEL_BTN_CLICK =
      "QcSaasAddAndActivateCommodityBtnClick";

  public static final String SHOP_EDIT_COMMODITY_CONFIRM_BTN_CLICK =
      "QcSaasEditCommodityConfirmBtnClick";
  public static final String SHOP_EDIT_COMMODITY_CANCEL_BTN_CLICK =
      "QcSaasEditCommodityCancelBtnClick";

  public static final String SHOP_COMMODITY_PROPERTY_HAS_IMAGE = "has_image";
  public static final String SHOP_COMMODITY_PROPERTY_IMAGE_COUNT = "images_count";
  public static final String SHOP_COMMODITY_PROPERTY_COMMDITY_UNIT = "commodity_unit";
  public static final String SHOP_COMMODITY_PROPERTY_COMMDITY_NAME = "commodity_name";
  public static final String SHOP_COMMODITY_PROPERTY_COMMODITY_CATEGORY = "commodity_category";
  public static final String SHOP_COMMODITY_PROPERTY_COMMODITY_PRIORITY = "commodity_priority";
  public static final String SHOP_COMMODITY_PROPERTY_IS_IN_HOUSE_PURCHASE_ENABLED =
      "is_in_house_purchase_enabled";
  public static final String SHOP_COMMODITY_PROPERTY_IS_SELF_PURCHASE_ENABLED =
      "is_self_purchase_enabled";
  public static final String SHOP_COMMODITY_PROPERTY_IS_DELIVERY_ENABLED = "is_delivery_enabled";
  public static final String SHOP_COMMODITY_PROPERTY_IS_CARD_PAY_SUPPORTED =
      "is_card_pay_supported";
  public static final String SHOP_COMMODITY_PROPERTY_SUPPORT_CARDS_COUNT = "support_cards_count";
  public static final String SHOP_COMMODITY_PROPERTY_OPTIONS_COUNT = "commodity_options_count";
  public static final String SHOP_COMMODITY_PROPERTY_GOOD_PRICES = "commodity_price_";
  public static final String SHOP_COMMODITY_PROPERTY_GOOD_CARD_PRICES = "card_pay_price_";
  public static final String SHOP_COMMODITY_PROPERTY_GOOD_INVENTORY = "commodity_inventory_";
  public static final String SHOP_COMMODITY_PROPERTY_HAS_DESC = "commodity_has_desc";

  public static final String SHOP_ADD_CATEGORY_CONFIRM_BTN_CLICK =
      "QcSaasAddCommodityCategoryConfirmBtnClick";
  public static final String SHOP_ADD_CATEGORY_CANCEL_BTN_CLICK =
      "QcSaasAddCommodityCategoryCancelBtnClick";
}
