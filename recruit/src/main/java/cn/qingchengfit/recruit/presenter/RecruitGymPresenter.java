package cn.qingchengfit.recruit.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.recruit.network.GetApi;
import cn.qingchengfit.recruit.network.PostApi;
import cn.qingchengfit.recruit.network.body.RecruitGymBody;
import cn.qingchengfit.saas.response.GymWrap;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RecruitGymPresenter extends BasePresenter {
  @Inject QcRestRepository qcRestRepository;
  private MVPView view;

  @Inject public RecruitGymPresenter() {
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  public void saveInfo(String gymid, RecruitGymBody gymBody) {
    RxRegiste(qcRestRepository.createPostApi(PostApi.class).editGymIntro(gymid, gymBody)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onSaveOk();
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public void queryGymInfo(String gymid) {
    RxRegiste(qcRestRepository.createGetApi(GetApi.class)
        .queryGymInfo(gymid)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<GymWrap>>() {
          @Override public void call(QcDataResponse<GymWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onDetail(qcResponse.data.gym);
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public interface MVPView extends CView {
    void onSaveOk();

    void onDetail(Gym gym);
  }
}
