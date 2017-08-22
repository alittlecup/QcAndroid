package cn.qingchengfit.recruit;

import cn.qingchengfit.staffkit.views.abstractflexibleitem.DelStudentItem;
import cn.qingchengfit.staffkit.views.bottom.BottomStudentsFragment;
import cn.qingchengfit.utils.ToastUtils;

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
 * Created by Paper on 2017/7/14.
 */

public class BottomStaffsBanRvSuFragment extends BottomStudentsFragment {
  @Override public boolean onItemClick(int position) {
    if (adapter.getItem(position) instanceof DelStudentItem) {
      if (((DelStudentItem) adapter.getItem(position)).getUser().is_superuser) {
        ToastUtils.show("超级管理员无法被移除");
        return true;
      }
    }
    super.onItemClick(position);
    return true;
  }

  @Override public void onClick() {
    for (int i = 0; i < adapter.getItemCount(); i++) {
      if (adapter.getItem(i) instanceof DelStudentItem) {
        if (!((DelStudentItem) adapter.getItem(i)).getUser().is_superuser) {
          adapter.removeItem(i);
        }
      }
    }
    dismiss();
  }

  @Override protected void showTitle() {
    tvStudCount.setText("已选择" + adapter.getItemCount() + "名工作人员");
  }
}
