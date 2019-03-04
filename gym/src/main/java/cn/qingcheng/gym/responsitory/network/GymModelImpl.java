package cn.qingcheng.gym.responsitory.network;

import cn.qingcheng.gym.bean.BrandsResponse;
import cn.qingcheng.gym.bean.GymTypeData;
import cn.qingcheng.gym.bean.ShopsResponse;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import javax.inject.Inject;
import rx.Observable;

public class GymModelImpl implements IGymModel {
  @Inject GymWrapper gymWrapper;
  @Inject LoginStatus loginStatus;
  GymApi gymApi;

  @Inject public GymModelImpl(QcRestRepository qcRestRepository) {
    gymApi = qcRestRepository.createRxJava1Api(GymApi.class);
  }


  @Override public Observable<QcDataResponse<BrandsResponse>> qcGetBrands() {
    return gymApi.qcGetBrands(loginStatus.staff_id());
  }

  @Override
  public Observable<QcDataResponse<ShopsResponse>> qcGetBrandAllShops( String brand_id) {
    return gymApi.qcGetBrandAllShops(loginStatus.staff_id(),brand_id);
  }

  @Override public Observable<QcDataResponse<GymTypeData>> qcLoadGymTypes() {
    return gymApi.qcGetGymType();
  }

  @Override public Observable<QcDataResponse> qcDelGym(String id) {
    return gymApi.qcDelGym(id);
  }
}
