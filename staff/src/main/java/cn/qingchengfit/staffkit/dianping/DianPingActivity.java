package cn.qingchengfit.staffkit.dianping;

import android.content.Intent;
import android.support.v4.app.Fragment;
import cn.qingchengfit.saasbase.common.views.SaasCommActivity;
import cn.qingchengfit.staff.routers.StaffRouterCenter;
import cn.qingchengfit.staffkit.dianping.pages.DianPingAccountPage;
import cn.qingchengfit.staffkit.dianping.pages.DianPingAccountSuccessPage;
import cn.qingchengfit.staffkit.dianping.pages.DianPingChoosePage;
import cn.qingchengfit.staffkit.dianping.pages.DianPingScanPage;
import com.anbillon.flabellum.annotations.Trunk;
import javax.inject.Inject;

@Trunk(fragments = {
    DianPingScanPage.class, DianPingAccountPage.class, DianPingChoosePage.class,
    DianPingAccountSuccessPage.class
}) public class DianPingActivity extends SaasCommActivity {
  @Inject StaffRouterCenter staffRouterCenter;

  @Override protected Fragment getRouterFragment(Intent intent) {
    return staffRouterCenter.getFragment(intent.getData(), intent.getExtras());
  }

  @Override public String getModuleName() {
    return "dianping";
  }
}
