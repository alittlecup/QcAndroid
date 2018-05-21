package cn.qingchengfit.saasbase.staff.items;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.R;

import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFilterable;
import eu.davidea.flexibleadapter.utils.FlexibleUtils;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class StaffItem extends AbstractFlexibleItem<StaffItem.StaffVH> implements IFilterable {

  Staff staff;

  public StaffItem(Staff staff) {
    this.staff = staff;
  }

  public Staff getStaff() {
    return staff;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_saas_staff;
  }

  @Override public StaffVH createViewHolder(final View view, FlexibleAdapter adapter) {
    StaffVH holder = new StaffVH(view, adapter);
    return holder;
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, StaffVH holder, int position, List payloads) {
    PhotoUtils.smallCircle(holder.itemStudentHeader, staff.getAvatar());
    if (adapter.hasSearchText()) {
      FlexibleUtils.highlightWords(holder.itemStudentName, staff.getUsername(),
          adapter.getSearchText());
      FlexibleUtils.highlightWords(holder.itemStudentPhonenum, staff.getPhone(),
          adapter.getSearchText());
    } else {
      holder.itemStudentName.setText(staff.getUsername());
      holder.itemStudentPhonenum.setText(staff.getPhone());
    }
    holder.itemStudentGender.setImageResource(
        staff.gender == 0 ? R.drawable.ic_gender_signal_male : R.drawable.ic_gender_signal_female);
  }

  @Override public boolean equals(Object o) {

    if (o instanceof StaffItem) {
      Staff tmp = ((StaffItem) o).getStaff();
      if (tmp == null || tmp.id == null) return false;
      return tmp.id.equalsIgnoreCase(staff.getId());
    } else {
      return false;
    }
  }

  @Override public boolean filter(String constraint) {
    if (staff == null) {
      return false;
    } else {
      return (staff.phone != null && staff.phone.contains(constraint)) || (staff.username != null
          && staff.username.contains(constraint));
    }
  }

  public class StaffVH extends FlexibleViewHolder {
	CheckBox cb;
	ImageView itemStudentHeader;
	RelativeLayout itemStudentHeaderLoop;
	TextView itemStudentName;
	ImageView itemStudentGender;
	TextView itemTvStudentStatus;
	TextView itemStudentPhonenum;
	TextView itemStudentGymname;
	ImageView iconRight;

    public StaffVH(View view, final FlexibleAdapter adapter) {
      super(view, adapter);
      cb = (CheckBox) view.findViewById(R.id.cb);
      itemStudentHeader = (ImageView) view.findViewById(R.id.item_student_header);
      itemStudentHeaderLoop = (RelativeLayout) view.findViewById(R.id.item_student_header_loop);
      itemStudentName = (TextView) view.findViewById(R.id.item_student_name);
      itemStudentGender = (ImageView) view.findViewById(R.id.item_student_gender);
      itemTvStudentStatus = (TextView) view.findViewById(R.id.item_tv_student_status);
      itemStudentPhonenum = (TextView) view.findViewById(R.id.item_student_phonenum);
      itemStudentGymname = (TextView) view.findViewById(R.id.item_student_gymname);
      iconRight = (ImageView) view.findViewById(R.id.icon_right);
    }
  }
}