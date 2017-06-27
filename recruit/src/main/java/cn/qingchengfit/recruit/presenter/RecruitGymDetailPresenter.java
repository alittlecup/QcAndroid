package cn.qingchengfit.recruit.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.recruit.network.GetApi;
import cn.qingchengfit.recruit.network.response.GymWrap;
import cn.qingchengfit.recruit.network.response.JobListWrap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class RecruitGymDetailPresenter extends BasePresenter {
  @Inject QcRestRepository restRepository;
  private MVPView view;
  private int page = 1, total = 1;

  @Inject public RecruitGymDetailPresenter() {
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  public void queryPositionOfGym(String gymid, final int init) {
    if (init == 1) page = total = 1;
    if (page <= total) {
      RxRegiste(restRepository.createGetApi(GetApi.class)
          .queryGymJobs(gymid, page)
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<QcDataResponse<JobListWrap>>() {
            @Override public void call(QcDataResponse<JobListWrap> jobListWrapQcDataResponse) {
              view.onJobList(jobListWrapQcDataResponse.data.jobs, page,
                  jobListWrapQcDataResponse.data.total_count);
              total = jobListWrapQcDataResponse.data.pages;
              page++;
            }
          }, new NetWorkThrowable()));
    } else {
      view.onJobList(null, 0, 0);
    }
  }

  public void queryGymDetail(String gymid) {
    RxRegiste(restRepository.createGetApi(GetApi.class)
        .queryGymInfo(gymid)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<GymWrap>>() {
          @Override public void call(QcDataResponse<GymWrap> objectQcDataResponse) {
            view.onGym(objectQcDataResponse.data.gym);
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
    void onGym(Gym gym);

    void onJobList(List<Job> jobs, int page, int totalCount);
  }
}
