package cn.qingchengfit.gym.pages.apply;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import cn.qingchengfit.gym.bean.GymApplyOrder;
import cn.qingchengfit.gym.bean.GymApplyOrderResponse;
import cn.qingchengfit.gym.bean.GymApplyOrderResponses;
import cn.qingchengfit.gym.bean.GymPosition;
import cn.qingchengfit.gym.bean.GymPositions;
import cn.qingchengfit.gym.responsitory.IGymResponsitory;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.network.Resource;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class GymApplyViewModel extends BaseViewModel {
  @Inject IGymResponsitory gymResponsitory;
  public LiveData<List<GymPosition>> positions;
  LiveData<Resource<GymApplyOrderResponse>> gymApplyOrder;
  private MutableLiveData<Map<String, Object>> params = new MutableLiveData<>();
  public MutableLiveData<GymApplyOrderResponse> gymApplyOrderPre = new MutableLiveData<>();

  @Inject public GymApplyViewModel(IGymResponsitory gymResponsitory) {
    this.gymResponsitory = gymResponsitory;
    gymApplyOrder = Transformations.switchMap(params,
        params -> Transformations.map(gymResponsitory.qcPostGymApply(params),
            gymApplyOrderResponseResource -> gymApplyOrderResponseResource));
  }

  public void loadGymPositions(String gymId) {
    positions =
        Transformations.map(gymResponsitory.qcGetGymPositions(gymId), gymPositionsResource -> {
          GymPositions gymPositions = dealResource(gymPositionsResource);
          return gymPositions == null ? null : gymPositions.positions;
        });
  }

  public LiveData<GymApplyOrder> loadGymOrder(Map<String, Object> params) {
    return Transformations.map(gymResponsitory.qcGetGymApplyOrder(params),
        gymApplyOrderResponseResource -> {
          GymApplyOrderResponses gymApplyOrderResponses =
              dealResource(gymApplyOrderResponseResource);
          if (gymApplyOrderResponses != null
              && gymApplyOrderResponses.gymApplyOrderResponses != null) {
            List<GymApplyOrder> orders = gymApplyOrderResponses.gymApplyOrderResponses;
            if (!orders.isEmpty()) {
              return orders.get(0);
            }
          }
          return null;
        });
  }

  public void postGymApplyOrder(Map<String, Object> params) {
    this.params.setValue(params);
  }

  public LiveData<GymPosition> findPositionInGym(String gymId, String type) {
    return Transformations.map(gymResponsitory.qcGetGymUserPosition(gymId, type),
        this::dealResource);
  }
}
