package com.qingchengfit.fitcoach.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import cn.qingchengfit.utils.DateUtils;
import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import cn.qingchengfit.bean.CmBean;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class BatchCircleItem extends AbstractFlexibleItem<BatchCircleItem.BatchCircleVH> {

    public CmBean cmBean;
    public boolean isPrivate;

    public BatchCircleItem(CmBean cmBean, boolean isPrivate) {
        this.cmBean = cmBean;
        this.isPrivate = isPrivate;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_batch_circle;
    }

    @Override public BatchCircleVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new BatchCircleVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, BatchCircleVH holder, int position, List payloads) {

        if (isPrivate) {
            holder.workTime.setText(DateUtils.getTimeHHMM(cmBean.dateStart) + "-" + DateUtils.getTimeHHMM(cmBean.dateEnd));
        } else {
            holder.workTime.setText(DateUtils.getTimeHHMM(cmBean.dateStart));
        }
        String workday = "每周";
        for (int i = 0; i < cmBean.week.size(); i++) {
            if (i < cmBean.week.size() - 1) {
                workday =
                    workday.concat(holder.itemView.getContext().getResources().getStringArray(R.array.week_simple)[cmBean.week.get(i) - 1])
                        .concat(Configs.SIGN_PAUSE);
            } else {
                workday =
                    workday.concat(holder.itemView.getContext().getResources().getStringArray(R.array.week_simple)[cmBean.week.get(i) - 1]);
            }
        }
        holder.workWeekday.setText(workday);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class BatchCircleVH extends FlexibleViewHolder {

	ImageView delete;
	TextView workTime;
	TextView workWeekday;

        public BatchCircleVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          delete = (ImageView) view.findViewById(R.id.delete);
          workTime = (TextView) view.findViewById(R.id.work_time);
          workWeekday = (TextView) view.findViewById(R.id.work_weekday);

          delete.setOnClickListener(v -> adapter.removeItem(getAdapterPosition()));
        }
    }
}