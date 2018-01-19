package cn.qingchengfit.saasbase.student.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;
import java.util.List;

import cn.qingchengfit.saasbase.R;

/**
 * Created by huangbaole on 2017/11/3.
 */

public class LineCharWithBottomDate extends LineChart {

    public LineCharWithBottomDate(Context context) {
        super(context);
        initView();
    }

    public LineCharWithBottomDate(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public LineCharWithBottomDate(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    private void initView() {
        setOnChartGestureListener(new SimpleOnChartGestureListener() {
            @Override
            public void onChartTranslate(MotionEvent me, float dX, float dY) {
                float minX = getLowestVisibleX();
                float maxX = getHighestVisibleX();
                if (minX == 0) {
                    highlightValue(minX + 3, 0);
                } else {
                    highlightValue(maxX - 3, 0);
                }
            }
        });
        setHighlightPerDragEnabled(false);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true
        setDrawGridBackground(false);
        setDrawBorders(false);  //是否在折线图上添加边框

        // no description text
        getDescription().setEnabled(true);
        // 如果没有数据的时候，会显示这个，类似listview的emtpyview
        setNoDataText("暂无数据");
        setNoDataTextColor(Color.parseColor("#6eb8f1"));

        // enable touch gestures
        setTouchEnabled(true);
        // enable scaling and dragging
        setDragEnabled(true);
        setScaleEnabled(false);
        // if disabled, scaling can be done on x- and y-axis separately
        setPinchZoom(false);
        setDragDecelerationEnabled(false);


        initXAxis();

        initYAxis();

        getAxisRight().setEnabled(false);
        animateX(500);

        Legend l = getLegend();

        l.setForm(Legend.LegendForm.NONE);
//        setViewPortOffsets(mViewPortHandler.offsetLeft(), mViewPortHandler.offsetTop(), mViewPortHandler.offsetRight(), dip2px(getContext(), 30));

    }

    private void initYAxis() {
        mAxisLeft.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        mAxisLeft.setAxisMaximum(5);//Y轴最大高度
        mAxisLeft.setSpaceTop(10);
        mAxisLeft.setStartAtZero(true);//设置Y轴坐标是否从0开始

        mAxisLeft.setDrawLimitLinesBehindData(false);
        mAxisLeft.setLabelCount(4, true);
        mAxisLeft.setTextColor(Color.parseColor("#999999"));
        mAxisLeft.setAxisLineColor(Color.TRANSPARENT);
        mAxisLeft.setGridColor(Color.parseColor("#dddddd"));
    }

    public void setYAxisMaximum(float yAxisMaximum) {
        mAxisLeft.setAxisMaximum(yAxisMaximum);
    }

    private void initXAxis() {
        mXAxis.removeAllLimitLines();
        mXAxis.enableGridDashedLine(10f, 10f, 0f);
        mXAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        mXAxis.setGridColor(Color.TRANSPARENT);// 去掉网格中竖线的显示
        //        mXAxis.setLabelCount(0, true);
        mXAxis.setDrawLabels(false);
        mXAxis.setTextColor(Color.parseColor("#999999"));
        mXAxis.setXOffset(20);
        mXAxis.setAxisLineColor(Color.parseColor("#dddddd"));
    }

    public void setLeftText(String text) {
        float x = 0 + mViewPortHandler.offsetLeft();
        float y = getHeight() - dip2px(getContext(), 10);
        Description description = new Description();
        description.setText(text);
        description.setPosition(x, y);
        description.setTextColor(Color.RED);
        description.setTextSize(dip2px(getContext(), 10));
        addDescription(description);
        invalidate();
    }

    public void setRightText(String text) {
        float y = getHeight() - dip2px(getContext(), 10);
        Description description = new Description();
        description.setText(text);
        description.setTextColor(Color.RED);
        description.setTextSize(dip2px(getContext(), 10));
        float x = getWidth() - mViewPortHandler.offsetRight() - mDescPaint.measureText(description.getText());
        description.setPosition(x, y);
        addDescription(description);
        invalidate();
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void addDescription(Description description) {
        if (descriptions.contains(description)) {
            return;
        }
        descriptions.add(description);
    }

    private List<Description> descriptions = new ArrayList<>();

    /**
     * Draws the description text in the bottom right corner of the chart (per default)
     */
//    @Override
//    protected void drawDescription(Canvas c) {
//
//        // check if description should be drawn
//        drawDescription(c, mDescription);
//        if (descriptions.size() >= 0) {
//            for (Description description : descriptions) {
//                drawDescription(c, description);
//            }
//        }
//    }
    private void drawDescription(Canvas c, Description mDescription) {
        if (mDescription != null && mDescription.isEnabled()) {

            MPPointF position = mDescription.getPosition();

            mDescPaint.setTypeface(mDescription.getTypeface());
            mDescPaint.setTextSize(mDescription.getTextSize());
            mDescPaint.setColor(mDescription.getTextColor());
            mDescPaint.setTextAlign(mDescription.getTextAlign());

            float x, y;

            // if no position specified, draw on default position
            if (position == null) {
                x = getWidth() - mViewPortHandler.offsetRight() - mDescription.getXOffset();
                y = getHeight() - mViewPortHandler.offsetBottom() - mDescription.getYOffset();
            } else {
                x = position.x;
                y = position.y;
            }

            c.drawText(mDescription.getText(), x + mDescPaint.measureText(mDescription.getText()), y, mDescPaint);
        }
    }

    public class SimpleOnChartGestureListener implements OnChartGestureListener {

        @Override
        public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

        }

        @Override
        public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

        }

        @Override
        public void onChartLongPressed(MotionEvent me) {

        }

        @Override
        public void onChartDoubleTapped(MotionEvent me) {

        }

        @Override
        public void onChartSingleTapped(MotionEvent me) {

        }

        @Override
        public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

        }

        @Override
        public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

        }

        @Override
        public void onChartTranslate(MotionEvent me, float dX, float dY) {

        }
    }
}
