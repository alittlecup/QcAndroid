package cn.qingchengfit.staffkit.dianping.pages;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.dianping.vo.DianPingShop;
import cn.qingchengfit.utils.ToastUtils;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

@SuppressLint("CheckResult") public class DianPingAccountViewModel extends BaseViewModel {
  public final MutableLiveData<DianPingShop> gymInfo = new MutableLiveData<>();
  public final MutableLiveData<Boolean> dianPingAccountResult = new MutableLiveData<>();
  @Inject StaffRespository staffRespository;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;

  @Inject public DianPingAccountViewModel() {

  }

  public void loadGymInfo() {
    staffRespository.getStaffAllApi()
        .qcGetGymInfo(loginStatus.staff_id(), gymWrapper.getParams())
        .compose(RxHelper.schedulersTransformerFlow())
        .subscribe(response -> {
          if (response.status == 200) {
            gymInfo.setValue(response.getData().shop);
          } else {
            ToastUtils.show(response.getMsg());
          }
        }, throwable -> ToastUtils.show(throwable.getMessage()));
  }

  public void putGymInfo(DianPingShop gym,String barCode) {
    if (checkGymInfo(gym)) {
      staffRespository.getStaffAllApi()
          .qcPutGymInfo(loginStatus.staff_id(), gym, gymWrapper.getParams())
          .compose(RxHelper.schedulersTransformerFlow())
          .subscribe(response -> {
            if (response.status == 200) {
              postDianPingAccount(gym.getId(), barCode);
            } else {
              ToastUtils.show(response.getMsg());
              dianPingAccountResult.setValue(false);
            }
          }, throwable -> {
            ToastUtils.show(throwable.getMessage());
            dianPingAccountResult.setValue(false);
          });
    }
  }

  private void postDianPingAccount(String gymId, String barCode) {
    Map<String, Object> params = new HashMap<>(2);
    params.put("gym_id", gymId);
    params.put("qrcode", barCode);
    staffRespository.getStaffAllApi()
        .qcPostDianPingAccount(gymId, params)
        .compose(RxHelper.schedulersTransformerFlow())
        .subscribe(response -> {
          if (response.status == 200) {
            dianPingAccountResult.setValue(true);
          } else {
            ToastUtils.show(response.getMsg());
            dianPingAccountResult.setValue(false);
          }
        }, throwable -> {
          ToastUtils.show(throwable.getMessage());
          dianPingAccountResult.setValue(false);
        });
  }

  private boolean checkGymInfo(DianPingShop gym) {
    return true;
  }
}
