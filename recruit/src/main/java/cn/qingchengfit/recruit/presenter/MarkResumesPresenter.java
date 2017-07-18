package cn.qingchengfit.recruit.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.recruit.network.PostApi;
import cn.qingchengfit.recruit.network.body.MarkResumeBody;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MarkResumesPresenter extends BasePresenter {
  @Inject QcRestRepository qcRestRepository;
  private MVPView view;

  @Inject public MarkResumesPresenter() {
  }

  public void markResume(MarkResumeBody body) {
    RxRegiste(qcRestRepository.createPostApi(PostApi.class)
        .markResume(body)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.markOk();
            } else {
              view.onShowError(qcResponse.getMsg());
            }
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
    void markOk();
  }
}
