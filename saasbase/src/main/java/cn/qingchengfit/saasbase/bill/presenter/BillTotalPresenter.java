package cn.qingchengfit.saasbase.bill.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.bill.beans.BillTotal;
import cn.qingchengfit.saasbase.bill.beans.BusinessBill;
import cn.qingchengfit.saasbase.repository.IBillModel;
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

  public void qcGetBillTotal(final String billId){
    RxRegiste(billModel.queryBillTotal(billId)
        .subscribeOn(Schedulers.io())
        .onBackpressureBuffer()
        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<QcDataResponse<BillTotal>>() {
          @Override public void call(QcDataResponse<BillTotal> billTotalQcDataResponse) {
            if (ResponseConstant.checkSuccess(billTotalQcDataResponse)){
              if (view != null){
                view.onGetTotal(billTotalQcDataResponse.data);
              }else{
                view.onShowError(billTotalQcDataResponse.getMsg());
              }
            }
          }
        }, new NetWorkThrowable()));
  }

  public void qcGetBillList(final String billId){
    RxRegiste(billModel.queryBillList(billId)
        .subscribeOn(Schedulers.io())
        .onBackpressureBuffer()
        .observeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<QcDataResponse<List<BusinessBill>>>() {
          @Override public void call(QcDataResponse<List<BusinessBill>> listQcDataResponse) {
            if (ResponseConstant.checkSuccess(listQcDataResponse)){
              if (view != null){
                view.onGetBillList(listQcDataResponse.data);
              }else{
                view.onShowError(listQcDataResponse.getMsg());
              }
            }
          }
        }, new NetWorkThrowable()));
  }

  public BillSummary getSummary(List<BusinessBill> billList){
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
  }

}
