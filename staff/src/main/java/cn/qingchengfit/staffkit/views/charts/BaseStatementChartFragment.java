package cn.qingchengfit.staffkit.views.charts;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.model.responese.FollowUpDataStatistic;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.BaseFragment;
import cn.qingchengfit.staffkit.rxbus.event.EventChartTitle;
import cn.qingchengfit.staffkit.views.custom.LinerChartHightRender;
import cn.qingchengfit.staffkit.views.custom.MyMarkerView;
import cn.qingchengfit.staffkit.views.student.followup.MyXAxisValueFormatter;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.MeasureUtils;
import cn.qingchengfit.widgets.CommonInputView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Utils;
import com.hannesdorfmann.fragmentargs.FragmentArgs;
import com.hannesdorfmann.fragmentargs.annotation.Arg;
import com.hannesdorfmann.fragmentargs.annotation.FragmentWithArgs;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import timber.log.Timber;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/1/12.
 */
@FragmentWithArgs public class BaseStatementChartFragment extends BaseFragment
    implements OnChartGestureListener, OnChartValueSelectedListener {

    @Arg int mChartType;

    /**
     * 0 是销售报表
     * 1 是课程报表
     * 2 是签到报表
     */

    @BindView(R.id.linechart) LineChart lineChart;
    @BindView(R.id.chart_detail) CommonInputView chartDetail;

    private LineDataSet set1;

    @Override public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FragmentArgs.inject(this);
    }

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_base_statemnt_charts, container, false);
        unbinder = ButterKnife.bind(this, view);
        chartDetail.setLabel(getChartName());
        initCharts();
        isLoading = true;
        return view;
    }

    public String getChartName() {
        switch (mChartType) {
            case 1:
                return "课程报表";
            case 2:
                return "签到报表";
            case 3:
                return "新增注册";
            default:
                return "会员卡销售";
        }
    }

    @OnClick(R.id.chart_detail) public void onClickTitle() {
        RxBus.getBus().post(new EventChartTitle.Builder().chartType(mChartType).build());
    }

    private void initCharts() {

        lineChart.setOnChartGestureListener(this);
        lineChart.setOnChartValueSelectedListener(this);
        lineChart.setHighlightPerDragEnabled(true);//能否拖拽高亮线(数据点与坐标的提示线)，默认是true
        lineChart.setDrawGridBackground(false);
        lineChart.setDrawBorders(false);  //是否在折线图上添加边框
        // no description text
        lineChart.getDescription().setEnabled(false);
        // 如果没有数据的时候，会显示这个，类似listview的emtpyview
        lineChart.setNoDataText("暂无数据");
        lineChart.setNoDataTextColor(Color.parseColor("#6eb8f1"));

        // enable touch gestures
        lineChart.setTouchEnabled(true);
        // enable scaling and dragging
        lineChart.setDragEnabled(false);
        lineChart.setScaleEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        lineChart.setPinchZoom(false);

        lineChart.setDragDecelerationEnabled(false);

        // create a custom MarkerView (extend MarkerView) and specify the layout to use for it
        MyMarkerView mv = new MyMarkerView(getActivity(), R.layout.custom_marker_view, getLineColor(), getUnit());
        mv.setChartView(lineChart); // For bounds control
        lineChart.setMarker(mv); // Set the marker to the chart
        lineChart.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override public void onGlobalLayout() {
                if (lineChart != null) {
                    CompatUtils.removeGlobalLayout(lineChart.getViewTreeObserver(), this);
                    lineChart.setRenderer(new LinerChartHightRender(lineChart, lineChart.getAnimator(), lineChart.getViewPortHandler()));
                }
            }
        });
        XAxis xAxis = lineChart.getXAxis();
        ArrayList<String> xValues = new ArrayList<>();
        xValues.add("");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -7);
        for (int i = 0; i < 7; i++) {
            c.add(Calendar.DATE, 1);
            xValues.add(DateUtils.Date2YYYYMMDD(c.getTime()));
        }
        xAxis.setValueFormatter(new MyXAxisValueFormatter(xValues));
        xAxis.removeAllLimitLines();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGridColor(Color.TRANSPARENT);// 去掉网格中竖线的显示
        xAxis.setLabelCount(2, true);
        xAxis.setDrawLabels(true);
        xAxis.setTextColor(Color.parseColor("#999999"));
        xAxis.setAxisLineColor(Color.parseColor("#dddddd"));
        xAxis.setYOffset(MeasureUtils.dpToPx(3f, getResources()));
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        //leftAxis.setAxisMaximum(Ymax == 0 ? 5 : (Ymax / 3f) * 4f);//Y轴最大高度

        leftAxis.setDrawZeroLine(false);//设置Y轴坐标是否从0开始
        leftAxis.setStartAtZero(true);
        // limit lines are drawn behind data (and not on top)
        leftAxis.setDrawLimitLinesBehindData(false);
        leftAxis.setLabelCount(5, true);
        leftAxis.setTextColor(Color.parseColor("#999999"));
        leftAxis.setAxisLineColor(Color.TRANSPARENT);
        leftAxis.setGridColor(Color.parseColor("#dddddd"));
        leftAxis.setSpaceBottom(MeasureUtils.dpToPx(10f, getResources()));
        leftAxis.setDrawAxisLine(false);
        leftAxis.setXOffset(MeasureUtils.dpToPx(5f, getResources()));
        leftAxis.setValueFormatter(new NumChartYValueFormatter());
        lineChart.setExtraBottomOffset(30f);

        lineChart.setExtraLeftOffset(0);
        lineChart.getAxisRight().setEnabled(false);
        //        lineChart.animateX(500);
        // get the legend (only possible after setting data)

        Legend l = lineChart.getLegend();
        setData(7);

        //        lineChart.setViewPortOffsets(100,0,0,0);
        // modify the legend ...
        l.setForm(Legend.LegendForm.NONE);
        //        lineChart.getXAxis().
        lineChart.setVisibleXRangeMaximum(6.3f);
        // 设置X轴最小的显示范围
        lineChart.setVisibleXRangeMinimum(6.3f);
        // 将图的左边移动到指定的index处
        lineChart.highlightValue(7, 0);
    }

    private void setData(int offDays) {

        // 折线图数据
        if (set1 == null) {
            ArrayList<Entry> values = new ArrayList<Entry>();
            set1 = new LineDataSet(values, null);
            set1.setColor(getLineColor());
            set1.setCircleColor(getLineColor());
            set1.setHighLightColor(getLineColor());
            set1.setHighlightLineWidth(2f);
            set1.setDrawHorizontalHighlightIndicator(false);
            set1.setDrawCircleHole(true);//
            set1.setLineWidth(2f);
            set1.setCircleRadius(4f);
            set1.setCircleHoleRadius(2f);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            if (Utils.getSDKInt() >= 18) {
                // fill drawable only supported on api level 18 and above
                Drawable drawable = getFillDrawable();

                set1.setFillDrawable(drawable);
            } else {
                set1.setFillColor(R.color.colorPrimary);
            }

            set1.setDrawValues(false);
        }
        getDefaultList(7);
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(set1); // add the datasets
        // create a data object with the datasets
        LineData data = new LineData(dataSets);
        // set data
        lineChart.setData(data);
        lineChart.invalidate();
        // 设置X轴最大的显示范围

    }

    public void doData(List<FollowUpDataStatistic.DateCountsBean> datasFromServer) {
        if (datasFromServer == null) return;
        set1.getValues().clear();
        float max = 1f;
        float min = 0f;

        /**
         * 从今天起 前面 7天
         */
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -7);
        for (int i = 1; i < 8; i++) {
            calendar.add(Calendar.DATE, 1);
            String dayStr = DateUtils.Date2YYYYMMDD(calendar.getTime());
            Entry entry = new Entry(i, 0, dayStr);

            for (int j = 0; j < datasFromServer.size(); j++) {
                if (dayStr.equalsIgnoreCase(datasFromServer.get(j).date)) {
                    float value = Float.parseFloat(datasFromServer.get(j).count);
                    entry.setY(value);
                    if (value > max) {
                        max = value;
                    } else if (value < min) {
                        min = value;
                    }
                }
            }
            set1.getValues().add(entry);
        }
        lineChart.getLineData().getDataSets().clear();
        lineChart.getLineData().getDataSets().add(set1);
        if (min < 0) {
            max = Math.max(max, -min);
        } else {
            max = max / 4 * 5;
            max = (int) Math.ceil((double) max);
            if (max % 4 != 0) max = max + 4 - max % 4;
        }
        lineChart.getAxisLeft().setDrawZeroLine(min >= 0);
        lineChart.getAxisLeft().setStartAtZero(min >= 0);
        lineChart.getAxisLeft().setAxisMaximum(max);
        lineChart.getAxisLeft().setAxisMinimum(min);
        lineChart.getAxisLeft().calculate(min, max);
        lineChart.getAxisLeft().setCenterAxisLabels(true);
        lineChart.notifyDataSetChanged();
        lineChart.animateY(300, Easing.EasingOption.Linear);
        lineChart.invalidate();
    }

    public void getDefaultList(int offDays) {
        List<Entry> d = new ArrayList<>();
        //datasExpand.clear();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, -offDays);
        for (int i = 1; i < offDays + 1; i++) {
            calendar.add(Calendar.DATE, 1);
            String dateStr = DateUtils.Date2YYYYMMDD(calendar.getTime());
            d.add(new Entry(i, 0, dateStr));
        }
        set1.clear();
        set1.setValues(d);
    }

    @Override public String getFragmentName() {
        return BaseStatementChartFragment.class.getName();
    }

    @Override public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override public void onChartLongPressed(MotionEvent me) {

    }

    @Override public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override public void onChartSingleTapped(MotionEvent me) {

    }

    @Override public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override public void onValueSelected(Entry e, Highlight h) {
        Timber.e("charts:" + e.getY() + "    " + h.getX());
    }

    @Override public void onNothingSelected() {

    }

    public int getLineColor() {
        switch (mChartType) {
            case 1:
                return Color.parseColor("#0DB14B");
            case 2:
                return Color.parseColor("#8CB5BA");
            case 3:
                return Color.parseColor("#6EB8F1");
            default:
                return Color.parseColor("#F8B359");
        }
    }

    public int getLineColorHalf() {
        switch (mChartType) {
            case 1:
                return Color.parseColor("#330DB14B");
            case 2:
                return Color.parseColor("#338CB5BA");
            case 3:
                return Color.parseColor("#336EB8F1");
            default:
                return Color.parseColor("#33F8B359");
        }
    }

    public String getUnit() {
        switch (mChartType) {
            case 1:
                return "人次";
            case 2:
                return "人次";
            case 3:
                return "人";
            default:
                return "元";
        }
    }

    private Drawable getFillDrawable() {
        return new ColorDrawable(getLineColorHalf());
    }
}
