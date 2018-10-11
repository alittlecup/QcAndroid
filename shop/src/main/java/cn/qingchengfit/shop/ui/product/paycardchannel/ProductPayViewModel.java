package cn.qingchengfit.shop.ui.product.paycardchannel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.router.QCResult;
import cn.qingchengfit.router.qc.IQcRouteCallback;
import cn.qingchengfit.router.qc.QcRouteUtil;
import cn.qingchengfit.router.qc.RouteOptions;
import cn.qingchengfit.saascommon.constant.Configs;
import cn.qingchengfit.saascommon.flexble.FlexibleItemProvider;
import cn.qingchengfit.saascommon.flexble.FlexibleViewModel;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.shop.listener.CategotyItemClickListener;
import cn.qingchengfit.shop.repository.ShopRepository;
import cn.qingchengfit.shop.ui.home.categorylist.ShopCategoryListViewModel;
import cn.qingchengfit.shop.ui.items.CommonItemFactory;
import cn.qingchengfit.shop.ui.items.category.CategoryListItem;
import cn.qingchengfit.shop.ui.items.product.CardSwitchItem;
import cn.qingchengfit.shop.vo.CardSwitchData;
import cn.qingchengfit.shop.vo.Category;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

/**
 * Created by huangbaole on 2017/12/20.
 */

public class ProductPayViewModel
    extends BaseViewModel {
  public final ObservableField<List<CardSwitchItem>> items = new ObservableField<>();
  public final MutableLiveData<List<CardSwitchData>> datas = new MutableLiveData<>();

  @Inject ShopRepository repository;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;

  @Inject public ProductPayViewModel() {
  }

  public void loadData(String isEnable) {
    RouteOptions routeOptions = new RouteOptions("card").setActionName("/load/cardtpl")
        .addParam("type", Configs.CATEGORY_VALUE+"")
        .addParam("isEnable", isEnable)
        .addParam("params", gymWrapper.getParams())
        .addParam("staff_id", loginStatus.staff_id());
    QcRouteUtil.setRouteOptions(routeOptions).callAsync(new IQcRouteCallback() {
      @Override public void onResult(QCResult qcResult) {
        if(qcResult.isSuccess()){
          Object cardtpls = qcResult.getDataMap().get("cardtpls");
          if(cardtpls!=null){
            Type listType = new TypeToken<List<CardSwitchData>>() {}.getType();
            List<CardSwitchData> cardSwitchDatas = new Gson().fromJson((String) cardtpls, listType);
            datas.postValue(cardSwitchDatas);
          }
        }
      }
    });
  }
}
