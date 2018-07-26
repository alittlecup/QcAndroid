package cn.qingchengfit.staffkit.card;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import cn.qingchengfit.saasbase.SaasBaseFragment;
import cn.qingchengfit.saasbase.cards.BindCardModel;
import cn.qingchengfit.saasbase.cards.CardActivity;
import cn.qingchengfit.saasbase.cards.views.AutoNotifySettingFragment;
import cn.qingchengfit.saasbase.cards.views.BatchPayCardFragment;
import cn.qingchengfit.saasbase.cards.views.CardBalanceFragment;
import cn.qingchengfit.saasbase.cards.views.CardBuyFragment;
import cn.qingchengfit.saasbase.cards.views.CardDetailFragment;
import cn.qingchengfit.saasbase.cards.views.CardFilterTplFragment;
import cn.qingchengfit.saasbase.cards.views.CardFixValidDayFragment;
import cn.qingchengfit.saasbase.cards.views.CardListFragment;
import cn.qingchengfit.saasbase.cards.views.CardListHomeFragment;
import cn.qingchengfit.saasbase.cards.views.CardRefundFragment;
import cn.qingchengfit.saasbase.cards.views.CardTplAddInBrandFragment;
import cn.qingchengfit.saasbase.cards.views.CardTplDetailFragment;
import cn.qingchengfit.saasbase.cards.views.CardTplDetailInBrandFragment;
import cn.qingchengfit.saasbase.cards.views.CardTplOptionFragment;
import cn.qingchengfit.saasbase.cards.views.CardTplsHomeInGymFragment;
import cn.qingchengfit.saasbase.cards.views.CardTplsInBrandFragment;
import cn.qingchengfit.saasbase.cards.views.CardtplAddFragment;
import cn.qingchengfit.saasbase.cards.views.CardtplOptionAddFragment;
import cn.qingchengfit.saasbase.cards.views.ChangeAutoNotifyFragment;
import cn.qingchengfit.saasbase.cards.views.ChooseCardTplForBuyCardFragment;
import cn.qingchengfit.saasbase.cards.views.ChooseCardTplForBuyCardNoNewTplFragment;
import cn.qingchengfit.saasbase.cards.views.EditCardTplFragment;
import cn.qingchengfit.saasbase.cards.views.MutiChooseGymFragment;
import cn.qingchengfit.saasbase.cards.views.NewCardChargeFragment;
import cn.qingchengfit.saasbase.cards.views.offday.AddOffDayFragment;
import cn.qingchengfit.saasbase.cards.views.offday.AheadOffDayFragment;
import cn.qingchengfit.saasbase.cards.views.offday.OffDayListFragment;
import cn.qingchengfit.saasbase.cards.views.spendrecord.CardBalanceListFragment;
import cn.qingchengfit.saasbase.cards.views.spendrecord.SpendRecordFragment;
import cn.qingchengfit.saasbase.cards.views.spendrecord.SpendRecordListFragment;
import cn.qingchengfit.staff.routers.StaffRouterCenter;
import cn.qingchengfit.staff.routers.cardImpl;
import cn.qingchengfit.staffkit.card.view.StaffCardBuyFragment;
import cn.qingchengfit.staffkit.card.view.StaffCardChargeFragment;
import cn.qingchengfit.staffkit.card.view.StaffCardListHomeFragment;
import cn.qingchengfit.staffkit.card.view.WebCardChargeFragment;
import cn.qingchengfit.staffkit.card.view.WebChargeFragment;
import com.anbillon.flabellum.annotations.Trunk;
import com.sensorsdata.analytics.android.sdk.SensorsDataIgnoreTrackAppViewScreen;


@SensorsDataIgnoreTrackAppViewScreen
@Trunk(fragments = {
  CardTplsHomeInGymFragment.class, CardTplsInBrandFragment.class, CardTplDetailFragment.class, AutoNotifySettingFragment.class,CardBalanceFragment.class,
  ChangeAutoNotifyFragment.class, BatchPayCardFragment.class,
  ChooseCardTplForBuyCardFragment.class, CardBuyFragment.class, CardListHomeFragment.class,
  CardFilterTplFragment.class, CardDetailFragment.class, CardtplOptionAddFragment.class,
  CardTplOptionFragment.class, NewCardChargeFragment.class, CardtplAddFragment.class,
  BindCardModel.class, EditCardTplFragment.class, CardTplAddInBrandFragment.class, MutiChooseGymFragment.class,
    StaffCardBuyFragment.class, CardRefundFragment.class, AddOffDayFragment.class,
    AheadOffDayFragment.class, OffDayListFragment.class, CardFixValidDayFragment.class,
    StaffCardChargeFragment.class, StaffCardListHomeFragment.class, CardTplDetailInBrandFragment.class,
    CardListFragment.class, SpendRecordFragment.class, SpendRecordListFragment.class,
    WebCardChargeFragment.class, WebChargeFragment.class,ChooseCardTplForBuyCardNoNewTplFragment.class,
    CardBalanceListFragment.class
}) public class StaffCardActivity extends CardActivity {
  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override protected Fragment getRouterFragment(Intent intent) {
    Fragment fragment = new StaffRouterCenter().registe(new cardImpl())
        .getFragment(intent.getData(), intent.getExtras());
    if(fragment instanceof SaasBaseFragment){
      return fragment;
    }
    return super.getRouterFragment(intent);
  }
}