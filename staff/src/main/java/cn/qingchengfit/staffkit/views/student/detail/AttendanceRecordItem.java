package cn.qingchengfit.staffkit.views.student.detail;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.common.AttendanceRecord;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.DateUtils;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class AttendanceRecordItem extends AbstractFlexibleItem<AttendanceRecordItem.AttendanceRecordVH> {

    AttendanceRecord attendanceRecord;
    boolean isfistDay;

    public AttendanceRecordItem(AttendanceRecord attendanceRecord, boolean isfistDay) {
        this.attendanceRecord = attendanceRecord;
        this.isfistDay = isfistDay;
    }

    public String getUrl() {
        if (attendanceRecord != null) {
            return attendanceRecord.url;
        } else {
            return "";
        }
    }

    @Override public int getLayoutRes() {
        return R.layout.item_attencance_reord;
    }

    @Override public AttendanceRecordVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new AttendanceRecordVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, AttendanceRecordVH holder, int position, List payloads) {
        if (isfistDay) {
            holder.itemStatementDetailHeaderdivier.setVisibility(View.VISIBLE);
            holder.itemStatementDetailDate.setVisibility(View.VISIBLE);
            holder.itemStatementDetailMonth.setText((DateUtils.getMonth(DateUtils.formatDateFromServer(attendanceRecord.start)) + 1) + "月");
            holder.itemStatementDetailDay.setText(
                cn.qingchengfit.utils.DateUtils.getDayOfMonth(DateUtils.formatDateFromServer(attendanceRecord.start)) + "");
        } else {
            holder.itemStatementDetailHeaderdivier.setVisibility(View.INVISIBLE);
            holder.itemStatementDetailDate.setVisibility(View.GONE);
        }
        if (attendanceRecord.type == 3) { //签到
            holder.itemStatementDetailPic.setImageResource(R.drawable.ic_wechat_scan);
            holder.itemStatementDetailName.setText("入场签到");
            holder.itemStatementTimeShop.setText(
                cn.qingchengfit.utils.DateUtils.getTimeHHMM(cn.qingchengfit.utils.DateUtils.formatDateFromServer(attendanceRecord.start))
                    + "  "
                    + attendanceRecord.shop.name);
        } else {// 团课私教
            Glide.with(holder.itemView.getContext())
                .load(PhotoUtils.getSmall(attendanceRecord.course.photo))
                .asBitmap()
                .into(new CircleImgWrapper(holder.itemStatementDetailPic, holder.itemView.getContext()));
            holder.itemStatementDetailName.setText(attendanceRecord.teacher.getUsername() + "-" + attendanceRecord.course.getName());
            holder.itemStatementTimeShop.setText(
                cn.qingchengfit.utils.DateUtils.getTimeHHMM(cn.qingchengfit.utils.DateUtils.formatDateFromServer(attendanceRecord.start))
                    + "-"
                    + cn.qingchengfit.utils.DateUtils.getTimeHHMM(
                    cn.qingchengfit.utils.DateUtils.formatDateFromServer(attendanceRecord.end))
                    + "  "
                    + attendanceRecord.shop.name);
        }
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class AttendanceRecordVH extends FlexibleViewHolder {
        @BindView(R.id.item_statement_detail_bottomdivier) View itemStatementDetailBottomdivier;
        @BindView(R.id.item_statement_detail_headerdivier) View itemStatementDetailHeaderdivier;
        @BindView(R.id.item_statement_detail_day) TextView itemStatementDetailDay;
        @BindView(R.id.item_statement_detail_month) TextView itemStatementDetailMonth;
        @BindView(R.id.item_statement_detail_date) LinearLayout itemStatementDetailDate;
        @BindView(R.id.item_statement_detail_pic) ImageView itemStatementDetailPic;
        @BindView(R.id.item_statement_detail_name) TextView itemStatementDetailName;
        @BindView(R.id.item_statement_time_shop) TextView itemStatementTimeShop;
        @BindView(R.id.account) TextView account;

        public AttendanceRecordVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}