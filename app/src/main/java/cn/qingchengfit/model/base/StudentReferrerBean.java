package cn.qingchengfit.model.base;

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
 * //Created by yangming on 16/12/2.
 *
 * 学员推荐人
 */

public class StudentReferrerBean extends Personage implements Parcelable {
    public static final Creator<StudentReferrerBean> CREATOR = new Creator<StudentReferrerBean>() {
        @Override public StudentReferrerBean createFromParcel(Parcel source) {
            return new StudentReferrerBean(source);
        }

        @Override public StudentReferrerBean[] newArray(int size) {
            return new StudentReferrerBean[size];
        }
    };
    public String referrerCount; // 已推荐人数
    public boolean isSelect;     // 列表中是否已选择该项
    public int type;             // 0:全部；1：normal；

    public StudentReferrerBean() {
    }

    public StudentReferrerBean(Parcel in, String referrerCount, boolean isSelect, int type) {
        super(in);
        this.referrerCount = referrerCount;
        this.isSelect = isSelect;
        this.type = type;
    }

    protected StudentReferrerBean(Parcel in) {
        super(in);
        this.referrerCount = in.readString();
        this.isSelect = in.readByte() != 0;
        this.type = in.readInt();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeString(this.referrerCount);
        dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
        dest.writeInt(this.type);
    }
}
