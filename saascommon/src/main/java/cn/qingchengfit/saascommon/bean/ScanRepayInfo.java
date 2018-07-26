package cn.qingchengfit.saascommon.bean;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.HashMap;
import java.util.Map;

public class ScanRepayInfo implements Parcelable {
 private String moduleName;
 private String actionName;

  public String getModuleName() {
    return moduleName;
  }

  public void setModuleName(String moduleName) {
    this.moduleName = moduleName;
  }

  public String getActionName() {
    return actionName;
  }

  public void setActionName(String actionName) {
    this.actionName = actionName;
  }

  public Map<String, String> getParams() {
    return params;
  }

  public void setParams(Map<String, String> params) {
    this.params = params;
  }

  private Map<String,String> params;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.moduleName);
    dest.writeString(this.actionName);
    dest.writeInt(this.params.size());
    for (Map.Entry<String, String> entry : this.params.entrySet()) {
      dest.writeString(entry.getKey());
      dest.writeString(entry.getValue());
    }
  }

  public ScanRepayInfo() {
  }

  protected ScanRepayInfo(Parcel in) {
    this.moduleName = in.readString();
    this.actionName = in.readString();
    int paramsSize = in.readInt();
    this.params = new HashMap<String, String>(paramsSize);
    for (int i = 0; i < paramsSize; i++) {
      String key = in.readString();
      String value = in.readString();
      this.params.put(key, value);
    }
  }

  public static final Parcelable.Creator<ScanRepayInfo> CREATOR =
      new Parcelable.Creator<ScanRepayInfo>() {
        @Override public ScanRepayInfo createFromParcel(Parcel source) {
          return new ScanRepayInfo(source);
        }

        @Override public ScanRepayInfo[] newArray(int size) {
          return new ScanRepayInfo[size];
        }
      };
}
