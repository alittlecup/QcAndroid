package cn.qingchengfit.model.responese;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

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
 * //   ┃　　　┃   阿弥陀佛
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/11/17.
 */

public class BodyTestExtra implements Parcelable {
    public static final Parcelable.Creator<BodyTestExtra> CREATOR = new Parcelable.Creator<BodyTestExtra>() {
        public BodyTestExtra createFromParcel(Parcel source) {
            return new BodyTestExtra(source);
        }

        public BodyTestExtra[] newArray(int size) {
            return new BodyTestExtra[size];
        }
    };
    @SerializedName("id") public String id;
    @SerializedName("unit") public String unit;
    @SerializedName("name") public String name;
    @SerializedName("value") public String value;

    public BodyTestExtra() {
    }

    protected BodyTestExtra(Parcel in) {
        this.id = in.readString();
        this.unit = in.readString();
        this.name = in.readString();
        this.value = in.readString();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.unit);
        dest.writeString(this.name);
        dest.writeString(this.value);
    }
}
