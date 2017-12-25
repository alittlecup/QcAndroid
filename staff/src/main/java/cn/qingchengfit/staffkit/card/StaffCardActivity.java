package cn.qingchengfit.staffkit.card;

import cn.qingchengfit.saasbase.cards.BindCardModel;
import cn.qingchengfit.saasbase.cards.CardActivity;
import cn.qingchengfit.saasbase.cards.views.AutoNotifySettingFragment;
import cn.qingchengfit.saasbase.cards.views.BatchPayCardFragment;
import cn.qingchengfit.saasbase.cards.views.CardBalanceFragment;
import cn.qingchengfit.saasbase.cards.views.CardBuyFragment;
import cn.qingchengfit.saasbase.cards.views.CardDetailFragment;
import cn.qingchengfit.saasbase.cards.views.CardFilterTplFragment;
import cn.qingchengfit.saasbase.cards.views.CardFixValidDayFragment;
import cn.qingchengfit.saasbase.cards.views.CardListHomeFragment;
import cn.qingchengfit.saasbase.cards.views.CardRefundFragment;
import cn.qingchengfit.saasbase.cards.views.CardTplAddInBrandFragment;
import cn.qingchengfit.saasbase.cards.views.CardTplDetailFragment;
import cn.qingchengfit.saasbase.cards.views.CardTplOptionFragment;
import cn.qingchengfit.saasbase.cards.views.CardTplsHomeInGymFragment;
import cn.qingchengfit.saasbase.cards.views.CardTplsInBrandFragment;
import cn.qingchengfit.saasbase.cards.views.CardtplAddFragment;
import cn.qingchengfit.saasbase.cards.views.CardtplOptionAddFragment;
import cn.qingchengfit.saasbase.cards.views.ChangeAutoNotifyFragment;
import cn.qingchengfit.saasbase.cards.views.ChooseCardTplForBuyCardFragment;
import cn.qingchengfit.saasbase.cards.views.EditCardTplFragment;
import cn.qingchengfit.saasbase.cards.views.MutiChooseGymFragment;
import cn.qingchengfit.saasbase.cards.views.NewCardChargeFragment;
import cn.qingchengfit.saasbase.cards.views.offday.AddOffDayFragment;
import cn.qingchengfit.saasbase.cards.views.offday.AheadOffDayFragment;
import cn.qingchengfit.saasbase.cards.views.offday.OffDayListFragment;
import cn.qingchengfit.staffkit.card.view.StaffCardBuyFragment;
import cn.qingchengfit.staffkit.card.view.StaffCardChargeFragment;
import com.anbillon.flabellum.annotations.Trunk;

@Trunk(fragments = {
  CardTplsHomeInGymFragment.class, CardTplsInBrandFragment.class, CardTplDetailFragment.class, AutoNotifySettingFragment.class,CardBalanceFragment.class,
  ChangeAutoNotifyFragment.class, BatchPayCardFragment.class,
  ChooseCardTplForBuyCardFragment.class, CardBuyFragment.class, CardListHomeFragment.class,
  CardFilterTplFragment.class, CardDetailFragment.class, CardtplOptionAddFragment.class,
  CardTplOptionFragment.class, NewCardChargeFragment.class, CardtplAddFragment.class,
  BindCardModel.class, EditCardTplFragment.class, CardTplAddInBrandFragment.class, MutiChooseGymFragment.class,
    StaffCardBuyFragment.class, CardRefundFragment.class, AddOffDayFragment.class,
    AheadOffDayFragment.class, OffDayListFragment.class, CardFixValidDayFragment.class,
    StaffCardChargeFragment.class,
}) public class StaffCardActivity extends CardActivity {
}