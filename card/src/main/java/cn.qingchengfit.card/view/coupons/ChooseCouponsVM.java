package cn.qingchengfit.card.view.coupons;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import cn.qingchengfit.card.bean.CouponResponse;
import cn.qingchengfit.card.bean.UserWithCoupons;
import cn.qingchengfit.card.network.CardRealModel;
import cn.qingchengfit.card.network.CardRespository;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.utils.StringUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class ChooseCouponsVM extends BaseViewModel {
  @Inject CardRespository cardRespository;
  @Inject CardRealModel cardRealModel;
  @Inject GymWrapper gymWrapper;
  @Inject LoginStatus loginStatus;

  public LiveData<List<UserWithCoupons>> getDatas() {
    return datas;
  }

  private MutableLiveData<Map<String, Object>> param = new MutableLiveData<>();

  private LiveData<List<UserWithCoupons>> datas;

  @Inject public ChooseCouponsVM() {
    datas = Transformations.switchMap(param,
        input -> Transformations.map(cardRespository.loadCoupons(input), input1 -> {
          CouponResponse couponResponse = dealResource(input1);
          return couponResponse == null ? null : couponResponse.user_coupons;
        }));
  }

  public void loadCoupons(float prices, List<String> user_ids) {
    if (user_ids == null || user_ids.isEmpty()) return;
    Map<String, Object> bodys = new HashMap<>();
    bodys.put("price", prices);
    bodys.put("type", "new_cards");
    bodys.put("user_ids", StringUtils.List2Str(user_ids));
    param.setValue(bodys);
  }

  public void loadCouponsCharge(float prices, List<String> user_ids, String cardID) {
    if (user_ids == null || user_ids.isEmpty()) return;
    Map<String, Object> bodys = new HashMap<>();
    bodys.put("price", prices);
    bodys.put("type", "charge_cards");
    bodys.put("card_id", cardID);
    bodys.put("user_ids", StringUtils.List2Str(user_ids));
    param.setValue(bodys);
  }
}
