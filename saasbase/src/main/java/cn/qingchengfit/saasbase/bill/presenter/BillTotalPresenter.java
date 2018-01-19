package cn.qingchengfit.saasbase.bill.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.bill.beans.BillTotal;
import cn.qingchengfit.saasbase.bill.beans.BillTotalWrapper;
import cn.qingchengfit.saasbase.bill.beans.BillWrapper;
import cn.qingchengfit.saasbase.bill.beans.BusinessBill;
import cn.qingchengfit.saasbase.repository.IBillModel;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by fb on 2017/10/22.
 */

public class BillTotalPresenter extends BasePresenter {

  private MVPView view;
  @Inject QcRestRepository qcRestRepository;
  @Inject IBillModel billModel;
  public BillSummary billSummary;

  @Inject
  public BillTotalPresenter() {
  }

  @Override public void attachView(PView v) {
    this.view = (MVPView) v;
  }

  public void qcGetBillTotal(String billId){
    RxRegiste(billModel.queryBillTotal(billId)
        .subscribeOn(Schedulers.io())
        .onBackpressureBuffer()
        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<QcDataResponse<BillTotalWrapper>>() {
          @Override public void call(QcDataResponse<BillTotalWrapper> billTotalQcDataResponse) {
            if (ResponseConstant.checkSuccess(billTotalQcDataResponse)){
              if (view != null){
                view.onGetTotal(billTotalQcDataResponse.data.bills_statistics);
              }else{
                view.onShowError(billTotalQcDataResponse.getMsg());
              }
            }
          }
        }, new NetWorkThrowable()));
  }

  public void qcGetBillList(final String billId, String time){
    HashMap<String, Object> params = new HashMap<>();
    params.put("pay_time", time);
    RxRegiste(billModel.queryBillList(billId, params)
        .subscribeOn(Schedulers.io())
        .onBackpressureBuffer()
        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<QcDataResponse<BillWrapper>>() {
          @Override public void call(QcDataResponse<BillWrapper> billWrapperQcDataResponse) {
            if (ResponseConstant.checkSuccess(billWrapperQcDataResponse)){
              if (view != null){
                view.onGetBillList(billWrapperQcDataResponse.data.bills);
              }else{
                view.onShowError(billWrapperQcDataResponse.getMsg());
              }
            }
          }
        }, new NetWorkThrowable()));
  }

  public void qcGetBillListbyFilter(final String billId, HashMap<String, Object> params){
    RxRegiste(billModel.queryBillList(billId, params)
        .subscribeOn(Schedulers.io())
        .onBackpressureBuffer()
        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<QcDataResponse<BillWrapper>>() {
          @Override public void call(QcDataResponse<BillWrapper> billWrapperQcDataResponse) {
            if (ResponseConstant.checkSuccess(billWrapperQcDataResponse)){
              if (view != null){
                view.onGetBillListByFilter(billWrapperQcDataResponse.data.bills);
              }
            }else{
                view.onFilterError(billWrapperQcDataResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public BillSummary getSummary(List<BusinessBill> billList){
    if (billSummary == null){
      billSummary = new BillSummary();
    }
    billSummary.sum = 0;
    billSummary.reduce = 0;
    for (BusinessBill bill : billList){
      if (bill.type == 5 || bill.type == 6){
        billSummary.sum += bill.price;
      }else{
        billSummary.reduce += bill.price;
      }
    }
    return billSummary;
  }

  @Override public void unattachView() {
    super.unattachView();
    if (view != null){
      view = null;
    }
  }

  public interface MVPView extends CView{
    void onGetTotal(BillTotal billTotal);

    void onGetBillList(List<BusinessBill> billList);

    void onGetBillListByFilter(List<BusinessBill> billList);

    void onFilterError(String msg);
  }

}
