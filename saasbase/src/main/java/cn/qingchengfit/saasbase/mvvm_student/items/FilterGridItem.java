package cn.qingchengfit.saasbase.mvvm_student.items;

import android.graphics.drawable.Animatable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.qingchengfit.model.base.User;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.utils.CircleImgWrapper;
import cn.qingchengfit.utils.PhotoUtils;
import com.bumptech.glide.Glide;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by huangbaole on 2017/12/11.
 */

public class FilterGridItem extends AbstractFlexibleItem<FilterGridItem.ChooseStaffVH> {

    protected User staff;

    public FilterGridItem(User staff) {
        this.staff = staff;
    }

    public User getStaff() {
        return staff;
    }

    @Override
    public int getLayoutRes() {
        return cn.qingchengfit.saasbase.R.layout.item_choose_saler;
    }

    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public boolean isSelectable() {
        return true;
    }

    @Override
    public FilterGridItem.ChooseStaffVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new FilterGridItem.ChooseStaffVH(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, FilterGridItem.ChooseStaffVH holder, int position, List payloads) {

        if ("-1".equals(staff.id)) {// 全部
            int avatarRes;
            if (adapter.isSelected(position)) {
                avatarRes = cn.qingchengfit.saasbase.R.drawable.ic_all_selected;
            } else {
                avatarRes = cn.qingchengfit.saasbase.R.drawable.ic_all_normal;
            }
            Glide.with(holder.itemView.getContext())
                    .load(avatarRes)
                    .asBitmap()
                    .into(new CircleImgWrapper(holder.salerHeaderImg, holder.itemView.getContext()));
        } else if ("0".equals(staff.id)) { // 未分配销售
            int avatarRes;
            if (adapter.isSelected(position)) {
                avatarRes = cn.qingchengfit.saasbase.R.drawable.ic_nosales_selected;
            } else {
                avatarRes = cn.qingchengfit.saasbase.R.drawable.ic_nosales_normal;
            }
            Glide.with(holder.itemView.getContext())
                    .load(avatarRes)
                    .asBitmap()
                    .into(new CircleImgWrapper(holder.salerHeaderImg, holder.itemView.getContext()));
        } else { // normal
            PhotoUtils.smallCircle(holder.salerHeaderImg, staff.avatar);
        }

        holder.salerNameTv.setText(staff.username);
        if (adapter.isSelected(position)) {
            holder.salerHeaderImg.setBackgroundResource(cn.qingchengfit.saasbase.R.drawable.ai_annulus_green);
            holder.chooseImg.setVisibility(View.VISIBLE);
        } else {
            holder.salerHeaderImg.setBackgroundResource(cn.qingchengfit.saasbase.R.drawable.circle_outside);
            holder.chooseImg.setVisibility(View.GONE);
        }
    }

    public class ChooseStaffVH extends FlexibleViewHolder {

        ImageView salerHeaderImg;

        ImageView chooseImg;

        TextView salerNameTv;

        public ChooseStaffVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
          salerHeaderImg = (ImageView) view.findViewById(R.id.saler_header_img);
          chooseImg = (ImageView) view.findViewById(R.id.choose_img);
          salerNameTv = (TextView) view.findViewById(R.id.saler_name_tv);
        }

        @Override
        public void toggleActivation() {
            super.toggleActivation();
            if (mAdapter.isSelected(getAdapterPosition())) {
                chooseImg.setVisibility(View.VISIBLE);
                salerHeaderImg.setBackgroundResource(cn.qingchengfit.saasbase.R.drawable.ai_annulus_green);
                chooseImg.setImageResource(cn.qingchengfit.saasbase.R.drawable.ai_saler_choose);

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
                salerHeaderImg.setBackgroundResource(cn.qingchengfit.saasbase.R.drawable.ai_annulus_green_exit);
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
