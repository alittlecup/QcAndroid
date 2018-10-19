package com.qingchengfit.fitcoach.items;

import android.view.View;
import cn.qingchengfit.model.base.CoachService;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.List;

public class ChosenGymItem extends GymItem {
    private String gymID;
    private boolean isLast;

    public ChosenGymItem(CoachService coachService, String gymID, boolean isLast) {
        super(coachService);
        this.gymID = gymID;
        this.isLast = isLast;
    }

    public ChosenGymItem(CoachService coachService) {
        super(coachService);
    }

    public ChosenGymItem(CoachService coachService, boolean forbid) {
        super(coachService, forbid);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, GymVH holder, int position, List payloads) {
        Glide.with(holder.itemView.getContext())
            .load(coachService.photo)
            .asBitmap()
            .into(new CircleImgWrapper(holder.itemGymHeader, holder.itemView.getContext()));
        holder.itemGymName.setText(coachService.name);
//        holder.itemGymBrand.setText(coachService.brand_name);
        String gymId = coachService.gym_id;
        if(gymID != null && gymId.equals(gymID)) {
            holder.itemGymCurrent.setVisibility(View.VISIBLE);
        }
        if(isLast == false) {
            holder.line.setVisibility(View.VISIBLE);
        }
        holder.itemGymBrand.setText("我的职位： 教练");
        holder.itemGymPhonenum.setVisibility(View.GONE);
        holder.forbid.setVisibility(View.GONE);
        if (adapter.isSelected(position)) {
            holder.itemRight.setVisibility(View.VISIBLE);
            holder.itemRight.setImageResource(R.drawable.ic_green_right);
        } else {
            holder.itemRight.setVisibility(View.GONE);
        }
    }
}