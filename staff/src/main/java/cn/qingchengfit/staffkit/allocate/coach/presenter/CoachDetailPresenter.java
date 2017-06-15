package cn.qingchengfit.staffkit.allocate.coach.presenter;

import android.content.Intent;
import android.text.TextUtils;
import cn.qingchengfit.inject.model.GymWrapper;
import cn.qingchengfit.model.base.CoachService;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.network.response.QcResponseData;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.allocate.coach.model.AllocateStudentBean;
import cn.qingchengfit.staffkit.allocate.coach.model.StudentWithCoach;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.CView;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.NetWorkThrowable;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilter;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by fb on 2017/5/3.
 */

public class CoachDetailPresenter extends BasePresenter {

    @Inject GymWrapper gymWrapper;
    private CoachPreView view;
    private RestRepository restRepository;

    @Inject public CoachDetailPresenter(RestRepository restRepository) {
        this.restRepository = restRepository;
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        this.view = (CoachPreView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void removeStudent(CoachService coachService, String salerId, String studId, final int posotion) {
        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("user_id", studId);
        params.put("seller_id", salerId);
        RxRegiste(restRepository.getPost_api()
            .qcDeleteStudent(App.staffId, params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (qcResponse.status == 200) {
                        view.onRemoveSucess(posotion);
                    } else {
                        view.onShowError(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                    view.onShowError(throwable.getMessage());
                }
            }));
    }

    public void queryStudent(String staffId, StudentFilter filter, String coachId) {

        HashMap<String, Object> params = gymWrapper.getParams();

        if (!TextUtils.isEmpty(coachId)) {
            params.put("coach_id", coachId);
        }
        params.put("show_all", 1);

        if (!TextUtils.isEmpty(filter.status)) {
            params.put("status_id", filter.status);
        }
        if (!TextUtils.isEmpty(filter.registerTimeStart) && !TextUtils.isEmpty(filter.registerTimeEnd)) {
            params.put("start", filter.registerTimeStart);
            params.put("end", filter.registerTimeEnd);
        }
        if (!TextUtils.isEmpty(filter.gender)) params.put("gender", filter.gender);

        if (filter.referrerBean != null) params.put("recommend_user_id", filter.referrerBean.id);

        if (filter.sourceBean != null) params.put("origin_id", filter.sourceBean.id);
        if (filter.sale != null) params.put("seller_id", filter.sale.getId());
        RxRegiste(restRepository.getGet_api()
            .qcGetCoachStudentDetail(staffId, params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<AllocateStudentBean>>() {
                @Override public void call(QcResponseData<AllocateStudentBean> allocateStudentBeanQcResponseData) {
                    view.onStudentList(allocateStudentBeanQcResponseData.data.users);
                }
            }, new NetWorkThrowable()));
    }

    public interface CoachPreView extends CView {
        void onStudentList(List<StudentWithCoach> list);

        void onRemoveSucess(int position);

        void clearDatas();
    }
}
