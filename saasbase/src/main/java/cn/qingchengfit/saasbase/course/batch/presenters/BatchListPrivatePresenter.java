package cn.qingchengfit.saasbase.course.batch.presenters;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.course.batch.bean.BatchCoach;
import cn.qingchengfit.saasbase.course.batch.network.response.BatchCoachListWrap;
import cn.qingchengfit.saasbase.repository.ICourseModel;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class BatchListPrivatePresenter extends BasePresenter {

  @Inject GymWrapper gymWrapper;
  @Inject ICourseModel courseApi;
  private MVPView view;

  @Inject public BatchListPrivatePresenter() {
  }

  public void getBatchList() {
//    RxRegiste(courseApi.qcGetPrivateCrourse()
//        .onBackpressureLatest()
//        .subscribeOn(Schedulers.io())
//        .observeOn(AndroidSchedulers.mainThread())
//        .subscribe(new Action1<QcDataResponse<BatchCoachListWrap>>() {
//          @Override public void call(QcDataResponse<BatchCoachListWrap> qcResponse) {
//            if (ResponseConstant.checkSuccess(qcResponse)) {
//              view.onList(qcResponse.data.coaches);
//            } else {
//              view.onShowError(qcResponse.getMsg());
//            }
//          }
//        }, new NetWorkThrowable()));
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public interface MVPView extends CView {
    void onList(List<BatchCoach> coaches);
  }
}
