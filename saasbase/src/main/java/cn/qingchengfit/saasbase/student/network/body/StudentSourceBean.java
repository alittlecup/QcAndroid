package cn.qingchengfit.saasbase.student.network.body;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/3.
 */

public class StudentSourceBean implements Parcelable {
    public static final Creator<StudentSourceBean> CREATOR = new Creator<StudentSourceBean>() {
        @Override public StudentSourceBean createFromParcel(Parcel source) {
            return new StudentSourceBean(source);
        }

        @Override public StudentSourceBean[] newArray(int size) {
            return new StudentSourceBean[size];
        }
    };
    public String id;
    public String name;
    public boolean isSelect;     // 列表中是否已选择该项
    public int type;             // 0:全部；1：normal；

    public StudentSourceBean() {
    }

    private StudentSourceBean(Builder builder) {
        id = builder.id;
        name = builder.name;
        isSelect = builder.isSelect;
        type = builder.type;
    }

    protected StudentSourceBean(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
        this.isSelect = in.readByte() != 0;
        this.type = in.readInt();
    }

    public static Builder newBuilder(StudentSourceBean copy) {
        Builder builder = new Builder();
        builder.type = copy.type;
        builder.isSelect = copy.isSelect;
        builder.name = copy.name;
        builder.id = copy.id;
        return builder;
    }

    public static IId builder() {
        return new Builder();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
        dest.writeInt(this.type);
    }

    public interface IBuild {
        StudentSourceBean build();
    }

    public interface IType {
        IBuild withType(int val);
    }

    public interface IIsSelect {
        IType withIsSelect(boolean val);
    }

    public interface IName {
        IIsSelect withName(String val);
    }

    public interface IId {
        IName withId(String val);
    }

    /**
     * {@code StudentSourceBean} builder static inner class.
     */
    public static final class Builder implements IType, IIsSelect, IName, IId, IBuild {
        private int type;
        private boolean isSelect;
        private String name;
        private String id;

        private Builder() {
        }

        /**
         * Sets the {@code type} and returns a reference to {@code IBuild}
         *
         * @param val the {@code type} to set
         * @return a reference to this Builder
         */
        @Override public IBuild withType(int val) {
            type = val;
            return this;
        }

        /**
         * Sets the {@code isSelect} and returns a reference to {@code IType}
         *
         * @param val the {@code isSelect} to set
         * @return a reference to this Builder
         */
        @Override public IType withIsSelect(boolean val) {
            isSelect = val;
            return this;
        }

        /**
         * Sets the {@code name} and returns a reference to {@code IIsSelect}
         *
         * @param val the {@code name} to set
         * @return a reference to this Builder
         */
        @Override public IIsSelect withName(String val) {
            name = val;
            return this;
        }

        /**
         * Sets the {@code id} and returns a reference to {@code IName}
         *
         * @param val the {@code id} to set
         * @return a reference to this Builder
         */
        @Override public IName withId(String val) {
            id = val;
            return this;
        }

        /**
         * Returns a {@code StudentSourceBean} built from the parameters previously set.
         *
         * @return a {@code StudentSourceBean} built with parameters of this {@code
         * StudentSourceBean.Builder}
         */
        public StudentSourceBean build() {
            return new StudentSourceBean(this);
        }
    }
}
