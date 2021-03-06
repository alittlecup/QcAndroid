package cn.qingchengfit.gym.pages.apply;

import android.arch.core.util.Function;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Transformations;
import cn.qingchengfit.gym.bean.GymApplyOrder;
import cn.qingchengfit.gym.bean.GymApplyOrderResponse;
import cn.qingchengfit.gym.bean.GymPosition;
import cn.qingchengfit.gym.bean.GymPositions;
import cn.qingchengfit.gym.responsitory.IGymResponsitory;
import cn.qingchengfit.saascommon.mvvm.BaseViewModel;
import cn.qingchengfit.saascommon.network.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;

public class GymApplyDealViewModel extends BaseViewModel {
  @Inject IGymResponsitory gymResponsitory;
  public LiveData<List<GymPosition>> positions;

  @Inject public GymApplyDealViewModel() {

  }

  public void loadGymPositions(String gymId) {
    positions =
        Transformations.map(gymResponsitory.qcGetGymPositions(gymId), gymPositionsResource -> {
          GymPositions gymPositions = dealResource(gymPositionsResource);
          return gymPositions == null ? null : gymPositions.positions;
        });
  }

  public LiveData<Resource<GymApplyOrderResponse>> dealApply(String gymId, String orderId, int status, String positionId) {
    Map<String, Object> param = new HashMap<>();
    param.put("status", status);
    param.put("position_id", positionId);
   return gymResponsitory.qcDealGymApplyOrder(gymId, orderId, param);
  }

  public LiveData<GymApplyOrder> loagAplyOrderInfo(String gymId, String applyId) {
    return Transformations.map(gymResponsitory.qcGetGymApplyOrderInfo(gymId, applyId),
        new Function<Resource<GymApplyOrderResponse>, GymApplyOrder>() {
          @Override public GymApplyOrder apply(Resource<GymApplyOrderResponse> resource) {
            GymApplyOrderResponse gymApplyOrderResponse = dealResource(resource);
            return gymApplyOrderResponse == null ? null : gymApplyOrderResponse.gymApplyOrder;
          }
        });
  }
}
