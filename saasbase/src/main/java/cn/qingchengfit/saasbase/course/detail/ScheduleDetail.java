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
  @SerializedName("photo") public String url;
  @SerializedName("id") public long id;
  @SerializedName("course") public Course course;
  @SerializedName("teacher") public List<ScheduleTeacherDTO> teacher;
  @SerializedName("shop") public Shop shop;
  @SerializedName("space") public Space space;
  @SerializedName("max_users")public int maxUsers;

  public void setTrainerClass(boolean trainerClass) {
    this.trainerClass = trainerClass;
  }

  @SerializedName("is_private") private boolean trainerClass;
  @SerializedName("users_count")public int usersCount;


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
    return teacher.get(0).getUser();
  }

  public Shop getShop() {
    return shop;
  }

  public Space getSpace() {
    return space;
  }

  public ScheduleDetail() {
  }

  public boolean isTrainerClass() {
    return trainerClass;
  }

  public static class ScheduleTeacherDTO implements Parcelable {

    String url;
    Staff user;
    float score;
    String id;
    List<String> tags;

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    public Staff getUser() {
      return user;
    }

    public void setUser(Staff user) {
      this.user = user;
    }

    public float getScore() {
      return score;
    }

    public void setScore(float score) {
      this.score = score;
    }

    public String getId() {
      return id;
    }

    public void setId(String id) {
      this.id = id;
    }

    public List<String> getTags() {
      return tags;
    }

    public void setTags(List<String> tags) {
      this.tags = tags;
    }

    @Override public int describeContents() {
      return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(this.url);
      dest.writeParcelable(this.user, flags);
      dest.writeFloat(this.score);
      dest.writeString(this.id);
      dest.writeStringList(this.tags);
    }

    public ScheduleTeacherDTO() {
    }

    protected ScheduleTeacherDTO(Parcel in) {
      this.url = in.readString();
      this.user = in.readParcelable(Staff.class.getClassLoader());
      this.score = in.readFloat();
      this.id = in.readString();
      this.tags = in.createStringArrayList();
    }

    public static final Creator<ScheduleTeacherDTO> CREATOR = new Creator<ScheduleTeacherDTO>() {
      @Override public ScheduleTeacherDTO createFromParcel(Parcel source) {
        return new ScheduleTeacherDTO(source);
      }

      @Override public ScheduleTeacherDTO[] newArray(int size) {
        return new ScheduleTeacherDTO[size];
      }
    };
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
    dest.writeInt(this.maxUsers);
    dest.writeByte(this.trainerClass ? (byte) 1 : (byte) 0);
    dest.writeInt(this.usersCount);
    dest.writeFloat(this.teacher_score);
  }

  protected ScheduleDetail(Parcel in) {
    this.start = in.readString();
    this.end = in.readString();
    this.url = in.readString();
    this.id = in.readLong();
    this.course = in.readParcelable(Course.class.getClassLoader());
    this.teacher = in.createTypedArrayList(ScheduleTeacherDTO.CREATOR);
    this.shop = in.readParcelable(Shop.class.getClassLoader());
    this.space = in.readParcelable(Space.class.getClassLoader());
    this.maxUsers = in.readInt();
    this.trainerClass = in.readByte() != 0;
    this.usersCount = in.readInt();
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
