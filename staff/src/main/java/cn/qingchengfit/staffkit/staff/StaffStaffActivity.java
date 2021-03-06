package cn.qingchengfit.staffkit.staff;

import android.content.Intent;
import android.support.v4.app.Fragment;
import cn.qingchengfit.saasbase.SaasContainerActivity;
import cn.qingchengfit.saasbase.coach.views.CoachListFragment;
import cn.qingchengfit.saasbase.coach.views.TrainerChooseFragment;
import cn.qingchengfit.saasbase.partner.PartnerAccountPage;
import cn.qingchengfit.saasbase.staff.views.ChooseSalerFragment;
import cn.qingchengfit.saasbase.staff.views.InviteLinkFragment;
import cn.qingchengfit.saasbase.staff.views.InviteQrCodeFragment;
import cn.qingchengfit.saasbase.staff.views.InviteSendMsgFragment;
import cn.qingchengfit.saasbase.staff.views.NewSalersListFragment;
import cn.qingchengfit.saasbase.staff.views.StaffAddFragment;
import cn.qingchengfit.saasbase.staff.views.StaffDetailFragment;
import cn.qingchengfit.saasbase.staff.views.StaffHomeFragment;
import cn.qingchengfit.saasbase.staff.views.StaffLeaveDetailFragment;
import cn.qingchengfit.saasbase.staff.views.StaffReInviteFragment;
import cn.qingchengfit.saasbase.staff.views.StaffTabInviteListFragment;
import cn.qingchengfit.saasbase.staff.views.StaffTabLeaveListFragment;
import cn.qingchengfit.saasbase.staff.views.StaffTabListFragment;
import cn.qingchengfit.saasbase.staff.views.SuFragment;
import cn.qingchengfit.saasbase.staff.views.SuIdendifyFragment;
import cn.qingchengfit.saasbase.staff.views.SuNewFragment;
import cn.qingchengfit.saasbase.staff.views.TrainerAddFragment;
import cn.qingchengfit.saasbase.staff.views.TrainerDetailFragment;
import cn.qingchengfit.saasbase.staff.views.TrainerHomeFragment;
import cn.qingchengfit.saasbase.staff.views.TrainerLeaveDetailFragment;
import cn.qingchengfit.saasbase.staff.views.TrainerTabInviteListFragment;
import cn.qingchengfit.saasbase.staff.views.TrainerTabLeaveListFragment;
import cn.qingchengfit.saasbase.staff.views.TrainerTabListFragment;
import cn.qingchengfit.saasbase.turnovers.TurnoverOrderDetailFragment;
import cn.qingchengfit.saasbase.turnovers.TurnoversHomePage;
import cn.qingchengfit.staff.routers.StaffRouterCenter;
import cn.qingchengfit.staffkit.views.GymServiceSettingFragment;
import cn.qingchengfit.staffkit.views.GymServiceSettingSuccessFragment;
import com.anbillon.flabellum.annotations.Trunk;
import javax.inject.Inject;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/12/1.
 */
@Trunk(fragments = {
    ChooseSalerFragment.class, NewSalersListFragment.class, TrainerChooseFragment.class,
    CoachListFragment.class, StaffDetailFragment.class, StaffHomeFragment.class,
    StaffAddFragment.class, StaffReInviteFragment.class, StaffTabListFragment.class,
    StaffTabInviteListFragment.class, StaffTabLeaveListFragment.class, InviteSendMsgFragment.class,
    InviteQrCodeFragment.class, InviteLinkFragment.class, TrainerHomeFragment.class,
    TrainerTabInviteListFragment.class, TrainerTabLeaveListFragment.class,
    TrainerTabListFragment.class, TrainerAddFragment.class, SuFragment.class,
    SuIdendifyFragment.class, SuNewFragment.class, TrainerDetailFragment.class,
    StaffLeaveDetailFragment.class, TrainerLeaveDetailFragment.class, TurnoversHomePage.class,
    TurnoverOrderDetailFragment.class, PartnerAccountPage.class, GymServiceSettingFragment.class,
    GymServiceSettingSuccessFragment.class,
})

public class StaffStaffActivity extends SaasContainerActivity {
  @Inject StaffRouterCenter staffRouterCenter;

  @Override public String getModuleName() {
    return "staff";
  }

  @Override protected Fragment getRouterFragment(Intent intent) {
    Fragment fragment = staffRouterCenter.getFragment(intent.getData(), intent.getExtras());
    if (fragment instanceof GymServiceSettingFragment
        || (fragment instanceof GymServiceSettingSuccessFragment)) {
      return fragment;
    }
    return super.getRouterFragment(intent);
  }
}
