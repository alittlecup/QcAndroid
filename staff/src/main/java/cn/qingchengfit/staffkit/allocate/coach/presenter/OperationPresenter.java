package cn.qingchengfit.staffkit.allocate.coach.presenter;

import android.content.Intent;
import android.text.TextUtils;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.responese.QcResponse;
import cn.qingchengfit.network.response.QcResponseData;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.allocate.coach.model.AllocateStudentBean;
import cn.qingchengfit.staffkit.allocate.coach.model.StudentWithCoach;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.PView;
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
 * Created by fb on 2017/5/4.
 */

public class OperationPresenter extends BasePresenter {
    public static final int ADD_TYPE = 104;
    public static final int OPERATION_TYPE = 105;

    public OperationView view;
    @Inject RestRepository restRepository;
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;

    @Inject public OperationPresenter() {
    }

    @Override public void onStart() {

    }

    @Override public void onStop() {

    }

    @Override public void onPause() {

    }

    @Override public void attachView(PView v) {
        super.attachView(v);
        this.view = (OperationView) v;
    }

    @Override public void attachIncomingIntent(Intent intent) {

    }

    @Override public void onCreate() {

    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    /**
     * 批量添加会员
     *
     * @param coachIds sellerId
     * @param list list
     */
    public void AddStudents(List<String> coachIds, List<StudentWithCoach> list) {
        HashMap<String, Object> body = gymWrapper.getParams();
        String coachRet = "";
        for (int i = 0; i < coachIds.size(); i++) {
            if (i < list.size() - 1) {
                coachRet = TextUtils.concat(coachRet, coachIds.get(i), ",").toString();
            } else {
                coachRet = TextUtils.concat(coachRet, coachIds.get(i)).toString();
            }
        }
        body.put("coach_ids", coachRet);

        String ret = "";
        for (int i = 0; i < list.size(); i++) {
            if (i < list.size() - 1) {
                ret = TextUtils.concat(ret, list.get(i).id, ",").toString();
            } else {
                ret = TextUtils.concat(ret, list.get(i).id).toString();
            }
        }

        body.put("user_ids", ret);
        RxRegiste(restRepository.getPost_api()
            .qcAllocateCoach(App.staffId, body)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (qcResponse.status == 200) {
                        view.onAddSuccess();
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

    /**
     * 批量移除会员
     *
     * @param coachId coachId
     * @param list list
     * POST {"user_ids":"1,3,2", "seller_id":5}
     */
    public void removeStudents(String coachId, List<StudentWithCoach> list) {
        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("coach_id", coachId);

        String ret = "";
        for (int i = 0; i < list.size(); i++) {
            if (i < list.size() - 1) {
                ret = TextUtils.concat(ret, list.get(i).id, ",").toString();
            } else {
                ret = TextUtils.concat(ret, list.get(i).id).toString();
            }
        }

        params.put("user_ids", ret);
        RxRegiste(restRepository.getPost_api()
            .qcRemoveStudent(App.staffId, params)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponse>() {
                @Override public void call(QcResponse qcResponse) {
                    if (qcResponse.status == 200) {
                        if (view != null) view.onRemoveSuccess();
                    } else {
                        if (view != null) view.onShowError(qcResponse.getMsg());
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {
                    Timber.e(throwable.getMessage());
                    if (view != null) view.onShowError(throwable.getMessage());
                }
            }));
    }

    public void queryStudent(StudentFilter filter, String coachId, int type) {

        final HashMap<String, Object> params = gymWrapper.getParams();
        params.put("show_all", 1);

        if (!TextUtils.isEmpty(filter.gender)) params.put("gender", filter.gender);
        if (filter.sale != null) params.put("seller_id", filter.sale.getId());

        if (!TextUtils.isEmpty(filter.status)) {
            params.put("status_id", filter.status);
        }
        if (!TextUtils.isEmpty(coachId) && type != ADD_TYPE) {
            params.put("coach_id", coachId);
        }
        if (!TextUtils.isEmpty(filter.registerTimeStart) && !TextUtils.isEmpty(filter.registerTimeEnd)) {
            params.put("start", filter.registerTimeStart);
            params.put("end", filter.registerTimeEnd);
        }
        //if (!TextUtils.isEmpty(keyword)){
        //    params.put("q", keyword);
        //}
        if (!TextUtils.isEmpty(filter.gender)) params.put("gender", filter.gender);

        if (filter.referrerBean != null) params.put("recommend_user_id", filter.referrerBean.id);

        if (filter.sourceBean != null) params.put("origin_id", filter.sourceBean.id);
        if (type == OPERATION_TYPE) {
            RxRegiste(restRepository.getGet_api()
                .qcGetCoachStudentDetail(loginStatus.staff_id(), params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<QcResponseData<AllocateStudentBean>>() {
                    @Override public void call(QcResponseData<AllocateStudentBean> allocateStudentBeanQcResponseData) {
                        view.onStudentList(allocateStudentBeanQcResponseData.data.users);
                    }
                }));
        } else if (type == ADD_TYPE) {
            RxRegiste(restRepository.getGet_api()
                .qcGetAllocateCoachStudents(loginStatus.staff_id(), params)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<cn.qingchengfit.model.responese.QcResponseData<AllocateStudentBean>>() {
                    @Override public void call(
                        cn.qingchengfit.model.responese.QcResponseData<AllocateStudentBean> allocateStudentBeanQcResponseData) {
                        view.onStudentList(allocateStudentBeanQcResponseData.data.users);
                    }
                }));
        }
    }

    public interface OperationView extends PView {
        void onStudentList(List<StudentWithCoach> list);

        void onShowError(String e);

        void onAddSuccess();

        void onRemoveSuccess();

        void onStopLoading();
    }
}

