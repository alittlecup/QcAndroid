package cn.qingchengfit.saasbase.student.items;

import android.graphics.drawable.Animatable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.base.Staff;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.utils.PhotoUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;

/**
 * Created by huangbaole on 2017/11/2.
 */

public class ChooseStaffItem extends AbstractFlexibleItem<ChooseStaffItem.ChooseStaffVH> {
    protected Staff staff;

    public ChooseStaffItem(Staff staff) {
        this.staff = staff;
    }

    public Staff getStaff() {
        return staff;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.item_choose_saler;
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
    public ChooseStaffVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new ChooseStaffVH(view,adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter adapter, ChooseStaffVH holder, int position, List payloads) {
        PhotoUtils.smallCircle(holder.salerHeaderImg,staff.avatar);
        holder.salerNameTv.setText(staff.username);
        if (adapter.isSelected(position)) {
            holder.salerHeaderImg.setBackgroundResource(R.drawable.ai_annulus_green);
            holder.chooseImg.setVisibility(View.VISIBLE);
        } else {
            holder.salerHeaderImg.setBackgroundResource(R.drawable.circle_outside);
            holder.chooseImg.setVisibility(View.GONE);
        }
    }

    public class ChooseStaffVH extends FlexibleViewHolder {
        @BindView(R2.id.saler_header_img)
        ImageView salerHeaderImg;
        @BindView(R2.id.choose_img)
        ImageView chooseImg;
        @BindView(R2.id.saler_name_tv)
        TextView salerNameTv;

        public ChooseStaffVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }

        @Override
        public void toggleActivation() {
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
