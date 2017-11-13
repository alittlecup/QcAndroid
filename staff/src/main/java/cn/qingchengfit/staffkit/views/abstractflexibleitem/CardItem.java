package cn.qingchengfit.staffkit.views.abstractflexibleitem;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.common.Card;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.staffkit.constant.Configs;
import cn.qingchengfit.utils.ColorUtils;
import cn.qingchengfit.utils.CompatUtils;
import cn.qingchengfit.utils.RealCardUtils;
import cn.qingchengfit.widgets.ConnerTag;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class CardItem extends AbstractFlexibleItem<CardItem.CardVH> {

    Card realCard;

    public CardItem(Card realCard) {
        this.realCard = realCard;
    }

    public Card getRealCard() {
        return realCard;
    }

    public void setRealCard(Card realCard) {
        this.realCard = realCard;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_realcard;
    }

    @Override public CardVH createViewHolder(View view, FlexibleAdapter adapter) {
        return new CardVH(view, adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, CardVH holder, int position, List payloads) {
        holder.realcardName.setText(realCard.getName());
        holder.realcardBalance.setText(RealCardUtils.getCardBlance(realCard));

        if (realCard.is_locked()) {
            holder.imgStatus.setVisibility(View.VISIBLE);
            holder.imgStatus.setBgColor(ContextCompat.getColor(holder.imgStatus.getContext(), R.color.bg_card_off_day));
            holder.imgStatus.setText("请假中");
        } else if (!realCard.is_active()) {
            holder.imgStatus.setVisibility(View.VISIBLE);
            holder.imgStatus.setBgColor(ContextCompat.getColor(holder.imgStatus.getContext(), R.color.bg_card_stop));
            holder.imgStatus.setText("已停卡");
            //Glide.with(holder.itemView.getContext()).load(R.drawable.img_corner_unregiste).into(holder.imgStatus);
        } else if (realCard.isExpired()) {
            holder.imgStatus.setVisibility(View.VISIBLE);
            holder.imgStatus.setBgColor(ContextCompat.getColor(holder.imgStatus.getContext(), R.color.bg_card_out_of_day));
            holder.imgStatus.setText("已过期");
            if (realCard.getType() == Configs.CATEGORY_DATE) {
                holder.realcardBalance.setText("已过期" + (-((Float) realCard.getBalance()).intValue()) + "天");
            }
        } else {
            holder.imgStatus.setVisibility(View.GONE);
        }
        holder.card.setCardBackgroundColor(ColorUtils.parseColor(realCard.getColor(), 200).getColor());
        CompatUtils.setBg(holder.cardBg, ColorUtils.parseColor(realCard.getColor(), 200));
        holder.realcardStudents.setText(realCard.getUsersStr());
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class CardVH extends FlexibleViewHolder {
        @BindView(R.id.realcard_name) TextView realcardName;
        @BindView(R.id.realcard_students) TextView realcardStudents;
        @BindView(R.id.realcard_balance) TextView realcardBalance;
        @BindView(R.id.img_stutus) ConnerTag imgStatus;
        @BindView(R.id.card_bg) RelativeLayout cardBg;
        @BindView(R.id.card) CardView card;

        public CardVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}