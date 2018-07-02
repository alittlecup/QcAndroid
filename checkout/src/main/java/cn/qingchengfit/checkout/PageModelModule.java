package cn.qingchengfit.checkout;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import cn.qingchengfit.checkout.routers.CheckoutRouterCenter;
import cn.qingchengfit.checkout.routers.checkoutImpl;
import cn.qingchengfit.checkout.view.checkout.CheckoutMoneyViewModel;
import cn.qingchengfit.checkout.view.home.CheckoutHomeViewModel;
import cn.qingchengfit.checkout.view.pay.CheckoutPayViewModel;
import cn.qingchengfit.saasbase.common.di.scope.ViewModelKey;
import cn.qingchengfit.saasbase.common.mvvm.ViewModelFactory;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import javax.inject.Singleton;

@Module public abstract class PageModelModule {
  @Binds abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);

  @Binds @IntoMap @ViewModelKey(CheckoutHomeViewModel.class)
  abstract ViewModel bindCheckoutHomeViewModel(CheckoutHomeViewModel model);

  @Singleton @Provides public static CheckoutRouterCenter provideRouterCenter() {
    return new CheckoutRouterCenter().registe(new checkoutImpl());
  }

  @Binds @IntoMap @ViewModelKey(CheckoutMoneyViewModel.class)
  abstract ViewModel bindCheckoutMoneyViewModel(CheckoutMoneyViewModel model);

  @Binds @IntoMap @ViewModelKey(CheckoutPayViewModel.class)
  abstract ViewModel bindCheckoutPayViewModel(CheckoutPayViewModel model);
}
