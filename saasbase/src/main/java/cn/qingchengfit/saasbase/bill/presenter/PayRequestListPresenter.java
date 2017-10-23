package cn.qingchengfit.saasbase.bill.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.bill.beans.BillLock;
import cn.qingchengfit.saasbase.bill.beans.PayRequest;
import cn.qingchengfit.saasbase.bill.network.PayRequestListWrap;
import cn.qingchengfit.saasbase.repository.IBillModel;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.utils.CmStringUtils;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class PayRequestListPresenter extends BasePresenter {
  private MVPView view;

  @Inject IBillModel billModel;

  @Inject public PayRequestListPresenter() {
  }

  int totalPage = 1;

  public int getTotalPage() {
    return totalPage;
  }

  public void loadMoreData(int currentPage) {
    RxRegiste(billModel.getPayRequestList(currentPage)
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<PayRequestListWrap>>() {
        @Override public void onNext(QcDataResponse<PayRequestListWrap> qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            view.onGetData(qcResponse.data.datas, qcResponse.data.current_page);
            totalPage = qcResponse.data.pages;
          } else {
            view.onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  /**
   * 判断是否有orderNO 有->直接支付  无->获取ID
   */
  public void pay(PayRequest payRequest) {
    if (CmStringUtils.isEmpty(payRequest.order_no)) {
      view.onPay(payRequest);
    } else {
      getBzlzId(payRequest.task_no, payRequest);
    }
  }

  private void getBzlzId(String taskId, final PayRequest pr) {
    RxRegiste(billModel.lockPayRequest(taskId)
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<BillLock>>() {
        @Override public void onNext(QcDataResponse<BillLock> qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            pr.order_no = qcResponse.data.order_no;
            view.onPay(pr);
          } else {
            view.onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  public void cancelTask(final String taskId) {
    RxRegiste(billModel.cancelPayRequest(taskId)
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse>() {
        @Override public void onNext(QcDataResponse qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            view.onRemoveTaskNo(taskId);
          } else {
            view.onShowError(qcResponse.getMsg());
          }
        }
      }));

  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public interface MVPView extends CView {

    void onGetData(List<PayRequest> datas, int page);

    void onPay(PayRequest payRequest);

    void onRemoveTaskNo(String taskNo);
  }
}
