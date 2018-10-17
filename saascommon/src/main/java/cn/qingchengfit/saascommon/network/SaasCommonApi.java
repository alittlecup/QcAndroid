package cn.qingchengfit.saascommon.network;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saascommon.model.Advertisement;
import cn.qingchengfit.saascommon.network.response.AdvertiseWrap;
import cn.qingchengfit.saascommon.qrcode.model.ScanBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface SaasCommonApi {

  /**
   * 扫描二维码
   */
  @PUT("/api/scans/{uuid}/") rx.Observable<QcResponse> qcScans(@Path("uuid") String uuid, @Body
      ScanBody body);

  /**
   * 获取广告弹窗相关信息
   * @param source staff/coach
   * @return
   */
  @GET("api/activities/")
  rx.Observable<QcDataResponse<Advertisement>> qcGetAdvertise(@Query("source")String source);
}
