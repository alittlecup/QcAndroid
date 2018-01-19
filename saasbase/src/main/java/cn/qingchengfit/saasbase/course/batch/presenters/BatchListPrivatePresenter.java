package cn.qingchengfit.saasbase.course.batch.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.saasbase.course.batch.bean.BatchCoach;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class BatchListPrivatePresenter extends BasePresenter<BatchListPrivatePresenter.MVPView> {

  @Inject GymWrapper gymWrapper;
  @Inject ICourseModel courseApi;


  @Inject public BatchListPrivatePresenter() {
  }

  public void getBatchList() {
    RxRegiste(courseApi.qcGetPrivateBatch()
        .onBackpressureLatest()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(qcResponse -> {
          if (ResponseConstant.checkSuccess(qcResponse)) {
            mvpView.onList(qcResponse.data.coaches);
          } else {
            mvpView.onShowError(qcResponse.getMsg());
          }
        }, new NetWorkThrowable()));
  }



  public interface MVPView extends CView {
    void onList(List<BatchCoach> coaches);
  }
}
