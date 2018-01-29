package cn.qingchengfit.saasbase.gymconfig;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.GymConfigApi;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.gymconfig.network.response.ShopConfigBody;
import cn.qingchengfit.saasbase.gymconfig.network.response.ShopConfigListWrap;
import cn.qingchengfit.saasbase.gymconfig.network.response.SpaceListWrap;
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
    api = qcRestRepository.createGetApi(GymConfigApi.class);
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
}
