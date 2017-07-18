package cn.qingchengfit.saas.di;

import android.support.v4.app.Fragment;
import cn.qingchengfit.saas.views.fragments.ChooseGymFragment;
import cn.qingchengfit.saas.views.fragments.EditGymInfoFragment;
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
 * Created by Paper on 2017/7/4.
 */

public class BindSaas {

  @Subcomponent() public interface ChooseGymFragmentSubcomponent
      extends AndroidInjector<ChooseGymFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<ChooseGymFragment> {
    }
  }

  @Subcomponent() public interface EditGymInfoFragmentSubcomponent
      extends AndroidInjector<EditGymInfoFragment> {
    @Subcomponent.Builder public abstract class Builder
        extends AndroidInjector.Builder<EditGymInfoFragment> {
    }
  }

  @Module(subcomponents = ChooseGymFragmentSubcomponent.class)
  public abstract class ChooseGymFragmentModule {
    @Binds @IntoMap @FragmentKey(ChooseGymFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        ChooseGymFragmentSubcomponent.Builder builder);
  }

  @Module(subcomponents = EditGymInfoFragmentSubcomponent.class)
  abstract class EditGymInfoFragmentModule {
    @Binds @IntoMap @FragmentKey(EditGymInfoFragment.class)
    abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(
        EditGymInfoFragmentSubcomponent.Builder builder);
  }
}
