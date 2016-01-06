package com.qingchengfit.fitcoach.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/1/6 2016.
 */
public class CourseManagerAdapter extends RecyclerView.Adapter<CourseManagerAdapter.CourseManagerVH>
    implements View.OnClickListener{

    public void setClickTimeListener(OnRecycleItemClickListener clickTimeListener) {
        this.clickTimeListener = clickTimeListener;
    }

    private OnRecycleItemClickListener clickTimeListener;

    @Override
    public CourseManagerVH onCreateViewHolder(ViewGroup parent, int viewType) {
        CourseManagerVH vh = new CourseManagerVH(View.inflate(parent.getContext(), R.layout.item_course_manage, parent));
        vh.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                vh.itemCheckbox.toggle();
                //数据修改 TODO
            }
        });
        vh.time.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(CourseManagerVH holder, int position) {
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onClick(View v) {
        if (clickTimeListener != null)
            clickTimeListener.onItemClick(v,(int)v.getTag());
    }

    public static class CourseManagerVH extends RecyclerView.ViewHolder {
        @Bind(R.id.month)
        TextView month;
        @Bind(R.id.item_checkbox)
        CheckBox itemCheckbox;
        @Bind(R.id.date)
        TextView date;
        @Bind(R.id.weekday)
        TextView weekday;
        @Bind(R.id.time)
        TextView time;
        @Bind(R.id.outofdate)
        TextView outofdate;
        public CourseManagerVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }
}
