package cn.qingchengfit.saasbase.cards.cardtypes.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import butterknife.BindView;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import cn.qingchengfit.saasbase.cards.cardtypes.bean.Card;
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

  @Override public CardActionsVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new CardActionsVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, CardActionsVH holder, int position,
      List payloads) {
    holder.tvBalance.setText(CardBusinessUtils.getCardBlance(card));
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class CardActionsVH extends FlexibleViewHolder {

    @BindView(R2.id.tv_balance) TextView tvBalance;
    @BindView(R2.id.btn_charge) Button btnCharge;

    public CardActionsVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
      btnCharge.setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          // TODO: 2017/9/28 充值按键

        }
      });
    }
  }
}