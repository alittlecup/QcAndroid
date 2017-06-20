package cn.qingchengfit.recruit.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.recruit.network.GetApi;
import cn.qingchengfit.recruit.network.PostApi;
import cn.qingchengfit.recruit.network.response.JobDetailWrap;
import cn.qingchengfit.recruit.network.response.JobListIndex;
import cn.qingchengfit.recruit.network.response.JobListWrap;
import cn.qingchengfit.utils.LogUtil;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SeekPositionPresenter extends BasePresenter {
  protected MVPView view;
  @Inject QcRestRepository restRepository;

  @Inject public SeekPositionPresenter() {
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  /**
   * 查询职位列表（分页）
   */
  public void queryList(final int page) {
    RxRegiste(restRepository.createGetApi(GetApi.class)
        .queryJobList(page)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<JobListWrap>>() {
          @Override public void call(QcDataResponse<JobListWrap> jobListWrapQcDataResponse) {
            view.onList(jobListWrapQcDataResponse.data.jobs, page, jobListWrapQcDataResponse.data.total_count);
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            view.onShowError(throwable.getMessage());
          }
        }));
  }

  public void queryIndex() {
    RxRegiste(restRepository.createPostApi(GetApi.class)
        .queryJobsIndex()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<JobListIndex>>() {
          @Override public void call(QcDataResponse<JobListIndex> jobListIndexQcDataResponse) {
            view.onJobsIndex(jobListIndexQcDataResponse.data.completion, jobListIndexQcDataResponse.data.fair_count,
                jobListIndexQcDataResponse.data.avatar, jobListIndexQcDataResponse.data.fair_banner);
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            view.onShowError(throwable.getMessage());
          }
        }));
  }

  /**
   * 查询职位详情
   */
  public void queryDetail(String jobid) {

    RxRegiste(restRepository.createGetApi(GetApi.class)
        .queryJobDetail(jobid)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<JobDetailWrap>>() {
          @Override public void call(QcDataResponse<JobDetailWrap> jobDetailWrapQcDataResponse) {
            view.onJob(jobDetailWrapQcDataResponse.data.job);
            view.onGym(jobDetailWrapQcDataResponse.data.job.gym);
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            LogUtil.e(throwable.getMessage());
            view.onShowError(throwable.getMessage());
          }
        }));
  }

  /**
   * 联系HR
   */
  public void contactHR() {

  }

  /**
   * 收藏职位
   */
  public void starPosition(Object jobid) {
    HashMap<String, Object> params = new HashMap<String, Object>();
    params.put("id", jobid);
    RxRegiste(restRepository.createPostApi(PostApi.class)
        .starJob(params)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            if (qcResponse.getStatus() == 200) {
              view.starOK();
            }
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            view.onShowError(throwable.getMessage());
          }
        }));
  }

  /**
   * 取消收藏职位
   */
  public void unstarPosition(String jobid) {
    RxRegiste(restRepository.createPostApi(PostApi.class)
        .cancelStarJob(jobid)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            if (qcResponse.getStatus() == 200) {
              view.starOK();
            }
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            view.onShowError(throwable.getMessage());
          }
        }));
  }

  /**
   * 投递简历
   */
  public void sendResume(String jobid) {
    HashMap<String, Object> params = new HashMap<String, Object>();
    params.put("id", jobid);
    RxRegiste(restRepository.createPostApi(PostApi.class)
        .sendResume(params)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            if (qcResponse.getStatus() == 200) {
              view.starOK();
            }
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            view.onShowError(throwable.getMessage());
          }
        }));
  }

  public interface MVPView extends CView {
    void onJob(Job job);

    void onList(List<Job> jobs, int page, int totalCount);

    void onGym(Gym service);

    void starOK();

    void unStarOk();

    void sendResumeOk();

    void onJobsIndex(Number completed, int fair_count, String avatar, String fiar_banner);
  }
}
