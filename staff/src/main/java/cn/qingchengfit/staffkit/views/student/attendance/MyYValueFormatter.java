package cn.qingchengfit.staffkit.views.student.attendance;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;

/**
 * Created by fb on 2017/3/10.
 */

public class MyYValueFormatter implements IValueFormatter {

    public MyYValueFormatter() {
    }

    @Override public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
        return ((Float) value).intValue() + "人次";
    }
}