package cn.qingchengfit.card.view.coupons;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import cn.qingchengfit.card.bean.UserWithCoupons;
import cn.qingchengfit.card.network.CardRespository;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import java.util.List;
import javax.inject.Inject;

public class ChooseCouponsVM extends BaseViewModel {
  @Inject CardRespository cardRespository;

  public LiveData<List<UserWithCoupons>> getDatas() {
    return datas;
  }

  private LiveData<List<UserWithCoupons>> datas = new MutableLiveData<>();

  @Inject public ChooseCouponsVM() {

  }

  public void loadCoupons(float price, List<String> user_ids) {
    datas = Transformations.map(cardRespository.loadCoupons(price, user_ids),
        input -> dealResource(input).user_coupons);
  }
}
