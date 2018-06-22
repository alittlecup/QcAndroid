package cn.qingchengfit.saasbase.mvvm_student.items;

import android.view.View;

import java.util.List;

import cn.qingchengfit.model.base.QcStudentBean;
import eu.davidea.flexibleadapter.FlexibleAdapter;

/**
 * Created by huangbaole on 2017/12/1.
 */

public class ChooseStaffItem extends StaffDetailItem {
  public ChooseStaffItem(QcStudentBean qcStudentBean, Integer type) {
    super(qcStudentBean, type);
  }

  @Override public StudentVH createViewHolder(View view, FlexibleAdapter adapter) {
    StudentVH vh = super.createViewHolder(view, adapter);
    return vh;
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, StudentVH holder, int position,
      List payloads) {
    super.bindViewHolder(adapter, holder, position, payloads);
    holder.cb.setVisibility(visibility ? View.VISIBLE : View.GONE);
    holder.cb.setChecked(adapter.isSelected(position));
  }

  private boolean visibility = true;

  public void setCheckBoxVisiblity(boolean visibility) {
    this.visibility = visibility;
  }
}
