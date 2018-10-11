package cn.qingchengfit.saasbase.staff.common;

import android.content.Intent;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.saasbase.permission.SerPermisAction;
import cn.qingchengfit.saascommon.qrcode.views.QRActivity;
import cn.qingchengfit.views.fragments.BaseFragment;
import javax.inject.Inject;

/**
 * Created by fb on 2017/8/21.
 */

public class StaffConstant {

  public static final String PERMISSION_STAFF = "/position/setting";
  @Inject SerPermisAction serPermisAction;

  @Inject
  public StaffConstant(){
  }

  public void goQrScan(final BaseFragment context, final String toUrl, String Permission, final CoachService mCoachService) {
    if (serPermisAction.check(mCoachService.getId(), mCoachService.getModel(), Permission) || Permission == null) {

      Intent toScan = new Intent(context.getActivity(), QRActivity.class);
      toScan.putExtra(QRActivity.LINK_URL,
          Configs.Server + "app2web/?id=" + mCoachService.getId() + "&model=" + mCoachService.getModel() + "&module=" + toUrl);
      context.startActivity(toScan);
    } else {
      context.showAlert(R.string.alert_permission_forbid);
    }
  }

}
