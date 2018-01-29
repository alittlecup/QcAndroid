package cn.qingchengfit.saasbase.course.presenters;

import android.content.Intent;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.course.batch.network.response.QcResponsePrivateDetail;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import cn.qingchengfit.subscribes.NetSubscribe;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CourseBatchDetailPresenter extends BasePresenter<CourseBatchDetailPresenter.MVPView> {

  @Inject ICourseModel courseModel;

  @Inject public CourseBatchDetailPresenter() {
  }


  @Override public void attachIncomingIntent(Intent intent) {

  }

  @Override public void onCreate() {

  }



  public void delCourse() {

  }

  public void delBtach() {

  }

  public void queryGroup() {
    RxRegiste(courseModel.qcGetPrivateCoaches("group")
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<QcResponsePrivateDetail>>() {
        @Override public void onNext(QcDataResponse<QcResponsePrivateDetail> qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            mvpView.onBatchList(qcResponse.data.batches);
          } else {
            mvpView.onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  public void queryPrivate() {
    RxRegiste(courseModel.qcGetPrivateCoaches("private")
      .onBackpressureLatest()
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribe(new NetSubscribe<QcDataResponse<QcResponsePrivateDetail>>() {
        @Override public void onNext(QcDataResponse<QcResponsePrivateDetail> qcResponse) {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            mvpView.onBatchList(qcResponse.data.batches);
          } else {
            mvpView.onShowError(qcResponse.getMsg());
          }
        }
      }));
  }

  public interface MVPView extends CView {
    void onBatchList(List<QcResponsePrivateDetail.PrivateBatch> list);
  }
}