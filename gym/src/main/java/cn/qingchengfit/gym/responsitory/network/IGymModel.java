package cn.qingchengfit.gym.responsitory.network;

import cn.qingchengfit.gym.bean.BrandPostBody;
import cn.qingchengfit.gym.bean.BrandResponse;
import cn.qingchengfit.gym.bean.BrandsResponse;
import cn.qingchengfit.gym.bean.BransShopsPremissions;
import cn.qingchengfit.gym.bean.GymApplyOrderResponse;
import cn.qingchengfit.gym.bean.GymApplyOrderResponses;
import cn.qingchengfit.gym.bean.GymPosition;
import cn.qingchengfit.gym.bean.GymPositions;
import cn.qingchengfit.gym.bean.GymSearchResponse;
import cn.qingchengfit.gym.bean.GymTypeData;
import cn.qingchengfit.gym.bean.ShopCreateBody;
import cn.qingchengfit.gym.bean.ShopsResponse;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import java.util.Map;
import rx.Observable;

public interface IGymModel {
  Observable<QcDataResponse<BrandsResponse>> qcGetBrands();

  Observable<QcDataResponse<ShopsResponse>> qcGetBrandAllShops(String brand_id);

  Observable<QcDataResponse<GymTypeData>> qcLoadGymTypes();

  Observable<QcDataResponse> qcDelGym(String id);

  Observable<QcDataResponse<BrandResponse>> qcCreatBrand(BrandPostBody body);

  Observable<QcDataResponse<Shop>> qcSystemInit(ShopCreateBody body,boolean istrainer);

  Observable<QcDataResponse> editGymIntro(String gymId, Shop body);

  Observable<QcDataResponse<GymSearchResponse>> qcGetGymsByName(String name);

  Observable<QcDataResponse<GymPositions>> qcGetGymPositions(String id);

  Observable<QcDataResponse<GymApplyOrderResponse>> qcPostGymApply(Map<String, Object> body);

  Observable<QcDataResponse<GymApplyOrderResponses>> qcGetGymApplyOrder(Map<String, Object> body);

  Observable<QcDataResponse<GymPosition>> qcGetGymUserPosition(String id, String type);

  Observable<QcDataResponse<GymApplyOrderResponse>> qcGetGymApplyOrderInfo(String gymId,
      String orderId);

  Observable<QcDataResponse<GymApplyOrderResponse>> qcDealGymApplyOrder(String gymId,
      String orderId, Map<String, Object> body);

  Observable<QcDataResponse<BransShopsPremissions>> qcGetBrandShopsPermission(String brandID,
      String permission);

  Observable<QcResponse> qcQuitGym(Map<String,Object> params);
}
