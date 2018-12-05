package cn.qingchengfit.checkout.bean;

import android.os.Parcelable;

public interface OrderListItemData extends Parcelable {
  @PayChannel int getType();

  String getOrderMoney();

  String getOrderCreateDate();
}
