package com.qingchengfit.fitcoach.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;


import com.qingchengfit.fitcoach.Configs;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.OnRecycleItemClickListener;
import java.util.List;

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
public class CourseManagerAdapter extends RecyclerView.Adapter<CourseManagerAdapter.CourseManagerVH> implements View.OnClickListener {

    private List<CourseManageBean> datas;

    private boolean isEditable = false;
    private OnRecycleItemClickListener clickTimeListener;

    public CourseManagerAdapter(List<CourseManageBean> datas) {
        this.datas = datas;
    }

    public boolean isEditable() {
        return isEditable;
    }

    public void setEditable(boolean editable) {
        isEditable = editable;
    }

    public void toggleEditable() {
        isEditable = !isEditable;
    }

    public void setClickTimeListener(OnRecycleItemClickListener clickTimeListener) {
        this.clickTimeListener = clickTimeListener;
    }

    @Override public CourseManagerVH onCreateViewHolder(ViewGroup parent, int viewType) {
        CourseManagerVH vh =
            new CourseManagerVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_course_manage, parent, false));
        vh.itemView.setOnClickListener(this);
        //vh.time.setOnClickListener(this);
        //vh.view.setOnTouchListener(new View.OnTouchListener() {
        //    @Override
        //    public boolean onTouch(View v, MotionEvent event) {
        //        return true;
        //    }
        //});
        return vh;
    }

    @Override public void onBindViewHolder(CourseManagerVH holder, int position) {
        holder.itemView.setTag(position);
        holder.time.setTag(position);
        CourseManageBean bean = datas.get(position);

        holder.date.setText(bean.day);
        holder.weekday.setText(Configs.STRINGS_WEEKDAY[bean.WeekDay]);
        holder.time.setText(bean.time);
        if (bean.outdue) {
            holder.outofdate.setVisibility(View.VISIBLE);
            holder.view.setVisibility(View.VISIBLE);
            holder.itemCheckbox.setVisibility(View.INVISIBLE);
        } else {
            holder.outofdate.setVisibility(View.GONE);
            holder.view.setVisibility(View.GONE);
            holder.itemCheckbox.setVisibility(View.VISIBLE);
            holder.itemCheckbox.setChecked(bean.checked);
        }
        //是否显示titile
        if (position > 0) {
            if (!bean.month.equalsIgnoreCase(datas.get(position - 1).month)) {
                holder.month.setVisibility(View.VISIBLE);
                holder.month.setText(bean.month + "排期");
            } else {
                holder.month.setVisibility(View.GONE);
            }
        } else {
            holder.month.setVisibility(View.VISIBLE);
            holder.month.setText(bean.month + "排期");
        }
        holder.itemCheckbox.setVisibility(isEditable ? View.VISIBLE : View.GONE);
    }

    @Override public int getItemCount() {
        return datas.size();
    }

    @Override public void onClick(View v) {
        if (clickTimeListener != null) clickTimeListener.onItemClick(v, (int) v.getTag());
    }

    public static class CourseManagerVH extends RecyclerView.ViewHolder {
	TextView month;
	CheckBox itemCheckbox;
	TextView date;
	TextView weekday;
	TextView time;
	TextView outofdate;
	View view;

        public CourseManagerVH(View itemView) {
            super(itemView);
          month = (TextView) itemView.findViewById(R.id.month);
          itemCheckbox = (CheckBox) itemView.findViewById(R.id.item_checkbox);
          date = (TextView) itemView.findViewById(R.id.date);
          weekday = (TextView) itemView.findViewById(R.id.weekday);
          time = (TextView) itemView.findViewById(R.id.time);
          outofdate = (TextView) itemView.findViewById(R.id.outofdate);
          view = (View) itemView.findViewById(R.id.outofdatelayout);
        }
    }
}
