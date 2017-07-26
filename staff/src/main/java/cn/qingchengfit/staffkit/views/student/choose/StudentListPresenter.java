package cn.qingchengfit.staffkit.views.student.choose;

import android.text.TextUtils;
import cn.qingchengfit.di.model.GymWrapper;
import cn.qingchengfit.di.model.LoginStatus;
import cn.qingchengfit.model.base.QcStudentBean;
import cn.qingchengfit.model.responese.QcResponseData;
import cn.qingchengfit.model.responese.ResponseConstant;
import cn.qingchengfit.model.responese.Students;
import cn.qingchengfit.staffkit.App;
import cn.qingchengfit.staffkit.mvpbase.BasePresenter;
import cn.qingchengfit.staffkit.mvpbase.CView;
import cn.qingchengfit.staffkit.mvpbase.PView;
import cn.qingchengfit.staffkit.rest.RestRepository;
import cn.qingchengfit.staffkit.views.student.filter.StudentFilter;
import java.util.HashMap;
import java.util.List;
import javax.inject.Inject;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

public class StudentListPresenter extends BasePresenter {
    @Inject LoginStatus loginStatus;
    @Inject GymWrapper gymWrapper;
    @Inject RestRepository restRepository;
    private MVPView view;

    @Inject public StudentListPresenter() {
    }

    @Override public void attachView(PView v) {
        view = (MVPView) v;
    }

    @Override public void unattachView() {
        super.unattachView();
        view = null;
    }

    public void queryStudentlist(StudentFilter filter) {
        HashMap<String, Object> params = gymWrapper.getParams();
        if (!TextUtils.isEmpty(filter.status)) {
            params.put("status", filter.status);
        }
        if (!TextUtils.isEmpty(filter.registerTimeStart) && !TextUtils.isEmpty(filter.registerTimeEnd)) {
            params.put("start", filter.registerTimeStart);
            params.put("end", filter.registerTimeEnd);
        }
        if (!TextUtils.isEmpty(filter.gender)) params.put("gender", filter.gender);

        if (filter.referrerBean != null) params.put("recommend_user_id", filter.referrerBean.id);

        if (filter.sourceBean != null) params.put("origin_id", filter.sourceBean.id);
        RxRegiste(restRepository.getGet_api()
            .qcGetAllStudents(App.staffId, params)
            .onBackpressureBuffer()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new Action1<QcResponseData<Students>>() {
                @Override public void call(QcResponseData<Students> studentsQcResponseData) {
                    if (ResponseConstant.checkSuccess(studentsQcResponseData)) {
                        view.onStudentsList(studentsQcResponseData.getData().users);
                    }
                }
            }, new Action1<Throwable>() {
                @Override public void call(Throwable throwable) {

                }
            }));
    }

    public interface MVPView extends CView {
        void onStudentsList(List<QcStudentBean> studentBeens);
    }
}
