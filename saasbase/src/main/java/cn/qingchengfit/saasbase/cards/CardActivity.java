package cn.qingchengfit.saasbase.cards;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.cards.views.CardBuyFragment;
import cn.qingchengfit.saasbase.cards.views.CardChargeFragment;
import cn.qingchengfit.saasbase.cards.views.CardDetailFragment;
import cn.qingchengfit.saasbase.cards.views.CardFilterTplFragment;
import cn.qingchengfit.saasbase.cards.views.CardListHomeFragment;
import cn.qingchengfit.saasbase.cards.views.CardTplDetailFragment;
import cn.qingchengfit.saasbase.cards.views.CardTplOptionFragment;
import cn.qingchengfit.saasbase.cards.views.CardTplsHomeInGymFragment;
import cn.qingchengfit.saasbase.cards.views.CardtplOptionAddFragment;
import cn.qingchengfit.saasbase.cards.views.ChooseCardTplForBuyCardFragment;
import cn.qingchengfit.saasbase.routers.Icard;
import cn.qingchengfit.saasbase.routers.RouterCenter;
import cn.qingchengfit.views.activity.BaseActivity;
import com.anbillon.flabellum.annotations.Trunk;
import dagger.Lazy;
import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.support.HasSupportFragmentInjector;
import javax.inject.Inject;

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
 * Created by Paper on 2017/9/29.
 */
@Trunk(fragments = {
    CardTplsHomeInGymFragment.class, CardTplDetailFragment.class, ChooseCardTplForBuyCardFragment.class,
    CardBuyFragment.class, CardListHomeFragment.class, CardFilterTplFragment.class,
    CardDetailFragment.class, CardtplOptionAddFragment.class, CardTplOptionFragment.class
    , CardChargeFragment.class
})
public class CardActivity extends BaseActivity implements HasSupportFragmentInjector {

  @Inject DispatchingAndroidInjector<Fragment> dispatchingFragmentInjector;
  @Inject RouterCenter routerCenter;
  @Inject Icard cardImpl;
  @Inject Lazy<BuyCardRouter> buyCardRouterLazy;

  @Override protected void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_base_frag);
    routerCenter.registe(cardImpl);
    onNewIntent(getIntent());
  }

  @Override protected Fragment getRouterFragment(Intent intent) {
    return routerCenter.getFragment(intent.getData(), intent.getBundleExtra("b"));
  }

  @Override public AndroidInjector<Fragment> supportFragmentInjector() {
    return dispatchingFragmentInjector;
  }

  @Override public String getModuleName() {
    return "card";
  }

  @Override protected boolean preHandle(Intent intent) {
    //if (intent.getData().getPath().equalsIgnoreCase("/buy/")){
    //  getSupportFragmentManager().beginTransaction()
    //      .add(buyCardRouterLazy.get(),"buycard").commitAllowingStateLoss();
    //  return true;
    //}
    return false;
  }
}
