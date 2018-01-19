package cn.qingchengfit.saasbase.cards.item;

import android.view.View;
import cn.qingchengfit.model.base.CardTplOption;
import eu.davidea.flexibleadapter.FlexibleAdapter;
import java.util.List;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/10/16.
 */

public class CardTplOptionForBuy extends CardtplOptionItem {

  private OnCustomCardOptionListener onCustomCardOptionListener;

  public CardTplOptionForBuy(CardTplOption option, int cardtplType) {
    super(option, cardtplType);
  }

  public CardTplOptionForBuy(CardTplOption option, int cardtplType, OnCustomCardOptionListener onCustomCardOptionListener) {
    super(option, cardtplType);
    this.option = option;
    this.cardtplType = cardtplType;
    this.onCustomCardOptionListener = onCustomCardOptionListener;

  }

  @Override public CardtplStandardVH createViewHolder(View view, FlexibleAdapter adapter) {
    CardtplStandardVH holder = super.createViewHolder(view, adapter);
    holder.imgCardOption.setOnClickListener(new View.OnClickListener() {
      @Override public void onClick(View view) {
        if (onCustomCardOptionListener != null) {
          onCustomCardOptionListener.onCustomCard((int)view.getTag());
        }
      }
    });
    return holder;
}

  @Override
  public void bindViewHolder(FlexibleAdapter adapter, CardtplStandardVH holder, int position,
      List payloads) {
    super.bindViewHolder(adapter, holder, position, payloads);
    holder.imgCardOption.setTag(position);
    holder.supportType.setVisibility(View.GONE);
    holder.tagOnlyStaff.setVisibility(View.GONE);
  }

public interface OnCustomCardOptionListener {
  void onCustomCard(int position);
}

}
