package cn.qingchengfit.saasbase.course.batch.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.course.batch.bean.BatchSchedule;
import cn.qingchengfit.saasbase.course.batch.network.response.BatchSchedulesWrap;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import cn.qingchengfit.subscribes.NetSubscribe;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BatchScheduleListPresenter extends BasePresenter {
  private MVPView view;

  @Inject GymWrapper gymWrapper;
  @Inject ICourseModel courseModel;

  private String batchId;
  private boolean isPrivate;

  public boolean isPrivate() {
    return isPrivate;
  }

  public void setPrivate(boolean aPrivate) {
    isPrivate = aPrivate;
  }

  @Inject public BatchScheduleListPresenter() {
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public void queryList() {
    RxRegiste(courseModel.qcGetbatchSchedules(batchId, isPrivate)
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new NetSubscribe<QcDataResponse<BatchSchedulesWrap>>() {
          @Override public void onNext(QcDataResponse<BatchSchedulesWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onList(isPrivate?qcResponse.data.timetables:qcResponse.data.schedules);
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }));
  }

  public void del(){

  }


  public interface MVPView extends CView {
    void onList(List<BatchSchedule> list);
    void onSuccess();
  }
}
