package cn.qingchengfit.staffkit.constant;

import cn.qingchengfit.network.QcRestRepository;
import javax.inject.Inject;

public final class StaffRespository {
  private StaffAllApi staffAllApi;
  @Inject QcRestRepository qcRestRepository;

  @Inject public StaffRespository(QcRestRepository qcRestRepository) {
    if (staffAllApi == null) {
      staffAllApi = qcRestRepository.createRxJava1Api(StaffAllApi.class);
    }
  }

  public StaffAllApi getStaffAllApi() {
    if (staffAllApi == null) {
      staffAllApi = qcRestRepository.createRxJava1Api(StaffAllApi.class);
    }
    return staffAllApi;
  }
}
