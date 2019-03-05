package cn.qingcheng.gym.pages.create;

import cn.qingcheng.gym.bean.BrandPostBody;
import cn.qingcheng.gym.bean.BrandResponse;
import cn.qingcheng.gym.responsitory.IGymResponsitory;
import cn.qingcheng.gym.responsitory.network.IGymModel;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.mvvm.SingleLiveEvent;
import cn.qingchengfit.saascommon.network.RxHelper;
import cn.qingchengfit.utils.ToastUtils;
import javax.inject.Inject;

public class GymBrandCreateViewModel extends BaseViewModel {
  @Inject IGymResponsitory gymResponsitory;
  SingleLiveEvent<BrandResponse>  brand=new SingleLiveEvent<>();
  @Inject IGymModel gymModel;

  @Inject public GymBrandCreateViewModel() {
  }

  public void postBrandInfo(BrandPostBody brandPostBody) {
    gymModel.qcCreatBrand(brandPostBody)
        .compose(RxHelper.schedulersTransformer())
        .subscribe(response -> {
          if(response.status==200){
            brand.setValue(response.data);
          }else{
            brand.setValue(null);
          }
        },throwable ->{
          ToastUtils.show(throwable.getMessage());
          brand.setValue(null);
        });
  }
}
