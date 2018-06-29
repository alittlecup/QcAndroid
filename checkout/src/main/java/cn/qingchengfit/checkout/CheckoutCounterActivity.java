package cn.qingchengfit.checkout;

import android.content.Intent;
import android.support.v4.app.Fragment;
import cn.qingchengfit.checkout.routers.CheckoutRouterCenter;
import cn.qingchengfit.checkout.routers.checkoutImpl;
import cn.qingchengfit.checkout.view.CheckoutHomePage;
import cn.qingchengfit.saasbase.SaasContainerActivity;
import com.anbillon.flabellum.annotations.Trunk;

@Trunk(fragments = {
    PageModelModule.class, CheckoutHomePage.class,
}) public class CheckoutCounterActivity extends SaasContainerActivity {
  @Override public String getModuleName() {
    return "checkout";
  }

  @Override protected Fragment getRouterFragment(Intent intent) {
    return new CheckoutRouterCenter().registe(new checkoutImpl()).getFragment(intent.getData(), intent.getExtras());
  }
}
