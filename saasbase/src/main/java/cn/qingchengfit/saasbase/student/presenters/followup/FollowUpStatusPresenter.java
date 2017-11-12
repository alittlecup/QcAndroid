package cn.qingchengfit.saasbase.student.presenters.followup;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import java.util.HashMap;

import javax.inject.Inject;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.network.response.QcDataResponse;
import cn.qingchengfit.saasbase.repository.IStudentModel;
import cn.qingchengfit.saasbase.student.network.body.StudentFilter;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrappeForFollow;
import cn.qingchengfit.saasbase.student.network.body.StudentListWrapper;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by huangbaole on 2017/11/7.
 */

public class FollowUpStatusPresenter extends BasePresenter<FollowUpStatusPresenter.MVPView> {
    @Inject
    LoginStatus loginStatus;
    @Inject
    GymWrapper gymWrapper;
    @Inject
    IStudentModel studentModel;
    HashMap<String, Object> params;
    int curPage = 1, totalPages = 1;

    @Inject
    public FollowUpStatusPresenter() {
    }


    public void getStudentsWithStatus(String staffId, StudentFilter filter, int changeStautsType) {
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
        mvpView.initEndlessLoad();
        loadMore(staffId, changeStautsType);
    }

    public void loadMore(String staffId, int changeStautsType) {
        if (curPage <= totalPages) {
            params.put("page", curPage);
            Observable<QcDataResponse<StudentListWrappeForFollow>> observable;
            switch (changeStautsType) {
                case 1:
                    observable = studentModel.qcGetTrackStudentFollow(staffId, params);
                    break;
                case 2:
                    observable = studentModel.qcGetTrackStudentMember(staffId, params);
                    break;
                default:
                    observable = studentModel.qcGetTrackStudentCreate(staffId, params);
                    break;
            }

            RxRegiste(observable.onBackpressureBuffer().subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(trackStudentsQcResponseData -> {
                        totalPages = trackStudentsQcResponseData.data.pages;
                        mvpView.onToatalPages(trackStudentsQcResponseData.data.total_count);
                        mvpView.onTrackStudent(trackStudentsQcResponseData.data, curPage);
                        curPage++;
                    }, throwable -> mvpView.onShowError(throwable.getMessage())));
        } else {
            mvpView.onNoMoreLoad();
        }
    }

    public interface MVPView extends CView {
        void onTrackStudent(StudentListWrappeForFollow students, int page);

        void onToatalPages(int toatalPags);

        void onNoMoreLoad();

        void onShowError(String e);
        void initEndlessLoad();

    }
}
