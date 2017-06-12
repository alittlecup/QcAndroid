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
    private boolean hasUndo = false;

    public RecruitPositionInGymItem(Job job) {
        super(job);
    }

    public RecruitPositionInGymItem(Job job, boolean hasUndo) {
        super(job);
        this.hasUndo = hasUndo;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_recruit_position_in_gym;
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, RecruitPositionVH holder, int position, List payloads) {
        super.bindViewHolder(adapter, holder, position, payloads);
        if (holder.tv_has_todo != null) {
            if (hasUndo) {
                holder.tv_has_todo.setVisibility(View.VISIBLE);
                holder.tv_has_todo.setCompoundDrawablesWithIntrinsicBounds(
                    ContextCompat.getDrawable(holder.itemView.getContext(), R.drawable.dot_red), null, null, null);
            } else {
                holder.tv_has_todo.setVisibility(View.GONE);
            }
        }
    }
}
