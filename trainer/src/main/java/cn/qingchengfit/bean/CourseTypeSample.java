//package cn.qingchengfit.bean;
//
//import android.os.Parcel;
//import android.os.Parcelable;
//import com.google.gson.annotations.SerializedName;
//
///**
// * power by
// * <p>
// * d8888b.  .d8b.  d8888b. d88888b d8888b.
// * 88  `8D d8' `8b 88  `8D 88'     88  `8D
// * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
// * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
// * 88      88   88 88      88.     88 `88.
// * 88      YP   YP 88      Y88888P 88   YD
// * <p>
// * 课程排期--选择课程种类--课程种类 item bean
// * <p>
// * Created by Paper on 16/2/23 2016.
// */
//public class CourseTypeSample implements Parcelable, Cloneable {
//    public static final Creator<CourseTypeSample> CREATOR = new Creator<CourseTypeSample>() {
//        @Override public CourseTypeSample createFromParcel(Parcel in) {
//            return new CourseTypeSample(in);
//        }
//
//        @Override public CourseTypeSample[] newArray(int size) {
//            return new CourseTypeSample[size];
//        }
//    };
//    @SerializedName("id") public String id;
//    @SerializedName("name") public String name;
//    @SerializedName("photo") public String photo;
//    @SerializedName("is_private") public boolean is_private;
//    @SerializedName("length") public int length;
//
//    public CourseTypeSample() {
//    }
//
//    protected CourseTypeSample(Parcel in) {
//        this.id = in.readString();
//        this.name = in.readString();
//        this.photo = in.readString();
//        this.length = in.readInt();
//        this.is_private = in.readByte() != 0;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public void setId(String id) {
//        this.id = id;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public String getPhoto() {
//        return photo;
//    }
//
//    public void setPhoto(String photo) {
//        this.photo = photo;
//    }
//
//    public int getLength() {
//        return length;
//    }
//
//    public void setLength(int length) {
//        this.length = length;
//    }
//
//    public boolean is_private() {
//        return is_private;
//    }
//
//    public void setIs_private(boolean is_private) {
//        this.is_private = is_private;
//    }
//
//    @Override public int hashCode() {
//        return id.hashCode();
//    }
//
//    @Override public boolean equals(Object o) {
//        if (o instanceof CourseTypeSample) {
//            return ((CourseTypeSample) o).getId().equalsIgnoreCase(this.id);
//        } else {
//            return false;
//        }
//    }
//
//    @Override public int describeContents() {
//        return 0;
//    }
//
//    @Override public void writeToParcel(Parcel dest, int flags) {
//        dest.writeString(this.id);
//        dest.writeString(this.name);
//        dest.writeString(this.photo);
//        dest.writeInt(this.length);
//        dest.writeByte(this.is_private ? (byte) 1 : (byte) 0);
//    }
//}
