package cn.qingchengfit.checkout;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import cn.qingchengfit.checkout.view.CheckoutHomeViewModel;
import cn.qingchengfit.saasbase.common.di.scope.ViewModelKey;
import cn.qingchengfit.saasbase.common.mvvm.ViewModelFactory;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;

@Module public abstract class PageModelModule {
  @Binds abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

  @Binds @IntoMap @ViewModelKey(CheckoutHomeViewModel.class)
  abstract ViewModel bindCheckoutHomeViewModel(CheckoutHomeViewModel model);
}
