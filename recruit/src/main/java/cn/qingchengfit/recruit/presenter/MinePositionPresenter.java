package cn.qingchengfit.recruit.presenter;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.network.QcRestRepository;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.recruit.model.Job;
import cn.qingchengfit.recruit.network.GetApi;
import cn.qingchengfit.recruit.network.response.JobListWrap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class MinePositionPresenter extends BasePresenter {

    @Inject QcRestRepository restRepository;
    private MVPView view;

    @Inject public MinePositionPresenter() {
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    /**
     * 收藏的职位
     */
    public void queryPositionOfStarred(final int page) {
        RxRegiste(restRepository.createGetApi(GetApi.class)
            .queryMyStared(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<JobListWrap>>() {
                @Override public void call(QcDataResponse<JobListWrap> jobListWrapQcDataResponse) {
                    view.onJobList(jobListWrapQcDataResponse.data.jobs, page, jobListWrapQcDataResponse.data.total_count);
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onShowError(throwable.getMessage());
                }
            }));
    }

    /**
     * 我投递的职位
     */
    public void queryPositionOfSent(final int page) {
        RxRegiste(restRepository.createGetApi(GetApi.class)
            .queryMySent(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<JobListWrap>>() {
                @Override public void call(QcDataResponse<JobListWrap> jobListWrapQcDataResponse) {
                    view.onJobList(jobListWrapQcDataResponse.data.jobs, page, jobListWrapQcDataResponse.data.total_count);
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onShowError(throwable.getMessage());
                }
            }));
    }

    /**
     * 被邀请的职位
     */
    public void queryPositionOfInvited(final int page) {
        RxRegiste(restRepository.createGetApi(GetApi.class)
            .queryMyInvited(page)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcDataResponse<JobListWrap>>() {
                @Override public void call(QcDataResponse<JobListWrap> jobListWrapQcDataResponse) {
                    view.onJobList(jobListWrapQcDataResponse.data.jobs, page, jobListWrapQcDataResponse.data.total_count);
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    view.onShowError(throwable.getMessage());
                }
            }));
    }


    public interface MVPView extends CView {
        void onJobList(List<Job> jobs, int page, int totalcount);
    }
}
