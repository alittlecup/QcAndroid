package cn.qingchengfit.saasbase.student.items;

import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.student.utils.StudentBusinessUtils;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFilterable;
import eu.davidea.flexibleadapter.items.IHeader;
import eu.davidea.flexibleadapter.items.ISectionable;
import eu.davidea.flexibleadapter.utils.FlexibleUtils;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class StudentItem extends AbstractFlexibleItem<StudentItem.StudentVH>
    implements IFilterable, ISectionable<StudentItem.StudentVH, IHeader> {

  QcStudentBean qcStudentBean;
  IHeader head;

  public StudentItem(QcStudentBean qcStudentBean, IHeader head) {
    this.qcStudentBean = qcStudentBean;
    this.head = head;
  }

  public StudentItem(QcStudentBean qcStudentBean) {
    this.qcStudentBean = qcStudentBean;
  }

  public String getId() {
    if (qcStudentBean != null) {
      return qcStudentBean.getId();
    } else {
      return "";
    }
  }

  public QcStudentBean getQcStudentBean() {
    return qcStudentBean;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_saas_staff;
  }

  @Override public StudentVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new StudentVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, StudentVH holder, int position,
      List payloads) {
    if (qcStudentBean.gender() != 1) {
      PhotoUtils.smallCircle(holder.itemStudentHeader, qcStudentBean.avatar(),
          R.drawable.ic_default_staff_man_head, R.drawable.ic_default_staff_man_head);
    }else{
      PhotoUtils.smallCircle(holder.itemStudentHeader, qcStudentBean.avatar(),
          R.drawable.ic_default_staff_women_head, R.drawable.ic_default_staff_women_head);
    }
    holder.itemStudentGender.setImageResource(
        ((qcStudentBean.gender() == null || qcStudentBean.gender() == 1)
            ? R.drawable.ic_gender_signal_female : R.drawable.ic_gender_signal_male));
    if (adapter.hasSearchText()) {
      FlexibleUtils.highlightWords(holder.itemStudentName, qcStudentBean.username(),
          adapter.getSearchText());
      FlexibleUtils.highlightWords(holder.itemStudentPhonenum, qcStudentBean.phone(),
          adapter.getSearchText());
    } else {
      holder.itemStudentName.setText(qcStudentBean.username());
      holder.itemStudentPhonenum.setText(qcStudentBean.phone());
    }
    holder.itemTvStudentStatus.setText(holder.itemTvStudentStatus.getContext()
        .getResources()
        .getStringArray(R.array.student_status)[qcStudentBean.getStatus() % 3]);
    holder.itemTvStudentStatus.setCompoundDrawablesWithIntrinsicBounds(
        StudentBusinessUtils.getStudentStatusDrawable(holder.itemView.getContext(),
            qcStudentBean.getStatus() % 3), null, null, null);
  }

  @Override public boolean equals(Object o) {
    return o instanceof StudentItem && ((StudentItem) o).getId().equalsIgnoreCase(getId());
  }

  @Override public boolean filter(String constraint) {
    return qcStudentBean != null && (qcStudentBean.getUsername()
        .toLowerCase()
        .contains(constraint.toLowerCase()) || qcStudentBean.getPhone()
        .toLowerCase()
        .contains(constraint));
  }

  @Override public IHeader getHeader() {
    return head;
  }

  @Override public void setHeader(IHeader header) {
    this.head = header;
  }

  public class StudentVH extends FlexibleViewHolder {
    @BindView(R2.id.item_student_header) ImageView itemStudentHeader;
    @BindView(R2.id.item_student_name) TextView itemStudentName;
    @BindView(R2.id.item_student_gender) ImageView itemStudentGender;
    @BindView(R2.id.item_tv_student_status) TextView itemTvStudentStatus;
    @BindView(R2.id.item_student_phonenum) TextView itemStudentPhonenum;
    @BindView(R2.id.item_student_gymname) public TextView itemStudentGymname;
    @BindView(R2.id.icon_right) ImageView iconRight;
    @BindView(R2.id.cb) public CheckBox cb;

    public StudentVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
      cb.setClickable(false);
    }
  }
}