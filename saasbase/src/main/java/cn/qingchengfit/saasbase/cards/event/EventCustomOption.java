package cn.qingchengfit.saasbase.cards.event;

import cn.qingchengfit.model.base.CardTplOption;

/**
 * Created by fb on 2017/12/15.
 */

public class EventCustomOption {

  private CardTplOption cardOptionCustom;

  public EventCustomOption(CardTplOption cardOptionCustom) {
    this.cardOptionCustom = cardOptionCustom;
  }

  public CardTplOption getCardOptionCustom() {
    return cardOptionCustom;
  }
}
