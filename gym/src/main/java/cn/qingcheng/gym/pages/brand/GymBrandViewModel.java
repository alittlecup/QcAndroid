package cn.qingcheng.gym.pages.brand;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import cn.qingcheng.gym.bean.BransShopsPremissions;
import cn.qingcheng.gym.bean.ShopsResponse;
import cn.qingcheng.gym.responsitory.IGymResponsitory;
import cn.qingchengfit.model.base.Shop;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;

public class GymBrandViewModel extends BaseViewModel {
  public LiveData<List<Shop>> datas;
  public MutableLiveData<String> brandId = new MutableLiveData<>();
  IGymResponsitory gymResponsitory;

  @Inject public GymBrandViewModel(IGymResponsitory gymResponsitory) {
    this.gymResponsitory = gymResponsitory;
    datas = Transformations.switchMap(brandId,
        brandId -> Transformations.map(gymResponsitory.qcGetBrandAllShops(brandId),
            shopsResponseResource -> {
              ShopsResponse shopsResponse = dealResource(shopsResponseResource);
              return shopsResponse == null ? new ArrayList<>() : shopsResponse.shops;
            }));
  }

  public void loadShops(String brandId) {
    this.brandId.setValue(brandId);
  }

  public LiveData<BransShopsPremissions> loadShopPermissions(String brandID,String gymPermission) {
    return Transformations.map(gymResponsitory.qcGetBrandShopsPermission(brandID,
        gymPermission),
        bransShopsPremissionsResource -> dealResource(bransShopsPremissionsResource));
  }
}
