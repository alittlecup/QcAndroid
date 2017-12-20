package cn.qingchengfit.saasbase.course.batch.presenters;

import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.course.batch.bean.BatchDetail;
import cn.qingchengfit.saasbase.course.batch.bean.Rule;
import cn.qingchengfit.saasbase.course.batch.network.response.BatchDetailWrap;
import cn.qingchengfit.subscribes.NetSubscribe;
import java.util.ArrayList;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BatchEditPresenter extends IBatchPresenter {

  String batchId;
  BatchDetail batchDetail;
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

  public BatchDetail getBatchDetail() {
    return batchDetail;
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
            batchDetail = qcResponse.data.batch;
            mvpView.onBatchDetail(qcResponse.data.batch);
          } else {
            mvpView.onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  /**
   * 删除排期
   */
  public void delBatch() {
    RxRegiste(courseApi.delBatch(batchId)
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse>() {
        @Override public void onNext(QcDataResponse qcDataResponse) {
          if (ResponseConstant.checkSuccess(qcDataResponse)) {
            mvpView.onShowError("删除成功");
            mvpView.popBack();
          } else {
            mvpView.onShowError(qcDataResponse.getMsg());
          }
        }
      }));
  }

  /**
   * 修改某一天的时间
   */
  public void editBatchTime() {
    //RxRegiste(courseApi.qcUpdateBatch(batchId,new ArrangeBatchBody.Builder()
    //    .time_repeats()
    //  .build())
    //  .onBackpressureLatest()
    //  .subscribeOn(Schedulers.io())
    //  .observeOn(AndroidSchedulers.mainThread())
    //  .subscribe(new NetSubscribe<QcDataResponse>() {
    //    @Override public void onNext(QcDataResponse qcDataResponse) {
    //      if (ResponseConstant.checkSuccess(qcDataResponse)){
    //        mvpView.onShowError("删除成功");
    //        mvpView.popBack();
    //      }else {
    //        mvpView.onShowError(qcDataResponse.getMsg());
    //      }
    //    }
    //  }));
  }

  /**
   * 保存排期
   */
  public void editBatch() {

  }

  @Override public void arrangeBatch() {
    body.teacher_id = mvpView.getTrainerId();
    body.course_id = mvpView.getCourseId();
    body.spaces = mvpView.getSupportSpace();
    body.max_users = mvpView.suportMemberNum();
    body.rules = (ArrayList<Rule>) mvpView.getRules();
    //body.batch_id = batchId;
    body.time_repeats = mvpView.getTimeRepeats();
    int err = body.checkAddBatch();
    if (err > 0) {
      mvpView.showAlert(err);
    } else {
      RxRegiste(courseApi.qcUpdateBatch(batchId, body)
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new NetSubscribe<QcDataResponse>() {
          @Override public void onNext(QcDataResponse qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              mvpView.onShowError("保存成功");
              mvpView.popBack();
            } else {
              mvpView.onShowError(qcResponse.getMsg());
            }
          }
        }));

    }
  }
}
