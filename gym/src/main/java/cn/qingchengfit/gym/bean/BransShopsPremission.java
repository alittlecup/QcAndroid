package cn.qingchengfit.gym.bean;

import android.os.Parcel;
import android.os.Parcelable;

public class BransShopsPremission implements Parcelable {

  /**
   * has_permission : false
   * id : 6
   * gym_id : 741
   * name : 亚洲健身学院店
   */

  private boolean has_permission;
  private int id;
  private int gym_id;
  private String name;

  public boolean isHas_permission() {
    return has_permission;
  }

  public void setHas_permission(boolean has_permission) {
    this.has_permission = has_permission;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getGym_id() {
    return gym_id;
  }

  public void setGym_id(int gym_id) {
    this.gym_id = gym_id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeByte(this.has_permission ? (byte) 1 : (byte) 0);
    dest.writeInt(this.id);
    dest.writeInt(this.gym_id);
    dest.writeString(this.name);
  }

  public BransShopsPremission() {
  }

  protected BransShopsPremission(Parcel in) {
    this.has_permission = in.readByte() != 0;
    this.id = in.readInt();
    this.gym_id = in.readInt();
    this.name = in.readString();
  }

  public static final Parcelable.Creator<BransShopsPremission> CREATOR =
      new Parcelable.Creator<BransShopsPremission>() {
        @Override public BransShopsPremission createFromParcel(Parcel source) {
          return new BransShopsPremission(source);
        }

        @Override public BransShopsPremission[] newArray(int size) {
          return new BransShopsPremission[size];
        }
      };
}
