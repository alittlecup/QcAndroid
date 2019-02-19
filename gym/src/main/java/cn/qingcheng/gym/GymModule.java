package cn.qingcheng.gym;

import android.arch.lifecycle.ViewModel;
import cn.qingcheng.gym.pages.my.MyGymsViewModel;
import cn.qingchengfit.gym.di.BindGymActivity;
import cn.qingchengfit.gym.routers.GymRouterCenter;
import cn.qingchengfit.gym.routers.gymImpl;
import cn.qingchengfit.saascommon.di.ViewModelKey;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;

@Module(includes = { BindGymActivity.class }) public abstract class GymModule {
  private static GymRouterCenter gymRouterCenter = new GymRouterCenter().registe(new gymImpl());

  @Binds @IntoMap @ViewModelKey(MyGymsViewModel.class)
  abstract ViewModel bindMyGymViewModel(MyGymsViewModel viewModel);

  @Provides  public static GymRouterCenter provideRouterCenter() {
    return gymRouterCenter;
  }
}
