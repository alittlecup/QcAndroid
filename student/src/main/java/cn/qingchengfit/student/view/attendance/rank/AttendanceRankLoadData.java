package cn.qingchengfit.student.view.attendance.rank;

import android.util.Pair;

/**
 * Created by huangbaole on 2017/11/16.
 */

public class AttendanceRankLoadData {
    Pair<String,String> day;
    boolean revert;
    String sortType;

    public Pair<String, String> getDay() {
        return day;
    }

    public void setDay(Pair<String, String> day) {
        this.day = day;
    }

    public boolean isRevert() {
        return revert;
    }

    public void setRevert(boolean revert) {
        this.revert = revert;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
    }

    public AttendanceRankLoadData(Pair<String, String> day, boolean revert, String sortType) {
        this.day = day;
        this.revert = revert;
        this.sortType = sortType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AttendanceRankLoadData)) return false;

        AttendanceRankLoadData that = (AttendanceRankLoadData) o;

        if (revert != that.revert) return false;
        if (day != null ? !day.equals(that.day) : that.day != null) return false;
        return sortType != null ? sortType.equals(that.sortType) : that.sortType == null;
    }

    @Override
    public int hashCode() {
        int result = day != null ? day.hashCode() : 0;
        result = 31 * result + (revert ? 1 : 0);
        result = 31 * result + (sortType != null ? sortType.hashCode() : 0);
        return result;
    }
}
