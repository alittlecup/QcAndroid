package cn.qingchengfit.student.item;

import android.view.View;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saascommon.item.StudentItem;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.SelectableAdapter;
import eu.davidea.flexibleadapter.items.IHeader;
import java.util.List;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/8/28.
 */

public class ChosenStudentItem extends StudentItem {

  public ChosenStudentItem(QcStudentBean qcStudentBean, IHeader head) {
    super(qcStudentBean, head);
  }

  public ChosenStudentItem(QcStudentBean qcStudentBean) {
    super(qcStudentBean);
  }

  @Override public StudentVH createViewHolder(View view, FlexibleAdapter adapter) {
    StudentVH vh = super.createViewHolder(view, adapter);
    if(adapter.getMode()==SelectableAdapter.Mode.SINGLE){
      vh.cb.setVisibility(View.GONE);
    }else{
      vh.cb.setVisibility(View.VISIBLE);
    }
    vh.iconRight.setVisibility(View.GONE);
    vh.itemTvStudentStatus.setVisibility(View.GONE);
    return vh;
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, StudentVH holder, int position,
      List payloads) {
    super.bindViewHolder(adapter, holder, position, payloads);
    holder.cb.setChecked(isSelected);
  }

  public boolean isSelected() {
    return isSelected;
  }

  private boolean isSelected;

  public void setSelected(boolean selected) {
    isSelected = selected;
  }
}

