package com.qingchengfit.fitcoach.items;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.model.base.CoachService;
import com.bumptech.glide.Glide;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.PhotoUtils;
import com.qingchengfit.fitcoach.component.CircleImgWrapper;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class GymItem extends AbstractFlexibleItem<GymItem.GymVH> {

    public CoachService coachService;
    public boolean mForbid = false;

    public GymItem(CoachService coachService) {
        this.coachService = coachService;
    }

    public GymItem(CoachService coachService, boolean forbid) {
        this.coachService = coachService;
        mForbid = forbid;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_gym;
    }

    @Override public GymVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new GymVH(view, adapter);
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
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class GymVH extends FlexibleViewHolder {

	ImageView itemGymHeader;
	TextView itemGymName;
	TextView itemGymCurrent;
	TextView itemIsPersonal;
	ImageView qcIdentify;
	TextView itemGymBrand;
	TextView itemGymPhonenum;
	ImageView itemRight;
	View forbid;
	View line;


        public GymVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          itemGymHeader = (ImageView) view.findViewById(R.id.item_gym_header);
          itemGymName = (TextView) view.findViewById(R.id.item_gym_name);
          itemGymCurrent = (TextView) view.findViewById(R.id.item_gym_current);
          itemIsPersonal = (TextView) view.findViewById(R.id.item_is_personal);
          qcIdentify = (ImageView) view.findViewById(R.id.qc_identify);
          itemGymBrand = (TextView) view.findViewById(R.id.item_gym_brand);
          itemGymPhonenum = (TextView) view.findViewById(R.id.item_gym_phonenum);
          itemRight = (ImageView) view.findViewById(R.id.item_right);
          forbid = (View) view.findViewById(R.id.forbid);
          line = (View) view.findViewById(R.id.item_line);
        }
    }
}