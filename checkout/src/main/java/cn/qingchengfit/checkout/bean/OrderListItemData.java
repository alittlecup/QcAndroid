package cn.qingchengfit.checkout.bean;

public interface OrderListItemData {
  @PayChannel int getType();

  String getOrderMoney();

  String getOrderCreateDate();
}
