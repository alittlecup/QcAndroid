package cn.qingchengfit.staffkit.dianping;

import android.arch.lifecycle.ViewModel;
import cn.qingchengfit.saascommon.di.ViewModelKey;
import cn.qingchengfit.staff.di.BindDianPingActivity;
import cn.qingchengfit.staffkit.dianping.pages.DianPingAccountViewModel;
import cn.qingchengfit.staffkit.dianping.pages.DianPingChooseViewModel;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module(includes = { BindDianPingActivity.class }) public abstract class DianPingModule {
  @Binds @IntoMap @ViewModelKey(DianPingAccountViewModel.class)
  abstract ViewModel bindDianPingAccountViewModel(DianPingAccountViewModel viewModel);

  @Binds @IntoMap @ViewModelKey(DianPingChooseViewModel.class)
  abstract ViewModel bindDianPingChooseViewModel(DianPingChooseViewModel viewModel);
}
