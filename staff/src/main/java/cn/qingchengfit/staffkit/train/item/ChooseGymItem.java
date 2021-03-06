package cn.qingchengfit.staffkit.train.item;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
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
        return R.layout.item_base_choose_gym;
    }

    @Override public ChooseGymVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new ChooseGymVH(view, adapter);
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
	ImageView imgAvatar;
	TextView tvGymName;
	TextView tvCompleteInfo;
	TextView tvGymBrand;
	ImageView icChooseGymArrow;

        public ChooseGymVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          imgAvatar = (ImageView) view.findViewById(R.id.img_avatar);
          tvGymName = (TextView) view.findViewById(R.id.tv_gym_name);
          tvCompleteInfo = (TextView) view.findViewById(R.id.tv_complete_info);
          tvGymBrand = (TextView) view.findViewById(R.id.tv_gym_brand);
          icChooseGymArrow = (ImageView) view.findViewById(R.id.ic_choose_gym_arrow);
        }
    }
}