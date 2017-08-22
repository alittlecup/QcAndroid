package cn.qingchengfit.staffkit.train.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.staffkit.R;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ChooseGymItem extends AbstractFlexibleItem<ChooseGymItem.ChooseGymVH> {

    CoachService coachService;

    public ChooseGymItem(CoachService coachService) {
        this.coachService = coachService;
    }

    public boolean isCompeleted() {
        return coachService.getcityCode() != 0;
    }

    public CoachService getCoachService() {
        return coachService;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_competition_gym;
    }

    @Override public ChooseGymVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new ChooseGymVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ChooseGymVH holder, int position, List payloads) {
        Glide.with(holder.itemView.getContext())
            .load(coachService.getPhoto())
            .asBitmap()
            .placeholder(R.drawable.ic_default_header)
            .into(new CircleImgWrapper(holder.imgAvatar, holder.imgAvatar.getContext()));
        holder.tvGymName.setText(coachService.getName());
        holder.tvGymBrand.setText(coachService.getBrand_name());
        if (isCompeleted()) {
            holder.tvCompleteInfo.setVisibility(View.GONE);
            holder.icChooseGymArrow.setVisibility(View.GONE);
        } else {
            holder.tvCompleteInfo.setVisibility(View.VISIBLE);
            holder.icChooseGymArrow.setVisibility(View.VISIBLE);
        }
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class ChooseGymVH extends FlexibleViewHolder {
        @BindView(R.id.img_avatar) ImageView imgAvatar;
        @BindView(R.id.tv_gym_name) TextView tvGymName;
        @BindView(R.id.tv_complete_info) TextView tvCompleteInfo;
        @BindView(R.id.tv_gym_brand) TextView tvGymBrand;
        @BindView(R.id.ic_choose_gym_arrow) ImageView icChooseGymArrow;

        public ChooseGymVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}