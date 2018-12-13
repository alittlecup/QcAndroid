package cn.qingchengfit.saasbase.turnovers;

import android.os.Parcel;

public class TestData implements ITurnoverFilterItemData {
  private String desc;
  private String sign;

  public TestData(String desc) {
    this.desc = desc;
  }

  @Override public String getText() {
    return desc;
  }

  @Override public void getSignature() {

  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.desc);
    dest.writeString(this.sign);
  }

  protected TestData(Parcel in) {
    this.desc = in.readString();
    this.sign = in.readString();
  }

  public static final Creator<TestData> CREATOR = new Creator<TestData>() {
    @Override public TestData createFromParcel(Parcel source) {
      return new TestData(source);
    }

    @Override public TestData[] newArray(int size) {
      return new TestData[size];
    }
  };
}
