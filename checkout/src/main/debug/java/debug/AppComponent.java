package debug;

import cn.qingchengfit.checkout.di.BindCheckoutCounterActivity;
import dagger.Component;
import dagger.android.AndroidInjector;


@Component(modules = {
    BindCheckoutCounterActivity.class,AppModel.class
}) public interface AppComponent extends AndroidInjector<MyApp> {
}
