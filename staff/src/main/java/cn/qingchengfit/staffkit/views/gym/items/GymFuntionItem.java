package cn.qingchengfit.staffkit.views.gym.items;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.GymFuntion;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.adapter.CommonFlexAdapter;
import cn.qingchengfit.staffkit.views.adapter.GymMoreAdapter;
import cn.qingchengfit.staffkit.views.gym.GymFunctionFactory;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.ISectionable;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class GymFuntionItem extends AbstractFlexibleItem<GymFuntionItem.GymFuntionVH>
    implements ISectionable<GymFuntionItem.GymFuntionVH, FunHeaderItem> {

    public static final int STATUS_REMOVE = 1;
    public static final int STATUS_ADD = 2;

    private GymFuntion mGymFuntion;

    private Context mContext;

    private FunHeaderItem mFunHeaderItem;

    public GymFuntionItem(GymFuntion gymFuntion) {
        mGymFuntion = gymFuntion;
    }

    public GymFuntionItem(GymFuntion gymFuntion, FunHeaderItem i) {
        mGymFuntion = gymFuntion;
        this.mFunHeaderItem = i;
    }

    public GymFuntion getGymFuntion() {
        if (mGymFuntion == null) {
            mGymFuntion = new GymFuntion.Builder().img(0).text(R.string.none)

                .moduleName("").build();
        }
        return mGymFuntion;
    }

    public void setGymFuntion(GymFuntion gymFuntion) {
        mGymFuntion = gymFuntion;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_gym_function;
    }

    @Override public GymFuntionVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new GymFuntionVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, GymFuntionVH holder, int position, List payloads) {
        if (mGymFuntion.getImg() != 0) {
            holder.funImg.setVisibility(View.VISIBLE);
            holder.funImg.setImageResource(mGymFuntion.getImg());
        } else {
            holder.funImg.setVisibility(View.INVISIBLE);
        }
        holder.funTxt.setText(mGymFuntion.getText());
        if (adapter instanceof GymMoreAdapter) {
            if (((GymMoreAdapter) adapter).getStatus() == 0 || mGymFuntion.getImg() == 0) {
                if (GymFunctionFactory.getModuleStatus(mGymFuntion.getModuleName()) > 0 && !(boolean) ((CommonFlexAdapter) adapter).getTag(
                    "isPro")) {
                    holder.statusIcon.setVisibility(View.VISIBLE);
                    holder.statusIcon.setImageResource(R.drawable.ic_pro_green);
                } else {
                    holder.statusIcon.setVisibility(View.GONE);
                }
                GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) holder.rootView.getLayoutParams();
                lp.setMargins(0, 0, 0, 0);
                holder.rootView.setLayoutParams(lp);
            } else if (((GymMoreAdapter) adapter).getStatus() == STATUS_REMOVE) {
                holder.statusIcon.setVisibility(View.VISIBLE);
                holder.statusIcon.setImageResource(R.drawable.ic_moudule_manage_remove);

                GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) holder.rootView.getLayoutParams();
                lp.setMargins(0, 0, 1, 1);
                holder.rootView.setLayoutParams(lp);
            } else if (((GymMoreAdapter) adapter).getStatus() == STATUS_ADD) {
                holder.statusIcon.setVisibility(View.VISIBLE);
                if (((GymMoreAdapter) adapter).mOtherDatas.contains(GymFuntionItem.this)) {
                    holder.statusIcon.setImageResource(R.drawable.ic_moudule_manage_added);
                } else {
                    holder.statusIcon.setImageResource(R.drawable.ic_moudule_manage_add);
                }
                GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) holder.rootView.getLayoutParams();
                lp.setMargins(0, 0, 1, 1);
                holder.rootView.setLayoutParams(lp);
            }
        }
    }

    @Override public boolean equals(Object o) {

        if (o instanceof GymFuntionItem) {
            return ((GymFuntionItem) o).mGymFuntion.getModuleName().toLowerCase().equalsIgnoreCase(this.mGymFuntion.getModuleName());
        } else {
            return false;
        }
    }

    @Override public FunHeaderItem getHeader() {
        return mFunHeaderItem;
    }

    @Override public void setHeader(FunHeaderItem header) {
        this.mFunHeaderItem = header;
    }

    public class GymFuntionVH extends FlexibleViewHolder {
        @BindView(R.id.root_view) ViewGroup rootView;
        @BindView(R.id.fun_img) ImageView funImg;
        @BindView(R.id.fun_txt) TextView funTxt;
        @BindView(R.id.status_icon) ImageView statusIcon;

        public GymFuntionVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}