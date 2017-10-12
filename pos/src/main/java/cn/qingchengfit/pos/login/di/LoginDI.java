package cn.qingchengfit.pos.login.di;

import android.support.v4.app.Fragment;
import cn.qingchengfit.pos.login.LoginFragment;
import dagger.Binds;
import dagger.Module;
import dagger.Subcomponent;
import dagger.android.AndroidInjector;
import dagger.android.support.FragmentKey;
import dagger.multibindings.IntoMap;

/**
 * Created by fb on 2017/10/11.
 */

public interface LoginDI {
  @Subcomponent() public interface LoginFragmentSubcomponent extends AndroidInjector<LoginFragment> {
      @Subcomponent.Builder public abstract class Builder extends AndroidInjector.Builder<LoginFragment> {}
  }
  @Module(subcomponents = LoginFragmentSubcomponent.class) abstract class LoginFragmentModule {
      @Binds @IntoMap @FragmentKey(LoginFragment.class)
      abstract AndroidInjector.Factory<? extends Fragment> bindYourFragmentInjectorFactory(LoginFragmentSubcomponent.Builder builder);
  }
}
