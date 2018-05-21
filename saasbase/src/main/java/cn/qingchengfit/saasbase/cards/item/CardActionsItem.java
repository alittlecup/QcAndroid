package cn.qingchengfit.saasbase.cards.item;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.events.EventRecycleClick;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.cards.bean.Card;
import cn.qingchengfit.saasbase.utils.CardBusinessUtils;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class CardActionsItem extends AbstractFlexibleItem<CardActionsItem.CardActionsVH> {

  Card card;

  public CardActionsItem(Card card) {
    this.card = card;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_card_action;
  }

  @Override public CardActionsVH createViewHolder(View view, FlexibleAdapter adapter) {
    return new CardActionsVH(view, adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, CardActionsVH holder, int position,
      List payloads) {
    holder.tvBalance.setText(CardBusinessUtils.getCardBlance(card));
  }

  @Override public boolean equals(Object o) {
    return o instanceof CardActionsItem;
  }

  public class CardActionsVH extends FlexibleViewHolder {

	TextView tvBalance;
	Button btnCharge;

    public CardActionsVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      tvBalance = (TextView) view.findViewById(R.id.tv_balance);
      btnCharge = (Button) view.findViewById(R.id.btn_charge);

      btnCharge.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          RxBus.getBus().post(new EventRecycleClick(getAdapterPosition(),btnCharge.getId()));
        }
      });
    }
  }
}