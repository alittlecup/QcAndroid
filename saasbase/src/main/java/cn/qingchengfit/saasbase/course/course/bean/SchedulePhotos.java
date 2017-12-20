package cn.qingchengfit.saasbase.course.course.bean;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.model.base.Staff;
import java.util.List;

public class SchedulePhotos implements Parcelable {
    public static final Creator<SchedulePhotos> CREATOR = new Creator<SchedulePhotos>() {
        @Override public SchedulePhotos createFromParcel(Parcel source) {
            return new SchedulePhotos(source);
        }

        @Override public SchedulePhotos[] newArray(int size) {
            return new SchedulePhotos[size];
        }
    };
    private String course_name;
    private String start;
    private String end;
    private String url;
    private Staff teacher;
    private Shop shop;
    private List<SchedulePhoto> photos;
    private Long id;

    public SchedulePhotos() {
    }

    protected SchedulePhotos(Parcel in) {
        this.course_name = in.readString();
        this.start = in.readString();
        this.end = in.readString();
        this.url = in.readString();
        this.teacher = in.readParcelable(Staff.class.getClassLoader());
        this.shop = in.readParcelable(Shop.class.getClassLoader());
        this.photos = in.createTypedArrayList(SchedulePhoto.CREATOR);
        this.id = (Long) in.readValue(Long.class.getClassLoader());
    }

    public Shop getShop() {
        return shop;
    }

    public void setShop(Shop shop) {
        this.shop = shop;
    }

    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Staff getTeacher() {
        return teacher;
    }

    public void setTeacher(Staff teacher) {
        this.teacher = teacher;
    }

    public List<SchedulePhoto> getPhotos() {
        return photos;
    }

    public void setPhotos(List<SchedulePhoto> photos) {
        this.photos = photos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.course_name);
        dest.writeString(this.start);
        dest.writeString(this.end);
        dest.writeString(this.url);
        dest.writeParcelable(this.teacher, flags);
        dest.writeParcelable(this.shop, flags);
        dest.writeTypedList(this.photos);
        dest.writeValue(this.id);
    }
}