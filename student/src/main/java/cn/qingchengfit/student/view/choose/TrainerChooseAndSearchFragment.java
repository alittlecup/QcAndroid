package cn.qingchengfit.student.view.choose;

import android.content.Intent;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.router.QCResult;
import cn.qingchengfit.router.qc.IQcRouteCallback;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.saascommon.permission.IPermissionModel;
import cn.qingchengfit.student.R;
import javax.inject.Inject;

public class TrainerChooseAndSearchFragment extends ChooseAndSearchStudentFragment {
  @Inject IPermissionModel permissionModel;

  @Override public void onBtnAddStudentClicked() {
    if (!permissionModel.check(PermissionServerUtils.PERSONAL_MANAGE_MEMBERS_CAN_WRITE)) {
      showAlert(R.string.alert_permission_forbid);
      return;
    }
    onAddstudent();//手动添加学员
  }

  //新增学员
  public void onAddstudent() {
    QcRouteUtil.setRouteOptions(new RouteOptions("trainer").setActionName("/add/student")).callAsync(
        new IQcRouteCallback() {
          @Override public void onResult(QCResult qcResult) {
            if(qcResult.isSuccess()){
              onRefresh();
            }
          }
        });
  }
}
