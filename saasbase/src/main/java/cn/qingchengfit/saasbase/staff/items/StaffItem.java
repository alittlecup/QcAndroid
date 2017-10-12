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
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class StaffItem extends AbstractFlexibleItem<StaffItem.StaffVH> {

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

  @Override public StaffVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new StaffVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, StaffVH holder, int position, List payloads) {
    PhotoUtils.smallCircle(holder.itemStudentHeader,staff.getAvatar());
    holder.itemStudentName.setText(staff.getUsername());
    holder.itemStudentPhonenum.setText(staff.getPhone());
    holder.itemStudentGender.setImageResource(staff.gender == 0 ?R.drawable.ic_gender_signal_male:R.drawable.ic_gender_signal_female);
    holder.iconRight.setVisibility(View.GONE);
  }

  @Override public boolean equals(Object o) {
    return false;
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
    public StaffVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}