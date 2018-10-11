package cn.qingchengfit.saasbase.staff.items;

import android.support.v4.content.ContextCompat;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.model.common.ICommonUser;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saascommon.item.CommonUserItem;
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

public class SalerItem extends CommonUserItem {
  public SalerItem(ICommonUser staff) {
    super(staff);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, CommonUserVH holder, int position,
    List payloads) {
    PhotoUtils.smallCircle(holder.itemStudentHeader,user.getAvatar());
    String others = "";
    if (user instanceof Staff) {
      others ="今日业绩统计：" + CmStringUtils.getMoneyStr(
        (float) ((Staff)user).amount / 100) + "元";
    }
    if (adapter.hasSearchText()) {
      FlexibleUtils.highlightWords(holder.tvTitle, user.getTitle(), adapter.getSearchText());
      FlexibleUtils.highlightWords(holder.tvSubTitle, user.getSubTitle()+"\n"+others,adapter.getSearchText());
    }else {
      holder.tvTitle.setText(user.getTitle());
        holder.tvSubTitle.setText(user.getSubTitle() + "\n" + others);
    }
    holder.tvTitle.setCompoundDrawables(null,null, ContextCompat.getDrawable(holder.tvTitle.getContext(),user.getGender() == 0 ? R.drawable.ic_gender_signal_male:R.drawable.ic_gender_signal_female),null);

  }


}
