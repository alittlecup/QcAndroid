package cn.qingchengfit.pos.login.di;

import cn.qingchengfit.pos.login.LoginActivity;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;

/**
 * Created by fb on 2017/10/11.
 */

@Module
public abstract class BindLoginModule {
  @ContributesAndroidInjector(modules = { LoginDI.LoginFragmentModule.class })
  abstract LoginActivity contributeLoginActivityInjector();

}
