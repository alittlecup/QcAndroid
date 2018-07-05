package cn.qingchengfit.saasbase.student.presenters.allot;

import android.text.TextUtils;

import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import cn.qingchengfit.di.BasePresenter;
import cn.qingchengfit.di.CView;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.saasbase.repository.IStudentModel;
import cn.qingchengfit.saasbase.student.network.body.StudentFilter;
import cn.qingchengfit.saascommon.network.RxHelper;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by huangbaole on 2017/10/30.
 */

public class SalesDetailPresenter extends BasePresenter<SalesDetailPresenter.MVPView> {

    @Inject
    LoginStatus loginStatus;
    @Inject
    GymWrapper gymWrapper;

    @Inject
    IStudentModel studentModel;

    @Inject
    public SalesDetailPresenter() {
    }


    public void queryStudent(String staff_id, StudentFilter filter, String salerId) {
        HashMap<String, Object> params = gymWrapper.getParams();

        if (!TextUtils.isEmpty(salerId)) {
            params.put("seller_id", salerId);
        }
        if (filter != null) {
            params.put("show_all", 1);
            if (!TextUtils.isEmpty(filter.status)) {
                params.put("status", filter.status);
            }
            if (!TextUtils.isEmpty(filter.registerTimeStart) && !TextUtils.isEmpty(filter.registerTimeEnd)) {
                params.put("start", filter.registerTimeStart);
                params.put("end", filter.registerTimeEnd);
            }
            if (!TextUtils.isEmpty(filter.gender)) params.put("gender", filter.gender);

            if (filter.referrerBean != null)
                params.put("recommend_user_id", filter.referrerBean.id);

            if (filter.sourceBean != null) params.put("origin_id", filter.sourceBean.id);
        }

        RxRegiste(studentModel
                .qcGetAllotSaleOwenUsers(staff_id, params)
                .compose(RxHelper.schedulersTransformer())
                .doOnTerminate(()->mvpView.stopRefresh())
                .subscribe(response -> {
                    if (response.status == 200) {
                        mvpView.onStudentList(response.data.users);
                    } else {
                        mvpView.onShowError(response.getMsg());
                    }
                }, throwable -> mvpView.onShowError(throwable.getMessage())));

    }
    /**
     * 批量移除会员
     *
     * @param sellerId sellerId
     * @param list list
     * POST {"user_ids":"1,3,2", "seller_id":5}
     */
    public void removeStudents(String staff_id,String sellerId, List<String> list) {
        HashMap<String, Object> params = gymWrapper.getParams();
        params.put("seller_id", sellerId);
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
                .qcDeleteStudents(staff_id, params)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
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
