package cn.qingchengfit.saasbase.cards.item;

import android.view.View;
import android.widget.LinearLayout;



import cn.qingchengfit.saasbase.R;

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

	LinearLayout btnSpend;
	LinearLayout askOffday;
	LinearLayout overflowMore;

    public CardDetailFunVH(View view, FlexibleAdapter adapter) {
      super(view, adapter);
      btnSpend = (LinearLayout) view.findViewById(R.id.btn_spend);
      askOffday = (LinearLayout) view.findViewById(R.id.ask_offday);
      overflowMore = (LinearLayout) view.findViewById(R.id.overflow_more);
      view.findViewById(R.id.btn_spend).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onSpend();
        }
      });
      view.findViewById(R.id.ask_offday).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onAskOffday();
        }
      });
      view.findViewById(R.id.overflow_more).setOnClickListener(new View.OnClickListener() {
        @Override public void onClick(View v) {
          onMore();
        }
      });
    }

 public void onSpend() {
      if (listener != null){
        listener.onClickSpend();
      }
    }

 public void onAskOffday() {
      if (listener != null){
        listener.onAskOffDay();
      }
    }

 public void onMore() {
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
