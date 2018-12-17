package cn.qingchengfit.card.network;

import cn.qingchengfit.card.bean.CouponResponse;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.repository.ICardModel;
import java.util.HashMap;
import rx.Observable;

public interface CardRealModel extends ICardModel {
  Observable<QcDataResponse<CouponResponse>> qcLoadCoupons(String staff_id,
      HashMap<String, Object> params);
}
