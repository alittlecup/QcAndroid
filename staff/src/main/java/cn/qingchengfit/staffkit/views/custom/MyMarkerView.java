package cn.qingchengfit.staffkit.views.custom;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.ColorInt;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.widget.TextView;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.StringUtils;
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
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    public MyMarkerView(Context context, int layoutResource, @ColorInt int statusColor) {
        super(context, layoutResource);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvContent = (TextView) findViewById(R.id.tvContent);
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_graph_indicator);
        Drawable a = drawable.mutate();
        DrawableCompat.setTint(a, statusColor);
        tvContent.setBackground(a);
    }

    public MyMarkerView(Context context, int layoutResource, @ColorInt int statusColor, String unit) {
        super(context, layoutResource);
        tvDate = (TextView) findViewById(R.id.tv_date);
        tvContent = (TextView) findViewById(R.id.tvContent);
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.ic_graph_indicator);
        Drawable a = drawable.mutate();
        DrawableCompat.setTint(a, statusColor);
        tvContent.setBackground(a);
        strUnit = unit;
    }

    // callbacks everytime the MarkerView is redrawn, can be used to update the
    // content (user-interface)
    @Override public void refreshContent(Entry e, Highlight highlight) {

        if (e instanceof CandleEntry) {

            CandleEntry ce = (CandleEntry) e;
            tvDate.setText((String) ce.getData());
            tvContent.setText("" + StringUtils.getNumString(ce.getHigh()) + strUnit);
        } else {
            tvDate.setText((String) e.getData());
            tvContent.setText("" + StringUtils.getNumString(e.getY()) + strUnit);
        }

        super.refreshContent(e, highlight);
    }

    @Override public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2), -getHeight());
    }
}
