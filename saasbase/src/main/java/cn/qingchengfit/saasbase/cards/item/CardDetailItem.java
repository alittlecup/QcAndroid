package cn.qingchengfit.saasbase.cards.item;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import butterknife.ButterKnife;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.cards.bean.Card;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

public class CardDetailItem extends AbstractFlexibleItem<CardDetailItem.CardDetailVH> {

  Card mCard;

  public CardDetailItem(Card mCard) {
    this.mCard = mCard;
  }

  public Card getCard(){
    return mCard;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_card_detail;
  }

  @Override public CardDetailVH createViewHolder(FlexibleAdapter adapter, LayoutInflater inflater,
      ViewGroup parent) {
    return new CardDetailVH(inflater.inflate(getLayoutRes(), parent, false), adapter);
  }

  @Override public void bindViewHolder(FlexibleAdapter adapter, CardDetailVH holder, int position,
      List payloads) {

  }

  @Override public boolean equals(Object o) {
    return false;
  }

  public class CardDetailVH extends FlexibleViewHolder {

    public CardDetailVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }
  }
}