package cn.qingcheng.gym.responsitory.network;

import cn.qingcheng.gym.bean.BrandPostBody;
import cn.qingcheng.gym.bean.BrandResponse;
import cn.qingcheng.gym.bean.BrandsResponse;
import cn.qingcheng.gym.bean.GymApplyOrderResponse;
import cn.qingcheng.gym.bean.GymPositions;
import cn.qingcheng.gym.bean.GymSearchResponse;
import cn.qingcheng.gym.bean.GymTypeData;
import cn.qingcheng.gym.bean.ShopsResponse;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.network.response.QcDataResponse;
import java.util.Map;
import rx.Observable;

public interface IGymModel {
  Observable<QcDataResponse<BrandsResponse>> qcGetBrands();

  Observable<QcDataResponse<ShopsResponse>> qcGetBrandAllShops(String brand_id);

  Observable<QcDataResponse<GymTypeData>> qcLoadGymTypes();

  Observable<QcDataResponse> qcDelGym(String id);

  Observable<QcDataResponse<BrandResponse>> qcCreatBrand(BrandPostBody body);
  Observable<QcDataResponse<Shop>> qcSystemInit(
       Shop body);

  Observable<QcDataResponse> editGymIntro(String gymId,  Shop body);
  Observable<QcDataResponse<GymSearchResponse>> qcGetGymsByName(String name);

  Observable<QcDataResponse<GymPositions>> qcGetGymPositions(String id);
  Observable<QcDataResponse<GymApplyOrderResponse>> qcPostGymApply(
       Map<String, Object> body);
  rx.Observable<QcDataResponse<GymApplyOrderResponse>> qcGetGymApplyOrder(
       Map<String, Object> body);


}
