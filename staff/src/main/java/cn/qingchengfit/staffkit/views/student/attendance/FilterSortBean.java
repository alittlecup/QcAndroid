package cn.qingchengfit.staffkit.views.student.attendance;

/**
 * Created by fb on 2017/3/7.
 */

public class FilterSortBean {

    String name;
    boolean isHighToLow;
    boolean isLowToHigh;

    public FilterSortBean(String name, boolean isHighToLow, boolean isLowToHigh) {
        this.name = name;
        this.isHighToLow = isHighToLow;
        this.isLowToHigh = isLowToHigh;
    }
}
