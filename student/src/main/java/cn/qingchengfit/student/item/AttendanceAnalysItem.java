package cn.qingchengfit.student.item;

import android.view.View;
import android.widget.TextView;
import cn.qingchengfit.student.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class AttendanceAnalysItem extends AbstractFlexibleItem<AttendanceAnalysItem.AttendanceAnalysVH> {

    String day;
    String action;
    int color;
    String unit;

    public AttendanceAnalysItem(String day, String action, int color, String unit) {
        this.day = day;
        this.action = action;
        this.color = color;
        this.unit = unit;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_attendance_class;
    }

    @Override public AttendanceAnalysVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new AttendanceAnalysVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, AttendanceAnalysVH holder, int position, List payloads) {
        holder.bgColor.setBackgroundColor(color);
        holder.tvDay.setText(day);
        holder.tvAction.setText(action);
        holder.unit.setText(unit);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class AttendanceAnalysVH extends FlexibleViewHolder {
	TextView tvDay;
	TextView tvAction;
	View bgColor;
	TextView unit;

        public AttendanceAnalysVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          tvDay = (TextView) view.findViewById(R.id.tv_day);
          tvAction = (TextView) view.findViewById(R.id.tv_action);
          bgColor = (View) view.findViewById(R.id.bg_color);
          unit = (TextView) view.findViewById(R.id.unit);
        }
    }
}