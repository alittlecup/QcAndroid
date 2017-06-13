package cn.qingchengfit.staffkit.views.student.followup;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.PhotoUtils;
import com.bumptech.glide.Glide;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class ChooseSalerItem extends AbstractFlexibleItem<ChooseSalerItem.ChooseSalerVH> {

    private Staff mSaler;

    public ChooseSalerItem(Staff saler) {
        mSaler = saler;
    }

    public Staff getSaler() {
        return mSaler;
    }

    public void setSaler(Staff saler) {
        mSaler = saler;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_choose_saler;
    }

    @Override public ChooseSalerVH createViewHolder(final FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        ChooseSalerVH chooseSalerVH = new ChooseSalerVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
        return chooseSalerVH;
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, ChooseSalerVH holder, final int position, List payloads) {

        if ("-1".equals(mSaler.id)) {// 全部
            int avatarRes;
            if (adapter.isSelected(position)) {
                avatarRes = R.drawable.ic_all_selected;
            } else {
                avatarRes = R.drawable.ic_all_normal;
            }
            Glide.with(holder.itemView.getContext())
                .load(avatarRes)
                .asBitmap()
                .into(new CircleImgWrapper(holder.salerHeaderImg, holder.itemView.getContext()));
        } else if ("0".equals(mSaler.id)) { // 未分配销售
            int avatarRes;
            if (adapter.isSelected(position)) {
                avatarRes = R.drawable.ic_nosales_selected;
            } else {
                avatarRes = R.drawable.ic_nosales_normal;
            }
            Glide.with(holder.itemView.getContext())
                .load(avatarRes)
                .asBitmap()
                .into(new CircleImgWrapper(holder.salerHeaderImg, holder.itemView.getContext()));
        } else { // normal
            Glide.with(holder.itemView.getContext())
                .load(PhotoUtils.getSmall(mSaler.avatar))
                .asBitmap()
                .into(new CircleImgWrapper(holder.salerHeaderImg, holder.itemView.getContext()));
        }

        holder.salerNameTv.setText(mSaler.username);
        //adapter.animateView(holder.itemView, position);

        if (adapter.isSelected(position)) {
            holder.salerHeaderImg.setBackgroundResource(R.drawable.ai_annulus_green);
            holder.chooseImg.setVisibility(View.VISIBLE);
        } else {
            holder.salerHeaderImg.setBackgroundResource(R.drawable.circle_outside);
            //holder.salerHeaderImg.setBackgroundResource(R.color.transparent);
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
            //if (mAdapter.isSelected(getAdapterPosition())) {
            //    chooseImg.setVisibility(View.VISIBLE);
            //    salerHeaderImg.setBackgroundResource(R.drawable.ai_annulus_green);
            //    chooseImg.setImageResource(R.drawable.ai_saler_choose);
            //
            //    if (chooseImg.getDrawable() instanceof Animatable) {
            //        Animatable ad = (Animatable) chooseImg.getDrawable();
            //        if (ad.isRunning()) {
            //            ad.stop();
            //        }
            //        ad.start();
            //    }
            //    if (salerHeaderImg.getBackground() instanceof Animatable) {
            //        Animatable ad1 = (Animatable) salerHeaderImg.getBackground();
            //        if (ad1.isRunning()) {
            //            ad1.stop();
            //        }
            //        ad1.start();
            //    }
            //} else {
            //    chooseImg.setVisibility(View.GONE);
            //    salerHeaderImg.setBackgroundResource(R.drawable.ai_annulus_green_exit);
            //}
        }
    }
}