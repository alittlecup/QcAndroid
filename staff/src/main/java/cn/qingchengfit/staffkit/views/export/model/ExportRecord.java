package cn.qingchengfit.staffkit.views.export.model;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.staffkit.usecase.bean.Create_By;

/**
 * Created by fb on 2017/9/6.
 */

public class ExportRecord implements Parcelable{
  public String file_name;
  public String file_size;
  public String created_at;
  public Create_By created_by;
  public int status;
  public String excel_url;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.file_name);
    dest.writeString(this.file_size);
    dest.writeString(this.created_at);
    dest.writeParcelable(this.created_by, flags);
    dest.writeInt(this.status);
    dest.writeString(this.excel_url);
  }

  public ExportRecord() {
  }

  protected ExportRecord(Parcel in) {
    this.file_name = in.readString();
    this.file_size = in.readString();
    this.created_at = in.readString();
    this.created_by = in.readParcelable(Create_By.class.getClassLoader());
    this.status = in.readInt();
    this.excel_url = in.readString();
  }

  public static final Creator<ExportRecord> CREATOR = new Creator<ExportRecord>() {
    @Override public ExportRecord createFromParcel(Parcel source) {
      return new ExportRecord(source);
    }

    @Override public ExportRecord[] newArray(int size) {
      return new ExportRecord[size];
    }
  };
}
