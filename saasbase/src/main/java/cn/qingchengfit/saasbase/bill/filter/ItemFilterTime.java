package cn.qingchengfit.saasbase.bill.filter;

import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
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

  public ItemFilterTime(FilterModel filterModel, OnTimeChooseListener onTimeChooseListener) {
    this.filterModel = filterModel;
    this.onTimeChooseListener = onTimeChooseListener;
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
    holder.billFilterTitle.setText(filterModel.name);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_filter_time;
  }

  class ItemFilterTimeVH extends FlexibleViewHolder {

    @BindView(R2.id.bill_filter_title) TextView billFilterTitle;
    @BindView(R2.id.tv_student_filter_time_start) TextView tvStudentFilterTimeStart;
    @BindView(R2.id.tv_student_filter_time_end) TextView tvStudentFilterTimeEnd;

    public ItemFilterTimeVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }

  public interface OnTimeChooseListener {
    void onTimeStart(String start, String key);

    void onTimeEnd(String end, String key);
  }
}
