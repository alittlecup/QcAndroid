package cn.qingchengfit.model.responese;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

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
 * get BodyTestMeasure by id from server detail data bean
 * <p/>
 * Created by Paper on 16/1/14 2016.
 */
public class BodyTestMeasureData implements Parcelable {

    public static final Creator<BodyTestMeasureData> CREATOR = new Creator<BodyTestMeasureData>() {
        @Override public BodyTestMeasureData createFromParcel(Parcel in) {
            return new BodyTestMeasureData(in);
        }

        @Override public BodyTestMeasureData[] newArray(int size) {
            return new BodyTestMeasureData[size];
        }
    };
    @SerializedName("measure") public BodyTestMeasure measure;

    protected BodyTestMeasureData(Parcel in) {
        measure = in.readParcelable(BodyTestMeasure.class.getClassLoader());
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(measure, i);
    }
}
