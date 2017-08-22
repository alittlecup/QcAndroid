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
public class ImageThreeTextBean implements Parcelable {

    public static final String TAG_MODEL = "model";
    public static final String TAG_ID = "gymid";
    public static final String TAG_COURSE = "courseid";
    public static final String TAG_LENGTH = "length";
    public static final String TAG_COURSETYPE = "coursetype";
    public static final Creator<ImageThreeTextBean> CREATOR = new Creator<ImageThreeTextBean>() {
        @Override public ImageThreeTextBean createFromParcel(Parcel in) {
            return new ImageThreeTextBean(in);
        }

        @Override public ImageThreeTextBean[] newArray(int size) {
            return new ImageThreeTextBean[size];
        }
    };
    public String imgUrl;
    public String text1;
    public String text2;
    public String text3;
    public boolean showIcon;
    public boolean showRight;
    public int type = 0;//默认为0
    @DrawableRes public int rightIcon;
    public HashMap<String, String> tags = new HashMap<>();

    public ImageThreeTextBean(String imgUrl, String text1, String text2, String text3) {
        this.imgUrl = imgUrl;
        this.text1 = text1;
        this.text2 = text2;
        this.text3 = text3;
        this.showIcon = false;
        this.showRight = false;
    }

    public ImageThreeTextBean(String imgUrl, String text1, String text2, boolean showIcon, boolean showRight) {
        this.imgUrl = imgUrl;
        this.text1 = text1;
        this.text2 = text2;
        this.showIcon = showIcon;
        this.showRight = showRight;
    }

    public ImageThreeTextBean(String imgUrl, String text1, String text2, boolean showIcon, boolean showRight, int rightIcon) {
        this.imgUrl = imgUrl;
        this.text1 = text1;
        this.text2 = text2;
        this.showIcon = showIcon;
        this.showRight = showRight;
        this.rightIcon = rightIcon;
    }

    protected ImageThreeTextBean(Parcel in) {
        imgUrl = in.readString();
        text1 = in.readString();
        text2 = in.readString();
        text3 = in.readString();
        showIcon = in.readByte() != 0;
        showRight = in.readByte() != 0;
        type = in.readInt();
        rightIcon = in.readInt();
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(imgUrl);
        dest.writeString(text1);
        dest.writeString(text2);
        dest.writeString(text3);
        dest.writeByte((byte) (showIcon ? 1 : 0));
        dest.writeByte((byte) (showRight ? 1 : 0));
        dest.writeInt(type);
        dest.writeInt(rightIcon);
    }

    @Override public int describeContents() {
        return 0;
    }
}
