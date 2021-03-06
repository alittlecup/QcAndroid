package cn.qingchengfit.saasbase.course.batch.items;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;


import cn.qingchengfit.Constants;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.saasbase.course.batch.bean.BatchSchedule;
import cn.qingchengfit.support.widgets.CompatTextView;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.widgets.CommonFlexAdapter;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class BatchScheduleItem extends AbstractFlexibleItem<BatchScheduleItem.BatchScheduleVH> {

  BatchSchedule batchSchedule;
  boolean isPrivate;

  public BatchScheduleItem(BatchSchedule batchSchedule,boolean is) {
    this.batchSchedule = batchSchedule;
    this.isPrivate = is;
  }

  public BatchSchedule getBatchSchedule() {
    return batchSchedule;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_batch_schedule;
  }

  @Override
  public BatchScheduleVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new BatchScheduleVH(view, adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, BatchScheduleVH holder, int position,
      List payloads) {
    holder.date.setText(DateUtils.Date2YYYYMMDD(DateUtils.formatDateFromServer(batchSchedule.start)));
    holder.weekday.setText(Constants.WEEKS[DateUtils.getDayOfWeek(DateUtils.formatDateFromServer(batchSchedule.start))]);
    holder.time.setText(DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(batchSchedule.start))+(isPrivate?" - "+DateUtils.getTimeHHMM(DateUtils.formatDateFromServer(batchSchedule.end)):""));

    if (DateUtils.isOutOfDate(DateUtils.formatDateFromServer(batchSchedule.start))){
      //已过期
      holder.outofdate.setText("已过期");
      holder.outofdate.setCompoundDrawablesWithIntrinsicBounds(null,null,null,null);
      holder.date.setTextColor(ContextCompat.getColor(holder.date.getContext(),R.color.text_grey));
      holder.weekday.setTextColor(ContextCompat.getColor(holder.date.getContext(),R.color.text_grey));
      holder.time.setTextColor(ContextCompat.getColor(holder.date.getContext(),R.color.text_grey));
    }else {
      holder.outofdate.setText("");
      holder.outofdate.setCompoundDrawablesWithIntrinsicBounds(null,null, ContextCompat.getDrawable(holder.outofdate.getContext(),R.drawable.ic_arrow_right),null);
      holder.date.setTextColor(ContextCompat.getColor(holder.date.getContext(),R.color.text_black));
      holder.weekday.setTextColor(ContextCompat.getColor(holder.date.getContext(),R.color.text_black));
      holder.time.setTextColor(ContextCompat.getColor(holder.date.getContext(),R.color.text_black));
    }

    if (adapter instanceof CommonFlexAdapter && ((CommonFlexAdapter) adapter).getStatus() == 1){
      //编辑状态
      holder.itemCheckbox.setVisibility(View.VISIBLE);
      holder.itemCheckbox.setChecked(adapter.isSelected(position));

    }else {
      holder.itemCheckbox.setVisibility(View.GONE);
    }
  }

  @Override public boolean equals(Object o) {
    if (o instanceof BatchSchedule){
      return TextUtils.equals(((BatchSchedule) o).id,batchSchedule.id);
    }else
      return false;
  }

  public class BatchScheduleVH extends FlexibleViewHolder {

	CheckBox itemCheckbox;
	TextView date;
	TextView weekday;
	TextView time;
	CompatTextView outofdate;

    public BatchScheduleVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      itemCheckbox = (CheckBox) view.findViewById(R.id.item_checkbox);
      date = (TextView) view.findViewById(R.id.date);
      weekday = (TextView) view.findViewById(R.id.weekday);
      time = (TextView) view.findViewById(R.id.time);
      outofdate = (CompatTextView) view.findViewById(R.id.outofdate);
    }
  }
}