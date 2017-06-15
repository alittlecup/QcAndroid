package cn.qingchengfit.staffkit.views.wardrobe.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.model.common.Card;
import cn.qingchengfit.staffkit.R;
import cn.qingchengfit.utils.StringUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class PayWardrobeItem extends AbstractFlexibleItem<PayWardrobeItem.PayWardrobeVH> {

    private int pay_mode;
    private Card card;

    public PayWardrobeItem(int pay_mode, Card card) {
        this.pay_mode = pay_mode;
        this.card = card;
    }

    public int getPay_mode() {
        return pay_mode;
    }

    public void setPay_mode(int pay_mode) {
        this.pay_mode = pay_mode;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }

    @Override public int getLayoutRes() {
        return R.layout.item_pay_wardrobe;
    }

    @Override public PayWardrobeVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater, ViewGroup parent) {
        return new PayWardrobeVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
    }

    @Override public void bindViewHolder(FlexibleAdapter adapter, PayWardrobeVH holder, int position, List payloads) {
        String s = "";
        holder.account.setVisibility(View.GONE);
        switch (pay_mode) {
            case 1://会员卡支付
                holder.img.setImageResource(R.drawable.ic_credit_pay);
                if (card != null) {
                    s = card.getName() + " (" + card.getId() + ")";
                    holder.account.setVisibility(View.VISIBLE);
                    holder.account.setText(card.getBalance() + StringUtils.getUnit(holder.itemView.getContext(), card.getType()));
                }
                break;
            case 2:
                s = "现金支付";
                holder.img.setImageResource(R.drawable.ic_cash_pay);
                break;
            case 3:
                s = "微信支付";
                break;
            case 4:
                s = "银行卡支付";
                holder.img.setImageResource(R.drawable.ic_credit_pay);
                break;

            case 6:
                s = "其他支付";
                holder.img.setImageResource(R.drawable.ic_other_pay);
                break;
            case 5:
                s = "转账支付";
                holder.img.setImageResource(R.drawable.ic_transit_pay);
                break;
        }
        holder.name.setText(s);
    }

    @Override public boolean equals(Object o) {
        return false;
    }

    public class PayWardrobeVH extends FlexibleViewHolder {
        @BindView(R.id.img) ImageView img;
        @BindView(R.id.name) TextView name;
        @BindView(R.id.account) TextView account;

        public PayWardrobeVH(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            ButterKnife.bind(this, view);
        }
    }
}