package cn.qingchengfit.staffkit.views.signin.config;

import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.views.signin.bean.SignInCheckInQrCodeBean;
import cn.qingchengfit.utils.ToastUtils;
import java.util.HashMap;
import javax.inject.Inject;

public class SignInMemberVM extends BaseViewModel {
  @Inject LoginStatus status;
  @Inject GymWrapper gymWrapper;
  @Inject StaffRespository staffRespository;
  public MutableLiveData<SignInCheckInQrCodeBean.Data> data = new MutableLiveData<>();

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
          ToastUtils.show(throwable.getMessage());
        });
  }
}
