package cn.qingchengfit.pos.bill.filter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.pos.R;
import cn.qingchengfit.utils.DateUtils;
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

  private String timeTitle;
  private OnTimeChooseListener onTimeChooseListener;

  public ItemFilterTime(String timeTitle, OnTimeChooseListener onTimeChooseListener) {
    this.timeTitle = timeTitle;
    this.onTimeChooseListener = onTimeChooseListener;
  }

  @Override
  public ItemFilterTimeVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    final ItemFilterTimeVH holder =
        new ItemFilterTimeVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    final TimeDialogWindow startTimeDialogWindow =
        new TimeDialogWindow(parent.getContext(), TimePopupWindow.Type.YEAR_MONTH_DAY);
    startTimeDialogWindow.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
      @Override public void onTimeSelect(Date date) {
        holder.tvStudentFilterTimeStart.setText(DateUtils.Date2YYYYMMDD(date));
        if (onTimeChooseListener != null) {
          onTimeChooseListener.onTimeStart(DateUtils.Date2YYYYMMDD(date));
        }
      }
    });
    final TimeDialogWindow endTimeDialogWindow =
        new TimeDialogWindow(parent.getContext(), TimePopupWindow.Type.YEAR_MONTH_DAY);
    endTimeDialogWindow.setOnTimeSelectListener(new TimeDialogWindow.OnTimeSelectListener() {
      @Override public void onTimeSelect(Date date) {
        holder.tvStudentFilterTimeEnd.setText(DateUtils.Date2YYYYMMDD(date));
        if (onTimeChooseListener != null) {
          onTimeChooseListener.onTimeEnd(DateUtils.Date2YYYYMMDD(date));
        }
      }
    });
    holder.tvStudentFilterTimeStart.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (startTimeDialogWindow.isShowing()) {
          startTimeDialogWindow.hide();
        } else {
          startTimeDialogWindow.show();
        }
      }
    });
    holder.tvStudentFilterTimeEnd.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View v) {
        if (endTimeDialogWindow.isShowing()) {
          endTimeDialogWindow.hide();
        } else {
          endTimeDialogWindow.show();
        }
      }
    });
    return holder;
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, ItemFilterTimeVH holder, int position,
      List payloads) {
    holder.billFilterTitle.setText(timeTitle);
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_filter_time;
  }

  class ItemFilterTimeVH extends FlexibleViewHolder {

    @BindView(R.id.bill_filter_title) TextView billFilterTitle;
    @BindView(R.id.tv_student_filter_time_start) TextView tvStudentFilterTimeStart;
    @BindView(R.id.tv_student_filter_time_end) TextView tvStudentFilterTimeEnd;

    public ItemFilterTimeVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }

  public interface OnTimeChooseListener {
    void onTimeStart(String start);

    void onTimeEnd(String end);
  }
}
