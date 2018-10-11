package cn.qingchengfit.checkout;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.util.Log;
import cn.qingchengfit.checkout.routers.CheckoutRouterCenter;
import cn.qingchengfit.checkout.view.checkout.CheckoutMoneyPage;
import cn.qingchengfit.checkout.view.home.CheckoutHomePage;
import cn.qingchengfit.checkout.view.pay.CheckoutPayPage;
import cn.qingchengfit.saascommon.SaasCommonActivity;
import com.anbillon.flabellum.annotations.Trunk;
import javax.inject.Inject;

@Trunk(fragments = {
    CheckoutHomePage.class, CheckoutMoneyPage.class, CheckoutPayPage.class
}) public class CheckoutCounterActivity extends SaasCommonActivity {
  @Inject CheckoutRouterCenter routerCenter;

  @Override public String getModuleName() {
    return "checkout";
  }

  @Override protected Fragment getRouterFragment(Intent intent) {
    return routerCenter.getFragment(intent.getData(), intent.getExtras());
  }

  @Override protected void onDestroy() {
    super.onDestroy();
    Log.d("TAG", "onDestroy: CheckoutCounterActivity"+this);
  }
}
