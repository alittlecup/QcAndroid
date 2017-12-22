package cn.qingchengfit.saasbase.cards.bean;

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
 * Created by Paper on 16/3/31 2016.
 */
public class Create_By implements Parcelable {

    public String username;
    public String id;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.username);
        dest.writeString(this.id);
    }

    public Create_By() {
    }

    protected Create_By(Parcel in) {
        this.username = in.readString();
        this.id = in.readString();
    }

    public static final Creator<Create_By> CREATOR = new Creator<Create_By>() {
        @Override public Create_By createFromParcel(Parcel source) {
            return new Create_By(source);
        }

        @Override public Create_By[] newArray(int size) {
            return new Create_By[size];
        }
    };
}
