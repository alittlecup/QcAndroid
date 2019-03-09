package cn.qingchengfit.model;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.api.GymConfigApi;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingcheng.gym.gymconfig.IGymConfigModel;
import cn.qingcheng.gym.gymconfig.network.response.ShopConfigBody;
import cn.qingcheng.gym.gymconfig.network.response.ShopConfigListWrap;
import cn.qingcheng.gym.gymconfig.network.response.SpaceListWrap;
import cn.qingcheng.gym.bean.GymList;
import java.util.HashMap;
import rx.Observable;

public class GymConfigModel implements IGymConfigModel {

  GymWrapper gymWrapper;
  LoginStatus loginStatus;
  GymConfigApi api;

  public GymConfigModel(GymWrapper gymWrapper, LoginStatus loginStatus,
    QcRestRepository qcRestRepository) {
    this.gymWrapper = gymWrapper;
    this.loginStatus = loginStatus;
    api = qcRestRepository.createRxJava1Api(GymConfigApi.class);
  }

  @Override public Observable<QcDataResponse<SpaceListWrap>> getSites() {
    HashMap<String, Object> params = gymWrapper.getParams();
    return api.qcGetGymSitesPermisson(loginStatus.staff_id(), params);
  }

  @Deprecated
  @Override public Observable<QcDataResponse<ShopConfigListWrap>> getConfigs(String configs) {
    return null;
  }

  @Deprecated
  @Override public Observable<QcDataResponse> saveShopConfigs(ShopConfigBody body) {
    return null;
  }

  @Override public Observable<QcDataResponse<GymList>> qcGetCoachService(String brand_id) {
    return api.qcGetCoachService(loginStatus.staff_id());
  }
}
