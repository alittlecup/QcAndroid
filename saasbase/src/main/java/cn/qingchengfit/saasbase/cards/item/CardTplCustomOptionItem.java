package cn.qingchengfit.saasbase.cards.item;

import android.view.View;
import cn.qingchengfit.model.base.CardTplOption;
import cn.qingchengfit.saasbase.R;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.List;

/**
 * Created by fb on 2017/12/22.
 */

public class CardTplCustomOptionItem extends CardTplOptionForBuy {

  public CardTplCustomOptionItem(CardTplOption option, int cardtplType) {
    super(option, cardtplType);
  }

  public CardTplCustomOptionItem(CardTplOption option, int cardtplType,
      OnCustomCardOptionListener onCustomCardOptionListener) {
    super(option, cardtplType, onCustomCardOptionListener);
  }

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, CardtplStandardVH holder, int position,
      List payloads) {
    super.bindViewHolder(adapter, holder, position, payloads);
    holder.imgCardOption.setImageDrawable(holder.itemView.getResources().getDrawable(R.drawable.vd_custom_card_option));
    if (adapter.isSelected(position)) {
      holder.imgCardOption.setVisibility(View.VISIBLE);
      holder.chargeLayout.setBackgroundResource(R.drawable.bg_card_option_primary);
    } else {
      holder.imgCardOption.setVisibility(View.GONE);
      holder.chargeLayout.setBackgroundResource(R.drawable.bg_card_option_grey);
    }
  }
}
