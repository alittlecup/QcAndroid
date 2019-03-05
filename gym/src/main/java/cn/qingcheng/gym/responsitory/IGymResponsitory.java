package cn.qingcheng.gym.responsitory;

import android.arch.lifecycle.LiveData;
import cn.qingcheng.gym.bean.BrandPostBody;
import cn.qingcheng.gym.bean.BrandResponse;
import cn.qingcheng.gym.bean.BrandsResponse;
import cn.qingcheng.gym.bean.GymTypeData;
import cn.qingcheng.gym.bean.ShopsResponse;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.saascommon.network.Resource;

public interface IGymResponsitory {
  LiveData<Resource<BrandsResponse>> qcGetBrands();

  LiveData<Resource<ShopsResponse>> qcGetBrandAllShops(String brand_id);

  LiveData<Resource<GymTypeData>> qcGetGymTypes();

  LiveData<Resource<Boolean>> qcDeleteShop(String shop_id);

  LiveData<Resource<BrandResponse>> qcCreateBrand(BrandPostBody body);

  LiveData<Resource<Shop>> qcSystemInit(Shop body);

  LiveData<Resource<Boolean>> editGymIntro(String gymId, Shop body);
}
