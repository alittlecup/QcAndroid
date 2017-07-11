package cn.qingchengfit.recruit.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.recruit.model.JobFair;
import cn.qingchengfit.recruit.model.Resume;
import cn.qingchengfit.recruit.network.GetApi;
import cn.qingchengfit.recruit.network.response.JobFariListWrap;
import cn.qingchengfit.recruit.network.response.ResumeListWrap;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class ResumeMarketPresenter extends BasePresenter {
  @Inject GymWrapper gymWrapper;
  @Inject QcRestRepository qcRestRepository;
  int page = 1, pageTotal = 1;
  private MVPView view;

  @Inject public ResumeMarketPresenter() {
  }

  public void queryResumeMarkets(boolean refresh, HashMap<String, Object> p) {
    if (refresh) page = pageTotal = 1;
    if (page <= pageTotal) {
      p.put("page", page);
      RxRegiste(qcRestRepository.createGetApi(GetApi.class)
          .queryResumeMarkets(p)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<QcDataResponse<ResumeListWrap>>() {
            @Override public void call(QcDataResponse<ResumeListWrap> qcResponse) {
              if (qcResponse.status == 200) {
                view.onResumeList(qcResponse.data.resumes, qcResponse.data.total_count, page);
                page++;
                pageTotal = qcResponse.data.pages;
              } else {
                view.onShowError(qcResponse.getMsg());
              }
            }
          }, new NetWorkThrowable()));
    } else {
      view.onResumeList(null, 1, 1);
    }
  }

  public void queryStarredResume(boolean refresh, HashMap<String, Object> p) {
    if (refresh) page = pageTotal = 1;
    if (page <= pageTotal) {
      p.put("page", page);
      RxRegiste(qcRestRepository.createGetApi(GetApi.class)
          .queryStarredResume(p)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<QcDataResponse<ResumeListWrap>>() {
            @Override public void call(QcDataResponse<ResumeListWrap> qcResponse) {
              if (qcResponse.status == 200) {
                view.onResumeList(qcResponse.data.resumes, qcResponse.data.total_count, page);
                page++;
                pageTotal = qcResponse.data.pages;
              } else {
                view.onShowError(qcResponse.getMsg());
              }
            }
          }, new NetWorkThrowable()));
    } else {
      view.onResumeList(null, 1, 1);
    }
  }

  /**
   * 1     # 不合适
   * 2     # 录用
   * 3     # 待沟通
   * 4     # 未处理
   */
  public void queryRecieveResume(boolean refresh, String jobid, int status) {
    if (refresh) page = pageTotal = 1;
    if (page <= pageTotal) {
      HashMap<String, Object> p = new HashMap<>();
      p.put("page", page);
      p.put("job_id", jobid);
      p.put("status", status);
      RxRegiste(qcRestRepository.createGetApi(GetApi.class)
          .queryRecieveResume(p)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<QcDataResponse<ResumeListWrap>>() {
            @Override public void call(QcDataResponse<ResumeListWrap> qcResponse) {
              if (qcResponse.status == 200) {
                view.onResumeList(qcResponse.data.resumes, qcResponse.data.total_count, page);
                page++;
                pageTotal = qcResponse.data.pages;
              } else {
                view.onShowError(qcResponse.getMsg());
              }
            }
          }, new NetWorkThrowable()));
    } else {
      view.onResumeList(null, 1, 1);
    }
  }

  /**
   * 1     # 不合适
   * 2     # 录用
   * 3     # 待沟通
   * 4     # 未处理
   */
  public void queryInvitedResume(boolean refresh, String jobid, int status) {
    if (refresh) page = pageTotal = 1;
    if (page <= pageTotal) {
      HashMap<String, Object> p = new HashMap<>();
      p.put("page", page);
      p.put("job_id", jobid);
      p.put("status", status);
      RxRegiste(qcRestRepository.createGetApi(GetApi.class).queryInvitedResume(p)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<QcDataResponse<ResumeListWrap>>() {
            @Override public void call(QcDataResponse<ResumeListWrap> qcResponse) {
              if (qcResponse.status == 200) {
                view.onResumeList(qcResponse.data.resumes, qcResponse.data.total_count, page);
                page++;
                pageTotal = qcResponse.data.pages;
              } else {
                view.onShowError(qcResponse.getMsg());
              }
            }
          }, new NetWorkThrowable()));
    } else {
      view.onResumeList(null, 1, 1);
    }
  }

  /**
   * 某场招聘会下的简历列表
   */
  public void queryJobFairResumes(String fairid) {
    RxRegiste(qcRestRepository.createGetApi(GetApi.class)
        .queryJobFairResumes(fairid)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<ResumeListWrap>>() {
          @Override public void call(QcDataResponse<ResumeListWrap> qcResponse) {
            if (qcResponse.status == 200) {
              view.onResumeList(qcResponse.data.resumes, qcResponse.data.total_count, page);
              page++;
              pageTotal = qcResponse.data.pages;
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public void queryMyJobFairList() {
    RxRegiste(qcRestRepository.createGetApi(GetApi.class)
        .queryMyJobFairs()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<JobFariListWrap>>() {
          @Override public void call(QcDataResponse<JobFariListWrap> qcResponse) {
            if (qcResponse.status == 200) {
              view.onJobFaris(qcResponse.data.fairs, qcResponse.data.job_count);
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
    void onResumeList(List<Resume> resumes, int total, int page);

    void onJobFaris(List<JobFair> jobfairs, int job_count);
  }
}
