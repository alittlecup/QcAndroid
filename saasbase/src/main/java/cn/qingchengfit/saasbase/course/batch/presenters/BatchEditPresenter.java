package cn.qingchengfit.saasbase.course.batch.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.cards.event.EventBatchPayCard;
import cn.qingchengfit.saasbase.course.batch.bean.BatchDetail;
import cn.qingchengfit.saasbase.course.batch.network.response.BatchDetailWrap;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import cn.qingchengfit.subscribes.BusSubscribe;
import cn.qingchengfit.subscribes.NetSubscribe;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BatchEditPresenter extends BasePresenter {
  private MVPView view;

  @Inject ICourseModel courseApi;

  String batchId;
  boolean isPrivate;

  public boolean isPrivate() {
    return isPrivate;
  }

  public void setPrivate(boolean aPrivate) {
    isPrivate = aPrivate;
  }

  public String getBatchId() {
    return batchId;
  }

  public void setBatchId(String batchId) {
    this.batchId = batchId;
  }

  @Inject public BatchEditPresenter() {

  }

  public void queryData() {
    RxRegiste(courseApi.qcGetBatchDetail(batchId)
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new NetSubscribe<QcDataResponse<BatchDetailWrap>>() {
          @Override public void onNext(QcDataResponse<BatchDetailWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onBatchDetail(qcResponse.data.batch);
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }));
  }

  /**
   * 删除排期
   */
  public void delBatch(){

  }

  /**
   * 修改某一天的时间
   */
  public void editBatchTime(){

  }

  /**
   * 保存排期
   */
  public void editBatch(){

  }



  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public interface MVPView extends CView {
    void onBatchDetail(BatchDetail batchDetail);

  }
}
