package cn.qingchengfit.model.base;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 16/11/15.
 */

public class Course implements Parcelable ,Cloneable{
  public String id;
    public String name;
    public int length;
    public boolean is_private;
    public int capacity;

    @SerializedName(value = "photo",alternate ={"image"})
    public String photo;

    public Course() {
    }

  @Override public Object clone() {
    Course stu = null;
    try{
      stu = (Course)super.clone();
    }catch(CloneNotSupportedException e) {
      e.printStackTrace();
    }
    return stu;
  }

  private Course(Builder builder) {
        setId(builder.id);
        setName(builder.name);
        setLength(builder.length);
        setIs_private(builder.is_private);
        setPhoto(builder.photo);
        setCapacity(builder.capacity);
    }

  public static final Creator<Course> CREATOR = new Creator<Course>() {
    @Override public Course createFromParcel(Parcel in) {
      return new Course(in);
    }

    @Override public Course[] newArray(int size) {
      return new Course[size];
    }
  };

  public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public int getLength() {
        return length;
    }

    public void setLength(float length) {
        this.length = (int) length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean is_private() {
        return is_private;
    }

    public void setIs_private(boolean is_private) {
        this.is_private = is_private;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

  public static final class Builder {
        private String id;
        private String name;
        private int length;
        private boolean is_private;
        private String photo;
        private int capacity;

        public Builder() {
        }

        public Builder id(String val) {
            id = val;
            return this;
        }

        public Builder name(String val) {
            name = val;
            return this;
        }

        public Builder length(int val) {
            length = val;
            return this;
        }

        public Builder length(float val) {
            length = (int) val;
            return this;
        }

        public Builder is_private(boolean val) {
            is_private = val;
            return this;
        }

        public Builder photo(String val) {
            photo = val;
            return this;
        }

        public Builder capacity(int val) {
            capacity = val;
            return this;
        }

        public Course build() {
            return new Course(this);
        }
    }

  @Override public int describeContents() {
    return 0;
  }

  @Override public void writeToParcel(Parcel dest, int flags) {
    dest.writeString(this.id);
    dest.writeString(this.name);
    dest.writeInt(this.length);
    dest.writeByte(this.is_private ? (byte) 1 : (byte) 0);
    dest.writeString(this.photo);
    dest.writeInt(this.capacity);
  }

  protected Course(Parcel in) {
    this.id = in.readString();
    this.name = in.readString();
    this.length = in.readInt();
    this.is_private = in.readByte() != 0;
    this.photo = in.readString();
    this.capacity = in.readInt();
  }
}
