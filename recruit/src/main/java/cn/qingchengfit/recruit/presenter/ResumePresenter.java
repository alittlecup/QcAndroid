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
import com.tencent.qcloud.timchat.chatmodel.ResumeModel;
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

  /**
   * 查看他人简历
   * @param resumeId   简历id
   */
  public void getResumeDetail(String resumeId){
    RxRegiste(restRepository.createGetApi(GetApi.class)
        .qcGetOtherResumeDetail(resumeId)
        .observeOn(AndroidSchedulers.mainThread())
        .subscribeOn(Schedulers.io())
        .subscribe(new Action1<QcDataResponse<ResumeHomeWrap>>() {
          @Override public void call(QcDataResponse<ResumeHomeWrap> resumeHomeQcDataResponse) {
            if (resumeHomeQcDataResponse.getStatus() == 200){
              view.onBaseInfo(resumeHomeQcDataResponse.data.resume);
            }else{
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

  public ResumeModel dealResumeMessage(ResumeHome resumeHome) {
    ResumeModel resumeModel = new ResumeModel();
    resumeModel.id = resumeHome.id;
    resumeModel.username = resumeHome.username;
    resumeModel.avatar = resumeHome.avatar;
    resumeModel.birthday = resumeHome.birthday;
    resumeModel.work_year = resumeHome.work_year;
    resumeModel.gender = resumeHome.gender;
    resumeModel.max_education = resumeHome.max_education;
    resumeModel.height = (int) resumeHome.height;
    resumeModel.min_salary = resumeHome.min_salary;
    resumeModel.max_salary = resumeHome.max_salary;
    if (resumeHome.exp_cities.size() > 0) {
      resumeModel.city = resumeHome.exp_cities.get(0);
    }
    resumeModel.weight = (int) resumeHome.weight;
    return resumeModel;
  }

  public void queryWorkExps() {
    RxRegiste(restRepository.createGetApi(GetApi.class)
        .queryWorkExps()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<WorkExpListWrap>>() {
          @Override
          public void call(QcDataResponse<WorkExpListWrap> workExpListWrapQcDataResponse) {
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
