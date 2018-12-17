package cn.qingchengfit.card.network;

import android.arch.lifecycle.LiveData;
import cn.qingchengfit.card.bean.CouponResponse;
import cn.qingchengfit.saascommon.network.Resource;

public interface CardRespository {
  LiveData<Resource<CouponResponse>> loadCoupons(float prices, String user_ids);
}
