package cn.qingchengfit.card.network;

import android.arch.lifecycle.LiveData;
import cn.qingchengfit.card.bean.UserWithCoupons;
import java.util.List;

public interface CardRespository {
  LiveData<List<UserWithCoupons>> loadCoupons(float prices, List<String> user_ids);
}
