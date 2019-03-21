package cn.qingchengfit.gym;

import android.content.Intent;
import android.support.v4.app.Fragment;
import cn.qingchengfit.gym.gymconfig.views.AddNewSiteFragment;
import cn.qingchengfit.gym.gymconfig.views.MsgNotiFragment;
import cn.qingchengfit.gym.gymconfig.views.OrderLimitFragment;
import cn.qingchengfit.gym.gymconfig.views.SiteFragment;
import cn.qingchengfit.gym.gymconfig.views.SiteSelectedFragment;
import cn.qingchengfit.gym.gymconfig.views.UpgradeDoneFragment;
import cn.qingchengfit.gym.pages.apply.GymApplyDealFinishPage;
import cn.qingchengfit.gym.pages.apply.GymApplyDealPage;
import cn.qingchengfit.gym.pages.apply.GymApplyPage;
import cn.qingchengfit.gym.pages.brand.ChangeGymPage;
import cn.qingchengfit.gym.pages.brand.GymBrandPage;
import cn.qingchengfit.gym.pages.create.GymBrandCreatePage;
import cn.qingchengfit.gym.pages.create.GymCreateChoosePage;
import cn.qingchengfit.gym.pages.create.GymCreatePage;
import cn.qingchengfit.gym.pages.gym.GymEditPage;
import cn.qingchengfit.gym.pages.gym.GymInfoPage;
import cn.qingchengfit.gym.pages.gym.GymSimpleListPage;
import cn.qingchengfit.gym.pages.my.MyGymsPage;
import cn.qingchengfit.gym.pages.search.GymSearchPage;
import cn.qingchengfit.gym.routers.GymRouterCenter;
import cn.qingchengfit.saascommon.SaasCommonActivity;
import com.anbillon.flabellum.annotations.Trunk;
import javax.inject.Inject;

@Trunk(fragments = {
    MyGymsPage.class, GymBrandPage.class, GymEditPage.class, GymInfoPage.class, GymCreatePage.class,
    GymCreateChoosePage.class, GymBrandCreatePage.class, GymSearchPage.class,
    GymSimpleListPage.class, ChangeGymPage.class, GymApplyDealPage.class, GymApplyPage.class,
    SiteFragment.class, SiteSelectedFragment.class, OrderLimitFragment.class, MsgNotiFragment.class,
    AddNewSiteFragment.class, UpgradeDoneFragment.class, GymApplyDealFinishPage.class
}) public class GymActivity extends SaasCommonActivity {
  @Inject GymRouterCenter routerCenter;

  @Override protected Fragment getRouterFragment(Intent intent) {
    return routerCenter.getFragment(intent.getData(), intent.getExtras());
  }

  @Override public String getModuleName() {
    return "gym";
  }
}
