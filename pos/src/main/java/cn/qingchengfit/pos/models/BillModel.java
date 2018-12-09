package cn.qingchengfit.pos.models;

import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.pos.net.BillApi;
import cn.qingchengfit.saasbase.bill.beans.BillLock;
import cn.qingchengfit.saasbase.bill.beans.BillPayStatus;
import cn.qingchengfit.saasbase.bill.beans.BillTotalWrapper;
import cn.qingchengfit.saasbase.bill.beans.BillWrapper;
import cn.qingchengfit.saascommon.filter.model.FilterWrapper;
import cn.qingchengfit.saasbase.bill.network.BusinessOrderWrap;
import cn.qingchengfit.saasbase.bill.network.PayRequestListWrap;
import cn.qingchengfit.saasbase.cards.network.response.PayBusinessResponseWrap;
import cn.qingchengfit.saasbase.repository.IBillModel;
import java.util.HashMap;
import retrofit2.http.Path;
import rx.Observable;

/**
 * power by
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MM.:  .:'   `:::  .:`MMMMMMMMMMM|`MMM'|MMMMMMMMMMM':  .:'   `:::  .:'.MM
 * MMMM.     :          `MMMMMMMMMM  :*'  MMMMMMMMMM'        :        .MMMM
 * MMMMM.    ::    .     `MMMMMMMM'  ::   `MMMMMMMM'   .     ::   .  .MMMMM
 * MMMMMM. :   :: ::'  :   :: ::'  :   :: ::'      :: ::'  :   :: ::.MMMMMM
 * MMMMMMM    ;::         ;::         ;::         ;::         ;::   MMMMMMM
 * MMMMMMM .:'   `:::  .:'   `:::  .:'   `:::  .:'   `:::  .:'   `::MMMMMMM
 * MMMMMM'     :           :           :           :           :    `MMMMMM
 * MMMMM'______::____      ::    .     ::    .     ::     ___._::____`MMMMM
 * MMMMMMMMMMMMMMMMMMM`---._ :: ::'  :   :: ::'  _.--::MMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMM::.         ::  .--MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMM-.     ;::-MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM. .:' .M:F_P:MMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM.   .MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM\ /MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * MMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMVMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMMM
 * Created by Paper on 2017/10/22.
 */

public class BillModel implements IBillModel {

  QcRestRepository repository;
  GymWrapper gymWrapper;
  LoginStatus loginStatus;
  BillApi billApi;

  public BillModel(QcRestRepository repository, GymWrapper gymWrapper, LoginStatus loginStatus) {
    this.repository = repository;
    this.gymWrapper = gymWrapper;
    this.loginStatus = loginStatus;
    billApi = repository.createRxJava1Api(BillApi.class);
  }

  @Override public Observable<QcDataResponse<PayBusinessResponseWrap>> directPay(long amount) {
    HashMap<String,Object> body = new HashMap<>();
    body.put("order_amount",amount);
    return billApi.directPay(gymWrapper.getGymId(),body);
  }

  @Override public Observable<QcDataResponse<PayRequestListWrap>> getPayRequestList(int page) {
    HashMap<String, Object> prams = new HashMap<>();
    prams.put("page", page);
    return billApi.getPayRequsetList(gymWrapper.getGymId(),prams);
  }

  @Override public Observable<QcDataResponse<BusinessOrderWrap>> getBusinessOrderDetail(
    String businessOrderId) {
    return billApi.getBillDetail(gymWrapper.getGymId() ,businessOrderId, gymWrapper.getParams());
  }

  @Override public Observable<QcDataResponse> editBusinessOrderMark(String businessOrderId,String mark) {
    HashMap<String,Object> params = new HashMap<>();
    params.put("remarks",mark);
    return billApi.editBillDetail(gymWrapper.getGymId(),businessOrderId,params);
  }

  @Override public Observable<QcDataResponse<BusinessOrderWrap>> getBusinessOrderList(
    HashMap<String, Object> params) {
    return null;
  }

  @Override
  public Observable<QcDataResponse<BillPayStatus>> queryBillStatus(String businessOrderId) {
    return billApi.getBillStatus(businessOrderId, gymWrapper.getParams());
  }

  @Override
  public Observable<QcDataResponse<BillLock>> lockPayRequest(@Path("task_id") String task_id) {
    return billApi.lockPayRequest(task_id);
  }

  @Override public Observable<QcDataResponse> cancelPayRequest(@Path("task_id") String task_id) {
    return billApi.cancelPayRequest(task_id);
  }

  @Override public Observable<QcDataResponse<BillTotalWrapper>> queryBillTotal(String businessOrderId) {
    return billApi.getBillToTal(businessOrderId);
  }

  @Override public Observable<QcDataResponse<BillWrapper>> queryBillList(String gym_id, HashMap<String, Object> params) {
    return billApi.getBillList(gym_id, params);
  }

  @Override public Observable<QcDataResponse<FilterWrapper>> getBillFilterList(String gym_id) {
    return billApi.getBillFilterList(gym_id);
  }
}
