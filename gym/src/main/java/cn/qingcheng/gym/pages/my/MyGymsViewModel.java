package cn.qingcheng.gym.pages.my;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import cn.qingcheng.gym.bean.BrandsResponse;
import cn.qingcheng.gym.responsitory.IGymResponsitory;
import cn.qingchengfit.model.base.Brand;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import java.util.List;
import javax.inject.Inject;

public class MyGymsViewModel extends BaseViewModel {

  public LiveData<List<Brand>> datas;
  @Inject IGymResponsitory gymResponsitory;

  @Inject public MyGymsViewModel(IGymResponsitory gymResponsitory) {
    this.gymResponsitory=gymResponsitory;
    datas = Transformations.map(gymResponsitory.qcGetBrands(), resource -> {
      BrandsResponse brandsResponse = dealResource(resource);
      return brandsResponse.brands;
    });
  }


}
