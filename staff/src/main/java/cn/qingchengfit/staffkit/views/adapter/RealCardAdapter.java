package cn.qingchengfit.staffkit.views.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.common.RealCard;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.staffkit.views.custom.OnRecycleItemClickListener;
import cn.qingchengfit.utils.ColorUtils;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.widgets.ConnerTag;
import java.util.List;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/3/19 2016.
 */
public class RealCardAdapter extends RecyclerView.Adapter<RealCardAdapter.RealCardVH> implements View.OnClickListener {
    OnRecycleItemClickListener listener;
    private List<RealCard> datas;

    public RealCardAdapter(List<RealCard> datas) {
        this.datas = datas;
    }

    public OnRecycleItemClickListener getListener() {
        return listener;
    }

    public void setListener(OnRecycleItemClickListener listener) {
        this.listener = listener;
    }

    public void setDatas(List<RealCard> datas) {
        this.datas = datas;
    }

    @Override public RealCardVH onCreateViewHolder(ViewGroup parent, int viewType) {
        RealCardVH vh = new RealCardVH(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_realcard, parent, false));
        vh.itemView.setOnClickListener(this);
        return vh;
    }

    @Override public void onBindViewHolder(RealCardVH holder, int position) {
        holder.itemView.setTag(position);
        RealCard card = datas.get(position);
        String balance;
        if (card.type == Configs.CATEGORY_VALUE) {
            balance = TextUtils.concat("余额:", card.balance, "元").toString();
        } else if (card.type == Configs.CATEGORY_DATE) {
            if (card.expired) {
                balance = TextUtils.concat("已过期", card.trial_days == null ? "0" : -card.trial_days.intValue() + "", "天").toString();
            } else {
                balance = TextUtils.concat("余额:", card.balance, "天").toString();
            }
        } else {
            balance = TextUtils.concat("余额:", card.balance, "次").toString();
        }
        holder.realcardBalance.setText(balance);
        holder.realcardName.setText(card.name + "(" + card.id + ")");
        holder.realcardStudents.setText(card.students);
        CompatUtils.setBg(holder.cardBg, ColorUtils.parseColor(card.color, 200));
        holder.cardView.setCardBackgroundColor(ColorUtils.parseColor(card.color, 200).getColor());
        if (card.isDuringHoloday) {
            holder.imgStatus.setVisibility(View.VISIBLE);
            holder.imgStatus.setBgColor(ContextCompat.getColor(holder.imgStatus.getContext(), R.color.bg_card_off_day));
            holder.imgStatus.setText("请假中");
        } else if (card.isCancel) {
            holder.imgStatus.setVisibility(View.VISIBLE);
            holder.imgStatus.setBgColor(ContextCompat.getColor(holder.imgStatus.getContext(), R.color.bg_card_stop));
            holder.imgStatus.setText("已停卡");
            //Glide.with(holder.itemView.getContext()).load(R.drawable.img_corner_unregiste).into(holder.imgStatus);
        } else if (card.expired) {
            holder.imgStatus.setVisibility(View.VISIBLE);
            holder.imgStatus.setBgColor(ContextCompat.getColor(holder.imgStatus.getContext(), R.color.bg_card_out_of_day));
            holder.imgStatus.setText("已过期");
        } else {
            holder.imgStatus.setVisibility(View.GONE);
        }
    }

    @Override public int getItemCount() {
        return datas.size();
    }

    @Override public void onClick(View v) {
        if (listener != null) {
            listener.onItemClick(v, (int) v.getTag());
        }
    }

    public class RealCardVH extends RecyclerView.ViewHolder {

        @BindView(R.id.realcard_name) TextView realcardName;
        @BindView(R.id.realcard_students) TextView realcardStudents;
        @BindView(R.id.realcard_balance) TextView realcardBalance;
        @BindView(R.id.img_stutus) ConnerTag imgStatus;
        @BindView(R.id.card) CardView cardView;
        @BindView(R.id.card_bg) RelativeLayout cardBg;

        public RealCardVH(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}