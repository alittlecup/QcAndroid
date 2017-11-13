package com.qingchengfit.fitcoach.items;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
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

        @BindView(R.id.item_gym_header) ImageView itemGymHeader;
        @BindView(R.id.item_gym_name) TextView itemGymName;
        @BindView(R.id.item_is_personal) TextView itemIsPersonal;
        @BindView(R.id.qc_identify) ImageView qcIdentify;
        @BindView(R.id.item_gym_brand) TextView itemGymBrand;
        @BindView(R.id.item_gym_phonenum) TextView itemGymPhonenum;
        @BindView(R.id.item_right) ImageView itemRight;
        @BindView(R.id.forbid) View forbid;

        public GymVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}