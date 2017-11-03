package cn.qingchengfit.saasbase.bill.presenter;

import android.support.annotation.StringRes;
import cn.qingchengfit.RxBus;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.events.EventTxT;
import cn.qingchengfit.events.NetWorkDialogEvent;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.R;
import cn.qingchengfit.saasbase.SaasConstant;
import cn.qingchengfit.saasbase.bill.beans.BillPayStatus;
import cn.qingchengfit.saasbase.bill.beans.BusinessBill;
import cn.qingchengfit.saasbase.bill.network.BusinessOrderWrap;
import cn.qingchengfit.saasbase.repository.IBillModel;
import cn.qingchengfit.saasbase.utils.ServerNotReadyExcetption;
import cn.qingchengfit.subscribes.BusSubscribe;
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

  public BusinessBill getBill() {
    return bill;
  }

  public void setBill(BusinessBill bill) {
    this.bill = bill;
  }

  public String getBillId() {
    return billId;
  }

  public void setBillId(String billId) {
    this.billId = billId;
  }

  @Inject public BillDetailPresenterPresenter() {
  }

  public String getCardId() {
    if (bill != null && bill.extra != null && bill.extra.card != null) {
      return bill.extra.card.getId();
    } else {
      return null;
    }
  }

  int errorTime = 0;

  public void rollBill() {
    RxBus.getBus().post(new NetWorkDialogEvent(NetWorkDialogEvent.EVENT_POST));
    sp = RxRegiste(billModel.queryBillStatus(billId)
      .subscribeOn(Schedulers.computation())
      .flatMap(
        new Func1<QcDataResponse<BillPayStatus>, Observable<QcDataResponse<BusinessOrderWrap>>>() {
          @Override public Observable<QcDataResponse<BusinessOrderWrap>> call(
            QcDataResponse<BillPayStatus> billPayStatusQcDataResponse) {
            if (!billPayStatusQcDataResponse.data.success) {
              throw new ServerNotReadyExcetption();
            }
            return billModel.getBusinessOrderDetail(billId)
              .subscribeOn(Schedulers.io())
              .observeOn(AndroidSchedulers.mainThread());
          }
        })
      .retryWhen(new Func1<Observable<? extends Throwable>, Observable<?>>() {
        @Override public Observable<?> call(final Observable<? extends Throwable> observable) {
          return observable.delay(3, TimeUnit.SECONDS)
            .flatMap(new Func1<Throwable, Observable<? extends Throwable>>() {
              @Override public Observable<? extends Throwable> call(Throwable throwable) {
                if (throwable instanceof ServerNotReadyExcetption) {
                  return Observable.just(null);
                } else {
                  if (errorTime < 5) {
                    errorTime++;
                    return Observable.just(null);
                  } else {
                    return Observable.error(throwable);
                  }
                }
              }
            });
        }
      })
      .subscribe(reSult));
  }

  public void queryBill() {
    RxRegiste(billModel.getBusinessOrderDetail(billId)
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(reSult));
  }

  private void exitRemarks(String remarks) {
    RxRegiste(billModel.editBusinessOrderMark(billId, remarks)
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse>() {
        @Override public void onNext(QcDataResponse qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            queryBill();
          } else {
            view.onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  NetSubscribe<QcDataResponse<BusinessOrderWrap>> reSult =
    new NetSubscribe<QcDataResponse<BusinessOrderWrap>>() {
      @Override public void onNext(QcDataResponse<BusinessOrderWrap> qcResponse) {
        RxBus.getBus().post(new NetWorkDialogEvent(NetWorkDialogEvent.EVENT_HIDE_DIALOG));
        if (ResponseConstant.checkSuccess(qcResponse)) {
          if (sp != null && !sp.isUnsubscribed()) sp.unsubscribe();
          bill = qcResponse.data.bill;
          view.onOrderDetail(qcResponse.data.bill);
        } else {
          view.onShowError(qcResponse.getMsg());
        }
      }

      @Override public void onError(Throwable e) {
        super.onError(e);
        if (view != null) view.showAlert(e.getMessage());
      }
    };

  @StringRes public int getPayType(String s) {
    switch (s) {
      case SaasConstant.PAY_TYPE_WX:
        return R.string.pay_wx;
      case SaasConstant.PAY_TYPE_QQPAY:
        return R.string.pay_qq_success;
      case SaasConstant.PAY_TYPE_ALIPAY:
        return R.string.pay_ali;
      case SaasConstant.PAY_TYPE_UNIONPAY:
        return R.string.pay_union_success;
      //case SaasConstant.PAY_TYPE_CARD:
      default:
        return R.string.pay_other_success;
    }
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
    RxBusAdd(EventTxT.class).subscribe(new BusSubscribe<EventTxT>() {
      @Override public void onNext(EventTxT eventTxT) {
        exitRemarks(eventTxT.txt);
      }
    });
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public interface MVPView extends CView {
    void onOrderDetail(BusinessBill order);
  }
}
