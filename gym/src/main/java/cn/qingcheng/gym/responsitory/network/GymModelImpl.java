package cn.qingcheng.gym.responsitory.network;

import cn.qingcheng.gym.bean.BrandPostBody;
import cn.qingcheng.gym.bean.BrandResponse;
import cn.qingcheng.gym.bean.BrandsResponse;
import cn.qingcheng.gym.bean.BransShopsPremissions;
import cn.qingcheng.gym.bean.GymApplyOrderResponse;
import cn.qingcheng.gym.bean.GymApplyOrderResponses;
import cn.qingcheng.gym.bean.GymPosition;
import cn.qingcheng.gym.bean.GymPositions;
import cn.qingcheng.gym.bean.GymSearchResponse;
import cn.qingcheng.gym.bean.GymTypeData;
import cn.qingcheng.gym.bean.ShopCreateBody;
import cn.qingcheng.gym.bean.ShopsResponse;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import java.util.HashMap;
import java.util.Map;
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

  @Override public Observable<QcDataResponse<ShopsResponse>> qcGetBrandAllShops(String brand_id) {
    return gymApi.qcGetBrandAllShops(loginStatus.staff_id(), brand_id);
  }

  @Override public Observable<QcDataResponse<GymTypeData>> qcLoadGymTypes() {
    return gymApi.qcGetGymType();
  }

  @Override public Observable<QcDataResponse> qcDelGym(String id) {
    return gymApi.qcDelGym(id);
  }

  @Override public Observable<QcDataResponse<BrandResponse>> qcCreatBrand(BrandPostBody body) {
    return gymApi.qcCreatBrand(body);
  }

  @Override public Observable<QcDataResponse<Shop>> qcSystemInit(ShopCreateBody body) {
    return gymApi.qcSystemInit(body);
  }

  @Override public Observable<QcDataResponse> editGymIntro(String gymId, Shop body) {
    return gymApi.editGymIntro(gymId, body);
  }

  @Override public Observable<QcDataResponse<GymSearchResponse>> qcGetGymsByName(String name) {
    return gymApi.qcGetGymsByName(name);
  }

  @Override public Observable<QcDataResponse<GymPositions>> qcGetGymPositions(String id) {
    return gymApi.qcGetGymPositions(id);
  }

  @Override public Observable<QcDataResponse<GymApplyOrderResponse>> qcPostGymApply(
      Map<String, Object> body) {
    return gymApi.qcPostGymApply(body);
  }

  @Override public Observable<QcDataResponse<GymApplyOrderResponses>> qcGetGymApplyOrder(
      Map<String, Object> body) {
    Map<String, Object> params = new HashMap<>(body);
    params.put("user_id", loginStatus.getUserId());
    params.put("status", 1);
    return gymApi.qcGetGymApplyOrder(params);
  }

  @Override
  public Observable<QcDataResponse<GymPosition>> qcGetGymUserPosition(String id, String type) {
    return gymApi.qcGetGymUserPosition(id, type);
  }

  @Override
  public Observable<QcDataResponse<GymApplyOrderResponse>> qcGetGymApplyOrderInfo(String gymId,
      String orderId) {
    return gymApi.qcGetGymApplyOrderInfo(gymId, orderId);
  }

  @Override
  public Observable<QcDataResponse<GymApplyOrderResponse>> qcDealGymApplyOrder(String gymId,
      String orderId, Map<String, Object> body) {
    return gymApi.qcDealGymApplyOrder(gymId, orderId, body);
  }

  @Override
  public Observable<QcDataResponse<BransShopsPremissions>> qcGetBrandShopsPermission(String brandID,
      String permission) {
    return gymApi.qcGetBrandShopsPermission(loginStatus.staff_id(), brandID, permission);
  }

  @Override public Observable<QcResponse> qcQuitGym(Map<String,Object> params) {
    return gymApi.qcQuitGym(loginStatus.staff_id(), params);
  }
}
