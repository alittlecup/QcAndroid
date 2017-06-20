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
import com.tencent.qcloud.timchat.chatmodel.RecruitModel;
import java.util.ArrayList;
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
        params.put("job_id", jobid);
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

    public RecruitModel getRecruitModel(Job job){
        RecruitModel recruitModel = new RecruitModel();
        recruitModel.id = job.id;
        recruitModel.address = job.gd_district.city.name + job.gd_district.name + job.gym.name;
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

    public List<String> filterSalary(){
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
        void onJob(Job job);

        void onList(List<Job> jobs, int page, int totalCount);

        void onGym(Gym service);

        void starOK();

        void unStarOk();

        void onJobsIndex(Number completed, int fair_count, String avatar, String fiar_banner);
    }
}
