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
import cn.qingchengfit.recruit.network.response.JobFariListWrap;
import cn.qingchengfit.utils.DateUtils;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class JobFairPresenter extends BasePresenter {
  @Inject QcRestRepository qcRestRepository;
  private MVPView view;

  @Inject public JobFairPresenter() {
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  /**
   * 所有招聘会列表
   * ：筛选正在报名{"end__gte": ：今日}
   * 筛选已结束{"end__lt": 今日}
   *
   * @param type 0,正在报名，1，已经结束
   */
  public void queryJobFairs(int type) {
    HashMap<String, Object> params = new HashMap<>();
    if (type == 0) {
      params.put("end__gte", DateUtils.getStringToday());
    } else {
      params.put("end__lt", DateUtils.getStringToday());
    }
    RxRegiste(qcRestRepository.createGetApi(GetApi.class)
        .queryJobFairs(params)
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<JobFariListWrap>>() {
          @Override public void call(QcDataResponse<JobFariListWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onJobFairList(qcResponse.data.fairs);
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  /**
   * 我的招聘会（工作人员）
   */
  public void queryStaffMyJobFairs() {
    RxRegiste(qcRestRepository.createGetApi(GetApi.class)
        .queryMyJobFairs()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<JobFariListWrap>>() {
          @Override public void call(QcDataResponse<JobFariListWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onJobFairList(qcResponse.data.fairs);
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  /**
   * 我的招聘会（应聘人员）
   */
  public void queryUserMyJobFairs() {
    RxRegiste(qcRestRepository.createGetApi(GetApi.class)
        .queryUserMyJobFairs()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<JobFariListWrap>>() {
          @Override public void call(QcDataResponse<JobFariListWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onJobFairList(qcResponse.data.fairs);
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public interface MVPView extends CView {
    void onJobFairList(List<JobFair> jobfairs);
  }
}
