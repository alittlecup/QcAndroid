package com.qingchengfit.fitcoach.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/4/13 2016.
 */
public class Brand implements Parcelable {
    public Brand() {
    }

    public Brand(String id) {
        this.id = id;
    }

    private String id;
    private String name;
    private String photo;
    private boolean has_add_permission;
    public boolean has_permission;
    private List<User_Student> owners;
    private String cname;
    private int gym_count;
    private String created_at;
    private User_Student created_by;

    private Brand(Builder builder) {
        setId(builder.id);
        setName(builder.name);
        setPhoto(builder.photo);
        setHas_add_permission(builder.has_add_permission);
        setOwners(builder.owners);
        setCname(builder.cname);
        setGym_count(builder.gym_count);
        setCreated_at(builder.created_at);
        setCreated_by(builder.created_by);
    }


    public User_Student getCreated_by() {
        return created_by;
    }

    public void setCreated_by(User_Student created_by) {
        this.created_by = created_by;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getGym_count() {
        return gym_count;
    }

    public void setGym_count(int gym_count) {
        this.gym_count = gym_count;
    }

    public boolean isHas_add_permission() {
        return has_add_permission;
    }

    public void setHas_add_permission(boolean has_add_permission) {
        this.has_add_permission = has_add_permission;
    }

    public List<User_Student> getOwners() {
        return owners;
    }

    public void setOwners(List<User_Student> owners) {
        this.owners = owners;
    }

    public String getCname() {
        return cname;
    }

    public void setCname(String cname) {
        this.cname = cname;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
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

    public static final class Builder {
        private String id;
        private String name;
        private String photo;
        private boolean has_add_permission;
        private List<User_Student> owners;
        private String cname;
        private int gym_count;
        private String created_at;
        private User_Student created_by;

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

        public Builder photo(String val) {
            photo = val;
            return this;
        }

        public Builder has_add_permission(boolean val) {
            has_add_permission = val;
            return this;
        }

        public Builder owners(List<User_Student> val) {
            owners = val;
            return this;
        }

        public Builder cname(String val) {
            cname = val;
            return this;
        }

        public Builder gym_count(int val) {
            gym_count = val;
            return this;
        }

        public Builder created_at(String val) {
            created_at = val;
            return this;
        }

        public Builder created_by(User_Student val) {
            created_by = val;
            return this;
        }

        public Brand build() {
            return new Brand(this);
        }
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeString(this.photo);
        dest.writeByte(this.has_add_permission ? (byte) 1 : (byte) 0);
        dest.writeByte(this.has_permission ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.owners);
        dest.writeString(this.cname);
        dest.writeInt(this.gym_count);
        dest.writeString(this.created_at);
        dest.writeParcelable(this.created_by, flags);
    }

    protected Brand(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.photo = in.readString();
        this.has_add_permission = in.readByte() != 0;
        this.has_permission = in.readByte() != 0;
        this.owners = in.createTypedArrayList(User_Student.CREATOR);
        this.cname = in.readString();
        this.gym_count = in.readInt();
        this.created_at = in.readString();
        this.created_by = in.readParcelable(User_Student.class.getClassLoader());
    }

    public static final Creator<Brand> CREATOR = new Creator<Brand>() {
        @Override public Brand createFromParcel(Parcel source) {
            return new Brand(source);
        }

        @Override public Brand[] newArray(int size) {
            return new Brand[size];
        }
    };
}
