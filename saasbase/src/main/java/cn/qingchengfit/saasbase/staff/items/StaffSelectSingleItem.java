package cn.qingchengfit.saasbase.staff.items;

import android.view.View;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.common.ICommonUser;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.staff.model.StaffShip;
import cn.qingchengfit.saascommon.item.CommonUserItem;
import eu.davidea.flexibleadapter.FlexibleAdapter;
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
 * Created by Paper on 2017/12/1.
 */

public class StaffSelectSingleItem extends CommonUserItem {
  public StaffSelectSingleItem(ICommonUser staff) {
    super(staff);
  }

  public StaffShip getStaff() {
    if (getUser() instanceof StaffShip) {
      return (StaffShip) getUser();
    } else {
      return null;
    }
  }

  public Staff getStaffForSeleted() {
    if (getUser() instanceof StaffShip) {
      Staff teacher = ((StaffShip) getUser()).teacher;
      if (teacher != null) {
        teacher.username = ((StaffShip) getUser()).username;
        return teacher;
      } else {
        return null;
      }
    } else {
      Staff ret =new Staff(getUser().getTitle(),getUser().getSubTitle(),getUser().getAvatar(),getUser().getGender());
      ret.id = getUser().getId();
      return ret;
    }
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, CommonUserVH holder, int position,
    List payloads) {
    super.bindViewHolder(adapter, holder, position, payloads);
    if (adapter.isSelected(position)) {
      holder.iconRight.setVisibility(View.VISIBLE);
      holder.iconRight.setImageResource(R.drawable.ic_green_right);
    } else {
      holder.iconRight.setVisibility(View.GONE);
    }
  }
}
