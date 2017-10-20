package cn.qingchengfit.saasbase.repository;

import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.bill.beans.BillPayStatus;
import cn.qingchengfit.saasbase.bill.network.BusinessOrderWrap;
import cn.qingchengfit.saasbase.bill.network.PayRequestListWrap;
import java.util.HashMap;
import rx.Observable;

/**
 * Created by fb on 2017/10/16.
 */

public interface IBillModel {
  /**
   *
   */
  Observable<QcDataResponse<PayRequestListWrap>> getPayRequestList(int page);

  Observable<QcDataResponse<BusinessOrderWrap>> getBusinessOrderDetail(String businessOrderId);

  Observable<QcDataResponse<BusinessOrderWrap>> getBusinessOrderList(HashMap<String,Object> params);

  //api/rongshu/payments/:order_no/status/
  Observable<QcDataResponse<BillPayStatus>> queryBillStatus(String businessOrderId);



}
