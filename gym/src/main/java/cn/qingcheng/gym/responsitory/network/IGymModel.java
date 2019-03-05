package cn.qingcheng.gym.responsitory.network;

import cn.qingcheng.gym.bean.BrandPostBody;
import cn.qingcheng.gym.bean.BrandResponse;
import cn.qingcheng.gym.bean.BrandsResponse;
import cn.qingcheng.gym.bean.GymTypeData;
import cn.qingcheng.gym.bean.ShopsResponse;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.network.response.QcDataResponse;
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

}
