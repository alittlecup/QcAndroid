package cn.qingchengfit.saasbase.gymconfig;

import cn.qingchengfit.saasbase.SaasContainerActivity;
import cn.qingchengfit.saasbase.gymconfig.views.AddNewSiteFragment;
import cn.qingchengfit.saasbase.gymconfig.views.MsgNotiFragment;
import cn.qingchengfit.saasbase.gymconfig.views.OrderLimitFragment;
import cn.qingchengfit.saasbase.gymconfig.views.SiteFragment;
import cn.qingchengfit.saasbase.gymconfig.views.SiteSelectedFragment;
import com.anbillon.flabellum.annotations.Trunk;

@Trunk(fragments = {
  //GymConfigModule.class,
  SiteFragment.class, SiteSelectedFragment.class,OrderLimitFragment.class, MsgNotiFragment.class,
  AddNewSiteFragment.class,
})
public class GymConfigAcitivty extends SaasContainerActivity {
  @Override public String getModuleName() {
    return "gym";
  }
}