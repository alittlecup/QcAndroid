package cn.qingchengfit.card.network;

import android.arch.lifecycle.LiveData;
import cn.qingchengfit.card.bean.CouponResponse;
import cn.qingchengfit.saascommon.network.Resource;
import java.util.Map;

public interface CardRespository {
  LiveData<Resource<CouponResponse>> loadCoupons(Map<String, Object> bodys);
}
