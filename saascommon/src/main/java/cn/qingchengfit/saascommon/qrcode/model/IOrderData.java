package cn.qingchengfit.saascommon.qrcode.model;

public interface IOrderData {
  String getQrCodeUri();
  String getPrices();
  String getOrderNumber();
  String getPollingNUmber();
}
