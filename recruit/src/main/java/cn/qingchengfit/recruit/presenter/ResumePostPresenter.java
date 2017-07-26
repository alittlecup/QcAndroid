package cn.qingchengfit.recruit.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.recruit.network.PostApi;
import cn.qingchengfit.recruit.network.body.ResumeBody;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ResumePostPresenter extends BasePresenter {
  @Inject QcRestRepository qcRestRepository;
  private MVPView view;

  @Inject public ResumePostPresenter() {
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  public void editResume(ResumeBody body) {
    RxRegiste(qcRestRepository.createGetApi(PostApi.class)
        .updateResume(body)
        .observeOn(AndroidSchedulers.mainThread())
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            if (qcResponse.getStatus() == 200) {
              view.onPostOk();
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            view.onShowError(throwable.getMessage());
          }
        }));
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public interface MVPView extends CView {
    void onPostOk();
  }
}
