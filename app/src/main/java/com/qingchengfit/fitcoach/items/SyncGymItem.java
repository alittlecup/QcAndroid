package com.qingchengfit.fitcoach.items;

import android.view.View;
import cn.qingchengfit.model.base.CoachService;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
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
 * Created by Paper on 2016/12/13.
 */

public class SyncGymItem extends GymItem {

    public SyncGymItem(CoachService coachService) {
        super(coachService);
    }

    public SyncGymItem(CoachService coachService, boolean forbid) {
        super(coachService, forbid);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, GymVH holder, int position, List payloads) {
        Glide.with(holder.itemView.getContext())
            .load(PhotoUtils.getSmall(coachService.photo))
            .asBitmap()
            .into(new CircleImgWrapper(holder.itemGymHeader, holder.itemView.getContext()));
        holder.itemGymName.setText(coachService.name);
        holder.itemGymBrand.setText(coachService.brand_name);
        holder.itemGymPhonenum.setVisibility(View.GONE);
        holder.forbid.setVisibility(mForbid ? View.VISIBLE : View.GONE);
        holder.itemRight.setVisibility(View.GONE);
    }
}
