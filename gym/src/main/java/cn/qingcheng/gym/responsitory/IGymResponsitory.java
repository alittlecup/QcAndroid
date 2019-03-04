package cn.qingcheng.gym.responsitory;

import android.arch.lifecycle.LiveData;
import cn.qingcheng.gym.bean.BrandsResponse;
import cn.qingcheng.gym.bean.ShopsResponse;
import cn.qingchengfit.saascommon.network.Resource;

public interface IGymResponsitory {
  LiveData<Resource<BrandsResponse>> qcGetBrands();
  LiveData<Resource<ShopsResponse>> qcGetBrandAllShops( String brand_id);

}
