package cn.qingchengfit.staffkit.card;

import cn.qingchengfit.saasbase.cards.BindCardModel;
import cn.qingchengfit.saasbase.cards.CardActivity;
import cn.qingchengfit.saasbase.cards.views.CardBuyFragment;
import cn.qingchengfit.saasbase.cards.views.CardChargeFragment;
import cn.qingchengfit.saasbase.cards.views.CardDetailFragment;
import cn.qingchengfit.saasbase.cards.views.CardFilterTplFragment;
import cn.qingchengfit.saasbase.cards.views.CardListHomeFragment;
import cn.qingchengfit.saasbase.cards.views.CardTplDetailFragment;
import cn.qingchengfit.saasbase.cards.views.CardTplOptionFragment;
import cn.qingchengfit.saasbase.cards.views.CardTplsHomeInGymFragment;
import cn.qingchengfit.saasbase.cards.views.CardtplAddFragment;
import cn.qingchengfit.saasbase.cards.views.CardtplOptionAddFragment;
import cn.qingchengfit.saasbase.cards.views.ChooseCardTplForBuyCardFragment;
import com.anbillon.flabellum.annotations.Trunk;

@Trunk(fragments = {
  CardTplsHomeInGymFragment.class, CardTplDetailFragment.class,
  ChooseCardTplForBuyCardFragment.class, CardBuyFragment.class, CardListHomeFragment.class,
  CardFilterTplFragment.class, CardDetailFragment.class, CardtplOptionAddFragment.class,
  CardTplOptionFragment.class, CardChargeFragment.class, CardtplAddFragment.class,
  BindCardModel.class
}) public class StaffCardActivity extends CardActivity {


}