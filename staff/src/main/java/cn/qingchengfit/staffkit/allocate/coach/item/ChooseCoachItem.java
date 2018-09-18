package cn.qingchengfit.staffkit.allocate.coach.item;

import android.graphics.drawable.Animatable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.allocate.coach.model.Coach;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ChooseCoachItem extends AbstractFlexibleItem<ChooseCoachItem.ChooseSalerVH> {

    private Staff data;

    public ChooseCoachItem(Staff data) {
        this.data = data;
    }

    public Staff getData() {
        return data;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_choose_saler;
    }

    @Override public ChooseSalerVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new ChooseSalerVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ChooseSalerVH holder, int position, List payloads) {
        int da = data.getGender() == 0?R.drawable.default_manage_male:R.drawable.default_manager_female;
        PhotoUtils.smallCircle(holder.salerHeaderImg,data.avatar,da,da);
        holder.salerNameTv.setText(data.username);
        //adapter.animateView(holder.itemView, position, adapter.isSelected(position));

        if (adapter.isSelected(position)) {
            holder.salerHeaderImg.setBackgroundResource(R.drawable.ai_annulus_green);
            holder.chooseImg.setVisibility(View.VISIBLE);
        } else {
            holder.salerHeaderImg.setBackgroundResource(R.drawable.circle_outside);
            holder.chooseImg.setVisibility(View.GONE);
        }
    }

    @Override public boolean isSelectable() {
        return true;
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class ChooseSalerVH extends FlexibleViewHolder {
	ImageView salerHeaderImg;
	ImageView chooseImg;
	TextView salerNameTv;

        public ChooseSalerVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          salerHeaderImg = (ImageView) view.findViewById(R.id.saler_header_img);
          chooseImg = (ImageView) view.findViewById(R.id.choose_img);
          salerNameTv = (TextView) view.findViewById(R.id.saler_name_tv);
        }

        @Override public void toggleActivation() {
            super.toggleActivation();
            if (mAdapter.isSelected(getAdapterPosition())) {
                chooseImg.setVisibility(View.VISIBLE);
                salerHeaderImg.setBackgroundResource(R.drawable.ai_annulus_green);
                chooseImg.setImageResource(R.drawable.ai_saler_choose);

                if (chooseImg.getDrawable() instanceof Animatable) {
                    Animatable ad = (Animatable) chooseImg.getDrawable();
                    if (ad.isRunning()) {
                        ad.stop();
                    }
                    ad.start();
                }
                if (salerHeaderImg.getBackground() instanceof Animatable) {
                    Animatable ad1 = (Animatable) salerHeaderImg.getBackground();
                    if (ad1.isRunning()) {
                        ad1.stop();
                    }
                    ad1.start();
                }
            } else {
                chooseImg.setVisibility(View.GONE);
                salerHeaderImg.setBackgroundResource(R.drawable.ai_annulus_green_exit);
                if (salerHeaderImg.getBackground() instanceof Animatable) {
                    Animatable ad1 = (Animatable) salerHeaderImg.getBackground();
                    if (ad1.isRunning()) {
                        ad1.stop();
                    }
                    ad1.start();
                }
            }
        }
    }
}