package cn.qingchengfit.saasbase.course.course.bean;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.base.Staff;

public class SchedulePhoto implements Parcelable {
    public static final Parcelable.Creator<SchedulePhoto> CREATOR = new Parcelable.Creator<SchedulePhoto>() {
        @Override public SchedulePhoto createFromParcel(Parcel source) {
            return new SchedulePhoto(source);
        }

        @Override public SchedulePhoto[] newArray(int size) {
            return new SchedulePhoto[size];
        }
    };
    private String photo;
    private String created_at;
    private Staff created_by;
    private Staff owner;
    private boolean is_public;
    private Long id;

    public SchedulePhoto() {
    }

    protected SchedulePhoto(Parcel in) {
        this.photo = in.readString();
        this.created_at = in.readString();
        this.created_by = in.readParcelable(Staff.class.getClassLoader());
        this.owner = in.readParcelable(Staff.class.getClassLoader());
        this.is_public = in.readByte() != 0;
        this.id = (Long) in.readValue(Long.class.getClassLoader());
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Staff getCreated_by() {
        return created_by;
    }

    public void setCreated_by(Staff created_by) {
        this.created_by = created_by;
    }

    public Staff getOwner() {
        return owner;
    }

    public void setOwner(Staff owner) {
        this.owner = owner;
    }

    public boolean is_public() {
        return is_public;
    }

    public void setIs_public(boolean is_public) {
        this.is_public = is_public;
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
        dest.writeString(this.photo);
        dest.writeString(this.created_at);
        dest.writeParcelable(this.created_by, flags);
        dest.writeParcelable(this.owner, flags);
        dest.writeByte(this.is_public ? (byte) 1 : (byte) 0);
        dest.writeValue(this.id);
    }
}
