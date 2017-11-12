package cn.qingchengfit.saasbase.student.presenters.allot;

import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.PView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.network.errors.NetWorkThrowable;
import cn.qingchengfit.network.response.QcResponse;
import cn.qingchengfit.saasbase.repository.IStudentModel;
import cn.qingchengfit.saasbase.student.items.QcStudentWithCoach;
import cn.qingchengfit.saasbase.student.network.body.StudentFilter;
import cn.qingchengfit.saasbase.student.other.RxHelper;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by huangbaole on 2017/11/1.
 */

public class CoashDetailPresenter extends BasePresenter<CoashDetailPresenter.MVPView> {
    @Inject
    LoginStatus loginStatus;
    @Inject
    GymWrapper gymWrapper;

    @Inject
    IStudentModel studentModel;

    @Inject
    public CoashDetailPresenter() {
    }



    public void queryStudent(String staff_id, StudentFilter filter, String coachId) {
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
        RxRegiste(studentModel
                .qcGetCoachStudentDetail(staff_id, params)
                .compose(RxHelper.schedulersTransformer())
                .doOnTerminate(()->mvpView.stopRefresh())
                .map(users->{
                    List<QcStudentBean> beans=new ArrayList<>();
                    for(QcStudentWithCoach bean:users.data.users){
                        beans.add(bean);
                    }
                    return beans;
                })
                .subscribe(allocateStudentBeanQcResponseData -> mvpView.onStudentList(allocateStudentBeanQcResponseData),
                        throwable -> mvpView.onShowError(throwable.getMessage())));
    }

    /**
     * 批量移除会员
     *
     * @param coachId coachId
     * @param list list
     * POST {"user_ids":"1,3,2", "seller_id":5}
     */
    public void removeStudents(String staff_id,String coachId, List<String> list) {
        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("coach_id", coachId);

        String ret = "";
        for (int i = 0; i < list.size(); i++) {
            if (i < list.size() - 1) {
                ret = TextUtils.concat(ret, list.get(i), ",").toString();
            } else {
                ret = TextUtils.concat(ret, list.get(i)).toString();
            }
        }
        params.put("user_ids", ret);
        RxRegiste(studentModel
                .qcRemoveStudent(staff_id, params)
                .compose(RxHelper.schedulersTransformer())
                .subscribe(qcResponse -> {
                    if (qcResponse.status == 200) {
                         mvpView.onRemoveSuccess();
                    } else {
                        mvpView.onShowError(qcResponse.getMsg());
                    }
                }, throwable -> mvpView.onShowError(throwable.getMessage())));
    }


    public interface MVPView extends CView {
        void onStudentList(List<QcStudentBean> list);
        void onRemoveSuccess();
        void onShowError(String e);
        void stopRefresh();

    }
}
