package cn.qingchengfit.saasbase.course.batch.bean;

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
public class Time_repeat implements ICmLRTxt{
    String id;
    String start;
    String end;
    boolean is_cross;
    int weekday;
    boolean isPrivate;

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public boolean is_cross() {
        return is_cross;
    }

    public void setIs_cross(boolean is_cross) {
        this.is_cross = is_cross;
    }

    @Override public String getLeftTxt() {
        return DateUtils.getChineseWeekStr(weekday-1);
    }

    @Override public String getRightTxt() {
        return isPrivate?(start+"-"+ end):start;
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
}
