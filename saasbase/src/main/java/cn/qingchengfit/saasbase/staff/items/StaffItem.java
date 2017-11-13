package cn.qingchengfit.saasbase.staff.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFilterable;
import eu.davidea.flexibleadapter.utils.Utils;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class StaffItem extends AbstractFlexibleItem<StaffItem.StaffVH> implements IFilterable{

  Staff staff;

  public StaffItem(Staff staff) {
    this.staff = staff;
  }

  public Staff getStaff() {
    return staff;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_student;
  }

  @Override public StaffVH createViewHolder(final FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    StaffVH holder = new StaffVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    return holder;
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, StaffVH holder, int position, List payloads) {
    PhotoUtils.smallCircle(holder.itemStudentHeader,staff.getAvatar());
    Utils.highlightText(holder.itemStudentName,staff.getUsername(),adapter.getSearchText());
    Utils.highlightText(holder.itemStudentPhonenum,staff.getPhone(),adapter.getSearchText());
    holder.itemStudentGender.setImageResource(staff.gender == 0 ?R.drawable.ic_gender_signal_male:R.drawable.ic_gender_signal_female);
  }



  @Override public boolean equals(Object o) {

    if (o instanceof StaffItem){
      Staff tmp = ((StaffItem) o).getStaff();
      if (tmp == null || tmp.id == null)
        return false;
      return tmp.id.equalsIgnoreCase(staff.getId());
    }else return false;

  }

  @Override public boolean filter(String constraint) {
    if (staff == null || staff.phone == null || staff.username == null)
      return false;
    else {
      return staff.username.contains(constraint) || staff.phone.contains(constraint);
    }
  }

  public class StaffVH extends FlexibleViewHolder {
    @BindView(R2.id.cb) CheckBox cb;
    @BindView(R2.id.item_student_header) ImageView itemStudentHeader;
    @BindView(R2.id.item_student_header_loop) RelativeLayout itemStudentHeaderLoop;
    @BindView(R2.id.item_student_name) TextView itemStudentName;
    @BindView(R2.id.item_student_gender) ImageView itemStudentGender;
    @BindView(R2.id.item_tv_student_status) TextView itemTvStudentStatus;
    @BindView(R2.id.item_student_phonenum) TextView itemStudentPhonenum;
    @BindView(R2.id.item_student_gymname) TextView itemStudentGymname;
    @BindView(R2.id.icon_right) ImageView iconRight;
    public StaffVH(View view, final FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}