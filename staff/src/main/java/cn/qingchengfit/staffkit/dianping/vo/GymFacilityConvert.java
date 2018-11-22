package cn.qingchengfit.staffkit.dianping.vo;

import android.text.TextUtils;

public final class GymFacilityConvert {
  private GymFacilityConvert() {
  }

  public static String convertFacilityKeyToString(String facility_key) {
    String text = "";
    if (TextUtils.isEmpty(facility_key)) {
      return text;
    }
    switch (facility_key) {
      case "locker-room":
        text = "更衣柜";
        break;
      case "shop":
        text = "商店";
        break;
      case "shower":
        text = "淋浴";
        break;
      case "swimming-pool":
        text = "游泳池";
        break;
      case "air-conditioner":
        text = "空调";
        break;
      case "air-cleaner":
        text = "空气净化器";
        break;
      case "dd-water":
        text = "直饮水";
        break;
      case "water-bar":
        text = "水吧";
        break;
      case "leisure-area":
        text = "休闲区";
        break;
      case "parking":
        text = "停车场";
        break;
      case "faCard":
        text = "刷卡";
        break;
      case "wifi":
        text = "wifi";
        break;
    }
    return text;
  }
}
