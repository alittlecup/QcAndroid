package cn.qingchengfit.saasbase.student.bean;

/**
 * Created by fb on 2017/3/7.
 */

public class FilterSortBean {

    public String name;
    public boolean isHighToLow;
    public boolean isLowToHigh;

    public FilterSortBean(String name, boolean isHighToLow, boolean isLowToHigh) {
        this.name = name;
        this.isHighToLow = isHighToLow;
        this.isLowToHigh = isLowToHigh;
    }
}
