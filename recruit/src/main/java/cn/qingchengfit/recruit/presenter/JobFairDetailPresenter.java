package cn.qingchengfit.recruit.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.recruit.model.JobFair;
import cn.qingchengfit.recruit.network.GetApi;
import cn.qingchengfit.recruit.network.response.JobFairWrap;
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
    RxRegiste(qcRestRepository.createGetApi(GetApi.class)
        .queryStaffJobFairDetail(id)
        .subscribeOn(Schedulers.io())
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

  //public void queryJobFairJobs(String fairid) {
  //  RxRegiste(qcRestRepository.createGetApi(GetApi.class)
  //      .queryJobFairJobs(fairid)
  //      .subscribeOn(Schedulers.io())
  //      .observeOn(AndroidSchedulers.mainThread())
  //      .subscribe(new Action1<QcDataResponse<JobListWrap>>() {
  //        @Override public void call(QcDataResponse<JobListWrap> qcResponse) {
  //          if (ResponseConstant.checkSuccess(qcResponse)) {
  //            if (qcResponse.data.jobs == null) return;
  //            List<IFlexible> item = new ArrayList<IFlexible>();
  //            for (Job resume : qcResponse.data.jobs) {
  //              item.add(new RecruitPositionItem(resume));
  //            }
  //            view.onList(item);
  //          } else {
  //            view.onShowError(qcResponse.getMsg());
  //          }
  //        }
  //      }, new NetWorkThrowable()));
  //}

  public interface MVPView extends CView {
    void onJobfairDetail(JobFair jobfair);
  }
}
