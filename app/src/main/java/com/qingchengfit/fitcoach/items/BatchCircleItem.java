package com.qingchengfit.fitcoach.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.bean.CmBean;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.qingchengfit.widgets.utils.DateUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

public class BatchCircleItem extends AbstractFlexibleItem<BatchCircleItem.BatchCircleVH> {

    public CmBean cmBean;

    public BatchCircleItem(CmBean cmBean) {
        this.cmBean = cmBean;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_batch_circle;
    }

    @Override
    public BatchCircleVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new BatchCircleVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, BatchCircleVH holder, int position, List payloads) {
        holder.workTime.setText(DateUtils.getTimeHHMM(cmBean.dateStart)+"-"+DateUtils.getTimeHHMM(cmBean.dateEnd));
        String workday = "每周";
        for (int i = 0; i < cmBean.week.size(); i++) {
            if (i < cmBean.week.size() -1){
               workday= workday.concat(holder.itemView.getContext().getResources().getStringArray(R.array.week_simple)[i]).concat(Configs.SIGN_PAUSE);
            }else workday= workday.concat(holder.itemView.getContext().getResources().getStringArray(R.array.week_simple)[i]);
        }
        holder.workWeekday.setText(workday);
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    public class BatchCircleVH extends FlexibleViewHolder {

        @Bind(R.id.delete)
        ImageView delete;
        @Bind(R.id.work_time)
        TextView workTime;
        @Bind(R.id.work_weekday)
        TextView workWeekday;

        public BatchCircleVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
            delete.setOnClickListener(v -> adapter.removeItem(getAdapterPosition()));
        }
    }
}