package cn.qingchengfit.staffkit.views.student.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
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
        @BindView(R.id.tv_day) TextView tvDay;
        @BindView(R.id.tv_action) TextView tvAction;
        @BindView(R.id.bg_color) View bgColor;
        @BindView(R.id.unit) TextView unit;

        public AttendanceAnalysVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}