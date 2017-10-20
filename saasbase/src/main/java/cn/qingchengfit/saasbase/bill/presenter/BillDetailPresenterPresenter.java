package cn.qingchengfit.saasbase.bill.presenter;

import android.support.annotation.StringRes;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.SaasConstant;
import cn.qingchengfit.saasbase.bill.beans.BillPayStatus;
import cn.qingchengfit.saasbase.bill.beans.BusinessBill;
import cn.qingchengfit.saasbase.bill.network.BusinessOrderWrap;
import cn.qingchengfit.saasbase.repository.IBillModel;
import cn.qingchengfit.subscribes.NetSubscribe;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class BillDetailPresenterPresenter extends BasePresenter {
  private MVPView view;

  @Inject IBillModel billModel;
  private Subscription sp;
  private BusinessBill bill;
  private String billId;

  @Inject public BillDetailPresenterPresenter() {
  }

  public String getCardId() {
    if (bill != null && bill.extra != null && bill.extra.card != null) {
      return bill.extra.card.getId();
    } else {
      return null;
    }
  }

  public void rollBill(){
    sp = RxRegiste(billModel.queryBillStatus(billId)
      .subscribeOn(Schedulers.computation())
      .repeatWhen(new Func1<Observable<? extends Void>, Observable<?>>() {
        @Override public Observable<?> call(Observable<? extends Void> observable) {
          return observable.delay(3, TimeUnit.SECONDS);
        }
      })
      .takeUntil(new Func1<QcDataResponse<BillPayStatus>, Boolean>() {
        @Override public Boolean call(QcDataResponse<BillPayStatus> billPayStatusQcDataResponse) {
          return billPayStatusQcDataResponse.data.success;
        }
      })
      .flatMap(
        new Func1<QcDataResponse<BillPayStatus>, Observable<QcDataResponse<BusinessOrderWrap>>>() {
          @Override public Observable<QcDataResponse<BusinessOrderWrap>> call(
            QcDataResponse<BillPayStatus> billPayStatusQcDataResponse) {
            return billModel.getBusinessOrderDetail(billId)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread());
          }
        })
      .subscribe(reSult));
  }

  public void queryBill() {
    RxRegiste(billModel.getBusinessOrderDetail(billId)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(reSult)
    );
  }

  NetSubscribe<QcDataResponse<BusinessOrderWrap>> reSult = new NetSubscribe<QcDataResponse<BusinessOrderWrap>>() {
    @Override
    public void onNext(QcDataResponse<BusinessOrderWrap> qcResponse) {
      if (ResponseConstant.checkSuccess(qcResponse)) {
        if (sp != null && !sp.isUnsubscribed()) sp.unsubscribe();
        view.onOrderDetail(qcResponse.data.bill);
      } else {
        view.onShowError(qcResponse.getMsg());
      }
    }
  };

  @StringRes public int getPayType(String s) {
    switch (s) {
      case SaasConstant.PAY_TYPE_WX:
        return R.string.pay_wx;
      case SaasConstant.PAY_TYPE_WX_SCAN:
        return R.string.pay_wx_scan;
      case SaasConstant.PAY_TYPE_ALIPAY:
        return R.string.pay_ali;
      case SaasConstant.PAY_TYPE_ALIPAY_SCAN:
        return R.string.pay_ali_scan;
      //case SaasConstant.PAY_TYPE_CARD:
      default:
        return R.string.pay_card;
    }
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public interface MVPView extends CView {
    void onOrderDetail(BusinessBill order);
  }
}
