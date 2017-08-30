package cn.qingchengfit.saasbase.staff.di;

import android.support.v4.app.Fragment;
import cn.qingchengfit.saasbase.SaasContainerActivity;
import cn.qingchengfit.saasbase.staff.views.StaffDetailFragment;
import cn.qingchengfit.saasbase.staff.views.StaffListFragment;
import dagger.Binds;
import dagger.Module;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;

/**
 * Created by fb on 2017/8/21.
 */

public interface StaffDI extends AndroidInjector<SaasContainerActivity>{

  @Subcomponent() public interface StaffListFragmentSubcomponent extends AndroidInjector<StaffListFragment> {
      @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<StaffListFragment> {}
  }
  @Module(subcomponents = StaffListFragmentSubcomponent.class) abstract class StaffListFragmentModule {
      @Binds @IntoMap @FragmentKey(StaffListFragment.class)
      abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(StaffListFragmentSubcomponent.Builder builder);
  }

  @Subcomponent() public interface StaffDetailFragmentSubcomponent extends AndroidInjector<StaffDetailFragment> {
      @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<StaffDetailFragment> {}
  }
  @Module(subcomponents = StaffDetailFragmentSubcomponent.class) abstract class StaffDetailFragmentModule {
      @Binds @IntoMap @FragmentKey(StaffDetailFragment.class)
      abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(StaffDetailFragmentSubcomponent.Builder builder);
  }

}
