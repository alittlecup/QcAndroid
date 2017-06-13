package cn.qingchengfit.model.responese;

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
 * //Created by yangming on 16/12/26.
 */

public class StudentScoreAwardRuleBean implements Parcelable {

    public static final Creator<StudentScoreAwardRuleBean> CREATOR = new Creator<StudentScoreAwardRuleBean>() {
        @Override public StudentScoreAwardRuleBean createFromParcel(Parcel source) {
            return new StudentScoreAwardRuleBean(source);
        }

        @Override public StudentScoreAwardRuleBean[] newArray(int size) {
            return new StudentScoreAwardRuleBean[size];
        }
    };
    public String id;
    public boolean is_active;
    public String dateStart;
    public String dateEnd;
    public Boolean groupTimes_enable;
    public String groupTimes;
    public Boolean privateTimes_enable;
    public String privateTimes;
    public Boolean signinTimes_enable;
    public String signinTimes;
    public Boolean buyTimes_enable;
    public String buyTimes;
    public Boolean chargeTimes_enable;
    public String chargeTimes;

    public StudentScoreAwardRuleBean() {
    }

    protected StudentScoreAwardRuleBean(Parcel in) {
        this.id = in.readString();
        this.is_active = in.readByte() != 0;
        this.dateStart = in.readString();
        this.dateEnd = in.readString();
        this.groupTimes_enable = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.groupTimes = in.readString();
        this.privateTimes_enable = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.privateTimes = in.readString();
        this.signinTimes_enable = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.signinTimes = in.readString();
        this.buyTimes_enable = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.buyTimes = in.readString();
        this.chargeTimes_enable = (Boolean) in.readValue(Boolean.class.getClassLoader());
        this.chargeTimes = in.readString();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeByte(this.is_active ? (byte) 1 : (byte) 0);
        dest.writeString(this.dateStart);
        dest.writeString(this.dateEnd);
        dest.writeValue(this.groupTimes_enable);
        dest.writeString(this.groupTimes);
        dest.writeValue(this.privateTimes_enable);
        dest.writeString(this.privateTimes);
        dest.writeValue(this.signinTimes_enable);
        dest.writeString(this.signinTimes);
        dest.writeValue(this.buyTimes_enable);
        dest.writeString(this.buyTimes);
        dest.writeValue(this.chargeTimes_enable);
        dest.writeString(this.chargeTimes);
    }
}
