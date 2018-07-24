package cn.qingchengfit.checkout.repository;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import cn.qingchengfit.checkout.bean.HomePageBean;
import cn.qingchengfit.saascommon.network.Resource;

public interface CheckoutRepository {

  void qcGetHomePageInfo(MutableLiveData<HomePageBean> result, MutableLiveData<Resource<Object>> defaultRes);
}
