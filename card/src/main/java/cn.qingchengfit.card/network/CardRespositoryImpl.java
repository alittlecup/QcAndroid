package cn.qingchengfit.card.network;

import android.arch.lifecycle.LiveData;
import cn.qingchengfit.card.bean.UserWithCoupons;
import cn.qingchengfit.network.QcRestRepository;
import java.util.List;
import javax.inject.Inject;

public class CardRespositoryImpl implements CardRespository{
  @Inject
  public CardRespositoryImpl(QcRestRepository restRepository){

  }
  @Override
  public LiveData<List<UserWithCoupons>> loadCoupons(float prices, List<String> user_ids) {
    return null;
  }
}
