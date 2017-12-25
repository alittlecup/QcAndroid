package cn.qingchengfit.inject.moudle;

import cn.qingchengfit.model.body.CreateCardBody;
import cn.qingchengfit.saasbase.cards.bean.CardTpl;
import dagger.Module;
import dagger.Provides;

/**
 * power by
 * <p>
 * d8888b.  .d8b.  d8888b. d88888b d8888b.
 * 88  `8D d8' `8b 88  `8D 88'     88  `8D
 * 88oodD' 88ooo88 88oodD' 88ooooo 88oobY'
 * 88~~~   88~~~88 88~~~   88~~~~~ 88`8b
 * 88      88   88 88      88.     88 `88.
 * 88      YP   YP 88      Y88888P 88   YD
 * <p>
 * <p>
 * Created by Paper on 16/1/28 2016.
 */
@Module public class BuyCardMoudle {

    CardTpl card_tpl;
    CreateCardBody buyCardBody;

    public BuyCardMoudle(CardTpl card_tpl, CreateCardBody buyCardBody) {
        this.card_tpl = card_tpl;
        this.buyCardBody = buyCardBody;
    }

    @Provides CardTpl provideCardTpl() {
        return card_tpl;
    }

    @Provides CreateCardBody provideCardBody() {
        return buyCardBody;
    }
}
