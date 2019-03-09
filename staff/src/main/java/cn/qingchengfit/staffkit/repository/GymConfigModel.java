package cn.qingchengfit.staffkit.repository;

import cn.qingchengfit.saasbase.apis.GymConfigApi;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingcheng.gym.gymconfig.IGymConfigModel;
import cn.qingcheng.gym.gymconfig.network.response.ShopConfigBody;
import cn.qingcheng.gym.gymconfig.network.response.ShopConfigListWrap;
import cn.qingcheng.gym.gymconfig.network.response.SpaceListWrap;
import cn.qingcheng.gym.bean.GymList;
import cn.qingchengfit.staffkit.constant.PermissionServerUtils;
import java.util.HashMap;
import rx.Observable;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/12/1.
 */

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
    params.put("key", PermissionServerUtils.STUDIO_LIST);
    params.put("method", "get");
    return api.qcGetGymSitesPermisson(loginStatus.staff_id(), params);
  }

  @Override public Observable<QcDataResponse<ShopConfigListWrap>> getConfigs(String configs) {
    return api.qcGetShopConfig(loginStatus.staff_id(), configs, gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse> saveShopConfigs(ShopConfigBody body) {
    return api.saveShopConfigs(loginStatus.staff_id(), gymWrapper.getParams(), body);
  }

  @Override public Observable<QcDataResponse<GymList>> qcGetCoachService(String brand_id) {
    return api.qcGetCoachService(loginStatus.staff_id(),brand_id);
  }
}
