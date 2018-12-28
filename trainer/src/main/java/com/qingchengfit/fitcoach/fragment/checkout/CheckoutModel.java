package com.qingchengfit.fitcoach.fragment.checkout;

import cn.qingchengfit.checkout.bean.CheckoutBillWrapper;
import cn.qingchengfit.checkout.bean.CheckoutBills;
import cn.qingchengfit.checkout.bean.HomePageBean;
import cn.qingchengfit.checkout.bean.OrderStatusBeanWrapper;
import cn.qingchengfit.checkout.bean.ScanResultBean;
import cn.qingchengfit.checkout.repository.ICheckoutModel;
import cn.qingchengfit.checkout.repository.PayApi;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.ComponentModuleManager;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.checkout.bean.CashierBean;
import com.google.gson.JsonObject;
import io.reactivex.Flowable;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;

public class CheckoutModel implements ICheckoutModel {
  CheckoutTrainerApi checkoutApi;
  PayApi payApi;
  @Inject LoginStatus loginStatus;
  @Inject GymWrapper gymWrapper;


  @Inject CheckoutModel(QcRestRepository qcRestRepository) {
    checkoutApi = qcRestRepository.createRxJava2Api(CheckoutTrainerApi.class);
    payApi = qcRestRepository.createRxJava2Api(PayApi.class);

    ComponentModuleManager.register(ICheckoutModel.class,this);

  }

  @Override public Flowable<QcDataResponse<HomePageBean>> qcGetHomePageInfo() {
    return checkoutApi.qcGetCheckoutHomeInfo(loginStatus.staff_id(), gymWrapper.getParams());
  }

  @Override
  public Flowable<QcDataResponse<CashierBean>> qcPostCashierOrder(Map<String, Object> params) {
    HashMap<String, Object> params1 = gymWrapper.getParams();
    params1.putAll(params);
    return checkoutApi.qcPostCashierOrder(loginStatus.staff_id(), params1);
  }

  @Override
  public Flowable<QcDataResponse<ScanResultBean>> qcPostScanOrder(Map<String, Object> params) {
    HashMap<String, Object> params1 = gymWrapper.getParams();
    params1.putAll(params);
    return payApi.qcPostScanOrder(params1);
  }

  @Override
  public Flowable<QcDataResponse<OrderStatusBeanWrapper>> qcGetOrderStatus(String orderNum) {
    return payApi.qcGetOrderStatus(orderNum, gymWrapper.getParams());
  }

  @Override public Flowable<QcDataResponse<CheckoutBills>> qcLoadCheckoutQrOrders() {
    return checkoutApi.qcLoadCheckoutBills(loginStatus.staff_id(), gymWrapper.getParams());
  }

  @Override
  public Flowable<QcDataResponse<CheckoutBillWrapper>> qcLoadCheckoutQrOrderDetail(String id) {
    return checkoutApi.qcLoadCheckoutBillDetail(loginStatus.staff_id(), id, gymWrapper.getParams());
  }

  @Override
  public Flowable<QcDataResponse<CheckoutBillWrapper>> qcPutCheckoutQrOrderDetail(String id,
      JsonObject body) {
    return checkoutApi.qcPutBillDetail(loginStatus.staff_id(), id, body,gymWrapper.getParams());
  }
}
