package cn.qingchengfit.gym.responsitory;

import android.arch.lifecycle.LiveData;
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
import cn.qingchengfit.saascommon.network.Resource;
import java.util.Map;

public interface IGymResponsitory {
  LiveData<Resource<BrandsResponse>> qcGetBrands();

  LiveData<Resource<ShopsResponse>> qcGetBrandAllShops(String brand_id);

  LiveData<Resource<GymTypeData>> qcGetGymTypes();

  LiveData<Resource<Boolean>> qcDeleteShop(String shop_id);

  LiveData<Resource<BrandResponse>> qcCreateBrand(BrandPostBody body);

  LiveData<Resource<Shop>> qcSystemInit(ShopCreateBody body);

  LiveData<Resource<Boolean>> editGymIntro(String gymId, Shop body);

  LiveData<Resource<GymSearchResponse>> qcGetGymsByName(String name);

  LiveData<Resource<GymPositions>> qcGetGymPositions(String id);

  LiveData<Resource<GymApplyOrderResponse>> qcPostGymApply(Map<String, Object> body);

  LiveData<Resource<GymApplyOrderResponses>> qcGetGymApplyOrder(Map<String, Object> body);

  LiveData<Resource<GymPosition>> qcGetGymUserPosition(String id, String type);

  LiveData<Resource<GymApplyOrderResponse>> qcGetGymApplyOrderInfo(String gymId, String orderId);

  LiveData<Resource<GymApplyOrderResponse>> qcDealGymApplyOrder(String gymId, String orderId,
      Map<String, Object> body);

  LiveData<Resource<BransShopsPremissions>> qcGetBrandShopsPermission(String brandID,
      String permission);
}
