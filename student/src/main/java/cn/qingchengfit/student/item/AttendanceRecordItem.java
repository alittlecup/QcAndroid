package cn.qingchengfit.student.item;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.qingchengfit.student.R;
import cn.qingchengfit.student.bean.AttendanceRecord;
import cn.qingchengfit.utils.DateUtils;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import com.tencent.qcloud.timchat.widget.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFilterable;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class AttendanceRecordItem
    extends AbstractFlexibleItem<AttendanceRecordItem.AttendanceRecordVH> implements IFilterable {

  public AttendanceRecord getAttendanceRecord() {
    return attendanceRecord;
  }

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

  @Override
  public AttendanceRecordVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new AttendanceRecordVH(view, adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, AttendanceRecordVH holder, int position,
      List payloads) {
    if (isfistDay) {
      holder.itemStatementDetailHeaderdivier.setVisibility(View.VISIBLE);
      holder.itemStatementDetailDate.setVisibility(View.VISIBLE);
      holder.itemStatementDetailMonth.setText(
          (DateUtils.getMonth(DateUtils.formatDateFromServer(attendanceRecord.start)) + 1) + "月");
      holder.itemStatementDetailDay.setText(
          DateUtils.getDayOfMonth(DateUtils.formatDateFromServer(attendanceRecord.start)) + "");
    } else {
      holder.itemStatementDetailHeaderdivier.setVisibility(View.INVISIBLE);
      holder.itemStatementDetailDate.setVisibility(View.GONE);
    }

    if (attendanceRecord.type == 3) { //签到
      holder.itemStatementDetailPic.setImageResource(R.drawable.ic_wechat_scan);
      holder.itemStatementDetailName.setText("入场签到");
      holder.itemStatementTimeShop.setText(
          DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(attendanceRecord.start))
              + "  "
              + attendanceRecord.shop.name);
      holder.notSignClass.setVisibility(View.GONE);
    } else {// 团课私教
      Glide.with(holder.itemView.getContext())
          .load(PhotoUtils.getSmall(attendanceRecord.course.photo))
          .asBitmap()
          .into(new CircleImgWrapper(holder.itemStatementDetailPic, holder.itemView.getContext()));
      holder.itemStatementDetailName.setText(
          attendanceRecord.teacher.getUsername() + "-" + attendanceRecord.course.getName());
      holder.itemStatementTimeShop.setText(
          DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(attendanceRecord.start))
              + "-"
              + DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(attendanceRecord.end))
              + "  "
              + attendanceRecord.shop.name);
    }
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public boolean filter(String constraint) {
    int type = 0;
    if (constraint.contains("全部")) {
      return true;
    }
    if (constraint.contains("自由训练")) {
      type = 3;
    } else if (constraint.contains("团课")) {
      type = 1;
    } else if (constraint.contains("私教")) {
      type = 2;
    }
    if (attendanceRecord.shop.name.contains(constraint) || attendanceRecord.type == type) {
      return true;
    }
    return false;
  }

  public class AttendanceRecordVH extends FlexibleViewHolder {
	View itemStatementDetailBottomdivier;
	View itemStatementDetailHeaderdivier;
	TextView itemStatementDetailDay;
	TextView itemStatementDetailMonth;
	LinearLayout itemStatementDetailDate;
	ImageView itemStatementDetailPic;
	TextView itemStatementDetailName;
	TextView itemStatementTimeShop;
	TextView account;
	LinearLayout notSignClass;

    public AttendanceRecordVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      itemStatementDetailBottomdivier =
          (View) view.findViewById(R.id.item_statement_detail_bottomdivier);
      itemStatementDetailHeaderdivier =
          (View) view.findViewById(R.id.item_statement_detail_headerdivier);
      itemStatementDetailDay = (TextView) view.findViewById(R.id.item_statement_detail_day);
      itemStatementDetailMonth = (TextView) view.findViewById(R.id.item_statement_detail_month);
      itemStatementDetailDate = (LinearLayout) view.findViewById(R.id.item_statement_detail_date);
      itemStatementDetailPic = (ImageView) view.findViewById(R.id.item_statement_detail_pic);
      itemStatementDetailName = (TextView) view.findViewById(R.id.item_statement_detail_name);
      itemStatementTimeShop = (TextView) view.findViewById(R.id.item_statement_time_shop);
      account = (TextView) view.findViewById(R.id.account);
      notSignClass = (LinearLayout) view.findViewById(R.id.not_sign_class);
    }
  }
}