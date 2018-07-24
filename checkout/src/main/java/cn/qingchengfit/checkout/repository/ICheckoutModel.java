package cn.qingchengfit.checkout.repository;

import cn.qingchengfit.checkout.bean.HomePageBean;
import cn.qingchengfit.network.response.QcDataResponse;
import io.reactivex.Flowable;
import java.util.Map;

public interface ICheckoutModel {
  Flowable <QcDataResponse<HomePageBean>> qcGetHomePageInfo(String staff_id,Map<String,Object> params);
}
