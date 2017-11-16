package cn.qingchengfit.saasbase.staff.items;

import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.utils.CmStringUtils;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.utils.FlexibleUtils;
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
 * Created by Paper on 2017/11/15.
 */

public class SalerItem extends StaffItem {
  public SalerItem(Staff staff) {
    super(staff);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, StaffVH holder, int position, List payloads) {
    PhotoUtils.smallCircle(holder.itemStudentHeader,staff.getAvatar());
    if (adapter.hasSearchText()) {
      FlexibleUtils.highlightWords(holder.itemStudentName, staff.getUsername(), adapter.getSearchText());
      FlexibleUtils.highlightWords(holder.itemStudentPhonenum, staff.getPhone()+"\n"+"今日业绩统计："+CmStringUtils.getMoneyStr((float) staff.amount/100)+"元", adapter.getSearchText());
    }else {
      holder.itemStudentName.setText(staff.getUsername());
      holder.itemStudentPhonenum.setText(staff.getPhone()+"\n"+"今日业绩统计："+CmStringUtils.getMoneyStr((float) staff.amount/100)+"元");
    }
    holder.itemStudentGender.setImageResource(staff.gender == 0 ? R.drawable.ic_gender_signal_male:R.drawable.ic_gender_signal_female);
  }
}
