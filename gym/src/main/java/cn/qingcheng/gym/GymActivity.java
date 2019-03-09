package cn.qingcheng.gym;

import android.content.Intent;
import android.support.v4.app.Fragment;
import cn.qingcheng.gym.gymconfig.views.AddNewSiteFragment;
import cn.qingcheng.gym.gymconfig.views.MsgNotiFragment;
import cn.qingcheng.gym.gymconfig.views.OrderLimitFragment;
import cn.qingcheng.gym.gymconfig.views.SiteFragment;
import cn.qingcheng.gym.gymconfig.views.SiteSelectedFragment;
import cn.qingcheng.gym.gymconfig.views.UpgradeDoneFragment;
import cn.qingcheng.gym.pages.apply.GymApplyDealPage;
import cn.qingcheng.gym.pages.apply.GymApplyPage;
import cn.qingcheng.gym.pages.brand.ChangeGymPage;
import cn.qingcheng.gym.pages.brand.GymBrandPage;
import cn.qingcheng.gym.pages.create.GymBrandCreatePage;
import cn.qingcheng.gym.pages.create.GymCreateChoosePage;
import cn.qingcheng.gym.pages.create.GymCreatePage;
import cn.qingcheng.gym.pages.gym.GymEditPage;
import cn.qingcheng.gym.pages.gym.GymInfoPage;
import cn.qingcheng.gym.pages.gym.GymSimpleListPage;
import cn.qingcheng.gym.pages.my.MyGymsPage;
import cn.qingcheng.gym.pages.search.GymSearchPage;
import cn.qingchengfit.gym.routers.GymRouterCenter;
import cn.qingchengfit.saascommon.SaasCommonActivity;
import com.anbillon.flabellum.annotations.Trunk;
import javax.inject.Inject;

@Trunk(fragments = {
    MyGymsPage.class, GymBrandPage.class, GymEditPage.class, GymInfoPage.class, GymCreatePage.class,
    GymCreateChoosePage.class, GymBrandCreatePage.class, GymSearchPage.class,
    GymSimpleListPage.class, ChangeGymPage.class, GymApplyDealPage.class, GymApplyPage.class,
    SiteFragment.class, SiteSelectedFragment.class, OrderLimitFragment.class, MsgNotiFragment.class,
    AddNewSiteFragment.class, UpgradeDoneFragment.class
}) public class GymActivity extends SaasCommonActivity {
  @Inject GymRouterCenter routerCenter;

  @Override protected Fragment getRouterFragment(Intent intent) {
    return routerCenter.getFragment(intent.getData(), intent.getExtras());
  }

  @Override public String getModuleName() {
    return "gym";
  }
}
