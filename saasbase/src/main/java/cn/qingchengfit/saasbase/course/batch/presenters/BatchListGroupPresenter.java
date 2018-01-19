package cn.qingchengfit.saasbase.course.batch.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.saasbase.course.batch.bean.BatchCourse;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BatchListGroupPresenter extends BasePresenter {

  @Inject GymWrapper gymWrapper;
  @Inject ICourseModel courseModel;
  private MVPView view;

  @Inject public BatchListGroupPresenter() {
  }

  public void getBatchList() {
    RxRegiste(courseModel.qcGetGroupBatch()
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(qcResponse -> {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            view.onList(qcResponse.data.courses);
          } else {
            view.onShowError(qcResponse.getMsg());
          }
        }, new NetWorkThrowable()));
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public interface MVPView extends CView {
    void onList(List<BatchCourse> coaches);
  }
}
