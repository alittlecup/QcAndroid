package cn.qingchengfit.saasbase.student.items;

import android.support.annotation.ColorInt;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TooManyListenersException;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.student.network.body.FollowUpDataStatistic;
import cn.qingchengfit.saasbase.student.utils.StudentBusinessUtils;
import cn.qingchengfit.saasbase.student.widget.LineCharDate;
import cn.qingchengfit.saasbase.student.widget.LineCharWithBottomDate;
import cn.qingchengfit.utils.DateUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by huangbaole on 2017/11/3.
 */

public class DataStatisticsItem extends AbstractFlexibleItem<DataStatisticsItem.DataStatisticsVH> {
    @Override
    public boolean equals(Object o) {
        return false;
    }

    String title;
    LineData data;
    @ColorInt int lineColor;
    @ColorInt int fillColor;
    int offSetDay=30;
    FollowUpDataStatistic.NewCreateUsersBean bean;

    public DataStatisticsItem(String title, FollowUpDataStatistic.NewCreateUsersBean bean,@ColorInt int lineColor,@ColorInt int fillColor) {
        this.bean=bean;
        this.data = StudentBusinessUtils.transformBean2Data(bean,offSetDay,lineColor,fillColor);
        this.lineColor=lineColor;
        this.fillColor=fillColor;
        this.title = title;
    }
    public String getTitle(){
        return title;
    }
    public void setOffDay(int days){
        this.offSetDay=days;
    }

    public void setLineColor(int lineColor) {
        this.lineColor = lineColor;
    }

    public void setFillColor(int fillColor) {
        this.fillColor = fillColor;
    }
    public void invalidate(){
        this.data=StudentBusinessUtils.transformBean2Data(bean,offSetDay,lineColor,fillColor);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_data_statistics;
    }

    @Override
    public DataStatisticsVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new DataStatisticsVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, DataStatisticsVH holder, int position, List payloads) {
        holder.tvTitle.setText(title);

        holder.tvDataToday.setText(String.valueOf(bean.today_count));
        holder.tvData7day.setText(String.valueOf(bean.week_count));
        holder.tvData30day.setText(String.valueOf(bean.month_count));

        holder.lineChar.setData(data);


    }



    public class DataStatisticsVH extends FlexibleViewHolder {
        @BindView(R2.id.tv_title)
        TextView tvTitle;
        @BindView(R2.id.layout_title)
        LinearLayout layoutTitle;
        @BindView(R2.id.tv_data_today)
        TextView tvDataToday;
        @BindView(R2.id.tv_data_7day)
        TextView tvData7day;
        @BindView(R2.id.tv_data_30day)
        TextView tvData30day;
        @BindView(R2.id.layout_data)
        LinearLayout layoutData;
        @BindView(R2.id.lineChar)
        LineCharDate lineChar;

        public DataStatisticsVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }

}
