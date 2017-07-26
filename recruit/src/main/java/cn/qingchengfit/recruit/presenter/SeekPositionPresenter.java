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
import cn.qingchengfit.recruit.network.response.JobListIndex;
import cn.qingchengfit.recruit.network.response.JobListWrap;
import cn.qingchengfit.utils.ListUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class SeekPositionPresenter extends BasePresenter {

  protected MVPView view;
  @Inject QcRestRepository restRepository;
  int page, totalPage = 1;
  /**
   * 查询职位列表（分页）
   */
  boolean isSync = false;

  @Inject public SeekPositionPresenter() {
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public void queryList(boolean init, HashMap<String, Object> params) {
    params = ListUtils.mapRemoveNull(params);
    if (init) {
      page = totalPage = 1;
    }
    if (isSync) return;
    if (page <= totalPage) {
      isSync = true;
      RxRegiste(restRepository.createGetApi(GetApi.class)
          .queryJobList(page, params).throttleFirst(500, TimeUnit.MILLISECONDS)
          .onBackpressureBuffer()
          .subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<QcDataResponse<JobListWrap>>() {
            @Override public void call(QcDataResponse<JobListWrap> jobListWrapQcDataResponse) {
              view.onList(jobListWrapQcDataResponse.data.jobs, page,
                  jobListWrapQcDataResponse.data.total_count);
              totalPage = jobListWrapQcDataResponse.data.pages;
              page++;
              isSync = false;
            }
          }, new NetWorkThrowable()));
    } else {
      view.onList(null, 0, 1);
    }
  }

  public void queryIndex() {
    RxRegiste(restRepository.createPostApi(GetApi.class)
        .queryJobsIndex().onBackpressureBuffer().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<JobListIndex>>() {
          @Override public void call(QcDataResponse<JobListIndex> jobListIndexQcDataResponse) {
            view.onJobsIndex(jobListIndexQcDataResponse.data);
          }
        }, new NetWorkThrowable()));
  }

  /**
   * 联系HR
   */
  public void contactHR() {

  }

  public List<String> filterSalary() {
    List<String> salaryList = new ArrayList<>();
    salaryList.add("全部");
    salaryList.add("面议");
    salaryList.add("5k以下");
    salaryList.add("5k-10k");
    salaryList.add("10k-15k");
    salaryList.add("15k-20k");
    salaryList.add("20k-50k");
    salaryList.add("50k以上");
    return salaryList;
  }

  public interface MVPView extends CView {

    void onList(List<Job> jobs, int page, int totalCount);

    void onGym(Gym service);

    void onJobsIndex(JobListIndex index);
  }
}
