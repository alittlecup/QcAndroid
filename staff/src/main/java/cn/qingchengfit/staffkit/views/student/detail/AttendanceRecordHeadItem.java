package cn.qingchengfit.staffkit.views.student.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.DateUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.Date;
import java.util.List;

public class AttendanceRecordHeadItem extends AbstractFlexibleItem<AttendanceRecordHeadItem.AttendanceRecordHeadVH> {

    Date date;
    int checkin;
    int group;
    int pr;

    public AttendanceRecordHeadItem(Date date, int checkin, int group, int pr) {
        this.date = date;
        this.checkin = checkin;
        this.group = group;
        this.pr = pr;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_attendece_record_head;
    }

    @Override public AttendanceRecordHeadVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new AttendanceRecordHeadVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, AttendanceRecordHeadVH holder, int position, List payloads) {
        holder.classrecordYear.setText(DateUtils.getChineseMonth(date));
        holder.monthData.setText(holder.monthData.getContext().getString(R.string.attendance_record_head, checkin, group, pr));
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class AttendanceRecordHeadVH extends FlexibleViewHolder {
        @BindView(R.id.classrecord_year) TextView classrecordYear;
        @BindView(R.id.month_data) TextView monthData;

        public AttendanceRecordHeadVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}