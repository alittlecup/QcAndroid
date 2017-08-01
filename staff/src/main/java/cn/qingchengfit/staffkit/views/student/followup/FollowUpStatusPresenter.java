package cn.qingchengfit.staffkit.views.student.followup;

import android.content.Intent;
import android.text.TextUtils;
import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.TrackStudents;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilter;
import java.util.HashMap;
import javax.inject.Inject;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * //  ┏┓　　　┏┓
 * //┏┛┻━━━┛┻┓
 * //┃　　　　　　　┃
 * //┃　　　━　　　┃
 * //┃　┳┛　┗┳　┃
 * //┃　　　　　　　┃
 * //┃　　　┻　　　┃
 * //┃　　　　　　　┃
 * //┗━┓　　　┏━┛
 * //   ┃　　　┃   神兽保佑
 * //   ┃　　　┃   没有bug
 * //   ┃　　　┗━━━┓
 * //   ┃　　　　　　　┣┓
 * //   ┃　　　　　　　┏┛
 * //   ┗┓┓┏━┳┓┏┛
 * //     ┃┫┫　┃┫┫
 * //     ┗┻┛　┗┻┛
 * //
 * //Created by yangming on 16/12/11.
 */
public class FollowUpStatusPresenter extends BasePresenter {

    public PresenterView view;
    public RestRepository restRepository;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    HashMap<String, Object> params;
    int curPage = 1, totalPages = 1;

    @Inject public FollowUpStatusPresenter(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        super.attachView(v);
        this.view = (PresenterView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void getStudentsWithStatus(StudentFilter filter, int changeStautsType) {
        params = gymWrapper.getParams();
        if (!TextUtils.isEmpty(filter.status)) {
            params.put("status", filter.status);
        }
        if (!TextUtils.isEmpty(filter.gender)) params.put("gender", filter.gender);

        if (filter.referrerBean != null) params.put("recommend_user_id", filter.referrerBean.id);

        if (filter.sourceBean != null) params.put("origin_id", filter.sourceBean.id);
        if (filter.sale != null) params.put("seller_id", filter.sale.getId());

        if (!TextUtils.isEmpty(filter.registerTimeStart) && !TextUtils.isEmpty(filter.registerTimeEnd)) {
            params.put("start", filter.registerTimeStart);
            params.put("end", filter.registerTimeEnd);
        }
        curPage = totalPages = 1;
        view.initEndlessLoad();
        loadMore(changeStautsType);
    }

    public void loadMore(int changeStautsType) {
        if (curPage <= totalPages) {
            params.put("page", curPage);
            Observable<QcDataResponse<TrackStudents>> observable;
            switch (changeStautsType) {
                case 1:
                    observable = restRepository.getGet_api().qcGetTrackStudentFollow(App.staffId, params);
                    break;
                case 2:
                    observable = restRepository.getGet_api().qcGetTrackStudentMember(App.staffId, params);
                    break;
                default:
                    observable = restRepository.getGet_api().qcGetTrackStudentCreate(App.staffId, params);
                    break;
            }

          RxRegiste(observable.onBackpressureBuffer().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcDataResponse<TrackStudents>>() {
                    @Override public void call(QcDataResponse<TrackStudents> trackStudentsQcResponseData) {
                        totalPages = trackStudentsQcResponseData.data.pages;
                        view.onToatalPages(trackStudentsQcResponseData.data.total_count);
                        view.onTrackStudent(trackStudentsQcResponseData.data, curPage);
                        curPage++;
                    }
                }, new Action1<Throwable>() {
                    @Override public void call(Throwable throwable) {
                        Timber.e(throwable.getMessage());
                        view.onShowError(throwable.getMessage());
                    }
                }));
        } else {
            view.onNoMoreLoad();
        }
    }

    public interface PresenterView extends PView {
        void onTrackStudent(TrackStudents students, int page);

        void onToatalPages(int toatalPags);

        void onNoMoreLoad();

        void onShowError(String e);

        void initEndlessLoad();
    }
}