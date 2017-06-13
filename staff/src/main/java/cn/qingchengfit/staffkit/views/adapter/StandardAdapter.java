package cn.qingchengfit.staffkit.views.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.responese.CardStandard;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.CompatUtils;
import java.util.List;

/**
 * power by
 * <p/>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p/>
 * <p/>
 * Created by Paper on 16/3/23 2016.
 */
public class StandardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    List<CardStandard> datas;
    OnRecycleItemClickListener listener;
    int chooseItem = 0;

    private boolean mIsChosen;
    private boolean isEnable = true;
    private Context mContext;

    public StandardAdapter(Context context, List<CardStandard> datas, boolean choosen, boolean isEnable) {
        this.datas = datas;
        this.mIsChosen = choosen;
        this.isEnable = isEnable;
        this.mContext = context;
    }

    public boolean isChosen() {
        return mIsChosen;
    }

    public void setChosen(boolean chosen) {
        mIsChosen = chosen;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public OnRecycleItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    public int getChooseItem() {
        return chooseItem;
    }

    public void setChooseItem(int chooseItem) {
        this.chooseItem = chooseItem;
    }

    @Override public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        if (viewType == 0) {
            vh = new StardardVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_stardart, parent, false));
        } else {
            vh = new AddVh(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_cardstandard, parent, false));
        }
        vh.itemView.setOnClickListener(this);
        return vh;
    }

    @Override public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setTag(position);
        CardStandard standard = datas.get(position);
        if (holder instanceof StardardVH) {
            String unit = "";
            if (standard.getCardtype() == 1) {
                unit = "元";
            } else if (standard.getCardtype() == 2) {
                unit = "次";
            } else if (standard.getCardtype() == 3) {
                unit = "天";
            } else {
                unit = "";
            }
            ((StardardVH) holder).title.setText(standard.getContent() + unit);
            if (TextUtils.isEmpty(standard.getReal_income())) {
                ((StardardVH) holder).realIncome.setVisibility(View.GONE);
            } else {
                ((StardardVH) holder).realIncome.setText(standard.getReal_income());
                ((StardardVH) holder).realIncome.setVisibility(View.VISIBLE);
            }
            if (TextUtils.isEmpty(standard.getValid_date())) {
                ((StardardVH) holder).validDate.setVisibility(View.GONE);
            } else {
                ((StardardVH) holder).validDate.setVisibility(View.VISIBLE);
                ((StardardVH) holder).validDate.setText(standard.getValid_date());
            }
            if (TextUtils.isEmpty(standard.getSupport_type())) {
                ((StardardVH) holder).supportType.setVisibility(View.GONE);
            } else {
                ((StardardVH) holder).supportType.setVisibility(View.VISIBLE);
                ((StardardVH) holder).supportType.setText(standard.getSupport_type());
            }
            if (mIsChosen) {
                ((StardardVH) holder).chosen.setVisibility(chooseItem == position ? View.VISIBLE : View.INVISIBLE);
            } else {
                ((StardardVH) holder).chosen.setVisibility(View.GONE);
            }

            ((StardardVH) holder).onlyStaff.setVisibility(standard.for_staff ? View.VISIBLE : View.GONE);
            if (!isEnable) {
                int colorgrey = CompatUtils.getColor(mContext, R.color.text_grey);
                ((StardardVH) holder).supportType.setTextColor(colorgrey);
                ((StardardVH) holder).realIncome.setTextColor(colorgrey);
                ((StardardVH) holder).validDate.setTextColor(colorgrey);
                ((StardardVH) holder).title.setTextColor(colorgrey);
                ((StardardVH) holder).chargeLayout.setBackgroundResource(R.drawable.bg_rect_grey_corner);
            }
        }
    }

    @Override public int getItemViewType(int position) {
        return datas.get(position).isAdd() ? 1 : 0;
    }

    @Override public int getItemCount() {
        return datas.size();
    }

    @Override public void onClick(View v) {
        int nPos = (int) v.getTag();

        if (listener != null) listener.onItemClick(v, nPos);
    }

    public class AddVh extends RecyclerView.ViewHolder {

        public AddVh(View itemView) {
            super(itemView);
        }
    }

    public class StardardVH extends RecyclerView.ViewHolder {
        @BindView(R.id.title) TextView title;
        @BindView(R.id.real_income) TextView realIncome;
        @BindView(R.id.valid_date) TextView validDate;
        @BindView(R.id.support_type) TextView supportType;
        @BindView(R.id.charge_layout) LinearLayout chargeLayout;
        @BindView(R.id.chosen) ImageView chosen;
        @BindView(R.id.tag_only_staff) ImageView onlyStaff;

        public StardardVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
