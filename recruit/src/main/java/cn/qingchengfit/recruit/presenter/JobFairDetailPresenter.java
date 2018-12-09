package cn.qingchengfit.recruit.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.recruit.item.RecruitPositionItem;
import cn.qingchengfit.recruit.item.ResumeItem;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.recruit.model.JobFair;
import cn.qingchengfit.recruit.model.Resume;
import cn.qingchengfit.recruit.network.GetApi;
import cn.qingchengfit.recruit.network.response.JobFairWrap;
import cn.qingchengfit.recruit.network.response.JobListWrap;
import cn.qingchengfit.recruit.network.response.ResumeListWrap;
import cn.qingchengfit.utils.ListUtils;
import eu.davidea.flexibleadapter.items.IFlexible;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class JobFairDetailPresenter extends BasePresenter {
  @Inject QcRestRepository qcRestRepository;
  private MVPView view;

  @Inject public JobFairDetailPresenter() {
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  public void queryJobFairDetail(String id) {
    RxRegiste(qcRestRepository.createRxJava1Api(GetApi.class)
        .queryStaffJobFairDetail(id).onBackpressureBuffer().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<JobFairWrap>>() {
          @Override public void call(QcDataResponse<JobFairWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onJobfairDetail(qcResponse.data.fair);
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public void queryJobFairJobs(String fairid, HashMap<String, Object> params) {
    RxRegiste(qcRestRepository.createRxJava1Api(GetApi.class)
        .queryJobFairJobs(fairid, ListUtils.mapRemoveNull(params))
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<JobListWrap>>() {
          @Override public void call(QcDataResponse<JobListWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              if (qcResponse.data.jobs == null) return;
              List<IFlexible> item = new ArrayList<IFlexible>();
              for (Job resume : qcResponse.data.jobs) {
                item.add(new RecruitPositionItem(resume));
              }
              view.onList(item);
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public void queryJobFairResumes(String fairid, HashMap<String, Object> params) {

    RxRegiste(qcRestRepository.createRxJava1Api(GetApi.class)
        .queryJobFairResumes(fairid, ListUtils.mapRemoveNull(params))
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<ResumeListWrap>>() {
          @Override public void call(QcDataResponse<ResumeListWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              if (qcResponse.data.resumes == null) return;
              List<IFlexible> item = new ArrayList<IFlexible>();
              for (Resume resume : qcResponse.data.resumes) {
                item.add(new ResumeItem(resume));
              }
              view.onList(item);
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public interface MVPView extends CView {
    void onJobfairDetail(JobFair jobfair);

    void onList(List list);
  }
}
