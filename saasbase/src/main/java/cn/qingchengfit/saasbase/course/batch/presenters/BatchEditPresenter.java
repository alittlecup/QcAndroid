package cn.qingchengfit.saasbase.course.batch.presenters;

import cn.qingchengfit.RxBus;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.course.batch.bean.BatchDetail;
import cn.qingchengfit.saasbase.course.batch.network.response.BatchDetailWrap;
import cn.qingchengfit.saascommon.events.EventSaasFresh;
import cn.qingchengfit.saascommon.utils.StringUtils;
import cn.qingchengfit.subscribes.NetSubscribe;
import cn.qingchengfit.utils.ToastUtils;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BatchEditPresenter extends IBatchPresenter {

  String batchId;
  BatchDetail batchDetail;

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
              body.open_rule = batchDetail.open_rule;
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
              RxBus.getBus().post(new EventSaasFresh.BatchList());
              mvpView.popBack();
            } else {
              mvpView.onShowError(qcDataResponse.getMsg());
            }
          }
        }));
  }

  /**
   * 保存排期
   */
  @Override public void arrangeBatch() {
    int err = body.checkAddBatch();

    if (err > 0) {
      mvpView.showAlert(err);
    } else if (!isPrivate && StringUtils.isEmpty(body.workout_plan_id)) {
      ToastUtils.show("请选择训练计划");
      return;
    } else {
      RxRegiste(courseApi.qcUpdateBatch(batchId, body)
          .onBackpressureLatest()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new NetSubscribe<QcDataResponse>() {
            @Override public void onNext(QcDataResponse qcResponse) {
              if (ResponseConstant.checkSuccess(qcResponse)) {
                mvpView.onShowError("保存成功");
                RxBus.getBus().post(new EventSaasFresh.BatchList());
                mvpView.popBack();
              } else {
                mvpView.onShowError(qcResponse.getMsg());
              }
            }
          }));
    }
  }
}
