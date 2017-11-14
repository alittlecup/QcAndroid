package cn.qingchengfit.saasbase.course.batch.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import javax.inject.Inject;

public class BatchListGroupPresenter extends BasePresenter {

  @Inject GymWrapper gymWrapper;
  //@Inject ICourseModel courseApi;
  private MVPView view;

  @Inject public BatchListGroupPresenter() {
  }

  public void getBatchList() {
    //RxRegiste(courseApi.qcGetGroupCourse()
    //    .onBackpressureLatest()
    //    .subscribeOn(Schedulers.io())
    //    .observeOn(AndroidSchedulers.mainThread())
    //    .subscribe(new Action1<QcDataResponse<BatchCourseListWrap>>() {
    //      @Override public void call(QcDataResponse<BatchCourseListWrap> qcResponse) {
    //        if (ResponseConstant.checkSuccess(qcResponse)) {
    //          view.onList(qcResponse.data.courses);
    //        } else {
    //          view.onShowError(qcResponse.getMsg());
    //        }
    //      }
    //    }, new NetWorkThrowable()));
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public interface MVPView extends CView {
    //void onList(List<BatchCourse> coaches);
  }
}
