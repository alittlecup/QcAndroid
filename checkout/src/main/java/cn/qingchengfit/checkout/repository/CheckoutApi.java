package cn.qingchengfit.checkout.repository;

import cn.qingchengfit.checkout.bean.HomePageBean;
import cn.qingchengfit.network.response.QcDataResponse;
import io.reactivex.Flowable;
import java.util.Map;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface CheckoutApi {

  @GET("api/staffs/{staff_id}/cashier/stat/") Flowable<QcDataResponse<HomePageBean>> qcGetCeckoutHomeInfo(
      @Path("staff_id") String staff_id,@QueryMap Map<String,Object> params);
}
