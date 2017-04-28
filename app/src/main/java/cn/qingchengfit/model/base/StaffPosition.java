package cn.qingchengfit.model.base;

import android.os.Parcel;
import android.os.Parcelable;

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
 * 工作人员职位--id--name
 * <p/>
 * Created by Paper on 16/4/22 2016.
 */
public class StaffPosition implements Parcelable {

    public static final Creator<StaffPosition> CREATOR = new Creator<StaffPosition>() {
        @Override
        public StaffPosition createFromParcel(Parcel source) {
            return new StaffPosition(source);
        }

        @Override
        public StaffPosition[] newArray(int size) {
            return new StaffPosition[size];
        }
    };
    public String id;
    public String name;

    public StaffPosition() {
    }

    protected StaffPosition(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.name);
    }
}
