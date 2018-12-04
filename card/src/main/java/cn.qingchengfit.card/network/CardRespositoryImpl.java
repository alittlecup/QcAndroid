package cn.qingchengfit.card.network;

import android.arch.lifecycle.LiveData;
import cn.qingchengfit.card.bean.CouponResponse;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saascommon.mvvm.LiveDataTransfer;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.saascommon.network.RxHelper;
import io.reactivex.Flowable;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;

public class CardRespositoryImpl implements CardRespository {
  @Inject CardRealModel cardModel;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;

  @Inject public CardRespositoryImpl() {
  }

  static <T> LiveData<Resource<T>> toLiveData(Flowable<QcDataResponse<T>> observable) {
    return LiveDataTransfer.fromPublisher(observable.compose(RxHelper.schedulersTransformerFlow()));
  }

  @Override
  public LiveData<Resource<CouponResponse>> loadCoupons(float prices, List<String> user_ids) {
    HashMap<String, Object> params = gymWrapper.getParams();
    params.put("price", prices);
    params.put("type", "new_cards");
    params.put("user_ids", user_ids);
    return toLiveData(cardModel.qcLoadCoupons(loginStatus.staff_id(), params));
  }
}
