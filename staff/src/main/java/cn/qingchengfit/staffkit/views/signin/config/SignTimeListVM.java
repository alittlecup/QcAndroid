package cn.qingchengfit.staffkit.views.signin.config;

import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.views.signin.bean.SignInTimeFrameBean;
import cn.qingchengfit.utils.ToastUtils;
import java.util.List;
import javax.inject.Inject;

public class SignTimeListVM extends BaseViewModel {

  @Inject GymWrapper gymWrapper;
  @Inject LoginStatus loginStatus;
  @Inject StaffRespository staffRespository;
  public MutableLiveData<List<SignInTimeFrameBean>> timeFrames = new MutableLiveData<>();

  @Inject public SignTimeListVM() {

  }

  public void loadTimeFrameList() {
    staffRespository.getStaffAllApi()
        .qcGetCheckInTimeFrames(loginStatus.staff_id(), gymWrapper.getParams())
        .compose(RxHelper.schedulersTransformer())
        .subscribe(timeFrameResponse -> {
          if (ResponseConstant.checkSuccess(timeFrameResponse)) {
            List<SignInTimeFrameBean> timeFrameBeans = timeFrameResponse.getData().timeFrameBeans;
            timeFrames.setValue(timeFrameBeans);
          } else {
            ToastUtils.show("网络请求异常");
          }
        }, throwable -> {
          ToastUtils.show("网络请求异常");
        });
  }
}
