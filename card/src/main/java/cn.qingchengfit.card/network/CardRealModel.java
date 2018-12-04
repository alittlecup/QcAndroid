package cn.qingchengfit.card.network;

import cn.qingchengfit.card.bean.CouponResponse;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.repository.ICardModel;
import io.reactivex.Flowable;
import java.util.HashMap;

public interface CardRealModel extends ICardModel {
  Flowable<QcDataResponse<CouponResponse>> qcLoadCoupons(String staff_id,
      HashMap<String, Object> params);
}
