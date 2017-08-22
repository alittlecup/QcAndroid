package com.qingchengfit.fitcoach.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 15/10/15 2015.
 */
public class StudentBean implements Parcelable {
    public static final Creator<StudentBean> CREATOR = new Creator<StudentBean>() {
        @Override public StudentBean createFromParcel(Parcel source) {
            return new StudentBean(source);
        }

        @Override public StudentBean[] newArray(int size) {
            return new StudentBean[size];
        }
    };
    public String username;
    public String phone;
    public String gymStr;
    public String headerPic;
  public int gender;
    public String systemUrl;
    public String head;
    public String id;
    public boolean isTag;
    public boolean isChosen;
    public String color;
    public String modelid;
    public String model;
    public int modeltype;
    public String ship_id;

    public StudentBean() {
    }

    protected StudentBean(Parcel in) {
        this.username = in.readString();
        this.phone = in.readString();
        this.gymStr = in.readString();
        this.headerPic = in.readString();
      this.gender = in.readInt();
        this.systemUrl = in.readString();
        this.head = in.readString();
        this.id = in.readString();
        this.isTag = in.readByte() != 0;
        this.isChosen = in.readByte() != 0;
        this.color = in.readString();
        this.modelid = in.readString();
        this.model = in.readString();
        this.modeltype = in.readInt();
        this.ship_id = in.readString();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeString(this.phone);
        dest.writeString(this.gymStr);
        dest.writeString(this.headerPic);
      dest.writeInt(this.gender);
        dest.writeString(this.systemUrl);
        dest.writeString(this.head);
        dest.writeString(this.id);
        dest.writeByte(this.isTag ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isChosen ? (byte) 1 : (byte) 0);
        dest.writeString(this.color);
        dest.writeString(this.modelid);
        dest.writeString(this.model);
        dest.writeInt(this.modeltype);
        dest.writeString(this.ship_id);
    }
}
