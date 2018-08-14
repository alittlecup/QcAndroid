package debug;

import cn.qingchengfit.login.LoginDepModel;
import cn.qingchengfit.login.di.BindLoginActivity;
import cn.qingchengfit.shop.di.BindShopActivity;
import cn.qingchengfit.shop.di.ShopViewModel;
import dagger.Component;
import dagger.android.AndroidInjectionModule;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjection;
import dagger.android.support.AndroidSupportInjectionModule;
import javax.inject.Singleton;

@Component(modules = {
    AppModel.class, BindShopActivity.class, ShopViewModel.class,
    AndroidInjectionModule.class,
    AndroidSupportInjectionModule.class,

    LoginDepModel.class, BindLoginActivity.class,
}) public interface AppComponent extends AndroidInjector<MyApp> {

}
