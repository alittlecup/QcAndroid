package cn.qingchengfit.staffkit.allocate.coach.item;

import android.graphics.drawable.Animatable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.allocate.coach.model.Coach;
import com.bumptech.glide.Glide;
import com.tencent.qcloud.timchat.widget.CircleImgWrapper;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ChooseCoachItem extends AbstractFlexibleItem<ChooseCoachItem.ChooseSalerVH> {

    private Coach data;

    public ChooseCoachItem(Coach data) {
        this.data = data;
    }

    public Coach getData() {
        return data;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_choose_saler;
    }

    @Override public ChooseSalerVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new ChooseSalerVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ChooseSalerVH holder, int position, List payloads) {
        Glide.with(holder.itemView.getContext())
            .load(data.coach.avatar)
            .asBitmap()
            .into(new CircleImgWrapper(holder.salerHeaderImg, holder.itemView.getContext()));
        holder.salerNameTv.setText(data.coach.username);
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
        @BindView(R.id.saler_header_img) ImageView salerHeaderImg;
        @BindView(R.id.choose_img) ImageView chooseImg;
        @BindView(R.id.saler_name_tv) TextView salerNameTv;

        public ChooseSalerVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
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