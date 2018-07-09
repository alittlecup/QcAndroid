package cn.qingchengfit.saascommon.network;

import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saascommon.qrcode.model.ScanBody;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface SaasCommonApi {

  /**
   * 扫描二维码
   */
  @PUT("/api/scans/{uuid}/") rx.Observable<QcResponse> qcScans(@Path("uuid") String uuid, @Body
      ScanBody body);
}
