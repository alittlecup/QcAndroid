package cn.qingchengfit.staffkit.views.gym.upgrate.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.GymFuntion;
import cn.qingchengfit.staffkit.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class LinearFunItem extends AbstractFlexibleItem<LinearFunItem.LinearFunVH> {

    GymFuntion mGymFuntion;

    public LinearFunItem(GymFuntion gymFuntion) {
        mGymFuntion = gymFuntion;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_linear_function;
    }

    @Override public LinearFunVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new LinearFunVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, LinearFunVH holder, int position, List payloads) {
        holder.imgFun.setImageResource(mGymFuntion.getImg());
        holder.tvTitle.setText(mGymFuntion.getModuleName());
        holder.tvContent.setText(mGymFuntion.getText());
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class LinearFunVH extends FlexibleViewHolder {

        @BindView(R.id.img_fun) ImageView imgFun;
        @BindView(R.id.tv_title) TextView tvTitle;
        @BindView(R.id.tv_content) TextView tvContent;

        public LinearFunVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}