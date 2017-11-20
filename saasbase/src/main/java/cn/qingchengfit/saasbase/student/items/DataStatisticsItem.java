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
    FollowUpDataStatistic.NewCreateUsersBean bean;

    public DataStatisticsItem(String title, FollowUpDataStatistic.NewCreateUsersBean bean) {
        this.bean = bean;
        this.title = title;
    }

    public String getTitle() {
        return title;
    }


    @Override
    public int getLayoutRes() {
        return R.layout.item_data_statistics;
    }

    @Override
    public DataStatisticsVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new DataStatisticsVH(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, DataStatisticsVH holder, int position, List payloads) {
        holder.tvTitle.setText(title);

        holder.tvDataToday.setText(String.valueOf(bean.today_count));
        holder.tvData7day.setText(String.valueOf(bean.week_count));
        holder.tvData30day.setText(String.valueOf(bean.month_count));
        holder.lineChar.setData(StudentBusinessUtils.transformBean2DataByType(bean, 30, position));


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
