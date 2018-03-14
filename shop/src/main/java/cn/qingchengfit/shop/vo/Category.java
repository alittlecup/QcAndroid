package cn.qingchengfit.shop.vo;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.shop.ui.items.category.ICategotyItemData;
import com.google.gson.annotations.Expose;

/**
 * Created by huangbaole on 2017/12/19.
 */

public class Category implements ICategotyItemData, Parcelable {
  @Expose(deserialize = false)
  private String id;
  private String name;
  private int priority;

  public int getProduct_count() {
    return product_count;
  }

  public void setProduct_count(int product_count) {
    this.product_count = product_count;
  }

  private int product_count;

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getPriority() {
    return priority;
  }

  public void setPriority(int priority) {
    this.priority = priority;
  }

  @Override public String getCategoryName() {
    return name;
  }

  @Override public int getCategoryPriority() {
    return priority;
  }

  @Override public int getCategoryProductCount() {
    return product_count;
  }

  public String getId() {
    return id;
  }

  public Category() {
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.name);
    dest.writeInt(this.priority);
    dest.writeInt(this.product_count);
  }

  protected Category(Parcel in) {
    this.id = in.readString();
    this.name = in.readString();
    this.priority = in.readInt();
    this.product_count = in.readInt();
  }

  public static final Creator<Category> CREATOR = new Creator<Category>() {
    @Override public Category createFromParcel(Parcel source) {
      return new Category(source);
    }

    @Override public Category[] newArray(int size) {
      return new Category[size];
    }
  };
}
