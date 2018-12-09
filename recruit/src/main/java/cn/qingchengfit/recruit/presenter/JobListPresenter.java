package cn.qingchengfit.recruit.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.recruit.network.GetApi;
import cn.qingchengfit.recruit.network.response.JobListWrap;
import cn.qingchengfit.utils.ListUtils;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class JobListPresenter extends BasePresenter {
  @Inject QcRestRepository qcRestRepository;
  /**
   * 查询职位列表（分页）
   */
  int page, totalPage = 1;
  private MVPView view;

  @Inject public JobListPresenter() {
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
    if (page <= totalPage) {
      RxRegiste(qcRestRepository.createRxJava1Api(GetApi.class)
          .queryJobList(page, params).onBackpressureBuffer().subscribeOn(Schedulers.io())
          .observeOn(AndroidSchedulers.mainThread())
          .subscribe(new Action1<QcDataResponse<JobListWrap>>() {
            @Override public void call(QcDataResponse<JobListWrap> jobListWrapQcDataResponse) {
              view.onList(jobListWrapQcDataResponse.data.jobs, page,
                  jobListWrapQcDataResponse.data.total_count);
              totalPage = jobListWrapQcDataResponse.data.pages;
              page++;
            }
          }, new NetWorkThrowable()));
    } else {
      view.onList(null, 1, 1);
    }
  }

  public interface MVPView extends CView {
    void onList(List<Job> jobs, int page, int total);
  }
}
