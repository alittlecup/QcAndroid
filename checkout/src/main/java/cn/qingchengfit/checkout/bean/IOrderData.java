package cn.qingchengfit.checkout.bean;


public interface IOrderData {
  String getQrCodeUri();
  String getPrice();
  String getOrderNumber();
  String getPollingNUmber();
  ScanRepayInfo getScanRePayInfo();
}
