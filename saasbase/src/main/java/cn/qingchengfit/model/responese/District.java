package cn.qingchengfit.model.responese;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yangming on 16/11/8.
 */

public class District implements Parcelable {

    public static final Creator<District> CREATOR = new Creator<District>() {
        @Override public District createFromParcel(Parcel source) {
            return new District(source);
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

    public District() {
    }

    protected District(Parcel in) {
        this.province = in.readParcelable(DistrictBean.class.getClassLoader());
        this.city = in.readParcelable(DistrictBean.class.getClassLoader());
        this.code = in.readInt();
        this.name = in.readString();
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

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.province, flags);
        dest.writeParcelable(this.city, flags);
        dest.writeInt(this.code);
        dest.writeString(this.name);
    }

    public static class DistrictBean implements Parcelable {
        public static final Creator<DistrictBean> CREATOR = new Creator<DistrictBean>() {
            @Override public DistrictBean createFromParcel(Parcel source) {
                return new DistrictBean(source);
            }

            @Override public DistrictBean[] newArray(int size) {
                return new DistrictBean[size];
            }
        };
        private int code;
        private String name;

        public DistrictBean() {
        }

        protected DistrictBean(Parcel in) {
            this.code = in.readInt();
            this.name = in.readString();
        }

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

        @Override public int describeContents() {
            return 0;
        }

        @Override public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.code);
            dest.writeString(this.name);
        }
    }
}
