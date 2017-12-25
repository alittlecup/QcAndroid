package cn.qingchengfit.saasbase.course.batch.bean;

import android.os.Parcel;
import android.os.Parcelable;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.items.types.ICmLRTxt;
import cn.qingchengfit.utils.DateUtils;

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
 * Created by Paper on 16/2/25 2016.
 */
public class Time_repeat implements ICmLRTxt, Parcelable {
    String id;
    String start;
    String end;
    int slice;
    boolean is_cross;
    int weekday;
    boolean isPrivate;

    public String getSliceTimeMin(){
        return slice/60+"分钟";
    }
    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public boolean is_cross() {
        return is_cross;
    }

    public void setIs_cross(boolean is_cross) {
        this.is_cross = is_cross;
    }

    public Time_repeat(String start, String end, int weekday) {
        this.start = start;
        this.end = end;
        this.weekday = weekday;
    }

    public int getSlice() {
        return slice;
    }

    public void setSlice(int slice) {
        this.slice = slice;
    }

    public boolean isIs_cross() {
        return is_cross;
    }

    @Override public String getLeftTxt() {
        return DateUtils.getChineseWeekStr(weekday-1);
    }

    @Override public String getRightTxt() {
        return isPrivate?(start+(is_cross?"-次日":"-")+ end+"\n约课时间间隔为"+slice/60+"分钟"):start;
    }

    @Override public int getLeftIcon() {
        return 0;
    }

    @Override public int getRightIcon() {
        return R.drawable.vd_filter_arrow_down;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getWeekday() {
        return weekday;
    }

    public void setWeekday(int weekday) {
        this.weekday = weekday;
    }

    @Override public int describeContents() {
        return 0;
    }

    @Override public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.start);
        dest.writeString(this.end);
        dest.writeInt(this.slice);
        dest.writeByte(this.is_cross ? (byte) 1 : (byte) 0);
        dest.writeInt(this.weekday);
        dest.writeByte(this.isPrivate ? (byte) 1 : (byte) 0);
    }

    public Time_repeat() {
    }

    protected Time_repeat(Parcel in) {
        this.id = in.readString();
        this.start = in.readString();
        this.end = in.readString();
        this.slice = in.readInt();
        this.is_cross = in.readByte() != 0;
        this.weekday = in.readInt();
        this.isPrivate = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Time_repeat> CREATOR =
      new Parcelable.Creator<Time_repeat>() {
          @Override public Time_repeat createFromParcel(Parcel source) {
              return new Time_repeat(source);
          }

          @Override public Time_repeat[] newArray(int size) {
              return new Time_repeat[size];
          }
      };
}
