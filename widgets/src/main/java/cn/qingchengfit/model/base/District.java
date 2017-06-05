package cn.qingchengfit.model.base;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yangming on 16/11/8.
 */

public class District implements Parcelable {

    public static final Creator<District> CREATOR = new Creator<District>() {
        @Override public District createFromParcel(Parcel in) {
            return new District(in);
        }

        @Override public District[] newArray(int size) {
            return new District[size];
        }
    };
    /**
     * id : 1
     * name : 安徽
     */

    private DistrictBean province;
    /**
     * id : 1
     * name : 安庆
     */

    private DistrictBean city;
    /**
     * province : {"id":1,"name":"安徽"}
     * city : {"id":1,"name":"安庆"}
     * id : 5
     * name : 潜山县
     */

    private int code;
    private String name;

    protected District(Parcel in) {
        code = in.readInt();
        name = in.readString();
    }

    public DistrictBean getProvince() {
        return province;
    }

    public void setProvince(DistrictBean province) {
        this.province = province;
    }

    public DistrictBean getCity() {
        return city;
    }

    public void setCity(DistrictBean city) {
        this.city = city;
    }

    public int getId() {
        return code;
    }

    public void setId(int id) {
        this.code = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(code);
        parcel.writeString(name);
    }

    public static class DistrictBean {
        private int code;
        private String name;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
