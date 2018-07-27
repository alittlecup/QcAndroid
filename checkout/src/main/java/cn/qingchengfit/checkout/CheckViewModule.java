package cn.qingchengfit.checkout;

import android.arch.lifecycle.ViewModel;
import cn.qingchengfit.checkout.component.CheckoutComponent;
import cn.qingchengfit.checkout.di.BindCheckoutCounterActivity;
import cn.qingchengfit.checkout.repository.CheckoutModel;
import cn.qingchengfit.checkout.repository.CheckoutRepository;
import cn.qingchengfit.checkout.repository.CheckoutRepositoryImpl;
import cn.qingchengfit.checkout.repository.ICheckoutModel;
import cn.qingchengfit.checkout.routers.CheckoutRouterCenter;
import cn.qingchengfit.checkout.routers.checkoutImpl;
import cn.qingchengfit.checkout.view.checkout.CheckoutMoneyViewModel;
import cn.qingchengfit.checkout.view.home.CheckoutHomeViewModel;
import cn.qingchengfit.checkout.view.pay.CheckoutPayViewModel;
import cn.qingchengfit.checkout.view.scan.QcScanActivity;
import cn.qingchengfit.checkout.view.scan.QcScanActivityModel;
import cn.qingchengfit.saascommon.di.ViewModelKey;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;
import javax.inject.Singleton;

@Module public abstract class CheckViewModule {
  static CheckoutRouterCenter routerCenter = new CheckoutRouterCenter().registe(new checkoutImpl());

  @Binds @IntoMap @ViewModelKey(CheckoutHomeViewModel.class)
  abstract ViewModel bindCheckoutHomeViewModel(CheckoutHomeViewModel model);

  @Provides public static CheckoutRouterCenter provideRouterCenter() {
    return routerCenter;
  }

  @Binds @IntoMap @ViewModelKey(CheckoutMoneyViewModel.class)
  abstract ViewModel bindCheckoutMoneyViewModel(CheckoutMoneyViewModel model);

  @Binds @IntoMap @ViewModelKey(CheckoutPayViewModel.class)
  abstract ViewModel bindCheckoutPayViewModel(CheckoutPayViewModel model);

  @Binds @IntoMap @ViewModelKey(QcScanActivityModel.class)
  abstract ViewModel bindQcScanActivityModel(QcScanActivityModel model);

  @Binds abstract CheckoutRepository bindCheckRepository(CheckoutRepositoryImpl checkoutRepository);

  @Singleton @ContributesAndroidInjector abstract QcScanActivity contributeQcScanActivityInjector();


}
