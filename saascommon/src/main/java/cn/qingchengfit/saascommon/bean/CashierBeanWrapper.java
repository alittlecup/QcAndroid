package cn.qingchengfit.saascommon.bean;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.saascommon.qrcode.model.IOrderData;

public class CashierBeanWrapper  implements IOrderData, Parcelable {
  public void setPrices(String prices) {
    this.prices = prices;
  }
  private CashierBean bean;
  public CashierBeanWrapper(CashierBean bean){
      this.bean=bean;
  }

  public void setInfo(ScanRepayInfo info) {
    this.info = info;
  }

  private ScanRepayInfo info;
  private String prices;
  @Override public String getQrCodeUri() {
    return bean.getUrl();
  }

  @Override public String getPrices() {
    return prices;
  }

  @Override public String getOrderNumber() {
    return bean.getPay_trade_no();
  }

  @Override public String getPollingNUmber() {
    return bean.getOut_trade_no();
  }

  @Override public ScanRepayInfo getScanRePayInfo() {
    return info;
  }
  public void setQrCodeUri(String uri){
    if(bean!=null)bean.setUrl(uri);
  }

  public void setPollingNumber(String pollingNumber){
    if(bean!=null)bean.setOut_trade_no(pollingNumber);
  }
  public void setOrderNumber(String orderNumber){
    if(bean!=null)bean.setPay_trade_no(orderNumber);
  }
  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeParcelable(this.bean, flags);
    dest.writeParcelable(this.info, flags);
    dest.writeString(this.prices);
  }

  protected CashierBeanWrapper(Parcel in) {
    this.bean = in.readParcelable(CashierBean.class.getClassLoader());
    this.info = in.readParcelable(ScanRepayInfo.class.getClassLoader());
    this.prices = in.readString();
  }

  public static final Parcelable.Creator<CashierBeanWrapper> CREATOR =
      new Parcelable.Creator<CashierBeanWrapper>() {
        @Override public CashierBeanWrapper createFromParcel(Parcel source) {
          return new CashierBeanWrapper(source);
        }

        @Override public CashierBeanWrapper[] newArray(int size) {
          return new CashierBeanWrapper[size];
        }
      };
}
