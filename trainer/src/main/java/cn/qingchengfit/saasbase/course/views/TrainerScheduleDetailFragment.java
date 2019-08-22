package cn.qingchengfit.saasbase.course.views;

import android.view.View;
import cn.qingchengfit.bean.Permission;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.PermissionServerUtils;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.saasbase.course.detail.ScheduleDetail;
import cn.qingchengfit.saasbase.course.detail.ScheduleDetailFragment;
import com.qingchengfit.fitcoach.R;
import com.qingchengfit.fitcoach.Utils.ToastUtils;
import com.qingchengfit.fitcoach.http.TrainerRepository;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class TrainerScheduleDetailFragment extends ScheduleDetailFragment {
  @Inject LoginStatus loginStatus;
  @Inject TrainerRepository repository;
  @Inject GymWrapper gymWrapper;

  @Override public void routeAddOrder(View view) {
    Map<String, Object> params = new HashMap<>();
    if(service==null){
      params.putAll(gymWrapper.getParams());
    }else{
      params.put("id", service.getId() + "");
      params.put("model", service.getModel());
    }
    repository.getTrainerAllApi()
        .qcGetPermission(loginStatus.staff_id(), params)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(response -> {
          if (ResponseConstant.checkSuccess(response)) {
            if (checkPermission(response.data.permissions)) {
              super.routeAddOrder(view);
            }else{
              showAlert(getString(R.string.alert_permission_forbid));
            }
          } else {
            ToastUtils.show("权限更新失败");
          }
        }, new NetWorkThrowable());
  }

  private boolean checkPermission(List<Permission> permissions) {
    ScheduleDetail scheduleDetail = mViewModel.detail.getValue();
    boolean trainerClass = scheduleDetail.isTrainerClass();
    boolean groupClassPermission = false, privateClassPermission = false;
    if (permissions != null && !permissions.isEmpty()) {
      for (Permission permission : permissions) {
        if (permission.key.equals(PermissionServerUtils.GROUP_ORDER_CAN_WRITE)) {
          groupClassPermission = permission.value;
        } else if (permission.key.equals(PermissionServerUtils.PRIVATE_ORDER_CAN_WRITE)) {
          privateClassPermission = permission.value;
        }
      }
    }
    if ((trainerClass && privateClassPermission) || (!trainerClass && groupClassPermission)) {
      return true;
    }
    return false;
  }
}
