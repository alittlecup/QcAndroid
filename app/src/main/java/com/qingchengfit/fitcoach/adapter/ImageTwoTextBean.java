package com.qingchengfit.fitcoach.adapter;

import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.DrawableRes;

import java.util.HashMap;

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
 * Created by Paper on 15/12/28 2015.
 */
public class ImageTwoTextBean implements Parcelable {
    public String imgUrl;
    public String text1;
    public String text2;
    public boolean showIcon;
    public boolean showRight;
    public int type = 0;//默认为0
    @DrawableRes
    public int rightIcon;
    public HashMap<String,String> tags = new HashMap<>();
    public ImageTwoTextBean(String imgUrl, String text1, String text2) {
        this.imgUrl = imgUrl;
        this.text1 = text1;
        this.text2 = text2;
        this.showIcon = false;
        this.showRight = false;
    }

    public ImageTwoTextBean(String imgUrl, String text1, String text2, boolean showIcon, boolean showRight) {
        this.imgUrl = imgUrl;
        this.text1 = text1;
        this.text2 = text2;
        this.showIcon = showIcon;
        this.showRight = showRight;
    }

    public ImageTwoTextBean(String imgUrl, String text1, String text2, boolean showIcon, boolean showRight, int rightIcon) {
        this.imgUrl = imgUrl;
        this.text1 = text1;
        this.text2 = text2;
        this.showIcon = showIcon;
        this.showRight = showRight;
        this.rightIcon = rightIcon;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imgUrl);
        dest.writeString(this.text1);
        dest.writeString(this.text2);
        dest.writeByte(showIcon ? (byte) 1 : (byte) 0);
        dest.writeByte(showRight ? (byte) 1 : (byte) 0);
        dest.writeInt(this.type);
        dest.writeInt(this.rightIcon);
        dest.writeSerializable(this.tags);
    }

    protected ImageTwoTextBean(Parcel in) {
        this.imgUrl = in.readString();
        this.text1 = in.readString();
        this.text2 = in.readString();
        this.showIcon = in.readByte() != 0;
        this.showRight = in.readByte() != 0;
        this.type = in.readInt();
        this.rightIcon = in.readInt();
        this.tags = (HashMap<String, String>) in.readSerializable();
    }

    public static final Parcelable.Creator<ImageTwoTextBean> CREATOR = new Parcelable.Creator<ImageTwoTextBean>() {
        public ImageTwoTextBean createFromParcel(Parcel source) {
            return new ImageTwoTextBean(source);
        }

        public ImageTwoTextBean[] newArray(int size) {
            return new ImageTwoTextBean[size];
        }
    };
}
