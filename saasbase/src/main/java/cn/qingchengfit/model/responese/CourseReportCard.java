package cn.qingchengfit.model.responese;

import android.os.Parcel;
import cn.qingchengfit.saasbase.cards.bean.Card;

/**
 * Created by fb on 2017/5/17.
 */

public class CourseReportCard extends Card {

    public static final Creator<CourseReportCard> CREATOR = new Creator<CourseReportCard>() {
        @Override public CourseReportCard createFromParcel(Parcel source) {
            return new CourseReportCard(source);
        }

        @Override public CourseReportCard[] newArray(int size) {
            return new CourseReportCard[size];
        }
    };
    public int card_type;
    public String unit;

    public CourseReportCard() {
    }

    protected CourseReportCard(Parcel in) {
        super(in);
        this.card_type = in.readInt();
        this.unit = in.readString();
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeInt(this.card_type);
        dest.writeString(this.unit);
    }
}
