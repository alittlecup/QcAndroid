package cn.qingchengfit.pos.card;

import cn.qingchengfit.saasbase.cards.BindCardModel;
import cn.qingchengfit.saasbase.cards.CardActivity;
import cn.qingchengfit.saasbase.cards.views.CardDetailFragment;
import cn.qingchengfit.saasbase.cards.views.CardFilterTplFragment;
import cn.qingchengfit.saasbase.cards.views.CardTplListFragment;
import cn.qingchengfit.saasbase.cards.views.CardTplOptionFragment;
import cn.qingchengfit.saasbase.cards.views.CardTplsHomeInGymFragment;
import cn.qingchengfit.saasbase.cards.views.CardtplAddFragment;
import cn.qingchengfit.saasbase.cards.views.CardtplOptionAddFragment;
import cn.qingchengfit.saasbase.cards.views.ChooseCardTplForBuyCardFragment;
import com.anbillon.flabellum.annotations.Trunk;

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
 * Created by Paper on 2017/10/12.
 */
@Trunk(fragments = {
  CardTplsHomeInGymFragment.class, PosCardTplDetailFragment.class,
  ChooseCardTplForBuyCardFragment.class, PosCardBuyFragment.class, PosCardListHomeFragment.class,
  CardFilterTplFragment.class, CardDetailFragment.class, CardtplOptionAddFragment.class,
  CardTplOptionFragment.class, PosCardChargeFragment.class, CardtplAddFragment.class,
  BindCardModel.class, CardTplListFragment.class,
}) public class PosCardActivity extends CardActivity {


}
