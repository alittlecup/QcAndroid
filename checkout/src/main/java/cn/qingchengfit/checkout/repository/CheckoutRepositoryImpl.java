package cn.qingchengfit.checkout.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.checkout.bean.HomePageBean;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saascommon.network.BindLiveData;
import cn.qingchengfit.saascommon.network.Resource;
import cn.qingchengfit.saascommon.network.RxHelper;
import io.reactivex.Flowable;
import javax.inject.Inject;

public class CheckoutRepositoryImpl implements CheckoutRepository {
  @Inject ICheckoutModel remoteService;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;
  @Inject public CheckoutRepositoryImpl(){}

  static <T> void bindToLiveData(MutableLiveData<T> liveData,
      Flowable<QcDataResponse<T>> observable, MutableLiveData<Resource<Object>> result,
      String tag) {
    BindLiveData.bindLiveData(observable.compose(RxHelper.schedulersTransformerFlow()), liveData,
        result, tag);
  }

  @Override public void qcGetHomePageInfo(MutableLiveData<HomePageBean> result,
      MutableLiveData<Resource<Object>> defaultRes) {
    bindToLiveData(result,
        remoteService.qcGetHomePageInfo(loginStatus.staff_id(), gymWrapper.getParams()), defaultRes,
        "");
  }
}
