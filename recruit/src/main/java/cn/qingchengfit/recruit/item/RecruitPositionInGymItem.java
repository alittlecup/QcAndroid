package cn.qingchengfit.recruit.item;

import android.support.v4.content.ContextCompat;
import android.view.View;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.model.Job;
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
 * Created by Paper on 2017/5/31.
 */

public class RecruitPositionInGymItem extends RecruitPositionItem {

  public RecruitPositionInGymItem(Job job) {
    super(job);
  }


  @Override public int getLayoutRes() {
    return R.layout.item_recruit_position_in_gym;
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, RecruitPositionVH holder, int position, List payloads) {
    super.bindViewHolder(adapter, holder, position, payloads);
    if (job.published) {
      holder.tvPositionName.setTextColor(
          ContextCompat.getColor(holder.tvPositionName.getContext(), R.color.text_dark));
      holder.tvSalary.setTextColor(
          ContextCompat.getColor(holder.tvPositionName.getContext(), R.color.red));
      if (holder.tv_has_todo != null) {
        if (job.has_new_resume) {
          holder.tv_has_todo.setVisibility(View.VISIBLE);
          holder.tv_has_todo.setCompoundDrawablesWithIntrinsicBounds(
              ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.dot_red), null,
              null, null);
        } else {
          holder.tv_has_todo.setVisibility(View.GONE);
        }
      }
    } else {
      holder.tvPositionName.setTextColor(
          ContextCompat.getColor(holder.tvPositionName.getContext(), R.color.qc_text_grey));
      holder.tvSalary.setTextColor(
          ContextCompat.getColor(holder.tvPositionName.getContext(), R.color.qc_text_grey));
      if (holder.tv_has_todo != null) holder.tv_has_todo.setVisibility(View.GONE);

    }
  }
}
