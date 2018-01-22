package cn.qingchengfit.shop.ui.product;

import android.databinding.ObservableBoolean;
import android.util.Log;
import cn.qingchengfit.saasbase.common.mvvm.ActionLiveEvent;
import cn.qingchengfit.shop.base.ShopBaseViewModel;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/18.
 */

public class ShopProductViewModel extends ShopBaseViewModel {
  public final ObservableBoolean isEdit = new ObservableBoolean(true);
  public final ActionLiveEvent payChannelEvent=new ActionLiveEvent();
  @Inject public ShopProductViewModel() {
  }

  public void chooseImageClick() {

  }

  public void toPayChannel() {
    payChannelEvent.call();
  }

  public void toPriceDetail() {

  }

  public void toProductDesc() {

  }

  public void toCategory() {

  }

  public void toDeliverChannel() {

  }

  @Override protected void onCleared() {
    Log.d("TAG", "onCleared: ");
  }
}
