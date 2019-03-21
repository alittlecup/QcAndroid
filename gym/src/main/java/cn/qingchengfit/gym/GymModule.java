package cn.qingchengfit.gym;

import android.arch.lifecycle.ViewModel;
import cn.qingchengfit.gym.pages.apply.GymApplyDealFinishVM;
import cn.qingchengfit.gym.pages.apply.GymApplyDealViewModel;
import cn.qingchengfit.gym.pages.apply.GymApplyViewModel;
import cn.qingchengfit.gym.pages.brand.ChangeGymViewModel;
import cn.qingchengfit.gym.pages.brand.GymBrandViewModel;
import cn.qingchengfit.gym.pages.create.GymBrandCreateViewModel;
import cn.qingchengfit.gym.pages.create.GymCreateChooseViewModel;
import cn.qingchengfit.gym.pages.gym.GymInfoViewModel;
import cn.qingchengfit.gym.pages.gym.GymSimpleListViewModel;
import cn.qingchengfit.gym.pages.my.MyGymsViewModel;
import cn.qingchengfit.gym.pages.search.GymSearchViewModel;
import cn.qingchengfit.gym.responsitory.GymResponsitoryImpl;
import cn.qingchengfit.gym.responsitory.IGymResponsitory;
import cn.qingchengfit.gym.responsitory.network.GymModelImpl;
import cn.qingchengfit.gym.responsitory.network.IGymModel;
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

  @Binds  abstract IGymModel bindGymModel(GymModelImpl gymModel);
  @Binds abstract IGymResponsitory bindIGymResponsitory(GymResponsitoryImpl gymResponsitory);

  @Binds @IntoMap @ViewModelKey(MyGymsViewModel.class)
  abstract ViewModel bindMyGymViewModel(MyGymsViewModel viewModel);

  @Binds @IntoMap @ViewModelKey(GymSearchViewModel.class)
  abstract ViewModel bindGymSearchViewModel(GymSearchViewModel viewModel);

  @Binds @IntoMap @ViewModelKey(GymSimpleListViewModel.class)
  abstract ViewModel bindGymSimpleListViewModel(GymSimpleListViewModel viewModel);

  @Binds @IntoMap @ViewModelKey(GymBrandViewModel.class)
  abstract ViewModel bindGymBrandViewModel(GymBrandViewModel viewModel);

  @Binds @IntoMap @ViewModelKey(GymInfoViewModel.class)
  abstract ViewModel bindGymInfoViewModel(GymInfoViewModel viewModel);

  @Binds @IntoMap @ViewModelKey(ChangeGymViewModel.class)
  abstract ViewModel bindChangeGymViewModel(ChangeGymViewModel viewModel);

  @Binds @IntoMap @ViewModelKey(GymBrandCreateViewModel.class)
  abstract ViewModel bindGymBrandCreateViewModel(GymBrandCreateViewModel viewModel);

  @Binds @IntoMap @ViewModelKey(GymCreateChooseViewModel.class)
  abstract ViewModel bindGymCreateChooseViewModel(GymCreateChooseViewModel viewModel);

  @Binds @IntoMap @ViewModelKey(GymApplyViewModel.class)
  abstract ViewModel bindGymApplyViewModel(GymApplyViewModel viewModel);

  @Binds @IntoMap @ViewModelKey(GymApplyDealViewModel.class)
  abstract ViewModel bindGymApplyDealViewModel(GymApplyDealViewModel viewModel);
  @Binds @IntoMap @ViewModelKey(GymApplyDealFinishVM.class)
  abstract ViewModel bindGymApplyDealFinishVM(GymApplyDealFinishVM viewModel);

  @Provides public static GymRouterCenter provideRouterCenter() {
    return gymRouterCenter;
  }
}
