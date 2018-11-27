package cn.qingchengfit.staffkit.dianping.pages;

import android.annotation.SuppressLint;
import android.arch.lifecycle.MutableLiveData;
import android.text.TextUtils;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.staffkit.constant.StaffRespository;
import cn.qingchengfit.staffkit.dianping.vo.DianPingShop;
import cn.qingchengfit.staffkit.dianping.vo.GymFacility;
import cn.qingchengfit.staffkit.dianping.vo.GymTag;
import cn.qingchengfit.staffkit.dianping.vo.ISimpleChooseData;
import cn.qingchengfit.utils.ToastUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

@SuppressLint("CheckResult") public class DianPingAccountViewModel extends BaseViewModel {
  public final MutableLiveData<DianPingShop> gymInfo = new MutableLiveData<>();
  public final MutableLiveData<Boolean> dianPingAccountResult = new MutableLiveData<>();
  public final MutableLiveData<List<ISimpleChooseData>> tags = new MutableLiveData<>();
  public final MutableLiveData<List<ISimpleChooseData>> facilities = new MutableLiveData<>();
  public final MutableLiveData<String> address = new MutableLiveData<>();
  @Inject StaffRespository staffRespository;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;

  @Inject public DianPingAccountViewModel() {

  }

  public void loadGymInfo() {
    staffRespository.getStaffAllApi()
        .qcGetGymInfo(loginStatus.staff_id(), gymWrapper.getParams())
        .compose(RxHelper.schedulersTransformer())
        .subscribe(response -> {
          if (response.status == 200) {
            gymInfo.setValue(response.getData().shop);
          } else {
            ToastUtils.show(response.getMsg());
          }
        }, throwable -> ToastUtils.show(throwable.getMessage()));
  }

  public void putGymInfo(DianPingShop gym, String barCode) {
    if (checkGymInfo(gym)) {
      Map<String, Object> bodys = new HashMap<>();
      bodys.putAll(gymWrapper.getParams());
      bodys.put("name", gym.getName());
      bodys.put("address", gym.getAddress());
      bodys.put("gd_district_id", gym.getDistrict_id());
      bodys.put("gd_lat", gym.getGd_lat());
      bodys.put("gd_lng", gym.getGd_lng());
      bodys.put("contact", gym.getPhone());
      bodys.put("area", gym.getArea());
      List<Integer> tags = new ArrayList<>();
      List<Integer> shop_servies = new ArrayList<>();
      List<GymTag> tags1 = gym.getTags();
      if (tags1 != null && !tags1.isEmpty()) {
        for (GymTag tag : tags1) {
          tags.add(tag.getId());
        }
      }
      List<GymFacility> facilities = gym.getShop_services();
      if (facilities != null && !facilities.isEmpty()) {
        for (GymFacility facility : facilities) {
          shop_servies.add(facility.getId());
        }
      }
      bodys.put("tags", tags);
      bodys.put("shop_servies", shop_servies);
      staffRespository.getStaffAllApi()
          .qcPutGymInfo(loginStatus.staff_id(), bodys, gymWrapper.getParams())
          .compose(RxHelper.schedulersTransformer())
          .subscribe(response -> {
            if (response.status == 200) {
              postDianPingAccount(String.valueOf(gym.getGym_id()), barCode);
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

  public void postDianPingAccount(String gymId, String barCode) {
    Map<String, Object> params = new HashMap<>(2);
    params.put("qrcode", barCode);
    params.putAll(gymWrapper.getParams());
    staffRespository.getStaffAllApi()
        .qcPostDianPingAccount(gymId, params)
        .compose(RxHelper.schedulersTransformer())
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
    if (TextUtils.isEmpty(gym.getName())) {
      ToastUtils.show("请填写场馆名称");
      return false;
    }
    if (TextUtils.isEmpty(gym.getPhone())) {
      ToastUtils.show("请填写场馆联系方式");
      return false;
    }
    return true;
  }
}
