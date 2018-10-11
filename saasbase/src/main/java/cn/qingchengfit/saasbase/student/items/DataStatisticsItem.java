package cn.qingchengfit.saasbase.student.items;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.student.network.body.FollowUpDataStatistic;
import cn.qingchengfit.saascommon.utils.StudentBusinessUtils;
import cn.qingchengfit.saascommon.widget.LineCharDate;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

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
        //holder.lineChar.setData(StudentBusinessUtils.transformBean2DataByType(bean, 30, position));


    }


    public class DataStatisticsVH extends FlexibleViewHolder {

        TextView tvTitle;

        LinearLayout layoutTitle;

        TextView tvDataToday;

        TextView tvData7day;

        TextView tvData30day;

        LinearLayout layoutData;

        LineCharDate lineChar;

        public DataStatisticsVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          tvTitle = (TextView) view.findViewById(R.id.tv_title);
          layoutTitle = (LinearLayout) view.findViewById(R.id.layout_title);
          tvDataToday = (TextView) view.findViewById(R.id.tv_data_today);
          tvData7day = (TextView) view.findViewById(R.id.tv_data_7day);
          tvData30day = (TextView) view.findViewById(R.id.tv_data_30day);
          layoutData = (LinearLayout) view.findViewById(R.id.layout_data);
          lineChar = (LineCharDate) view.findViewById(R.id.lineChar);
        }
    }

}
