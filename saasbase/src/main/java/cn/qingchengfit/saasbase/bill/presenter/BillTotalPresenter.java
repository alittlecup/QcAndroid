package cn.qingchengfit.saasbase.bill.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.bill.beans.BillTotal;
import cn.qingchengfit.saasbase.repository.IBillModel;
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

  @Override public void attachView(PView v) {
    this.view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    if (view != null){
      view = null;
    }
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
        }));
  }

  public interface MVPView extends CView{
    void onGetTotal(BillTotal billTotal);
  }

}
