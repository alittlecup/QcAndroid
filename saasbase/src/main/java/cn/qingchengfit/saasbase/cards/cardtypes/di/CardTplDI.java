package cn.qingchengfit.saasbase.cards.cardtypes.di;

import android.support.v4.app.Fragment;
import cn.qingchengfit.saasbase.cards.cardtypes.views.CardTplListFragment;
import cn.qingchengfit.saasbase.cards.cardtypes.views.CardTplsHomeInGymFragment;
import cn.qingchengfit.saasbase.cards.cardtypes.views.ChooseCardTplFragment;
import dagger.Binds;
import dagger.Module;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;

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
 * Created by Paper on 2017/8/15.
 */
public interface CardTplDI {


  @Subcomponent() public interface CardTypesHomeInGymFragmentSubcomponent extends AndroidInjector<CardTplsHomeInGymFragment> {
    @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<CardTplsHomeInGymFragment> {}
  }
  @Subcomponent() public interface CardTypeListFragmentSubcomponent extends AndroidInjector<CardTplListFragment> {
    @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<CardTplListFragment> {}
  }

  @Subcomponent() public interface ChooseCardTplFragmentSubcomponent extends AndroidInjector<ChooseCardTplFragment> {
      @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<ChooseCardTplFragment> {}
  }

  @Module(subcomponents = CardTypesHomeInGymFragmentSubcomponent.class) abstract class CardTypesHomeInGymFragmentModule {
    @Binds @IntoMap @FragmentKey(CardTplsHomeInGymFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(CardTypesHomeInGymFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = CardTypeListFragmentSubcomponent.class) abstract class CardTypeListFragmentModule {
    @Binds @IntoMap @FragmentKey(CardTplListFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(CardTypeListFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = ChooseCardTplFragmentSubcomponent.class) abstract class ChooseCardTplFragmentModule {
      @Binds @IntoMap @FragmentKey(ChooseCardTplFragment.class)
      abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(ChooseCardTplFragmentSubcomponent.Builder builder);
  }
}
