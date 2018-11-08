package com.qingchengfit.fitcoach.http;

import cn.qingchengfit.model.ComponentModuleManager;
import cn.qingchengfit.network.QcRestRepository;
import javax.inject.Inject;
import retrofit2.Retrofit;

public final class TrainerRepository {
  private static TrainerAllApi trainerAllApi;
  @Inject QcRestRepository qcRestRepository;

  @Inject public TrainerRepository(QcRestRepository qcRestRepository) {
    if (trainerAllApi == null) {
      trainerAllApi = qcRestRepository.createRxJava1Api(TrainerAllApi.class);
    }
  }

  public TrainerAllApi getTrainerAllApi() {
    if (trainerAllApi == null) {
      trainerAllApi = qcRestRepository.createRxJava1Api(TrainerAllApi.class);
    }
    return trainerAllApi;
  }
  public static TrainerAllApi getStaticTrainerAllApi(){
    if (trainerAllApi == null) {
      Retrofit retrofit = ComponentModuleManager.get(Retrofit.class);
      trainerAllApi = retrofit.create(TrainerAllApi.class);
    }
    return trainerAllApi;
  }
}
