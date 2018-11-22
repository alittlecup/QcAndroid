package cn.qingchengfit.staffkit.dianping.pages;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.dianping.vo.ISimpleChooseData;
import cn.qingchengfit.utils.ToastUtils;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

@SuppressLint("CheckResult") public class DianPingChooseViewModel extends BaseViewModel {
  public final MutableLiveData<List<ISimpleChooseData>> chooseDatas = new MutableLiveData<>();
  @Inject StaffRespository respository;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;

  @Inject public DianPingChooseViewModel() {
  }

  public void loadGymFacilities() {
    respository.getStaffAllApi()
        .qcGetGymFacilities(loginStatus.staff_id(), gymWrapper.getParams())
        .compose(RxHelper.schedulersTransformerFlow())
        .subscribe(response -> {
          if (response.status == 200) {
            chooseDatas.setValue(new ArrayList<>(response.getData().shop_services));
          }
        }, throwable -> ToastUtils.show(throwable.getMessage()));
  }

  public void loadGymTags() {
    respository.getStaffAllApi()
        .qcGetGymTags(loginStatus.staff_id(), gymWrapper.getParams())
        .compose(RxHelper.schedulersTransformerFlow())
        .subscribe(response -> {
          if (response.status == 200) {
            //chooseDatas.setValue();
          } else {
            ToastUtils.show(response.getMsg());
          }
        }, throwable -> ToastUtils.show(throwable.getMessage()));
  }
}
