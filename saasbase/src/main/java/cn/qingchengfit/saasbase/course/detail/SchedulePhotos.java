package cn.qingchengfit.saasbase.course.detail;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.network.response.QcListData;
import cn.qingchengfit.saasbase.course.course.bean.SchedulePhoto;
import java.util.List;

public class SchedulePhotos extends QcListData implements Parcelable {

  public List<SchedulePhoto> photos;

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeTypedList(this.photos);
  }

  public SchedulePhotos() {
  }

  protected SchedulePhotos(Parcel in) {
    this.photos = in.createTypedArrayList(SchedulePhoto.CREATOR);
  }

  public static final Parcelable.Creator<SchedulePhotos> CREATOR =
      new Parcelable.Creator<SchedulePhotos>() {
        @Override public SchedulePhotos createFromParcel(Parcel source) {
          return new SchedulePhotos(source);
        }

        @Override public SchedulePhotos[] newArray(int size) {
          return new SchedulePhotos[size];
        }
      };
}
