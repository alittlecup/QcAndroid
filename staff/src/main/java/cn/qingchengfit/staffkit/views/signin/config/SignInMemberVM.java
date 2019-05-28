package cn.qingchengfit.staffkit.views.signin.config;

import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.body.SignInManualBody;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.views.signin.bean.SignInCheckInQrCodeBean;
import cn.qingchengfit.staffkit.views.signin.bean.UserCheckInOrder;
import cn.qingchengfit.utils.ToastUtils;
import java.util.HashMap;
import javax.inject.Inject;
import timber.log.Timber;

public class SignInMemberVM extends BaseViewModel {
  @Inject LoginStatus status;
  @Inject GymWrapper gymWrapper;
  @Inject StaffRespository staffRespository;
  public MutableLiveData<SignInCheckInQrCodeBean.Data> data = new MutableLiveData<>();
  public MutableLiveData<Boolean> checkInResult = new MutableLiveData<>();
  public MutableLiveData<UserCheckInOrder> orders = new MutableLiveData<>();

  @Inject public SignInMemberVM() {

  }

  public void loadMemberInfo(String qrcode) {
    HashMap<String, Object> params = gymWrapper.getParams();
    params.put("qrcode", qrcode);
    staffRespository.getStaffAllApi()
        .qcGetCheckinInfoByQrCode(status.staff_id(), params)
        .compose(RxHelper.schedulersTransformer())
        .subscribe(response -> {
          if (ResponseConstant.checkSuccess(response)) {
            data.setValue(response.getData().data);
          } else {
            ToastUtils.show(response.getMsg());
          }
        }, throwable -> {
          ToastUtils.show("获取二维码信息失败，请重试");
        });
  }

  public void checkIn(String userId, String cardId, String locakid) {
    SignInManualBody body = new SignInManualBody.Builder().user_id(Integer.valueOf(userId))
        .card_id(cardId)
        .locker_id(locakid)
        .build();
    staffRespository.getStaffAllApi()
        .qcPostCheckInMaual(App.staffId, gymWrapper.getParams(), body)
        .compose(RxHelper.schedulersTransformer())
        .subscribe(qcResponse -> {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            checkInResult.setValue(true);
          } else {
            checkInResult.setValue(false);
            ToastUtils.show(qcResponse.msg);
          }
        }, throwable -> Timber.e(throwable.getMessage()));
  }

  public void loadCheckInOrders(String userId) {
    HashMap<String, Object> params = gymWrapper.getParams();
    params.put("user_id", userId);
    staffRespository.getStaffAllApi()
        .qcGetUserCheckinOrders(status.staff_id(), params)
        .compose(RxHelper.schedulersTransformer())
        .subscribe(response -> {
          if (ResponseConstant.checkSuccess(response)) {
            this.orders.setValue(response.data.order);
          } else {
            ToastUtils.show(response.msg);
          }
        }, throwable -> {
          ToastUtils.show(throwable.getMessage());
        });
  }
}
