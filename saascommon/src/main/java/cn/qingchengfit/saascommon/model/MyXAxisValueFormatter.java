package cn.qingchengfit.saascommon.model;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import java.util.ArrayList;

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
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/14.
 */

public class MyXAxisValueFormatter implements IAxisValueFormatter {
    private ArrayList<String> xValues;

    public MyXAxisValueFormatter(ArrayList<String> xValues) {
        this.xValues = xValues;
    }

    @Override public String getFormattedValue(float value, AxisBase axis) {
        if (value >= xValues.size()) return "";
        return xValues.get((int) value).substring(5);
    }
}
