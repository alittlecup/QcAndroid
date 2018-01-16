package cn.qingchengfit.shop.vo;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.shop.ui.items.category.ICategotyItemData;

/**
 * Created by huangbaole on 2017/12/19.
 */

public class Category implements ICategotyItemData,Parcelable{
  @Override public String getCategoryName() {
    return null;
  }

  @Override public String getCategoryPriority() {
    return null;
  }

  @Override public int getCategoryProductCount() {
    return 0;
  }

  public String getId() {
    return "0";
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {

  }
}
