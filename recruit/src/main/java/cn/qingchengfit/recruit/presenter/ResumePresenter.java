package cn.qingchengfit.recruit.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.recruit.model.Certificate;
import cn.qingchengfit.recruit.model.Education;
import cn.qingchengfit.recruit.model.ResumeHome;
import cn.qingchengfit.recruit.model.WorkExp;
import cn.qingchengfit.recruit.network.GetApi;
import cn.qingchengfit.recruit.network.response.CertificateListWrap;
import cn.qingchengfit.recruit.network.response.EduExpListWrap;
import cn.qingchengfit.recruit.network.response.ResumeHomeWrap;
import cn.qingchengfit.recruit.network.response.WorkExpListWrap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ResumePresenter extends BasePresenter {

  @Inject QcRestRepository restRepository;
  private MVPView view;

  @Inject public ResumePresenter() {
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  /**
   * 简历详情
   */
  public void queryResumeHome() {
    RxRegiste(restRepository.createGetApi(GetApi.class)
        .queryMyResumeHome()
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(new Action1<QcDataResponse<ResumeHomeWrap>>() {
          @Override public void call(QcDataResponse<ResumeHomeWrap> resumeHomeQcDataResponse) {
            if (resumeHomeQcDataResponse.getStatus() == 200) {
              view.onBaseInfo(resumeHomeQcDataResponse.data.resume);
            } else {
              view.onShowError(resumeHomeQcDataResponse.getMsg());
            }
          }
        }));
  }

  public void queryEducations() {
    RxRegiste(restRepository.createGetApi(GetApi.class)
        .queryEducations()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<EduExpListWrap>>() {
          @Override public void call(QcDataResponse<EduExpListWrap> eduExpListWrapQcDataResponse) {
            if (eduExpListWrapQcDataResponse.getStatus() == 200) {
              view.onEduExpList(eduExpListWrapQcDataResponse.data.educations);
            } else {
              view.onShowError(eduExpListWrapQcDataResponse.getMsg());
            }
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            view.onShowError(throwable.getMessage());
          }
        }));
  }

  public void queryWorkExps() {
    RxRegiste(restRepository.createGetApi(GetApi.class)
        .queryWorkExps()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<WorkExpListWrap>>() {
          @Override public void call(QcDataResponse<WorkExpListWrap> workExpListWrapQcDataResponse) {
            if (workExpListWrapQcDataResponse.getStatus() == 200) {
              view.onWorkExpList(workExpListWrapQcDataResponse.data.experiences);
            } else {
              view.onShowError(workExpListWrapQcDataResponse.getMsg());
            }
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            view.onShowError(throwable.getMessage());
          }
        }));
  }

  public void queryExps() {
    RxRegiste(restRepository.createGetApi(GetApi.class)
        .queryCertifications()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<CertificateListWrap>>() {
          @Override public void call(QcDataResponse<CertificateListWrap> qcResonese) {
            if (qcResonese.getStatus() == 200) {
              view.onCertiList(qcResonese.data.certificates);
            } else {
              view.onShowError(qcResonese.getMsg());
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
    void onBaseInfo(ResumeHome resumeHome);

    void onWorkExpList(List<WorkExp> workExps);

    void onEduExpList(List<Education> eduExps);

    void onCertiList(List<Certificate> certificates);
  }
}
