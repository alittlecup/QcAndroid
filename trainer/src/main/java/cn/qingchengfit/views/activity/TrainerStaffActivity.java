package cn.qingchengfit.views.activity;

import cn.qingchengfit.saasbase.SaasContainerActivity;
import cn.qingchengfit.saasbase.staff.views.ChooseSalerFragment;
import com.anbillon.flabellum.annotations.Trunk;

@Trunk(fragments = { ChooseSalerFragment.class }) public class TrainerStaffActivity
    extends SaasContainerActivity {
  @Override public String getModuleName() {
    return "staff";
  }
}
