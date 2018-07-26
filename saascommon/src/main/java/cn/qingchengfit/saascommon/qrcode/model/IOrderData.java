package cn.qingchengfit.saascommon.qrcode.model;

import cn.qingchengfit.saascommon.bean.ScanRepayInfo;

public interface IOrderData {
  String getQrCodeUri();
  String getPrices();
  String getOrderNumber();
  String getPollingNUmber();
  ScanRepayInfo getScanRePayInfo();
}
