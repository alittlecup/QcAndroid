package cn.qingcheng.gym.pages.search;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import cn.qingcheng.gym.bean.GymSearchResponse;
import cn.qingcheng.gym.responsitory.IGymResponsitory;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.network.Resource;
import javax.inject.Inject;

public class GymSearchViewModel extends BaseViewModel {
  @Inject IGymResponsitory gymResponsitory;
  private MutableLiveData<String> name=new MutableLiveData<>();
  LiveData<GymSearchResponse> response;

  @Inject public GymSearchViewModel() {
    response= Transformations.switchMap(name,name->Transformations.map(gymResponsitory.qcGetGymsByName(name),
        new Function<Resource<GymSearchResponse>, GymSearchResponse>() {
          @Override
          public GymSearchResponse apply(Resource<GymSearchResponse> gymSearchResponseResource) {
            return dealResource(gymSearchResponseResource);
          }
        }));
  }

  public void searchGym(String text) {
    name.setValue(text);
  }
}
