package cn.qingchengfit.staffkit.gym;

import cn.qingchengfit.saasbase.SaasContainerActivity;
import cn.qingchengfit.saasbase.gymconfig.views.SiteFragment;
import cn.qingchengfit.saasbase.gymconfig.views.SiteSelectedFragment;
import com.anbillon.flabellum.annotations.Trunk;

@Trunk(fragments = {

  SiteFragment.class, SiteSelectedFragment.class,
})
public class GymConfigAcitivty extends SaasContainerActivity {
  @Override public String getModuleName() {
    return "gym";
  }
}