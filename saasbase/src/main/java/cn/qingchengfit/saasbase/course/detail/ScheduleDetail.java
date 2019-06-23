package cn.qingchengfit.saasbase.course.detail;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.model.base.Space;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.common.Course;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class ScheduleDetail implements Parcelable {
  @SerializedName("start") public String start;
  @SerializedName("end") public String end;
  @SerializedName("url") public String url;
  @SerializedName("id") public long id;
  @SerializedName("course") public Course course;
  @SerializedName("teacher") public List<Staff> teacher;
  @SerializedName("shop") public Shop shop;
  @SerializedName("space") public Space space;
 public float teacher_score;

  public String getStart() {
    return start;
  }

  public String getEnd() {
    return end;
  }

  public String getUrl() {
    return url;
  }

  public long getId() {
    return id;
  }

  public Course getCourse() {
    return course;
  }

  public Staff getTeacher() {
    return teacher.get(0);
  }

  public Shop getShop() {
    return shop;
  }

  public Space getSpace() {
    return space;
  }

  public ScheduleDetail() {
  }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.start);
    dest.writeString(this.end);
    dest.writeString(this.url);
    dest.writeLong(this.id);
    dest.writeParcelable(this.course, flags);
    dest.writeTypedList(this.teacher);
    dest.writeParcelable(this.shop, flags);
    dest.writeParcelable(this.space, flags);
    dest.writeFloat(this.teacher_score);
  }

  protected ScheduleDetail(Parcel in) {
    this.start = in.readString();
    this.end = in.readString();
    this.url = in.readString();
    this.id = in.readLong();
    this.course = in.readParcelable(Course.class.getClassLoader());
    this.teacher = in.createTypedArrayList(Staff.CREATOR);
    this.shop = in.readParcelable(Shop.class.getClassLoader());
    this.space = in.readParcelable(Space.class.getClassLoader());
    this.teacher_score = in.readFloat();
  }

  public static final Creator<ScheduleDetail> CREATOR = new Creator<ScheduleDetail>() {
    @Override public ScheduleDetail createFromParcel(Parcel source) {
      return new ScheduleDetail(source);
    }

    @Override public ScheduleDetail[] newArray(int size) {
      return new ScheduleDetail[size];
    }
  };
}
