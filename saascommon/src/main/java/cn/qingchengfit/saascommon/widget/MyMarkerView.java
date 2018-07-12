package cn.qingchengfit.saascommon.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.TextView;
import cn.qingchengfit.saascommon.R;
import cn.qingchengfit.saascommon.utils.StudentBusinessUtils;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;

/**
 * Custom implementation of the MarkerView.
 *
 * @author Philipp Jahoda
 */
public class MyMarkerView extends MarkerView {

    private TextView tvDate;
    private TextView tvContent;
    private String strUnit = "";

    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        initView();
    }

    private void initView() {
        tvDate = findViewById(R.id.tv_date);
        tvContent = findViewById(R.id.tvContent);
        Drawable mutate = getResources().getDrawable(R.drawable.ic_graph_indicator).mutate();
        tvContent.setBackground(mutate);
    }

    public void setStringUnit(String unit) {
        strUnit = unit;
    }

    public void setDrawableTintColor(@ColorInt int statusColor) {
        Drawable mutate = getResources().getDrawable(R.drawable.ic_graph_indicator).mutate();
        DrawableCompat.setTint(mutate, statusColor);
        tvContent.setBackground(mutate);
    }

    public void hideDate(){
        tvDate.setVisibility(GONE);
    }
    @Override
    public void refreshContent(Entry e, Highlight highlight) {

        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;
            tvDate.setText((String) ce.getData());
            tvContent.setText("" + StudentBusinessUtils.getNumString(ce.getHigh()) + strUnit);
        } else {
            tvDate.setText((String) e.getData());
            tvContent.setText("" + StudentBusinessUtils.getNumString(e.getY()) + strUnit);
        }

        super.refreshContent(e, highlight);
    }

    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
