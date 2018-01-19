package cn.qingchengfit.saasbase.cards.item;

import android.view.View;
import android.widget.LinearLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.R2;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.viewholders.FlexibleViewHolder;
import java.util.List;

/**
 * Created by fb on 2017/12/21.
 */

public class CardDetailFunItem extends AbstractFlexibleItem<CardDetailFunItem.CardDetailFunVH> {

  OnClickCardFunListener listener;

  public CardDetailFunItem(OnClickCardFunListener listener) {
    this.listener = listener;
  }

  @Override public boolean equals(Object o) {
    return false;
  }

  @Override public int getLayoutRes() {
    return R.layout.item_card_detail_fun;
  }

  @Override public CardDetailFunVH createViewHolder(View view, FlexibleAdapter adapter) {
    CardDetailFunVH holder = new CardDetailFunVH(view, adapter);

    return holder;
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, CardDetailFunVH holder, int position,
      List payloads) {

  }

  class CardDetailFunVH extends FlexibleViewHolder {

    @BindView(R2.id.btn_spend) LinearLayout btnSpend;
    @BindView(R2.id.ask_offday) LinearLayout askOffday;
    @BindView(R2.id.overflow_more) LinearLayout overflowMore;

    public CardDetailFunVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      ButterKnife.bind(this, view);
    }

    @OnClick(R2.id.btn_spend) public void onSpend() {
      if (listener != null){
        listener.onClickSpend();
      }
    }

    @OnClick(R2.id.ask_offday) public void onAskOffday() {
      if (listener != null){
        listener.onAskOffDay();
      }
    }

    @OnClick(R2.id.overflow_more) public void onMore() {
      if (listener != null){
        listener.onClickMore();
      }
    }
  }

  public interface OnClickCardFunListener{
    void onClickSpend();
    void onAskOffDay();
    void onClickMore();
  }

}
