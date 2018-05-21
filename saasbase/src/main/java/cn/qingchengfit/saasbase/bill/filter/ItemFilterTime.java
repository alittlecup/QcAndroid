package cn.qingchengfit.saasbase.bill.filter;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.bill.filter.model.FilterModel;
import cn.qingchengfit.utils.DateUtils;
import cn.qingchengfit.utils.DialogUtils;
import com.bigkoo.pickerview.TimeDialogWindow;
import com.bigkoo.pickerview.TimePopupWindow;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.Date;
import java.util.List;

/**
 * Created by fb on 2017/10/12.
 */

//筛选栏中的时间筛选块
public class ItemFilterTime extends AbstractFlexibleItem<ItemFilterTime.ItemFilterTimeVH> {

  private FilterModel filterModel;
  private OnTimeChooseListener onTimeChooseListener;
  private Date startDate;
  private Date endDate;
  private String start;
  private String end;

  public ItemFilterTime(FilterModel filterModel, OnTimeChooseListener onTimeChooseListener) {
    this.filterModel = filterModel;
    this.onTimeChooseListener = onTimeChooseListener;
  }

  public FilterModel getFilterModel() {
    if (filterModel == null){
      return  new FilterModel();
    }
    return filterModel;
  }

  public void setInitTime(String start, String end){
    this.start = start;
    this.end = end;
  }

  @Override
  public ItemFilterTimeVH createViewHolder(View view,FlexibleAdapter adapter) {
    final ItemFilterTimeVH holder =
        new ItemFilterTimeVH(view, adapter);
    final TimeDialogWindow startTimeDialogWindow =
        new TimeDialogWindow(view.getContext(), TimePopupWindow.Type.YEAR_MONTH_DAY);
    startTimeDialogWindow.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
      @Override public void onTimeSelect(Date date) {
        if (endDate != null){
          if (endDate.before(date)){
            DialogUtils.showAlert(view.getContext(), "结束时间不得早于开始时间");
            return;
          }
        }
        startDate = date;
        holder.tvStudentFilterTimeStart.setText(DateUtils.Date2YYYYMMDD(date));
        if (onTimeChooseListener != null) {
          onTimeChooseListener.onTimeStart(DateUtils.Date2YYYYMMDD(date), filterModel.key);
        }
      }
    });
    final TimeDialogWindow endTimeDialogWindow =
        new TimeDialogWindow(view.getContext(), TimePopupWindow.Type.YEAR_MONTH_DAY);
    endTimeDialogWindow.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
      @Override public void onTimeSelect(Date date) {
        if (startDate != null){
          if (startDate.after(date)){
            DialogUtils.showAlert(view.getContext(), "结束时间不得早于开始时间");
            return;
          }

        }
        endDate = date;
        holder.tvStudentFilterTimeEnd.setText(DateUtils.Date2YYYYMMDD(date));
        if (onTimeChooseListener != null) {
          onTimeChooseListener.onTimeEnd(DateUtils.Date2YYYYMMDD(date), filterModel.key);
        }
      }
    });
    holder.tvStudentFilterTimeStart.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (startTimeDialogWindow.isShowing()) {
          startTimeDialogWindow.hide();
        } else {
          startTimeDialogWindow.showAtLocation(null, 0, 0, 0, new Date());
        }
      }
    });
    holder.tvStudentFilterTimeEnd.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (endTimeDialogWindow.isShowing()) {
          endTimeDialogWindow.hide();
        } else {
          endTimeDialogWindow.showAtLocation(null, 0, 0, 0, new Date());
        }
      }
    });
    return holder;
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, ItemFilterTimeVH holder, int position,
      List payloads) {
    if (adapter.isSelected(position)){
      holder.tvStudentFilterTimeEnd.setText("");
      holder.tvStudentFilterTimeStart.setText("");
      return;
    }
    if (!TextUtils.isEmpty(start)){
      holder.tvStudentFilterTimeStart.setText(start.substring(0,10));
      start = "";
    }
    if (!TextUtils.isEmpty(end)){
      holder.tvStudentFilterTimeEnd.setText(end.substring(0,10));
      end = "";
    }
    holder.billFilterTitle.setText(filterModel.name);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_filter_time;
  }

  class ItemFilterTimeVH extends FlexibleViewHolder {

	TextView billFilterTitle;
	TextView tvStudentFilterTimeStart;
	TextView tvStudentFilterTimeEnd;

    public ItemFilterTimeVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      billFilterTitle = (TextView) view.findViewById(R.id.bill_filter_title);
      tvStudentFilterTimeStart = (TextView) view.findViewById(R.id.tv_student_filter_time_start);
      tvStudentFilterTimeEnd = (TextView) view.findViewById(R.id.tv_student_filter_time_end);
    }
  }

  public interface OnTimeChooseListener {
    void onTimeStart(String start, String key);

    void onTimeEnd(String end, String key);
  }
}
