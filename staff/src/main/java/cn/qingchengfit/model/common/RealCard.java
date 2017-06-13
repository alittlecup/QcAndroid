package cn.qingchengfit.model.common;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.model.responese.Student;
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
 * Created by Paper on 16/3/17 2016.
 *
 * 实体卡 废弃
 */
@Deprecated public class RealCard implements Parcelable {

    public static final Creator<RealCard> CREATOR = new Creator<RealCard>() {
        @Override public RealCard createFromParcel(Parcel source) {
            return new RealCard(source);
        }

        @Override public RealCard[] newArray(int size) {
            return new RealCard[size];
        }
    };
    public String name;
    public String students;
    public List<String> student_ids;
    public String balance;
    public String id;
    public int type;
    public String valid_data;
    public String realcard_num;
    public boolean isDuringHoloday;
    public boolean isCancel;
    public boolean expired;
    public Integer trial_days;
    public String color;
    public String gymId;
    public String gymModel;
    public String valid_from;
    public String valid_to;
    public boolean check_valid;
    public String card_tpl_id;
    public List<String> shopids;
    public String total_account;
    public String total_cost;
    public String start;
    public String end;
    public String support_gyms;
    public List<Student> users;

    public RealCard(String name, String students, String balance, String color) {
        this.name = name;
        this.students = students;
        this.balance = balance;
        this.color = color;
    }

    protected RealCard(Parcel in) {
        this.name = in.readString();
        this.students = in.readString();
        this.student_ids = in.createStringArrayList();
        this.balance = in.readString();
        this.id = in.readString();
        this.type = in.readInt();
        this.valid_data = in.readString();
        this.realcard_num = in.readString();
        this.isDuringHoloday = in.readByte() != 0;
        this.isCancel = in.readByte() != 0;
        this.expired = in.readByte() != 0;
        this.trial_days = in.readInt();
        this.color = in.readString();
        this.gymId = in.readString();
        this.gymModel = in.readString();
        this.valid_from = in.readString();
        this.valid_to = in.readString();
        this.check_valid = in.readByte() != 0;
        this.card_tpl_id = in.readString();
        this.shopids = in.createStringArrayList();
        this.total_account = in.readString();
        this.total_cost = in.readString();
        this.start = in.readString();
        this.end = in.readString();
        this.support_gyms = in.readString();
        this.users = in.createTypedArrayList(Student.CREATOR);
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.students);
        dest.writeStringList(this.student_ids);
        dest.writeString(this.balance);
        dest.writeString(this.id);
        dest.writeInt(this.type);
        dest.writeString(this.valid_data);
        dest.writeString(this.realcard_num);
        dest.writeByte(this.isDuringHoloday ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isCancel ? (byte) 1 : (byte) 0);
        dest.writeByte(this.expired ? (byte) 1 : (byte) 0);
        if (trial_days == null) {
            dest.writeInt(0);
        } else {
            dest.writeInt(this.trial_days);
        }
        dest.writeString(this.color);
        dest.writeString(this.gymId);
        dest.writeString(this.gymModel);
        dest.writeString(this.valid_from);
        dest.writeString(this.valid_to);
        dest.writeByte(this.check_valid ? (byte) 1 : (byte) 0);
        dest.writeString(this.card_tpl_id);
        dest.writeStringList(this.shopids);
        dest.writeString(this.total_account);
        dest.writeString(this.total_cost);
        dest.writeString(this.start);
        dest.writeString(this.end);
        dest.writeString(this.support_gyms);
        dest.writeTypedList(this.users);
    }
}
