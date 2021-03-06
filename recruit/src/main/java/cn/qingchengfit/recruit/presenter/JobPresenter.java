package cn.qingchengfit.recruit.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.model.base.Gym;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.ResponseConstant;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.recruit.R;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.recruit.network.GetApi;
import cn.qingchengfit.recruit.network.PostApi;
import cn.qingchengfit.recruit.network.body.InviteBody;
import cn.qingchengfit.recruit.network.body.JobBody;
import cn.qingchengfit.recruit.network.response.JobDetailWrap;
import cn.qingchengfit.recruit.network.response.JobListWrap;
import cn.qingchengfit.recruit.network.response.OnePermissionWrap;
import cn.qingchengfit.saas.response.GymWrap;
import com.tencent.qcloud.timchat.chatmodel.RecruitModel;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class JobPresenter extends BasePresenter {
  @Inject QcRestRepository qcRestRepository;
  @Inject GymWrapper gymWrapper;
  private MVPView view;

  @Inject public JobPresenter() {
  }

  @Override public void attachView(PView v) {
    view = (MVPView) v;
  }

  @Override public void unattachView() {
    super.unattachView();
    view = null;
  }

  /**
   * 发布职位
   */
  public void publishJob(JobBody body) {
    RxRegiste(qcRestRepository.createRxJava1Api(PostApi.class).qcPublishPosition(body)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<JobDetailWrap>>() {
          @Override public void call(QcDataResponse<JobDetailWrap> jobQcDataResponse) {
            if (jobQcDataResponse.status == 200){
              view.onJobDetail(jobQcDataResponse.data.job);
            }else{
              view.onShowError(jobQcDataResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  /**
   * 修改
   */
  public void editJob(String jobid, JobBody body) {
    RxRegiste(qcRestRepository.createRxJava1Api(PostApi.class)
        .qcEditPosition(jobid, body).onBackpressureBuffer().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            if (qcResponse.status == 200) {
              view.onEditOk();
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public void modifyJob(String jobId, JobBody body) {
    RxRegiste(qcRestRepository.createRxJava1Api(PostApi.class).editPosition(jobId, body)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            if (qcResponse.status == 200) {
              view.onEditOk();
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public void queryJob(String jobid) {
    RxRegiste(qcRestRepository.createRxJava1Api(GetApi.class)
        .queryJobDetail(jobid).onBackpressureBuffer().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<JobDetailWrap>>() {
          @Override public void call(QcDataResponse<JobDetailWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onJobDetail(qcResponse.data.job);
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable())

    );
  }

  public void queryStaffJob(String jobid) {
    RxRegiste(qcRestRepository.createRxJava1Api(GetApi.class)
        .querystaffJobDetail(jobid).onBackpressureBuffer().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<JobDetailWrap>>() {
          @Override public void call(QcDataResponse<JobDetailWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onJobDetail(qcResponse.data.job);
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable())

    );
  }


  /**
   * 获取可邀约的职位列表
   */
  public void getInviteJobs(String fairid) {
    RxRegiste(qcRestRepository.createRxJava1Api(GetApi.class).qcGetInviteJobs(fairid)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<JobListWrap>>() {
          @Override public void call(QcDataResponse<JobListWrap> jobList) {
            if (ResponseConstant.checkSuccess(jobList)) {
              view.onJobList(jobList.data.jobs);
            } else {
            }
          }
        }, new NetWorkThrowable())

    );
  }

  public void queryEditPermiss(String gymid, String key) {
    RxRegiste(qcRestRepository.createRxJava1Api(GetApi.class).queryOnepermission(gymid, key)
        .onBackpressureBuffer()
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<OnePermissionWrap>>() {
          @Override public void call(QcDataResponse<OnePermissionWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              if (qcResponse.data.has_permission) {
                view.toEditJob();
              } else {
                view.showAlert(R.string.alert_permission_forbid_contact_su);
              }
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public RecruitModel getRecruitModel(Job job) {
    RecruitModel recruitModel = new RecruitModel();
    recruitModel.id = job.id;
    recruitModel.address = job.getAddress() + job.gym.name;
    recruitModel.gender = job.gender;
    recruitModel.photo = job.gym.photo;
    recruitModel.max_age = job.max_age;
    recruitModel.min_age = job.min_age;
    recruitModel.max_height = job.max_height;
    recruitModel.min_height = job.min_height;
    recruitModel.max_salary = job.max_salary;
    recruitModel.min_salary = job.min_salary;
    recruitModel.max_work_year = job.max_work_year;
    recruitModel.min_work_year = job.min_work_year;
    recruitModel.name = job.name;
    return recruitModel;
  }

  /**
   * 投递简历
   */
  public void sendResume(String jobid) {
    HashMap<String, Object> params = new HashMap<String, Object>();
    params.put("job_id", jobid);
    params.put("published", true);
    RxRegiste(qcRestRepository.createRxJava1Api(PostApi.class)
        .sendResume(params).onBackpressureBuffer().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            if (qcResponse.getStatus() == 200) {
              view.onPostResumeOk();
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new Action1<Throwable>() {
          @Override public void call(Throwable throwable) {
            view.onShowError(throwable.getMessage());
          }
        }));
  }

  /**
   * 收藏职位
   */
  public void starPosition(Object jobid) {
    HashMap<String, Object> params = new HashMap<String, Object>();
    params.put("id", jobid);
    RxRegiste(qcRestRepository.createRxJava1Api(PostApi.class)
        .starJob(params).onBackpressureBuffer().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            if (qcResponse.getStatus() == 200) {
              view.starOK();
            }
          }
        }, new NetWorkThrowable()));
  }

  /**
   * 取消收藏职位
   */
  public void unstarPosition(String jobid) {
    RxRegiste(qcRestRepository.createRxJava1Api(PostApi.class)
        .cancelStarJob(jobid).onBackpressureBuffer().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            if (qcResponse.getStatus() == 200) {
              view.unStarOk();
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public void invitePosition(List<String> jobs, String resumeId) {
    InviteBody body = InviteBody.build(jobs, resumeId);
    RxRegiste(qcRestRepository.createRxJava1Api(PostApi.class)
        .qcInvitePosition(body).onBackpressureBuffer().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcResponse>() {
          @Override public void call(QcResponse qcResponse) {
            if (qcResponse.getStatus() == 200) {
              view.onInviteOk();
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public void queryGymDetail(String gymId){
    RxRegiste(qcRestRepository.createRxJava1Api(GetApi.class)
        .queryGymsDetail(gymId).onBackpressureBuffer().subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(new Action1<QcDataResponse<GymWrap>>() {
          @Override public void call(QcDataResponse<GymWrap> qcResponse) {
            if (ResponseConstant.checkSuccess(qcResponse)) {
              view.onGymDetail(qcResponse.data.gym);
            } else {
              view.onShowError(qcResponse.getMsg());
            }
          }
        }, new NetWorkThrowable()));
  }

  public interface MVPView extends CView {
    void onEditOk();

    void onJobDetail(Job job);

    void starOK();

    void unStarOk();

    void onPostResumeOk();

    void onInviteOk();

    void toEditJob();
    void onJobList(List<Job> jobList);
    void onGymDetail(Gym gym);
  }
}
