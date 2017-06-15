package cn.qingchengfit.staffkit.views.abstractflexibleitem;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.Time_repeat;
import cn.qingchengfit.staffkit.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class BatchWeekLoopItem extends AbstractFlexibleItem<BatchWeekLoopItem.BatchWeekLoopVH> {

    Time_repeat time_repeat;
    boolean isPrivate;

    public BatchWeekLoopItem(Time_repeat time_repeat, boolean isPrivate) {
        this.time_repeat = time_repeat;
        this.isPrivate = isPrivate;
    }

    public Time_repeat getTime_repeat() {
        return time_repeat;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_batch_week_loop;
    }

    @Override public BatchWeekLoopVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new BatchWeekLoopVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, BatchWeekLoopVH holder, int position, List payloads) {
        try {
            holder.tvWeek.setText(
                holder.tvWeek.getContext().getResources().getStringArray(R.array.weeks)[time_repeat.getWeekday() - 1] + (isPrivate ? "可约时间段"
                    : ""));
            String time = time_repeat.getStart();
            if (isPrivate) {
                time = TextUtils.concat(time, "-", time_repeat.getEnd()).toString();
            }
            holder.tvTime.setText(time);
        } catch (Exception e) {

        }
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class BatchWeekLoopVH extends FlexibleViewHolder {
        @BindView(R.id.tv_week) TextView tvWeek;
        @BindView(R.id.tv_time) TextView tvTime;

        public BatchWeekLoopVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}