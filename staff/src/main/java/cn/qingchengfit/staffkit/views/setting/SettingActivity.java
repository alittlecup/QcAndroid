package cn.qingchengfit.staffkit.views.setting;

import android.content.Intent;
import android.support.v4.app.Fragment;
import cn.qingchengfit.saasbase.SaasContainerActivity;
import cn.qingchengfit.staff.routers.StaffRouterCenter;
import cn.qingchengfit.staffkit.views.main.SettingDetailFragment;
import com.anbillon.flabellum.annotations.Trunk;
import javax.inject.Inject;

@Trunk(fragments = { SettingDetailFragment.class }) public class SettingActivity
    extends SaasContainerActivity {

  @Override public String getModuleName() {
    return "setting";
  }
  @Inject StaffRouterCenter staffRouterCenter;

  @Override protected Fragment getRouterFragment(Intent intent) {
    return staffRouterCenter.getFragment(intent.getData(), intent.getExtras());
  }
}
